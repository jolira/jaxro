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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.google.code.joliratools.bind.model.Class;

/**
 * 
 * @author Joachim Kainz
 * 
 */
public final class CompileQueue {
    private final Collection<String> names = new HashSet<String>();
    private final LinkedList<Class> pending = new LinkedList<Class>();

    public boolean add(final Class clazz) {
        final String name = clazz.getName();

        if (!names.add(name)) {
            return false;
        }

        final boolean added = pending.add(clazz);

        assert added : "inconsistent add; check for multi-threading access";

        return true;
    }

    public Class next() {
        if (pending.isEmpty()) {
            return null;
        }

        return pending.removeFirst();
    }
}
