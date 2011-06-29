package com.google.code.joliratools.bind.generate;

import java.io.PrintWriter;
import java.util.Date;

import com.google.code.joliratools.bind.schema.ArrayEntity;
import com.google.code.joliratools.bind.schema.BuiltInEntity;
import com.google.code.joliratools.bind.schema.CollectionEntity;
import com.google.code.joliratools.bind.schema.ComplexEntity;
import com.google.code.joliratools.bind.schema.Entity;
import com.google.code.joliratools.bind.schema.EntityVisitor;
import com.google.code.joliratools.bind.schema.EnumEntity;
import com.google.code.joliratools.bind.schema.Property;
import com.google.code.joliratools.bind.schema.Schema;

/**
 * This class is responsible for generating the Java Source of Adapter classes
 * for a given Schema.
 * 
 * @author Joachim F. Kainz, Gabe Hopper
 */
public abstract class JSONAdapterClassGenerator {
    private static final String DEFAULT_PACKAGE_PREFIX = "com.google.code.joliratools.bind.";
    /**
     * This postfix will be added to a className to create the name of the
     * Adapter.
     */
    public final static String ADAPTER_POSTFIX = "JSONAdapter";
    private final static String JAVA_PREFIX = "java.";

    private static Property findRoot(final Property[] roots, final Entity entity) {
        for (final Property root : roots) {
            final Entity _entity = root.getType();

            if (entity == _entity) {
                return root;
            }
        }
        return null;
    }

    /**
     * Create an adapter name.
     * 
     * @param entity
     *            the entity
     * @return the adapter name
     */
    protected static String getAdapterName(final Entity entity) {
        final String className = getNormalizedClassName(entity);

        if (!className.startsWith(JAVA_PREFIX)) {
            return className + ADAPTER_POSTFIX;
        }

        final int prefixLen = JAVA_PREFIX.length();
        final String name = className.substring(prefixLen);

        return DEFAULT_PACKAGE_PREFIX + name + ADAPTER_POSTFIX;
    }

    /**
     * @param entity
     * @return the normalized class name
     */
    protected static String getNormalizedClassName(final Entity entity) {
        return entity.visit(new EntityVisitor<String>() {
            @Override
            public String visit(final ArrayEntity _entity) {
                final Entity componentEntity = _entity.getComponentEntity();

                return getNormalizedClassName(componentEntity)
                        + ArrayEntity.POST_FIX;
            }

            @Override
            public String visit(final BuiltInEntity _entity) {
                return visit((Entity) _entity);
            }

            @Override
            public String visit(final CollectionEntity _entity) {
                final Entity typeArgument = _entity.getTypeArgument();

                return getNormalizedClassName(typeArgument)
                        + CollectionEntity.POST_FIX;
            }

            @Override
            public String visit(final ComplexEntity _entity) {
                return visit((Entity) _entity);
            }

            public String visit(final Entity _entity) {
                return _entity.getClassName();
            }

            @Override
            public String visit(final EnumEntity _entity) {
                return visit((Entity) _entity);
            }
        });
    }

    private static String getPackageName(final String name) {
        final int lastDot = name.lastIndexOf('.');

        return name.substring(0, lastDot);
    }

    private static String getSimpleName(final String name) {
        final int lastDot = name.lastIndexOf('.');

        return name.substring(lastDot + 1);
    }

    private final Schema schema;
    private final boolean generateStringsOnly;

    /**
     * Create a new generator.
     * 
     * @param schema
     *            the schema to use for the generator
     * @param generateStringsOnly
     *            {@literal true} to support a backwards compatible mode for
     *            applications that need all built-in types to be output as
     *            strings.
     */
    public JSONAdapterClassGenerator(final Schema schema, final boolean generateStringsOnly) {
        this.schema = schema;
        this.generateStringsOnly = generateStringsOnly;
    }

    /**
     * Subclasses need to provide the ability to actually create the Java source
     * files.
     * 
     * @param classname
     *            the name of the class for which a source file should be
     *            generated
     * @return a print writer to write into the newly created source file.
     */
    protected abstract PrintWriter createSourceFile(String classname);

    /**
     * Create the Java Source for all the adapters required by the schema.
     */
    public void generate() {
        final Entity[] entities = schema.getEntities();
        final Property[] roots = schema.getRootElements();

        for (final Entity entity : entities) {
            final Property root = findRoot(roots, entity);
            generate(entity, root);
        }
    }

