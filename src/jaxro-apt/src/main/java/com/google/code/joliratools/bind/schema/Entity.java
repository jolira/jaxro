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
 * Represents an entity in a schema.
 * 
 * @author jfk
 * @since 1.0
 * 
 */
public abstract class Entity {
    private boolean isCompiled = false;
    private final String name;
    private String className = null;

    /**
     * Create a new one.
     * 
     * @param name
     *            the name of the entity
     */
    protected Entity(final String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Entity)) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * @param resolver
     */
    void compile(final Class _clazz, final SchemaResolver resolver) {
        isCompiled = true;
        className = _clazz.getName();
    }

    /**
     * @return the name of the associated class.
     * @throws IllegalStateException
     *             when called before the class is compiled.
     */
    public String getClassName() {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return className;
    }

    /**
     * @return the name of the entity
     */
    public String getName() {
        assert name != null;

        return name;
    }

    /**
     * @return {@literal true} to indicate that the entity is compiled.
     */
    protected boolean isCompiled() {
        return isCompiled;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Investigate the internals of the entity.
     * 
     * @see "http://en.wikipedia.org/wiki/Visitor_pattern"
     * @param visitor
     *            the visitor
     * @return the result of the visit.
     */
    public abstract <T> T visit(final EntityVisitor<T> visitor);
}