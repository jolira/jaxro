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
 * Represents a method for the purposes of this library.
 * 
 * @author Joachim F. Kainz
 */
public interface Method extends AnnotatedElement {
    /**
     * @return the name of the method (such as {@code "toString"}
     */
    String getName();

    /**
     * @return the parameter types
     */
    Class[] getParameterTypes();

    /**
     * @return the return type
     */
    Class getReturnType();

    /**
     * @return {@code true} to indicate the method is public
     */
    boolean isPublic();

    /**
     * @return {@code true} to indicate the method is static
     */
    boolean isStatic();
}
