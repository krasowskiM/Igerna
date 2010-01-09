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
package info.ktos.igerna.xmpp;

import java.io.ByteArrayInputStream;
import org.w3c.dom.Document;
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
        xmlnode.appendChild(createAttributeValue("from", from.toString()));
    }

    public Presence(String to, String from, String id, String type, String lang, String children)
    {
        super(to, from, id, type, lang, children);

        try
        {
            xmlnode = xmldoc.createElement("iq");

            if (!this.from.equals(""))
                xmlnode.appendChild(createAttributeValue("from", this.from));

            if (!this.to.equals(""))
                xmlnode.appendChild(createAttributeValue("to", this.to));

            if (!this.id.equals(""))
                xmlnode.appendChild(createAttributeValue("id", this.id));

            if (!this.type.equals(""))
                xmlnode.appendChild(createAttributeValue("type", this.type));

            if (!this.lang.equals(""))
                xmlnode.appendChild(createAttributeValue("lang", this.lang));

            // parsowanie dzieci i przepisywanie ich do xmlnode
            if (!this.childXML.equals(""))
            {
                Document child = parser.parse(new ByteArrayInputStream(childXML.getBytes()));
                for (int i = 0; i < child.getChildNodes().getLength(); i++)
                    xmlnode.appendChild(child.getChildNodes().item(i));
            }
        }
        catch (Exception ex)
        {
            // TODO: co zrobić z wyjątkiem w takim wypadku?
        }

    }

    public Presence(String from, String type)
    {
        this("", from, "", type, "", "");
    }

    @Override
    public String toString()
    {
        // TODO: zmienić na rzeczywistą zamianę xml na stringa, outerXML

        String result = "<presence type='unavaliable' />";

        return result;
    }

}
