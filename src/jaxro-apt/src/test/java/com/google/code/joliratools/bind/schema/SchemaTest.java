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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.code.joliratools.bind.annotation.RoTransient;
import com.google.code.joliratools.bind.annotation.RoType;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.reflect.ClassAdapter;
import com.google.code.joliratools.bind.schema.EntityTest.FailEverythingVisitor;

/**
 * @author jfk
 */
public class SchemaTest {
    @RoType
    static class Account {
        public double getBalance() {
            return 0.0;
        }

        public String getNumber() {
            return "1";
        }

        @RoTransient
        public String getPassword() {
            return "007";
        }

        public AccountStatus getStatus() {
            return AccountStatus.OPEN;
        }
    }

    @RoType
    enum AccountStatus {
        OPEN, CLOSED
    }

    @RoType
    static interface Customer {
        public Account getAccount(String number);

        public Account[] getAccounts();

        String getName();

        boolean isHappy();
    }

    /**
     * Test method for {@link Schema}.
     */
    @Test
    public void testCompile() {
        final Class clazz = new ClassAdapter(Customer.class);
        final Schema schema = new Schema(null, new Class[] { clazz }, false);
        final Entity[] types = schema.getEntities();

        assertEquals(4, types.length);

        final Map<String, Entity> typeByName = new HashMap<String, Entity>();

        for (final Entity type : types) {
            final String name = type.getName();

            typeByName.put(name, type);
        }

        assertEquals(4, typeByName.size());

        final Entity accountStatusType = typeByName.get("AccountStatus");

        accountStatusType.visit(new EntityTest.FailEverythingVisitor() {
            @Override
            public Void visit(final EnumEntity t) {
                final Property[] elems = t.getProperties();

                assertEquals(2, elems.length);
                assertEquals(AccountStatus.OPEN.name(), elems[0].getName());
                assertEquals(AccountStatus.CLOSED.name(), elems[1].getName());
                assertEquals(BuiltInEntity.STRING, elems[0].getType());
                assertEquals(BuiltInEntity.STRING, elems[1].getType());

                return null;
            }
        });

        final Entity accountType = typeByName.get("Account");

        accountType.visit(new FailEverythingVisitor() {
            @Override
            public Void visit(final ComplexEntity t) {
                final Property[] elems = t.getProperties();

                assertEquals(4, elems.length);

                return null;
            }
        });
    }
}
