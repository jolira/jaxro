/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.google.code.joliratools.bind.schema;

import static com.google.code.joliratools.bind.schema.SchemaResolver.getAnnotation;

import com.google.code.joliratools.bind.annotation.Qualifer;
import com.google.code.joliratools.bind.annotation.RoQualifier;
import com.google.code.joliratools.bind.annotation.RoTransient;
import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

/**
 * Represents a property.
 * 
 * @author jfk
 * 
 */
public final class Property {
    /**
     * Create a new one.
     * 
     * @param method
     *            the method
     * @param resolver
     * @return the property that was created.
     */
    public static Property createIfApplicable(final Method method,
            final SchemaResolver resolver) {
        if (!method.isPublic()) {
            return null;
        }

        if (method.isStatic()) {
            return null;
        }

        final Annotation transientAnnotation = getAnnotation(method,
                RoTransient.class);

        if (transientAnnotation != null) {
            return null;
        }

        final String name = method.getName();
        final String elementName = getElementName(name);

        if (elementName == null) {
            return null;
        }

        final Class[] params = method.getParameterTypes();

        if (params == null || params.length != 0) {
            return null;
        }

        final Class returnType = method.getReturnType();
        final Entity rtype = resolver.resolve(returnType);

        if (rtype == null) {
            return null;
        }

        return new Property(elementName, rtype, method);
    }

    private static String getAccessorName(final Method method) {
        if (method == null) {
            return null;
        }

        final String methodName = method.getName();

        return methodName + "()";
    }

    private static String getElementName(final String name) {
        String elementName = getElementName(name, "get");

        if (elementName != null) {
            return elementName;
        }

        elementName = getElementName(name, "is");

        if (elementName != null) {
            return elementName;
        }

        return getElementName(name, "has");
    }

    private static String getElementName(final String name, final String prefix) {
        if (!name.startsWith(prefix)) {
            return null;
        }

        final int len = prefix.length();

        if (name.length() <= len) {
            return null;
        }

        final char firstLetterOfPropertyName = name.charAt(len);

        if (!Character.isUpperCase(firstLetterOfPropertyName)) {
            return null;
        }

        return Character.toLowerCase(firstLetterOfPropertyName)
                + name.substring(len + 1);
    }

    static String getPropertyName(final String name) {
        final int pos = name.indexOf(':');
        final String _name = pos == -1 ? name : name.substring(pos + 1);
        final int len = name.length();

        if (len < 1) {
            return "";
        }

        final String firstLetter = _name.substring(0, 1).toLowerCase();

        if (len == 1) {
            return firstLetter;
        }

        return firstLetter + _name.substring(1);
    }

    private static Qualifer getQualifier(final Method method) {
        if (method == null) {
            return null;
        }

        final Annotation[] annotations = method.getAnnotations();

        if (annotations == null) {
            return null;
        }

        for (final Annotation annotation : annotations) {
            if (annotation instanceof RoQualifier) {
                return (Qualifer) annotation.getValue("value");
            }
        }

        return null;
    }

    private final String name;
    private final Entity type;
    private final String maxOccurs;
    private final String accessorName;
    private final Qualifer qualifer;

    Property(final String name, final Entity type) {
        this(name, type, null, null);
    }

    private Property(final String name, final Entity type, final Method method) {
        this(name, type, null, method);
    }

    Property(final String name, final Entity type, final String maxOccurs) {
        this(name, type, maxOccurs, null);
    }

    private Property(final String name, final Entity type,
            final String maxOccurs, final Method method) {
        this.name = name;
        this.type = type;
        this.maxOccurs = maxOccurs;

        accessorName = getAccessorName(method);
        qualifer = getQualifier(method);
    }

    /**
     * @return the name of the accessor
     */
    public String getAccessorName() {
        return accessorName;
    }

    /**
     * @return the upper bound
     */
    public String getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * @return the name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * @return the qualifier
     */
    public Qualifer getQualifer() {
        return qualifer;
    }

    /**
     * @return the type of the property
     */
    public Entity getType() {
        return type;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();

        buf.append("<element name=\"");
        buf.append(name);
        buf.append('"');

        if (type != null) {
            buf.append(" type=\"");
            buf.append(type);
            buf.append('"');
        }

        buf.append("/>");

        return buf.toString();
    }
}
