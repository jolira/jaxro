/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.google.code.joliratools.bind.apt;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.type.DeclaredType;

import com.google.code.joliratools.bind.model.Annotation;

class AnnotationMirrorAdapter implements Annotation {
    private final AnnotationMirror a;

    public AnnotationMirrorAdapter(final AnnotationMirror a) {
        this.a = a;

    }

    @Override
    public String getName() {
        final DeclaredType type = a.getAnnotationType();

        return type.toString();
    }

    @Override
    public Object getValue(final String name) {
        final Map<? extends ExecutableElement, ? extends AnnotationValue> vals = a
                .getElementValues();
        final Set<?> entries = vals.entrySet();

        for (final Object _entry : entries) {
            @SuppressWarnings("unchecked")
            final Map.Entry<ExecutableElement, AnnotationValue> entry = (Map.Entry<ExecutableElement, AnnotationValue>) _entry;
            final ExecutableElement key = entry.getKey();
            final Name ename = key.getSimpleName();

            if (ename.contentEquals(name)) {
                final AnnotationValue value = entry.getValue();

                return value.getValue();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return a.toString();
    }
}
