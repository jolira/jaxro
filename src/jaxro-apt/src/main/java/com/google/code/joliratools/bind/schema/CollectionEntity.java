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

import java.util.Collection;

import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.reflect.ClassAdapter;

/**
 * Represent a collection such as {@link Collection}.
 * 
 * @author jfk
 * @since 1.0
 * 
 */
public final class CollectionEntity extends Entity {
    /**
     * The postfix to be used to name collection entities.
     */
    public static final String POST_FIX = "Collection";
    private Entity typeArgument;

    CollectionEntity(final String name) {
        super(name + POST_FIX);
    }

    @Override
    void compile(final Class clazz, final SchemaResolver resolver) {
        final Class[] args = clazz.getActualTypeArguments();
        final Class arg = args[0];

        assert args.length == 1;

        typeArgument = resolver.resolve(arg);

        super.compile(new ClassAdapter(Collection.class) {
            @Override
            public Class[] getActualTypeArguments() {
                return args;
            }

            @Override
            public String getName() {
                final String baseName = super.getName();
                final String argName = arg.getName();
                final StringBuilder buf = new StringBuilder();

                buf.append(baseName);
                buf.append('<');
                buf.append(argName);
                buf.append('>');

                return buf.toString();
            }
        }, resolver);
    }

    /**
     * @return the properties of the collection.
     * @throws IllegalStateException
     *             thrown when called before the entity has been compiled.
     */
    public Property[] getProperties() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return new Property[] { new Property("entry", typeArgument, "unbounded") };
    }

    /**
     * @return the type argument
     * @throws IllegalStateException
     *             thrown when called before the entity has been compiled.
     */
    public Entity getTypeArgument() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return typeArgument;
    }

    @Override
    public <T> T visit(final EntityVisitor<T> visitor) {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return visitor.visit(this);
    }
}
