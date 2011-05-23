/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.google.code.joliratools.bind.reflect;

import java.lang.reflect.Modifier;

import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

final class MethodAdapter extends AnnotatedElementAdapter implements Method {
    private final java.lang.reflect.Method method;

    public MethodAdapter(final java.lang.reflect.Method method) {
        super(method);

        this.method = method;
    }

    /**
     * @see AnnotatedElementAdapter#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Class[] getParameterTypes() {
        final java.lang.Class<?>[] types = method.getParameterTypes();
        final Class[] result = new Class[types.length];

        for (int idx = 0; idx < result.length; idx++) {
            result[idx] = new ClassAdapter(types[0]);
        }

        return result;
    }

    @Override
    public Class getReturnType() {
        final java.lang.Class<?> returnType = method.getReturnType();

        return new ClassAdapter(returnType);
    }

    /**
     * @see AnnotatedElementAdapter#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean isPublic() {
        final int modifiers = method.getModifiers();

        return Modifier.isPublic(modifiers);
    }

    @Override
    public boolean isStatic() {
        final int modifiers = method.getModifiers();

        return Modifier.isStatic(modifiers);
    }

    @Override
    public String toString() {
        return method.toString();
    }
}
