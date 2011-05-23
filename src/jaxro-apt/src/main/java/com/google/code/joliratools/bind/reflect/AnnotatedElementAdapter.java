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
package com.google.code.joliratools.bind.reflect;

import com.google.code.joliratools.bind.model.AnnotatedElement;
import com.google.code.joliratools.bind.model.Annotation;

/**
 * An adapter for {@link java.lang.reflect.AnnotatedElement}.
 * 
 * @author Joachim Kainz
 */
public class AnnotatedElementAdapter implements AnnotatedElement {
    private final java.lang.reflect.AnnotatedElement element;

    AnnotatedElementAdapter(final java.lang.reflect.AnnotatedElement element) {
        this.element = element;

        assert element != null;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AnnotatedElementAdapter)) {
            return false;
        }
        final AnnotatedElementAdapter other = (AnnotatedElementAdapter) obj;
        if (element == null) {
            if (other.element != null) {
                return false;
            }
        }
        else if (!element.equals(other.element)) {
            return false;
        }
        return true;
    }

    @Override
    public Annotation[] getAnnotations() {
        final java.lang.annotation.Annotation[] annotations = element
                .getAnnotations();
        final Annotation[] result = new Annotation[annotations.length];

        for (int idx = 0; idx < result.length; idx++) {
            result[idx] = new AnnotationAdapter(annotations[idx]);
        }

        return result;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (element == null ? 0 : element.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
