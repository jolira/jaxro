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

package com.google.code.joliratools.bind.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Maps a class, an interface or an enum type to a XML Schema type.
 * <p>
 */
@Retention(SOURCE)
@Target( { TYPE })
public @interface RoType {
    /**
     * Name of the XML Schema type which the class is mapped.
     */
    String name() default "##default";

    /**
     * Specifies the order for XML Schema elements when class is mapped to a XML
     * Schema complex type.
     */
    String[] propOrder() default { "" };
}
