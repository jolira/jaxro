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

/**
 * A visitor for entities.
 * 
 * @see "http://en.wikipedia.org/wiki/Visitor_pattern"
 * @author jfk
 * @param <T>
 */
public interface EntityVisitor<T> {
    /**
     * Called is the visited entity is an {@link ArrayEntity}.
     * 
     * @param entity
     *            the entity to visit
     * @return the result of the visit.
     */
    T visit(ArrayEntity entity);

    /**
     * Called is the visited entity is an {@link BuiltInEntity}.
     * 
     * @param entity
     *            the entity to visit
     * @return the result of the visit.
     */
    T visit(BuiltInEntity entity);

    /**
     * Called is the visited entity is an {@link CollectionEntity}.
     * 
     * @param entity
     *            the entity to visit
     * @return the result of the visit.
     */
    T visit(CollectionEntity entity);

    /**
     * Called is the visited entity is an {@link ComplexEntity}.
     * 
     * @param entity
     *            the entity to visit
     * @return the result of the visit.
     */
    T visit(ComplexEntity entity);

    /**
     * Called is the visited entity is an {@link EnumEntity}.
     * 
     * @param entity
     *            the entity to visit
     * @return the result of the visit.
     */
    T visit(EnumEntity entity);
}
