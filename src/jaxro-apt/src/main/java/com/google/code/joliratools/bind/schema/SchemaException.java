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
 * Throw when there are problems with the schema.
 * 
 * @author jfk
 * 
 */
public class SchemaException extends RuntimeException {
    private static final long serialVersionUID = 4965287145189927959L;

    /**
     * Create a new exception.
     * 
     * @param msg
     *            the message
     */
    public SchemaException(final String msg) {
        super(msg);
    }

}
