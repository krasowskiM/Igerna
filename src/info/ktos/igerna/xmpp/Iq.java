/*
 * Igerna, version 0.1
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

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Klasa reprezentująca wiadomości typu Iq
 */
public class Iq extends Stanza
{
    public Iq(String xml) throws ParserConfigurationException, IOException, SAXException
    {
        super(xml);
    }
    

    @Override
    public String toXML() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static String BindResult(String id, String jid)
    {
        return String.format("<iq type='result' id='%1s'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>%2s</jid></bind></iq>", id, jid);
    }

    public static String GoodResult(String id, String from)
    {
        return String.format("<iq type='result' id='%1s' from='%2s'/>", id, from);
    }

    public static String ServiceUnavaliableError(String id)
    {
        return String.format("<iq type='error' id='%1s'>" +
                "<error type='cancel'>" +
                "<service-unavaliable xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/>" +
                "</error>" + "</iq>", id);

    }
}
