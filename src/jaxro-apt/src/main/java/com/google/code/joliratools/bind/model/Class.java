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
package com.google.code.joliratools.bind.model;

/**
 * Represents a class for the purposes of this library.
 * 
 * @author Joachim F. Kainz
 */
public interface Class extends AnnotatedElement {
    /**
     * Allows access to type arguments. This class would return an array
     * containing {@code String.class} if the class represent a {@code
     * java.util.Collection<String>}.
     * 
     * @return the type arguments passed to this class.
     */
    Class[] getActualTypeArguments();

    /**
     * Returns the {@code Class} representing the component type of an array. If
     * this class does not represent an array class this method returns null.
     * 
     * @return the {@code Class} representing the component type of this class
     *         if this class is an array
     */
    Class getComponentType();

    /**
     * @return the enumeration constants if this is an enumeration; otherwise
     *         {@code null} must be returned to indicate it is not an
     *         enumeration.
     */
    String[] getEnumConstants();

    /**
     * @return the interfaces implemented by this class
     */
    Class[] getInterfaces();

    /**
     * @return the methods implemented by this class
     */
    Method[] getMethods();

    /**
     * @return the fully qualified name of the class (e.g. java.lang.String)
     */
    String getName();

    /**
     * @return the superclass of this class or {@code null}, if there is not
     *         superclass.
     */
    Class getSuperclass();
}
