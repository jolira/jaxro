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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import com.google.code.joliratools.bind.model.AnnotatedElement;
import com.google.code.joliratools.bind.model.Annotation;

class ElementAdapter<T extends Element> implements AnnotatedElement {
    protected final T element;
    protected final AdaptorFactory factory;

    ElementAdapter(final AdaptorFactory factory, final T element) {
        this.factory = factory;
        this.element = element;

        assert factory != null;
        assert element != null;
    }

    @Override
    public Annotation[] getAnnotations() {
        final List<? extends AnnotationMirror> mirrors = element
                .getAnnotationMirrors();

        if (mirrors == null) {
            return null;
        }

        final int size = mirrors.size();
        final Collection<Annotation> result = new ArrayList<Annotation>(size);

        for (final AnnotationMirror mirror : mirrors) {
            final Annotation anno = factory.getAdapter(mirror);

            if (anno != null) {
                result.add(anno);
            }
        }

        final int resultSize = result.size();

        return result.toArray(new Annotation[resultSize]);
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
