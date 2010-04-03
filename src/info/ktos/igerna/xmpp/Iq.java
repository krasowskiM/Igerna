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
import org.w3c.dom.*;


/**
 * Klasa reprezentująca wiadomości typu Iq
 */
public class Iq extends Stanza
{    
    public Iq(Node n)
    {
        super(n);
    }

    public Iq(String to, String from, String id, String type, String lang, String children)
    {
        super(to, from, id, type, lang, children);        
        try
        {                        
            Element xmlelem = xmldoc.createElement("iq");

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

    public Iq(String from, String id, String type)
    {
        this("", from, id, type, "", "");
    }

    /// metody statyczne odpowiedzialne za tworzenie Iq będących różnymi
    /// odpowiedziami serwera

    /**
     * Tworzy wiadomość o poprawnym rezultacie podłaczenia do zasobu
     * 
     * @param id
     * @param jid
     * @return
     */
    public static Iq BindResult(String id, JID jid)
    {
        return new Iq("", "", id, "result", "", "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>" + jid.toString() + "</jid></bind>");
    }

    /**
     * Tworzy wiadomość potwierdzającą wykonanie operacji
     * 
     * @param id
     * @param from
     * @return
     */
    public static Iq GoodResult(String id, String from)
    {
        return new Iq(from, id, "result");
    }

    /**
     * Tworzy wiadomość błędu, że taka usługa nie jest dostępna
     * 
     * @param id
     * @return
     */
    public static Iq ServiceUnavaliableError(String id)
    {
        return new Iq("", "", id, "error", "",
                "<error type='cancel'>" +
                "<service-unavaliable xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/>" +
                "</error>");

    }
}