    /**
     * Generate code for an {@link ArrayEntity}
     * 
     * @param writer
     *            the writer
     * @param entity
     *            the entity
     */
    protected void generate(final ArrayEntity entity, final PrintWriter writer) {
        final Property[] properties = entity.getProperties();
        final Property property = properties[0];

        generateForLoop(writer, property);
    }

    /**
     * Generate code for an {@link BuiltInEntity}
     * 
     * @param writer
     *            the writer
     * @param entity
     *            the entity
     */
    protected void generate(final BuiltInEntity entity, final PrintWriter writer) {
        writer.println("    writer.print(adapted);");
    }

    /**
     * Generate code for an {@link CollectionEntity}
     * 
     * @param writer
     *            the writer
     * @param entity
     *            the entity
     */
    protected void generate(final CollectionEntity entity,
            final PrintWriter writer) {
        final Property[] properties = entity.getProperties();
        final Property property = properties[0];

        generateForLoop(writer, property);
    }

    /**
     * Generate code for an {@link ComplexEntity}
     * 
     * @param writer
     *            the writer
     * @param entity
     *            the entity
     */
    protected void generate(final ComplexEntity entity, final PrintWriter writer) {
        writer.println("    writer.print(\"{\");");
        final Property[] properties = entity.getProperties();
        for (final Property property : properties) {
            generate(writer, property);
        }
        writer.println("    writer.print(\"}\");");
    }

    private void generate(final Entity _entity, final PrintWriter writer) {

        _entity.visit(new EntityVisitor<Void>() {
            @Override
            public Void visit(final ArrayEntity entity) {
                generate(entity, writer);
                return null;
            }

            @Override
            public Void visit(final BuiltInEntity entity) {
                generate(entity, writer);
                return null;
            }

            @Override
            public Void visit(final CollectionEntity entity) {
                generate(entity, writer);
                return null;
            }

            @Override
            public Void visit(final ComplexEntity entity) {
                generate(entity, writer);
                return null;
            }

            @Override
            public Void visit(final EnumEntity entity) {
                generate(entity, writer);
                return null;
            }
        });
    }

    private void generate(final Entity entity, final Property root) {
        final String clazzName = entity.getClassName();
        final String adapterName = getAdapterName(entity);
        final PrintWriter writer = createSourceFile(adapterName);

        try {
            final String pkgName = getPackageName(adapterName);
            final String simpleName = getSimpleName(adapterName);

            // header
            writer.print("// AUTOMATICALLY GENERATED ");
            writer.println(new Date());
            writer.println("// DO NOT MODIFY!");
            writer.println();
            writer.print("package ");
            writer.print(pkgName);
            writer.println(';');
            writer.println();

            // class
            writer.print("public class ");
            writer.print(simpleName);
            writer
            .print(" implements com.google.code.joliratools.bind.JSONAdapter ");
            writer.println("{");
            writer.print("  private final ");
            writer.print(clazzName);
            writer.println(" adapted;");
            writer.println();

            // constructor
            writer.print("  public ");
            writer.print(simpleName);
            writer.print('(');
            writer.print(clazzName);
            writer.print(" adapted");
            writer.println(") {");
            writer.println("    this.adapted = adapted;");
            writer.println("  }");
            writer.println();
            writer.println("  /**");
            writer.println("   * <b>DO NOT USE THIS METHOD!</b> ");
            writer.println("   * This method will be called be the");
            writer.println("   * toXml(PrintWriter) method that is only");
            writer.println("   * generated for classes marked with ");
            writer.println("   * @RoRootElement.");
            writer.println("   */");
            writer.println("  public void toJSON(java.io.PrintWriter writer, "
                    + "java.util.Collection<Object> _processed) {");
            writer.println("    if (!_processed.add(adapted)) {");
            writer.println("      writer.print(\"null\");");
            writer.println("      return;");
            writer.println("    }");
            writer.println("    else if (adapted==null) {");
            writer.println("        return;");
            writer.println("    }");
            writer.println();
            writer
            .println("    java.util.Collection<Object> processed = new java.util.HashSet<Object>(_processed);");
            writer.println();

            writer.println("boolean firstProp = true;");

            generate(entity, writer);

            writer.println("  }");

            // if (root != null) {
            writer.println();
            writer.println("  public void toJSON(java.io.PrintWriter "
                    + "writer) {");
            writer.println("    java.util.Collection<Object> processed = "
                    + "new java.util.HashSet<Object>();");
            writer.println();

            printIntent(writer, 2);
            writer.println("toJSON(writer, processed);");
            printIntent(writer, 1);
            writer.println("}");
            // }

            writer.println("}");
        } finally {
            writer.close();
        }
    }

