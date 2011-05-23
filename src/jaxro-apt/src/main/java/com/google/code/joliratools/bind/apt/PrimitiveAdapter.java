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

import javax.lang.model.type.PrimitiveType;

import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

class PrimitiveAdapter implements Class {
    private final PrimitiveType t;

    PrimitiveAdapter(final PrimitiveType t) {
        this.t = t;
    }

    @Override
    public Class[] getActualTypeArguments() {
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return null;
    }

    @Override
    public Class getComponentType() {
        return null;
    }

    @Override
    public String[] getEnumConstants() {
        return null;
    }

    @Override
    public Class[] getInterfaces() {
        return null;
    }

    @Override
    public Method[] getMethods() {
        return null;
    }

    @Override
    public String getName() {
        return t.toString();
    }

    @Override
    public Class getSuperclass() {
        return null;
    }

    @Override
    public String toString() {
        return t.toString();
    }
}
