/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General
 * Public License, Version 3.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.google.code.joliratools.bind.schema;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;

import com.google.code.joliratools.bind.annotation.RoRootElement;
import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

/**
 * The goal of this class is to encapsulate the generation of Types and elements
 * from information provided by java.lang.reflect.* as well as by the
 * abstractions used by APT.
 * 
 * @author Joachim
 */
public class Schema {
    private static String getCommonNamespace(final Class[] classes) {
        if (classes == null || classes.length < 1) {
            return "";
        }

        final String _name = classes[0].getName();
        String result = getPackageName(_name);

        for (int idx = 1; idx < classes.length; idx++) {
            final String name = classes[idx].getName();

            result = getCommonPrefix(result, name);
        }

        return result;
    }

    private static String getCommonPrefix(final String s1, final String s2) {
        final int l1 = s1.length();
        final int l2 = s2.length();
        int idx = 0;

        for (; idx < l1 && idx < l2; idx++) {
            final char c1 = s1.charAt(idx);
            final char c2 = s2.charAt(idx);

            if (c1 != c2) {
                break;
            }
        }

        return s1.substring(0, idx);
    }

    private static String getPackageName(final String name) {
        final int lastIndexOf = name.lastIndexOf('.');

        assert lastIndexOf != -1;

        return name.substring(0, lastIndexOf);
    }

    private static Collection<Entity> newEntities() {
        return new TreeSet<Entity>(new Comparator<Entity>() {
            @Override
            public int compare(final Entity e1, final Entity e2) {
                final String n1 = e1.getName();
                final String n2 = e2.getName();

                if (n1 == null) {
                    return n2 == null ? 0 : -1;
                }

                return n1.compareTo(n2);
            }
        });
    }

    private static Collection<Property> newProperties() {
        return new TreeSet<Property>(new Comparator<Property>() {
            @Override
            public int compare(final Property p1, final Property p2) {
                final String n1 = p1.getName();
                final String n2 = p2.getName();

                if (n1 == null) {
                    return n2 == null ? 0 : -1;
                }

                return n1.compareTo(n2);
            }
        });
    }

    private Entity[] entities = null;

    private Property[] rootElements = null;

    private final EntityFactory factory;

    private final String commonNamespace;

    /**
     * Create a new schema.
     * 
     * @param messager
     * @param classes
     * @param isDense
     */
    public Schema(final Messager messager, final Class[] classes,
            final boolean isDense) {
        commonNamespace = getCommonNamespace(classes);
        factory = new EntityFactory(messager, isDense);

        compile(classes == null ? new Class[0] : classes);
    }

    private void compile(final Class[] classes) {
        final Collection<Property> properties = newProperties();
        final Collection<Entity> _entities = newEntities();
        final Map<String, Entity> entityByClassName = factory.getBuiltIns();
        final SchemaResolver _resolver = newResolver(properties, _entities,
                entityByClassName);

        for (final Class clazz : classes) {
            compile(entityByClassName, _entities, properties, clazz, _resolver);
        }

        final int tsize = _entities.size();
        final int psize = properties.size();

        entities = _entities.toArray(new Entity[tsize]);
        rootElements = properties.toArray(new Property[psize]);
    }

    /**
     * Compile the schema.
     * 
     * @param entityByClassName
     * @param _entities
     * @param properties
     * @param clazz
     * @param resolver
     * @return the entity represented by the schema.
     */
    protected Entity compile(final Map<String, Entity> entityByClassName,
            final Collection<Entity> _entities,
            final Collection<Property> properties, final Class clazz,
            final SchemaResolver resolver) {
        try {
            final String name = clazz.getName();
            final Entity existing = entityByClassName.get(name);

            if (existing != null) {
                return existing;
            }

            if (entityByClassName.containsKey(name)) {
                return null;
            }

            final Entity entity = factory.create(clazz, resolver);

            entityByClassName.put(name, entity);

            if (entity == null) {
                return null;
            }

            _entities.add(entity);
            entity.compile(clazz, resolver);

            final Property rootElement = factory.createRootElement(clazz,
                    entity);

            if (rootElement == null) {
                return entity;
            }

            return compile(entityByClassName, _entities, properties, clazz,
                    resolver, name, entity, rootElement);
        } catch (final Throwable e) {
            throw new RuntimeException("Failed to process " + clazz, e);
        }
    }

    private Entity compile(final Map<String, Entity> entityByClassName,
            final Collection<Entity> _entities,
            final Collection<Property> properties, final Class clazz,
            final SchemaResolver resolver, final String name,
            final Entity entity, final Property rootElement) {
        properties.add(rootElement);

        final Class componentType = clazz.getComponentType();

        if (componentType == null) {
            // Create Array Entities for RoRootElement
            compile(entityByClassName, _entities, properties, new Class() {
                @Override
                public Class[] getActualTypeArguments() {
                    return null;
                }

                @Override
                public Annotation[] getAnnotations() {
                    return new Annotation[] { new Annotation() {
                        @Override
                        public String getName() {
                            return RoRootElement.class.getName();
                        }

                        @Override
                        public Object getValue(final String _name) {
                            return null;
                        }
                    } };
                }

                @Override
                public Class getComponentType() {
                    return clazz;
                }

                @Override
                public String[] getEnumConstants() {
                    return null;
                }

                @Override
                public Class[] getInterfaces() {
                    return null;
                }

                @Override
                public Method[] getMethods() {
                    return null;
                }

                @Override
                public String getName() {
                    return name + "[]";
                }

                @Override
                public Class getSuperclass() {
                    return null;
                }
            }, resolver);
        }

        return entity;
    }

    /**
     * @return the common namespace.
     */
    public String getCommonNamespace() {
        return commonNamespace;
    }

    /**
     * @return the entities found in the schema.
     */
    public Entity[] getEntities() {
        return entities;
    }

    /**
     * @return the root element
     */
    public Property[] getRootElements() {
        return rootElements;
    }

    private SchemaResolver newResolver(final Collection<Property> properties,
            final Collection<Entity> _entities,
            final Map<String, Entity> entityByClassName) {
        final SchemaResolver _resolver = new SchemaResolver() {
            @Override
            public Entity resolve(final Class clazz) {
                return compile(entityByClassName, _entities, properties, clazz,
                        this);
            }
        };
        return _resolver;
    }
}
