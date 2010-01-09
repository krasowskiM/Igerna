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
 * Klasa będąca realizacją stanzy <message />
 */
public class Message extends Stanza
{
    public Message(Node n)
    {
        super(n);
    }

    public Message(String to, String from, String id, String type, String lang, String children)
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

    @Override
    public String toString()
    {
        // TODO: zmienić na rzeczywistą zamianę xml na stringa, outerXML

        String result = "<message />";

        return result;
    }

}
