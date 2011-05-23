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

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

class DeclaredTypeAdapter implements Class {
    private final AdaptorFactory factory;
    private final DeclaredType t;
    private final Class element;

    DeclaredTypeAdapter(final AdaptorFactory factory, final DeclaredType t) {
        this.factory = factory;
        this.t = t;
        final Element e = t.asElement();

        element = factory.getAdapter(e);

        assert element != null;
    }

    @Override
    public Class[] getActualTypeArguments() {
        final List<? extends TypeMirror> args = t.getTypeArguments();

        if (args == null) {
            return null;
        }

        final int size = args.size();
        final Class[] adapters = new Class[size];
        int idx = 0;

        for (final TypeMirror arg : args) {
            final Class adapter = factory.getAdapter(arg);

            assert adapter != null;

            adapters[idx++] = adapter;
        }

        return adapters;
    }

    @Override
    public Annotation[] getAnnotations() {
        return element.getAnnotations();
    }

    @Override
    public Class getComponentType() {
        return null;
    }

    @Override
    public String[] getEnumConstants() {
        return element.getEnumConstants();
    }

    @Override
    public Class[] getInterfaces() {
        return element.getInterfaces();
    }

    @Override
    public Method[] getMethods() {
        return element.getMethods();
    }

    @Override
    public String getName() {
        return t.toString();
    }

    @Override
    public Class getSuperclass() {
        return element.getSuperclass();
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
