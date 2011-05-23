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
package com.google.code.joliratools.bind.apt;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import java.util.Collection;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

class ExecutableElementAdapter extends ElementAdapter<ExecutableElement>
        implements Method {

    public ExecutableElementAdapter(final AdaptorFactory factory,
            final ExecutableElement element) {
        super(factory, element);
    }

    @Override
    public String getName() {
        final Name name = element.getSimpleName();

        return name.toString();
    }

    @Override
    public Class[] getParameterTypes() {
        final List<? extends VariableElement> params = element.getParameters();
        final int size = params.size();
        final Class[] result = new Class[size];
        int idx = 0;

        for (final VariableElement param : params) {
            final TypeMirror type = param.asType();

            result[idx++] = factory.getAdapter(type);

            assert result[idx - 1] != null;
        }

        return result;
    }

    @Override
    public Class getReturnType() {
        final TypeMirror returnType = element.getReturnType();

        return factory.getAdapter(returnType);
    }

    @Override
    public boolean isPublic() {
        final Collection<Modifier> modifiers = element.getModifiers();

        return modifiers != null && modifiers.contains(PUBLIC);
    }

    @Override
    public boolean isStatic() {
        final Collection<Modifier> modifiers = element.getModifiers();

        return modifiers != null && modifiers.contains(STATIC);
    }
}
