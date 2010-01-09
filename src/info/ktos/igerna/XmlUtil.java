/*
 * Igerna, version 0.2
 *
 * Copyright (C) Marcin Badurowicz 2009
 *
 *
 * This file is part of Igerna.
 *
 * Igerna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Igerna is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Igerna.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.ktos.igerna;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Klasa zawierająca pewne dodatkowe funkcje do obsługi XML, używane
 * zarówno przez klasy z pakietu xmpp, jak i XMPPStreamReader 
 */
public class XmlUtil
{
    /**
     * Pobiera atrybut danego węzła jako string, w przeciwnym razie
     * zwraca pustego stringa.
     *
     * Metoda refaktoryzowana z obsługi zdarzeń XMPPStreamReadera
     * żeby mi było nieco łatwiej, ale okazała się potrzebna też
     * bezpośrednio w Stanza
     *
     * @param n Węzeł
     * @param att Tekstowa nazwa atrybutu
     * @return Tekstowa zawartość atrybutu albo pusty string jeśli atrybut nie istnieje
     */
    public static String getAttributeAsString(Node n, String att)
    {
        if (n.getAttributes().getNamedItem(att) == null)
        {
            return "";
        }
        else
        {
            return n.getAttributes().getNamedItem(att).getTextContent();
        }
    }

    /**
     * Zwracanie dzieci węzła XML jako pojedynczego stringa
     *
     * @param node
     * @return
     */
    private static String innerXml(Node node)
    {
        String result = "";

        node.normalize();
        if (node.getNodeType() == Node.TEXT_NODE)
        {
                return node.getNodeValue();
        }

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
                result += outerXml(childNodes.item(i));
        }

        return result;
    }

    /**
     * Zwraca węzeł XML i jego dzieci jako string
     *
     * @param node
     * @return
     */
    private static String outerXml(Node node) 
    {
        String result = "";
        node.normalize();

        if (node.getNodeType() == Node.TEXT_NODE)
        {         
            return node.getNodeValue();
        }

        result = "<" + node.getNodeName() + " ";

        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null)
        {
            for (int i = 0; i < attributes.getLength(); i++)
            {
                Node item = attributes.item(i);
                result += item.getNodeName() + "='" + item.getNodeValue() + "'";
            }
        }        

        NodeList childNodes = node.getChildNodes();
        if (childNodes.getLength() == 0)
        {
            result += " />";
            return result;
        }
        else
        {
            result += ">";
        }

        for (int i = 0; i < childNodes.getLength(); i++)
        {
                result += outerXml(childNodes.item(i));
        }

        result += "<" + node.getNodeName() + " />";

        return result;
    }


}
