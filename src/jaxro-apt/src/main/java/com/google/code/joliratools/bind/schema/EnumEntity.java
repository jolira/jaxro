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

public final class EnumEntity extends Entity {
    private Property[] properties;

    EnumEntity(final String name) {
        super(name);
    }

    @Override
    void compile(final Class clazz, final SchemaResolver resolver) {
        super.compile(clazz, resolver);

        final String[] enums = clazz.getEnumConstants();
        final Property[] props = new Property[enums.length];

        for (int idx = 0; idx < props.length; idx++) {
            props[idx] = new Property(enums[idx], BuiltInEntity.STRING);
        }

        properties = props;
    }

    public Property[] getProperties() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return properties;
    }

    @Override
    public <T> T visit(final EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
