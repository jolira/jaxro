package com.google.code.joliratools.bind.generate;

import java.io.PrintWriter;

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
 * @author Joachim F. Kainz
 */
public abstract class XMLAdapterClassGenerator {
    /**
     * This postfix will be added to a className to create the name of the
     * Adapter.
     */
    public final static String ADAPTER_POSTFIX = "JAXROAdapter";
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

        return "com.google.code.joliratools.bind." + name + ADAPTER_POSTFIX;
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

    /**
     * Create a new generator.
     * 
     * @param schema
     *            the schema to use for the generator
     */
    public XMLAdapterClassGenerator(final Schema schema) {
        this.schema = schema;
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

    void generate(final ArrayEntity entity, final PrintWriter writer) {
        final Property[] properties = entity.getProperties();
        final Property property = properties[0];

        generateForLoop(writer, property);
    }

    void generate(final BuiltInEntity entity, final PrintWriter writer) {
        writer.println("    writer.print(adapted);");
    }

    void generate(final CollectionEntity entity,
            final PrintWriter writer) {
        final Property[] properties = entity.getProperties();
        final Property property = properties[0];

        generateForLoop(writer, property);
    }

    void generate(final ComplexEntity entity, final PrintWriter writer) {
        final Property[] properties = entity.getProperties();
        boolean isFirst = true;

        for (final Property property : properties) {
            if (!isFirst) {
                writer.println();
            } else {
                isFirst = false;
            }

            generate(writer, property);
        }
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
            writer.print("// AUTOMATICALLY GENERATED");
            // writer.println(new Date());
            writer.println("// DO NOT MODIFY!");
            writer.println();
            writer.print("package ");
            writer.print(pkgName);
            writer.println(';');
            writer.println();

            // class
            writer.print("public class ");
            writer.print(simpleName);
            writer.println(" {");

            // private member
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

            // toXML(PrintWriter, Collection)
            writer.println("  /**");
            writer.println("   * <b>DO NOT USE THIS METHOD!</b> ");
            writer.println("   * This method will be called be the");
            writer.println("   * toXml(PrintWriter) method that is only");
            writer.println("   * generated for classes marked with ");
            writer.println("   * @RoRootElement.");
            writer.println("   */");
            writer.println("  public void toXML(java.io.PrintWriter writer, "
                    + "java.util.Collection<Object> _processed) {");
            writer.println("    if (!_processed.add(adapted)) {");
            writer.println("      return;");
            writer.println("    }");
            writer.println();
            writer
            .println("    java.util.Collection<Object> processed = new java.util.HashSet<Object>(_processed);");
            writer.println();

            generate(entity, writer);
            writer.println("  }");

            if (root != null) {
                // toXML(PrintWriter)
                writer.println();
                writer.println("  public void toXML(java.io.PrintWriter "
                        + "writer) {");
                writer.println("    java.util.Collection<Object> processed = "
                        + "new java.util.HashSet<Object>();");
                writer.println();

                writer.println("    writer.print(\"<?xml version=\\\"1.0\\\" "
                        + "encoding=\\\"UTF-8\\\"?>\");");

                final String rootName = root.getName();

                printOpenTag(writer, rootName, 2);
                writer.println("    toXML(writer, processed);");
                printCloseTag(writer, rootName, 2);
                writer.println("  }");
            }
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
        writer.println("    writer.print(adapted);");
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

                printOpenTag(writer, name, level);
                printIntent(writer, level);
                writer.print("writer.print(");

                final String className = _entity.getClassName();

                if ("java.lang.String".equals(className)) {
                    writer
                    .print("org.apache.commons.lang.StringEscapeUtils.escapeXml(");
                    writer.print("_" + name);
                    writer.print(')');
                } else if ("java.util.Date".equals(className)) {
                    writer
                    .print("new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\").");
                    writer.print("format(");
                    writer.print("_" + name);
                    writer.print(')');
                } else if ("java.util.Calendar".equals(className) || "java.util.GregorianCalendar".equals(className)) {
                    writer.print("new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\").");
                    writer.print("format(");
                    writer.print("_" + name);
                    writer.println(".getTime())");
                } else {
                    writer.print("_" + name);
                }
                writer.println(");");
                printCloseTag(writer, name, level);

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
                printOpenTag(writer, name, baseLevel + 1);
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
                writer.println("adapter.toXML(writer, processed);");
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

        writer.print("    for (");
        writer.print(clazzName);
        writer.print(' ');
        writer.print("_" + name);
        writer.println(" : adapted) {");
        generate(writer, name, type, 3);
        writer.println("    }");
    }

    void printCloseTag(final PrintWriter writer, final String name,
            final int level) {
        printIntent(writer, level);
        writer.print("writer.print(\"</");
        writer.print(name);
        writer.println(">\");");
    }

    void printIntent(final PrintWriter writer, final int level) {
        for (int idx = 0; idx < level; idx++) {
            writer.print("  ");
        }
    }

    void printOpenTag(final PrintWriter writer, final String name,
            final int level) {
        printIntent(writer, level);
        writer.print("writer.print(\"<");
        writer.print(name);
        writer.println(">\");");
    }
}
