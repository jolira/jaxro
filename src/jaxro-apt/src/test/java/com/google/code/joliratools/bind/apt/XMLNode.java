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
package com.google.code.joliratools.bind.apt;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XMLNode {
    private final Node node;

    public XMLNode(final Document document) {
        node = document.getDocumentElement();
    }

    public XMLNode(final Node node) {
        this.node = node;
    }

    public String getAttribute(final String name) {
        final NamedNodeMap attributes = node.getAttributes();
        final Node attr = attributes.getNamedItem(name);

        if (attr == null) {
            return null;
        }

        return attr.getNodeValue();
    }

    public XMLNode[] getChildren() {
        final NodeList childNodes = node.getChildNodes();

        if (childNodes == null) {
            return null;
        }

        final int len = childNodes.getLength();
        final Collection<XMLNode> children = new ArrayList<XMLNode>(len);

        for (int idx = 0; idx < len; idx++) {
            final Node child = childNodes.item(idx);
            final short type = child.getNodeType();

            switch (type) {
            case Node.TEXT_NODE:
                final String val = child.getNodeValue();

                if (val == null || val.trim().length() < 1) {
                    continue;
                }
                break;

            case Node.ATTRIBUTE_NODE:
                continue;
            }

            children.add(new XMLNode(child));
        }

        final int size = children.size();

        return children.toArray(new XMLNode[size]);
    }

    public String getName() {
        return node.getLocalName();
    }

    public String getNamespaceURI() {
        return node.getNamespaceURI();
    }

    @Override
    public String toString() {
        return getName();
    }
}