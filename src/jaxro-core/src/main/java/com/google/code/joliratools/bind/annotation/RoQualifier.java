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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows the user to specify a class that controls whether a particular method
 * is being called.
 * 
 * @since 1.1.2
 */
@Retention(SOURCE)
@Target( { METHOD })
public @interface RoQualifier {
    /**
     * Name of the XML Schema type which the class is mapped.
     */
    Class<? extends Qualifer> value() default Qualifer.class;
}