    /**
     * @param entity
     *            the entity to be generated
     * @param writer
     *            the writer
     */
    protected void generate(final EnumEntity entity, final PrintWriter writer) {
        printSingleQuote(writer, 1);
        writer.println("    writer.print(adapted);");
        printSingleQuote(writer, 1);
    }

    private void generate(final PrintWriter writer, final Property property) {
        final String name = property.getName();
        final Entity type = property.getType();
        final String clazzName = type.getClassName();
        final String accessorName = property.getAccessorName();

        writer.print("    // ");
        writer.println("_" + name);
        writer.print("    ");
        writer.print(clazzName);
        writer.print(' ');
        writer.print("_" + name);
        writer.print(" = adapted.");
        writer.print(accessorName);
        writer.println(';');
        writer.println();

        generate(writer, name, type, 2);
    }

    private void generate(final PrintWriter writer, final String name,
            final Entity type, final int baseLevel) {
        final boolean stringsOnly = generateStringsOnly;

        type.visit(new EntityVisitor<Void>() {
            private boolean isJSONResultInQuote(final String className) {
                if (stringsOnly) {
                    return true;
                }

                return !("boolean".equals(className) || "double".equals(className) || "float".equals(className)
                        || "int".equals(className) || "long".equals(className) || "java.lang.Boolean".equals(className)
                        || "java.lang.Integer".equals(className) || "java.lang.Double".equals(className)
                        || "java.lang.Float".equals(className) || "java.lang.Long".equals(className));
            }

            @Override
            public Void visit(final ArrayEntity _entity) {
                visit((Entity) _entity);
                return null;
            }

            @Override
            public Void visit(final BuiltInEntity _entity) {
                final boolean isObject = _entity.isObject();

                if (isObject) {
                    printIntent(writer, baseLevel);

                    writer.print("if (null != ");
                    writer.print("_" + name);
                    writer.println(") {");
                }

                final int level = baseLevel + (isObject ? 1 : 0);
                final String className = _entity.getClassName();
                final boolean isJSONResultInQuote = isJSONResultInQuote(className);

                printOpenTag(writer, name, level);

                if (isJSONResultInQuote) {
                    printSingleQuote(writer, level);
                }

                printIntent(writer, level);

                if ("java.lang.String".equals(className)) {
                    writer.print("writer.print(");
                    writer
                    .print("org.apache.commons.lang.StringEscapeUtils.escapeJava(");
                    writer.print("_" + name);
                    writer.print(')');
                    writer.println(");");
                } else if ("java.util.Date".equals(className)) {
                    writer.print("writer.print(");
                    writer.print("new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\").");
                    writer.print("format(");
                    writer.print("_" + name);
                    writer.print(')');
                    writer.println(");");
                } else if ("java.util.Calendar".equals(className) || "java.util.GregorianCalendar".equals(className)) {
                    writer.print("writer.print(");
                    writer.print("new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\").");
                    writer.print("format(");
                    writer.print("_" + name);
                    writer.println(".getTime()));");
                } else {
                    writer.print("writer.print(");
                    writer.print("_" + name);
                    writer.println(");");
                }
                if (isJSONResultInQuote) {
                    printSingleQuote(writer, level);
                }

                if (isObject) {
                    printIntent(writer, baseLevel);
                    writer.println("}");
                }

                return null;
            }

            @Override
            public Void visit(final CollectionEntity _entity) {
                visit((Entity) _entity);
                return null;
            }

            @Override
            public Void visit(final ComplexEntity _entity) {
                visit((Entity) _entity);
                return null;
            }

            private void visit(final Entity _entity) {

                printIntent(writer, baseLevel);
                writer.print("if (null != ");
                writer.print("_" + name);
                writer.println(") {");
                printOpenTag(writer, name, baseLevel);

                writer.println();

                final String _name = getAdapterName(_entity);

                printIntent(writer, baseLevel + 1);
                writer.print(_name);
                writer.print(" adapter = new ");
                writer.print(_name);
                writer.print("(");
                writer.print("_" + name);
                writer.println(");");
                writer.println();

                printIntent(writer, baseLevel + 1);
                writer.println("adapter.toJSON(writer, processed);");
                printCloseTag(writer, name, baseLevel + 1);

                printIntent(writer, baseLevel);
                writer.println("}");

            }

            @Override
            public Void visit(final EnumEntity _entity) {
                visit((Entity) _entity);
                return null;
            }
        });
    }

