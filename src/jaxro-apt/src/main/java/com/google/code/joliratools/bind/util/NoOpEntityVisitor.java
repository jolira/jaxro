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
package com.google.code.joliratools.bind.util;

import com.google.code.joliratools.bind.schema.ArrayEntity;
import com.google.code.joliratools.bind.schema.BuiltInEntity;
import com.google.code.joliratools.bind.schema.CollectionEntity;
import com.google.code.joliratools.bind.schema.ComplexEntity;
import com.google.code.joliratools.bind.schema.EntityVisitor;
import com.google.code.joliratools.bind.schema.EnumEntity;

/**
 * An implementation of an EntityVisitor that just returns null
 * 
 * @author Joachim F. Kainz
 * @param <T>
 *            the type to be returned by the soltuion
 */
public class NoOpEntityVisitor<T> implements EntityVisitor<T> {
    @Override
    public T visit(final ArrayEntity t) {
        return null;
    }

    @Override
    public T visit(final BuiltInEntity t) {
        return null;
    }

    @Override
    public T visit(final CollectionEntity t) {
        return null;
    }

    @Override
    public T visit(final ComplexEntity t) {
        return null;
    }

    @Override
    public T visit(final EnumEntity t) {
        return null;
    }
}