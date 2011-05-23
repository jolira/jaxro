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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.code.joliratools.bind.model.Annotation;

final class AnnotationAdapter implements Annotation {
    private final java.lang.annotation.Annotation annotation;

    AnnotationAdapter(final java.lang.annotation.Annotation annotation) {
        assert annotation != null;

        this.annotation = annotation;
    }

    @Override
    public String getName() {
        final Class<? extends java.lang.annotation.Annotation> clazz = annotation
                .annotationType();

        return clazz.getName();
    }

    @Override
    public Object getValue(final String name) {
        final Class<? extends java.lang.annotation.Annotation> clazz = annotation
                .getClass();

        try {
            final Method method = clazz.getMethod(name);

            return method.invoke(annotation);
        }
        catch (final SecurityException e) {
            throw new RuntimeException(e);
        }
        catch (final NoSuchMethodException e) {
            // no such value
            return null;
        }
        catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return annotation.toString();
    }
}