    private void generateForLoop(final PrintWriter writer,
            final Property property) {
        final Entity type = property.getType();
        final String name = property.getName();
        final String clazzName = type.getClassName();

        printOpenTagLoop(writer, name, 1);

        writer.println("    boolean first = true;");
        writer.print("    for (");
        writer.print(clazzName);
        writer.print(' ');
        writer.print("_" + name);
        writer.println(" : adapted) {");
        writer.println("    if(!first) {");
        writer.println("      writer.print(\", \");");
        writer.println("    }");
        generateLoop(writer, name, type, 3);
        writer.println("    first = false;");
        writer.println("    }");

        printCloseTagLoop(writer, name, 1);
    }

    private void generateLoop(final PrintWriter writer, final String name,
            final Entity type, final int baseLevel) {

        type.visit(new EntityVisitor<Void>() {
            @Override
            public Void visit(final ArrayEntity _entity) {
                visit((Entity) _entity);
                return null;
            }

            @Override
            public Void visit(final BuiltInEntity _entity) {
                final boolean isObject = _entity.isObject();

                if (isObject) {
                    printIntent(writer, baseLevel);
                    writer.print("if (null != ");
                    writer.print("_" + name);
                    writer.println(") {");
                }

                final int level = baseLevel + (isObject ? 1 : 0);

                printSingleQuote(writer, level);
                writer.print("writer.print(");

                final String className = _entity.getClassName();

                if ("java.lang.String".equals(className)) {
                    writer
                    .print("org.apache.commons.lang.StringEscapeUtils.escapeJava(");
                    writer.print("_" + name);
                    writer.print(')');
                } else if ("java.util.Date".equals(className)) {
                    writer
                    .print("new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\").");
                    writer.print("format(");
                    writer.print("_" + name);
                    writer.print(')');
                } else {
                    writer.print("_" + name);
                }
                writer.println(");");
                printSingleQuote(writer, level);

                if (isObject) {
                    printIntent(writer, baseLevel);
                    writer.println("}");
                }

                return null;
            }

            @Override
            public Void visit(final CollectionEntity _entity) {
                visit((Entity) _entity);
                return null;
            }

            @Override
            public Void visit(final ComplexEntity _entity) {
                visit((Entity) _entity);
                return null;
            }

            private void visit(final Entity _entity) {
                printIntent(writer, baseLevel);
                writer.print("if (null != ");
                writer.print("_" + name);
                writer.println(") {");
                writer.println();

                final String _name = getAdapterName(_entity);

                printIntent(writer, baseLevel + 1);
                writer.print(_name);
                writer.println(" adapter = new ");
                writer.print(_name);
                writer.print("(");
                writer.print("_" + name);
                writer.println(");");
                writer.println();

                printIntent(writer, baseLevel + 1);
                writer.println("adapter.toJSON(writer, processed);");
                printIntent(writer, baseLevel);
                writer.println("}");
            }

            @Override
            public Void visit(final EnumEntity _entity) {
                visit((Entity) _entity);
                return null;
            }
        });
    }

    void printCloseTag(final PrintWriter writer, final String name,
            final int level) {
        // nothing
    }

    private void printCloseTagLoop(final PrintWriter writer,
            final String name, final int level) {
        printIntent(writer, level);
        writer.println("writer.print(\"]\");");
    }

    void printIntent(final PrintWriter writer, final int level) {
        for (int idx = 0; idx < level; idx++) {
            writer.print("  ");
        }
    }

    void printOpenTag(final PrintWriter writer, final String name,
            final int level) {
        printIntent(writer, level);
        writer.println("if (!firstProp) {");
        printIntent(writer, level);
        writer.println("    writer.print(\",\");");
        printIntent(writer, level);
        writer.println("} else {");
        printIntent(writer, level);
        writer.println("    firstProp = false;");
        printIntent(writer, level);
        writer.println("}");
        printIntent(writer, level);
        writer.print("writer.print(\"\\\"");
        writer.print(name);
        writer.println("\\\":\");");
    }

    void printOpenTagLoop(final PrintWriter writer,
            final String name, final int level) {
        printIntent(writer, level);
        writer.println("writer.print(\"[\");");
    }

    void printSingleQuote(final PrintWriter writer, final int level) {
        printIntent(writer, level);
        writer.println("  writer.print(\"\\\"\");");
    }
}
