/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.google.code.joliratools.bind.reflect;

import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

/**
 * An adapter for a java class.
 * 
 * @author Joachim Kainz
 */
/**
 * @author jfk
 */
public class ClassAdapter extends AnnotatedElementAdapter implements Class {
    private final java.lang.Class<?> clazz;

    /**
     * Create an adapter for a java class.
     * 
     * @param clazz
     */
    public ClassAdapter(final java.lang.Class<?> clazz) {
        super(clazz);

        this.clazz = clazz;
    }

    /**
     * @see AnnotatedElementAdapter#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public Class[] getActualTypeArguments() {
        return null;
    }

    @Override
    public Class getComponentType() {
        final java.lang.Class<?> componentType = clazz.getComponentType();

        if (componentType == null) {
            return null;
        }

        return new ClassAdapter(componentType);
    }

    @Override
    public String[] getEnumConstants() {
        if (!isEnum()) {
            return null;
        }

        final Object[] enums = clazz.getEnumConstants();

        if (enums == null) {
            return null;
        }

        final String[] result = new String[enums.length];

        for (int idx = 0; idx < result.length; idx++) {
            result[idx] = enums[idx].toString();
        }

        return result;
    }

    @Override
    public Class[] getInterfaces() {
        final java.lang.Class<?>[] ifaces = clazz.getInterfaces();

        if (ifaces == null) {
            return null;
        }

        final Class[] results = new Class[ifaces.length];

        for (int idx = 0; idx < results.length; idx++) {
            results[idx] = new ClassAdapter(ifaces[idx]);
        }

        return results;
    }

    @Override
    public Method[] getMethods() {
        final java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        final Method[] result = new Method[methods.length];

        for (int idx = 0; idx < result.length; idx++) {
            result[idx] = new MethodAdapter(methods[idx]);
        }

        return result;
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Class getSuperclass() {
        final java.lang.Class<?> superclass = clazz.getSuperclass();

        if (superclass == null) {
            return null;
        }

        return new ClassAdapter(superclass);
    }

    /**
     * @see AnnotatedElementAdapter#hashCode()
     */
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    private boolean isEnum() {
        return clazz.isEnum();
    }
}
