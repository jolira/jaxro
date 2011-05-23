package com.google.code.joliratools.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

/**
 * @author jfk
 */
public class CompileQueueTest {
    private static final class MockClass implements Class {
        private final String name;

        MockClass(final String name) {
            this.name = name;

        }

        @Override
        public Class[] getActualTypeArguments() {
            fail();
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            fail();
            return null;
        }

        @Override
        public Class getComponentType() {
            fail();
            return null;
        }

        @Override
        public String[] getEnumConstants() {
            fail();
            return null;
        }

        @Override
        public Class[] getInterfaces() {
            fail();
            return null;
        }

        @Override
        public Method[] getMethods() {
            fail();
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class getSuperclass() {
            fail();
            return null;
        }
    }

    private static final int COUNT = 10000;

    /**
     *
     */
    @Test
    public void testAccess() {
        final CompileQueue q = new CompileQueue();

        for (int idx = 0; idx < COUNT; idx++) {
            final boolean added = q.add(new MockClass(Integer.toString(idx)));

            assertTrue(added);
        }

        for (int idx = COUNT; idx > 0; idx--) {
            final boolean added = q.add(new MockClass(Integer.toString(idx - 1)));

            assertFalse(added);
        }

        for (int idx = 0; idx < COUNT; idx++) {
            final boolean added = q.add(new MockClass(Integer.toString(idx)));

            assertFalse(added);
        }

        for (int idx = 0; idx < COUNT; idx++) {
            final Class next = q.next();

            assertNotNull(next);

            final String name = next.getName();

            assertEquals(Integer.toString(idx), name);
        }

        final Class next = q.next();

        assertNull(next);

        for (int idx = 0; idx < COUNT; idx++) {
            final boolean added = q.add(new MockClass(Integer.toString(idx)));

            assertFalse(added);
        }
    }
}
