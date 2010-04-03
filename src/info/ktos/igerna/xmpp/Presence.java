/*
 * Igerna, version 0.2
 *
 * Copyright (C) Marcin Badurowicz 2009-2010
 *
 *
 * This file is part of Igerna.
 *
 * Igerna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Igerna is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * Licensealong with Igerna. If not, see <http://www.gnu.org/licenses/>.
 */
package info.ktos.igerna.xmpp;

import java.io.ByteArrayInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Klasa implementująca element typu <presence />
 */
public class Presence extends Stanza
{
    public Presence(Node n)
    {
        super(n);
    }

    /**
     * Tworzenie <presence /> w którym jest atrybut from
     *
     * Normalnie element presence nie zawiera from, gdy jest wysyłany
     * od klienta do serwera, ale gdy serwer chce go rozesłać innym,
     * to from jest wymagany
     *
     * @param n
     * @param from
     */
    public Presence(Node n, JID from)
    {
        super(n);

        // chora konstrukcja, ale działa        
        Element xmlelem = (Element)xmlnode;
        xmlelem.setAttribute("from", from.toString());
        xmlnode = xmlelem;
    }

    public Presence(String to, String from, String id, String type, String lang, String children)
    {
        super(to, from, id, type, lang, children);

        try
        {
            Element xmlelem = xmldoc.createElement("presence");

            if (!this.from.equals(""))
                xmlelem.setAttribute("from", this.from);

            if (!this.to.equals(""))
                xmlelem.setAttribute("to", this.to);

            if (!this.id.equals(""))
                xmlelem.setAttribute("id", this.id);

            if (!this.lang.equals(""))
                xmlelem.setAttribute("xml:lang", this.lang);

            if (!this.type.equals(""))
                xmlelem.setAttribute("type", this.type);

            // parsowanie dzieci i przepisywanie ich do xmlnode
            if (!this.childXML.equals(""))
            {
                Document child = parser.parse(new ByteArrayInputStream(childXML.getBytes()));
                xmlelem.appendChild(xmldoc.importNode(child.getFirstChild(), true));
            }

            if (xmlelem == null)
                System.out.println("here");


            // i zrzutowanie elementu na Node
            xmlnode = xmlelem;
        }
        catch (Exception ex)
        {
            System.out.println("Błąd: " + ex.getMessage() + " (in stanza!)");
        }

    }

    public Presence(String from, String type)
    {
        this("", from, "", type, "", "");
    }
}
