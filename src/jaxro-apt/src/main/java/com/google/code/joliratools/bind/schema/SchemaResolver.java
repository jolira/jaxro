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

import com.google.code.joliratools.bind.model.AnnotatedElement;
import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;

abstract class SchemaResolver {
    static Annotation getAnnotation(
            final AnnotatedElement element,
            final java.lang.Class<? extends java.lang.annotation.Annotation> annotation) {
        final Annotation[] annotations = element.getAnnotations();

        if (annotations == null || annotations.length < 1) {
            return null;
        }

        final String aName = annotation.getName();

        for (final Annotation anno : annotations) {
            final String name = anno.getName();

            if (aName.equals(name)) {
                return anno;
            }
        }

        return null;
    }

    abstract Entity resolve(final Class clazz);
}
