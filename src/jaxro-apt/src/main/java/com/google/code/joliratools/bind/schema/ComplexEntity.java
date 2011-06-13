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

import static com.google.code.joliratools.bind.schema.SchemaResolver.getAnnotation;

import java.util.ArrayList;
import java.util.Collection;

import com.google.code.joliratools.bind.annotation.RoType;
import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

/**
 * Represents classes.
 * 
 * @author jfk
 * 
 */
public final class ComplexEntity extends Entity {
    private static Property findElement(final Property[] elems,
            final String property) {
        for (final Property elem : elems) {
            final String elemName = elem.getName();

            if (property.equals(elemName)) {
                return elem;
            }
        }

        return null;
    }

    private static String[] getPropOrder(final Class clazz) {
        final Annotation annotation = getAnnotation(clazz, RoType.class);

        if (annotation == null) {
            return null;
        }

        final String[] propOrder = (String[]) annotation.getValue("propOrder");

        if (propOrder == null || propOrder.length == 1
                && "".equals(propOrder[0])) {
            return null;
        }

        return propOrder;
    }

    private Property[] properties = null;
    private Entity[] superentities = null;

    ComplexEntity(final String name) {
        super(name);
    }

    @Override
    void compile(final Class clazz, final SchemaResolver resolver) {
        super.compile(clazz, resolver);

        final String[] propOrder = getPropOrder(clazz);
        final Property[] props = compileProperties(clazz, resolver);

        properties = sort(props, propOrder);

        final Collection<Entity> adapted = new ArrayList<Entity>();
        final Class _superclass = clazz.getSuperclass();

        if (_superclass != null) {
            final Entity resolved = resolver.resolve(_superclass);

            if (resolved != null) {
                adapted.add(resolved);
            }
        }

        final Class[] ifaces = clazz.getInterfaces();

        if (ifaces != null) {
            for (final Class iface : ifaces) {
                final Entity resolved = resolver.resolve(iface);

                if (resolved != null) {
                    adapted.add(resolved);
                }
            }
        }

        final int size = adapted.size();

        superentities = adapted.toArray(new Entity[size]);
    }

    private Property[] compileProperties(final Class clazz,
            final SchemaResolver resolver) {
        final Method[] methods = clazz.getMethods();

        if (methods == null) {
            return new Property[0];
        }

        final Collection<Property> props = new ArrayList<Property>();

        for (final Method method : methods) {
            final Property property = Property.createIfApplicable(method,
                    resolver);

            if (property != null) {
                props.add(property);
            }
        }

        final int size = props.size();

        return props.toArray(new Property[size]);
    }

    /**
     * @return the properties of the class
     * @throws IllegalStateException
     *             thrown when called before the entity has been compiled.
     */
    public Property[] getProperties() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return properties;
    }

    /**
     * @return the super-class and interfaces.
     * @throws IllegalStateException
     *             thrown when called before the entity has been compiled.
     */
    public Entity[] getSuperEntities() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return superentities;
    }

    private Property[] sort(final Property[] elems, final String[] propOrder) {
        if (propOrder == null) {
            return elems;
        }

        final Property[] result = new Property[propOrder.length];

        for (int idx = 0; idx < result.length; idx++) {
            final String property = propOrder[idx];
            final Property element = findElement(elems, property);

            if (element == null) {
                throw new SchemaException("no element found for property "
                        + property + " while processing type " + this);
            }

            result[idx] = element;
        }

        return result;
    }

    @Override
    public <T> T visit(final EntityVisitor<T> visitor) {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return visitor.visit(this);
    }
}
