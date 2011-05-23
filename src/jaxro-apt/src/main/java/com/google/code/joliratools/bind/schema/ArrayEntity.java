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

import com.google.code.joliratools.bind.model.Class;

public final class ArrayEntity extends Entity {
    public static final String POST_FIX = "Array";
    private Entity componentType;

    ArrayEntity(final String name) {
        super(name + POST_FIX);
    }

    @Override
    void compile(final Class clazz, final SchemaResolver resolver) {
        super.compile(clazz, resolver);

        final Class _componentType = clazz.getComponentType();

        componentType = resolver.resolve(_componentType);
    }

    public Entity getComponentEntity() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return componentType;
    }

    public Property[] getProperties() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return new Property[] { new Property("entry", componentType,
                "unbounded") };
    }

    @Override
    public <T> T visit(final EntityVisitor<T> visitor) {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return visitor.visit(this);
    }
}
