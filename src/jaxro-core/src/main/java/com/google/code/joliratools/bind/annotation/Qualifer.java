/**
 * (C) 2010 jolira (http://www.jolira.com). Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.google.code.joliratools.bind.annotation;

/**
 * A callback interface that provides users with a run-time facility to check if a particular property should accessed
 * or not.
 * 
 * @since 1.1.2
 * @see RoQualifier
 */
public interface Qualifer {
    /**
     * Determine if a property of an object should be accessed.
     * 
     * @param object
     *            the object
     * @param property
     *            the name of the property
     * @return {@code true} to indicate that the property should be written to the output stream
     */
    boolean shouldBeAccessed(Object object, String property);
}
