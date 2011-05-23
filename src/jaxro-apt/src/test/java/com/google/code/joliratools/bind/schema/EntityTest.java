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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.google.code.joliratools.bind.annotation.RoType;
import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;
import com.google.code.joliratools.bind.reflect.ClassAdapter;

/**
 * @author jfk
 */
public class EntityTest {
    @RoType
    static class Account {
        public double getBalance() {
            return 0.0;
        }

        public String getNumber() {
            return "1";
        }

        public AccountStatus getStatus() {
            return AccountStatus.OPEN;
        }
    }

    @RoType
    enum AccountStatus {
        OPEN, CLOSED
    }

    @RoType
    static interface Customer {
        public Account getAccount(String number);

        public Account[] getAccounts();

        String getName();

        boolean isHappy();
    }

    static class FailEverythingVisitor implements EntityVisitor<Void> {
        @Override
        public Void visit(final ArrayEntity t) {
            fail();
            return null;
        }

        @Override
        public Void visit(final BuiltInEntity t) {
            fail();
            return null;
        }

        @Override
        public Void visit(final CollectionEntity collectionEntity) {
            fail();
            return null;
        }

        @Override
        public Void visit(final ComplexEntity t) {
            fail();
            return null;
        }

        @Override
        public Void visit(final EnumEntity t) {
            fail();
            return null;
        }
    }

    private static final String TEST_TYPE_NAME = "TestType";

    /**
     * 
     */
    @Test
    public void testCompileComplexType() {
        final ClassAdapter adapter = new ClassAdapter(Customer.class);
        final Map<String, Entity> typeByClassName = BuiltInEntity.getBuiltIns();
        final EntityFactory factory = new EntityFactory(null, false);
        final SchemaResolver resolver = new SchemaResolver() {
            @Override
            public Entity resolve(final Class clazz) {
                final String className = clazz.getName();
                final Entity existing = typeByClassName.get(className);

                if (existing != null) {
                    return existing;
                }

                final Entity _type = factory.create(clazz, this);

                if (_type == null) {
                    return null;
                }

                typeByClassName.put(className, _type);
                _type.compile(clazz, this);

                return _type;
            }
        };
        final Entity type = factory.create(adapter, resolver);

        typeByClassName.put(Customer.class.getName(), type);
        type.compile(adapter, resolver);

        final String typeName = type.getName();

        assertEquals("Customer", typeName);

        type.visit(new FailEverythingVisitor() {
            @Override
            public Void visit(final ComplexEntity t) {
                final Property[] elements = t.getProperties();

                assertEquals(3, elements.length);
                return null;
            }
        });
    }

    /**
     * 
     */
    @Test
    public void testCompileEnumType() {
        final ClassAdapter adapter = new ClassAdapter(AccountStatus.class);
        final Map<String, Entity> typeByClassName = BuiltInEntity.getBuiltIns();
        final EntityFactory factory = new EntityFactory(null, false);
        final SchemaResolver resolver = new SchemaResolver() {
            @Override
            public Entity resolve(final Class clazz) {
                final String className = clazz.getName();
                final Entity existing = typeByClassName.get(className);

                if (existing != null) {
                    return existing;
                }

                final Entity _type = factory.create(clazz, this);

                if (_type == null) {
                    return null;
                }

                typeByClassName.put(className, _type);
                _type.compile(clazz, this);

                return _type;
            }
        };
        final Entity type = factory.create(adapter, resolver);

        typeByClassName.put(AccountStatus.class.getName(), type);
        type.compile(adapter, resolver);

        final String typeName = type.getName();

        assertEquals("AccountStatus", typeName);

        type.visit(new FailEverythingVisitor() {
            @Override
            public Void visit(final EnumEntity t) {
                final Property[] elements = t.getProperties();

                assertEquals(2, elements.length);
                assertEquals(AccountStatus.OPEN.name(), elements[0].getName());
                assertEquals(AccountStatus.CLOSED.name(), elements[1].getName());

                return null;
            }
        });
    }

    /**
     * 
     */
    @Test
    public void testNameNull() {
        final Class clazz = new Class() {
            @Override
            public Class[] getActualTypeArguments() {
                fail();
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[] { new Annotation() {
                    @Override
                    public String getName() {
                        return RoType.class.getName();
                    }

                    @Override
                    public Object getValue(final String name) {
                        if ("name".equals(name)) {
                            return null;
                        }

                        if ("namespace".equals(name)) {
                            return null;
                        }

                        if ("propOrder".equals(name)) {
                            return null;
                        }

                        return null;
                    }
                } };
            }

            @Override
            public Class getComponentType() {
                return null;
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
                return new Method[0];
            }

            @Override
            public String getName() {
                return TEST_TYPE_NAME;
            }

            @Override
            public Class getSuperclass() {
                return null;
            }
        };
        final EntityFactory factory = new EntityFactory(null, false);
        final SchemaResolver resolver = new SchemaResolver() {
            @Override
            public Entity resolve(final Class _clazz) {
                fail();
                return null;
            }
        };
        final Entity type = factory.create(clazz, resolver);

        type.compile(clazz, resolver);

        final String typeName = type.getName();

        assertEquals(TEST_TYPE_NAME, typeName);
    }

    /**
     * 
     */
    @Test
    public void testNameSpecifiedInAnnotation() {
        final Class clazz = new Class() {
            @Override
            public Class[] getActualTypeArguments() {
                fail();
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[] { new Annotation() {
                    @Override
                    public String getName() {
                        return RoType.class.getName();
                    }

                    @Override
                    public Object getValue(final String name) {
                        if ("name".equals(name)) {
                            return TEST_TYPE_NAME;
                        }

                        if ("namespace".equals(name)) {
                            return null;
                        }

                        if ("propOrder".equals(name)) {
                            return null;
                        }

                        return null;
                    }
                } };
            }

            @Override
            public Class getComponentType() {
                return null;
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
                return new Method[0];
            }

            @Override
            public String getName() {
                return "com.fideo.x";
            }

            @Override
            public Class getSuperclass() {
                return null;
            }
        };
        final SchemaResolver resolver = new SchemaResolver() {
            @Override
            public Entity resolve(final Class _clazz) {
                fail();
                return null;
            }
        };
        final EntityFactory factory = new EntityFactory(null, false);
        final Entity type = factory.create(clazz, resolver);

        type.compile(clazz, resolver);

        final String typeName = type.getName();

        assertEquals(TEST_TYPE_NAME, typeName);
    }
}
