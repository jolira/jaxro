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

import java.util.Collection;
import java.util.Map;

import com.google.code.joliratools.bind.annotation.RoRootElement;
import com.google.code.joliratools.bind.annotation.RoTransient;
import com.google.code.joliratools.bind.annotation.RoType;
import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;

final class EntityFactory {
    private static final String DEFAULT = "##default";

    private static String getName(final Class clazz, final Annotation annotation) {
        if (annotation == null) {
            return getSimpleName(clazz);
        }

        final String name = (String) annotation.getValue("name");

        if (name != null && !DEFAULT.equals(name)) {
            return name;
        }

        return getSimpleName(clazz);
    }

    private static String getSimpleName(final Class clazz) {
        final String className = clazz.getName();

        return getSimpleName(className);
    }

    private static String getSimpleName(final String name) {
        final String _name = stripTypeArguments(name);
        final int dollarPos = _name.lastIndexOf('$');
        final int dotPos = _name.lastIndexOf('.');
        final int pos = dollarPos > dotPos ? dollarPos : dotPos;

        return _name.substring(pos + 1);
    }

    private static boolean isTransient(final Class clazz) {
        final Annotation annotation = getAnnotation(clazz, RoTransient.class);

        return annotation != null;
    }

    private static java.lang.Class<?> loadClass(final String className) {
        try {
            return java.lang.Class.forName(className);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

    private static String stripTypeArguments(final String name) {
        final int lessThanPos = name.lastIndexOf('<');
        final String _name = lessThanPos == -1 ? name : name.substring(0,
                lessThanPos);
        return _name;
    }

    private final boolean isDense;
    private final Messager messager;

    EntityFactory(final Messager messager, final boolean isDense) {
        this.messager = messager;
        this.isDense = isDense;
    }

    public Entity create(final Class clazz, final SchemaResolver resolver) {
        if (isTransient(clazz)) {
            return null;
        }

        final String classname = clazz.getName();
        final Class componentType = clazz.getComponentType();

        if (componentType != null) {
            return createArray(componentType, resolver);
        }

        final Annotation annotation = getAnnotation(clazz, RoType.class);
        final String name = getName(clazz, annotation);
        final String[] constants = clazz.getEnumConstants();
        final boolean isEnum = constants != null;

        assert classname != null;

        if (classname.startsWith("java.")) {
            return createSelectedJavaType(classname, clazz, resolver);
        }

        if (isDense && annotation == null) {
            final Annotation rootAnnotation = getAnnotation(clazz,
                    RoRootElement.class);

            if (rootAnnotation == null) {
                return null;
            }
        }

        if (isEnum) {
            return new EnumEntity(name);
        }

        return new ComplexEntity(name);
    }

    private Entity createArray(final Class componentType,
            final SchemaResolver resolver) {
        if (isTransient(componentType)) {
            return null;
        }

        final Entity componentEntity = resolver.resolve(componentType);

        if (componentEntity == null) {
            return null;
        }

        final Annotation annotation = getAnnotation(componentType, RoType.class);
        final String name = getName(componentType, annotation);

        return new ArrayEntity(name);
    }

    private Entity createCollection(final Class clazz,
            final SchemaResolver resolver) {
        final Class[] args = clazz.getActualTypeArguments();

        if (args == null || args.length != 1) {
            messager.warning(clazz, "skipping raw type");

            return null;
        }

        final Class arg = args[0];

        if (isTransient(arg)) {
            return null;
        }

        final Entity resolved = resolver.resolve(arg);

        if (resolved == null) {
            return null;
        }

        final Annotation annotation = getAnnotation(arg, RoType.class);
        final String name = getName(arg, annotation);

        return new CollectionEntity(name);
    }

    // private Entity createMap(final Class clazz) {
    // final Class[] args = clazz.getActualTypeArguments();
    //
    // if (args == null || args.length != 2) {
    // messager.warning(clazz, "skipping raw type");
    //
    // return null;
    // }
    //
    // final String[] names = new String[args.length];
    // final String[] namespaces = new String[args.length];
    // int idx = 0;
    //
    // for (final Class arg : args) {
    // if (isTransient(arg)) {
    // return null;
    // }
    //
    // final Annotation annotation = getAnnotation(arg, RoType.class);
    //
    // if (isTense && annotation == null) {
    // return null;
    // }
    //
    // names[idx] = getName(arg, annotation);
    // namespaces[idx++] = getNamespace(arg, annotation);
    // }
    //
    // return new MapEntity(names[0] + names[1], namespaces[1]);
    // }

    Property createRootElement(final Class clazz, final Entity type) {
        final Annotation annotation = getAnnotation(clazz, RoRootElement.class);

        if (annotation == null) {
            return null;
        }

        String name = getName(clazz, annotation);

        if (name.endsWith("[]")) {
            final int length = name.length();

            name = name.substring(0, length - 2) + ArrayEntity.POST_FIX;
        }

        final String pname = Property.getPropertyName(name);
        // final String namespace = getNamespace(clazz, annotation);

        return new Property(pname, type);
    }

    private Entity createSelectedJavaType(final String className,
            final Class clazz, final SchemaResolver resolver) {
        final String classname = stripTypeArguments(className);
        final java.lang.Class<?> cls = loadClass(classname);

        if (cls == null) {
            return null;
        }

        if (Collection.class.isAssignableFrom(cls)) {
            return createCollection(clazz, resolver);
        }

        // if (Map.class.isAssignableFrom(cls)) {
        // return createMap(clazz);
        // }

        return null;
    }

    Map<String, Entity> getBuiltIns() {
        return BuiltInEntity.getBuiltIns();
    }
}
