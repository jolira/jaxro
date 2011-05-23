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

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

final class AdaptorFactoryImpl implements AdaptorFactory {
    @Override
    public Annotation getAdapter(final AnnotationMirror a) {
        return new AnnotationMirrorAdapter(a);
    }

    protected Class getAdapter(final ArrayType t) {
        return new ArrayTypeAdapter(AdaptorFactoryImpl.this, t);
    }

    protected Class getAdapter(final DeclaredType t) {
        return new DeclaredTypeAdapter(AdaptorFactoryImpl.this, t);
    }

    @Override
    public Class getAdapter(final Element e) {
        return e.accept(new SimpleElementVisitor6<Class, Void>() {
            @Override
            public Class visitType(final TypeElement _e, final Void p) {
                return getAdapter(_e);
            }
        }, null);
    }

    @Override
    public Method getAdapter(final ExecutableElement e) {
        return new ExecutableElementAdapter(this, e);
    }

    protected Class getAdapter(final PrimitiveType t) {
        return new PrimitiveAdapter(t);
    }

    protected Class getAdapter(final TypeElement e) {
        return new TypeElementAdapter(this, e);
    }

    @Override
    public Class getAdapter(final TypeMirror m) {
        return m.accept(new SimpleTypeVisitor6<Class, Void>() {
            @Override
            public Class visitArray(final ArrayType t, final Void p) {
                return getAdapter(t);
            }

            @Override
            public Class visitDeclared(final DeclaredType t, final Void p) {
                return getAdapter(t);
            }

            @Override
            public Class visitPrimitive(final PrimitiveType t, final Void p) {
                return getAdapter(t);
            }
        }, null);
    }
}
