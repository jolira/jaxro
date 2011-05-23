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
 * Represents a minimal abstraction of an annotation for the purposes of this
 * library.
 * 
 * @author Joachim Kainz
 */
public interface Annotation {

    /**
     * @return the fully qualified name of the annotation (e.g.
     *         java.lang.Override)
     */
    String getName();

    /**
     * Enables access to the member variables of the annotation.
     * 
     * @param name
     *            the name of the member variable
     * @return the value specified or {@literal null}
     */
    Object getValue(String name);
}
