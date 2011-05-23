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

/**
 * NOT YET USED
 * 
 * @author jfk
 */

class MapEntity extends Entity {
    private static final String POST_FIX = "Map";
    private Entity[] typeArguments;

    public MapEntity(final String name) {
        super(name + POST_FIX);
    }

    @Override
    void compile(final Class clazz, final SchemaResolver resolver) {
        super.compile(clazz, resolver);

        final Class[] args = clazz.getActualTypeArguments();

        assert args != null && args.length == 2;

        typeArguments = new Entity[2];
        typeArguments[0] = resolver.resolve(args[0]);
        typeArguments[1] = resolver.resolve(args[1]);
    }

    @Override
    public <T> T visit(final EntityVisitor<T> visitor) {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return null; // TODO: visitor.visit(this);
    }
}
