/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.google.code.joliratools.bind.apt;

import static javax.lang.model.element.ElementKind.ENUM;

import java.util.ArrayList;
import java.util.Collection;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

class TypeElementAdapter extends ElementAdapter<TypeElement> implements Class {
    private String[] cacheConstants = null;

    public TypeElementAdapter(final AdaptorFactory factory, final TypeElement e) {
        super(factory, e);
    }

    @Override
    public Class[] getActualTypeArguments() {
        return null; // The element does not have any args, it only has params.
    }

    @Override
    public Class getComponentType() {
        return null;
    }

    @Override
    public synchronized String[] getEnumConstants() {
        if (cacheConstants != null) {
            return cacheConstants;
        }

        final boolean isEnum = isEnum();

        if (!isEnum) {
            return null;
        }

        final Collection<? extends Element> enclosed = element
                .getEnclosedElements();

        if (enclosed == null) {
            return null;
        }

        final Collection<VariableElement> fields = ElementFilter
                .fieldsIn(enclosed);

        if (fields == null) {
            return null;
        }

        final int size = fields.size();
        final String[] constants = new String[size];
        int idx = 0;

        for (final VariableElement var : fields) {
            constants[idx++] = var.toString();
        }

        cacheConstants = constants;

        return constants;
    }

    @Override
    public Class[] getInterfaces() {
        final Collection<? extends TypeMirror> ifaces = element.getInterfaces();

        if (ifaces == null) {
            return null;
        }

        final int size = ifaces.size();
        final Collection<Class> adapters = new ArrayList<Class>(size);

        for (final TypeMirror iface : ifaces) {
            final Class adapter = factory.getAdapter(iface);

            if (adapter != null) {
                adapters.add(adapter);
            }
        }

        final int asize = adapters.size();

        return adapters.toArray(new Class[asize]);
    }

    @Override
    public Method[] getMethods() {
        final Collection<? extends Element> enclosed = element
                .getEnclosedElements();

        if (enclosed == null) {
            return null;
        }

        final Collection<ExecutableElement> methods = ElementFilter
                .methodsIn(enclosed);

        if (methods == null) {
            return null;
        }

        final int size = methods.size();
        final Collection<Method> adapters = new ArrayList<Method>(size);

        for (final ExecutableElement method : methods) {
            final Method adapter = factory.getAdapter(method);

            if (adapter != null) {
                adapters.add(adapter);
            }
        }

        final int asize = adapters.size();

        return adapters.toArray(new Method[asize]);
    }

    @Override
    public String getName() {
        final Name qname = element.getQualifiedName();
        final String name = qname.toString();

        return name == null ? element.toString() : name;
    }

    @Override
    public Class getSuperclass() {
        final TypeMirror superclass = element.getSuperclass();

        if (superclass == null) {
            return null;
        }

        return factory.getAdapter(superclass);
    }

    private boolean isEnum() {
        final ElementKind kind = element.getKind();

        return kind.equals(ENUM);
    }

}
