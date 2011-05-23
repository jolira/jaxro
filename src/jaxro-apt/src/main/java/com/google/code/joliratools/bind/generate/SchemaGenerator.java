/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.google.code.joliratools.bind.generate;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;

import com.google.code.joliratools.bind.schema.ArrayEntity;
import com.google.code.joliratools.bind.schema.BuiltInEntity;
import com.google.code.joliratools.bind.schema.CollectionEntity;
import com.google.code.joliratools.bind.schema.ComplexEntity;
import com.google.code.joliratools.bind.schema.Entity;
import com.google.code.joliratools.bind.schema.EntityVisitor;
import com.google.code.joliratools.bind.schema.EnumEntity;
import com.google.code.joliratools.bind.schema.Property;
import com.google.code.joliratools.bind.schema.Schema;
import com.google.code.joliratools.bind.util.NoOpEntityVisitor;

/**
 * Generate a schema file from the {@link Schema}. .
 * 
 * @author Joachim F. Kainz
 */
public abstract class SchemaGenerator {
    private final Schema schema;

    /**
     * Create a new generator.
     * 
     * @param schema
     *            the schema
     */
    public SchemaGenerator(final Schema schema) {
        this.schema = schema;
    }

    /**
     * Subclasses need to provide the ability to actually create the schema files.
     * 
     * @param pkg
     *            the name of the package for which a source file should be generated
     * @return a print writer to write into the newly created source file.
     */
    protected abstract PrintWriter createTextFile(String pkg);

    /**
     * Run the generation process.
     */
    public void generate() {
        final String pkg = schema.getCommonNamespace();
        final PrintWriter writer = createTextFile(pkg);

        try {
            generate(writer);
        } finally {
            writer.close();
        }
    }

    private void generate(final PrintWriter writer) {
        // TODO: final String targetNS = "http://code.google.com"

        writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
        writer.println("<xs:schema " + "xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" "
                + "attributeFormDefault=\"unqualified\" "
                // TODO: SHould be qualified
                + "elementFormDefault=\"unqualified\" "
                // TODO: + "targetNamespace=\"" + targetNS
                + ">");

        generateEntities(writer);
        generateProperties(writer);

        writer.println("</xs:schema>");
    }

    protected void generate(final PrintWriter writer, final ArrayEntity type) {
        final String name = type.getName();

        writer.print("  <xs:complexType name=\"");
        writer.print(name);
        writer.println("\">");
        writer.println("    <xs:sequence>");

        final Property[] elems = type.getProperties();

        generate(writer, elems, null, 3);

        writer.println("    </xs:sequence>");
        writer.println("  </xs:complexType>");
    }

    protected void generate(final PrintWriter writer, final CollectionEntity type) {
        final String name = type.getName();

        writer.print("  <xs:complexType name=\"");
        writer.print(name);
        writer.println("\">");
        writer.println("    <xs:sequence>");

        final Property[] elems = type.getProperties();

        generate(writer, elems, null, 3);

        writer.println("    </xs:sequence>");
        writer.println("  </xs:complexType>");
    }

    protected void generate(final PrintWriter writer, final ComplexEntity entity) {
        final String name = entity.getName();

        writer.print("  <xs:complexType name=\"");
        writer.print(name);
        writer.println("\">");
        writer.println("    <xs:all>");

        final Collection<String> names = new HashSet<String>();
        final Entity[] superEntities = entity.getSuperEntities();

        if (superEntities != null) {
            for (final Entity _super : superEntities) {
                _super.visit(new NoOpEntityVisitor<Void>() {
                    @Override
                    public Void visit(final ComplexEntity t) {
                        final Property[] elems = t.getProperties();

                        generate(writer, elems, names, 3);

                        return null;
                    }
                });
            }
        }

        final Property[] elems = entity.getProperties();

        generate(writer, elems, names, 3);

        writer.println("    </xs:all>");
        writer.println("  </xs:complexType>");
    }

    protected void generate(final PrintWriter writer, final EnumEntity entity) {
        final String name = entity.getName();

        writer.print("  <xs:simpleType name=\"");
        writer.print(name);
        writer.println("\">");
        writer.println("    <xs:restriction base=\"xs:string\">");

        final Property[] elems = entity.getProperties();
        final Collection<String> names = new HashSet<String>();

        for (final Property element : elems) {
            final String ename = element.getName();

            if (!names.add(ename)) {
                continue;
            }

            writer.print("      <xs:enumeration value=\"");
            writer.print(ename);
            writer.println("\"/>");
        }

        writer.println("    </xs:restriction>");
        writer.println("  </xs:simpleType>");
    }

    private void generate(final PrintWriter writer, final Property element, final Collection<String> names,
            final int level) {
        final String ename = element.getName();

        if (names != null && !names.add(ename)) {
            return;
        }

        final Entity etype = element.getType();
        final String tname = etype.getName();
        final String maxOccurs = element.getMaxOccurs();

        for (int idx = 0; idx < level; idx++) {
            writer.print("  ");
        }

        writer.print("<xs:element name=\"");
        writer.print(ename);
        writer.print("\" type=\"");
        writer.print(tname);
        writer.print('"');

        if (maxOccurs != null) {
            writer.print(" maxOccurs=\"");
            writer.print(maxOccurs);
            writer.print('"');
        }

        writer.println("/>");
    }

    void generate(final PrintWriter writer, final Property[] elements, final Collection<String> names, final int level) {
        for (final Property element : elements) {
            generate(writer, element, names, level);
        }
    }

    private void generateEntities(final PrintWriter writer) {
        final Entity[] entities = schema.getEntities();

        for (final Entity entity : entities) {
            entity.visit(new EntityVisitor<Void>() {
                @Override
                public Void visit(final ArrayEntity t) {
                    generate(writer, t);
                    return null;
                }

                @Override
                public Void visit(final BuiltInEntity t) {
                    throw new Error("cannot genrate built-ins");
                }

                @Override
                public Void visit(final CollectionEntity t) {
                    generate(writer, t);
                    return null;
                }

                @Override
                public Void visit(final ComplexEntity t) {
                    generate(writer, t);
                    return null;
                }

                @Override
                public Void visit(final EnumEntity t) {
                    generate(writer, t);
                    return null;
                }
            });
        }
    }

    private void generateProperties(final PrintWriter writer) {
        final Property[] roots = schema.getRootElements();
        final Collection<String> names = new HashSet<String>();

        generate(writer, roots, names, 1);
    }
}
