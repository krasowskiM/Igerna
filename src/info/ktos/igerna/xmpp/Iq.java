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

import org.w3c.dom.Node;

/**
 * Klasa reprezentująca wiadomości typu Iq
 */
public class Iq extends Stanza
{
    protected String children = "";

    public Iq(Node n)
    {
        super(n);
    }

    public Iq(String to, String from, String id, String type, String lang, String children)
    {
        super(to, from, id, type, lang);
        this.children = children;
    }

    public Iq(String from, String id, String type)
    {
        super("", from, id, type, "");
    }

    @Override
    public String toString()
    {
        String result = "<iq type='" + this.type + "' id='" + this.id + "'";

        if (!from.equals("")) result += " from='" + this.from + "'";
        if (!lang.equals("")) result += " xml:lang='" + this.lang + "'";

        if (!children.equals(""))
            result += ">" + this.children + "</iq>";
        else
            result += "/>";

        return result;
    }

    // TODO: przenieśc te funkcje do specjalnej klasy IqFactory tworzącej tego typu
    // obiekty Iq odpowiadające za różne odpowiedzi serwera

    public static String BindResult(String id, String jid)
    {
        return String.format("<iq type='result' id='%1s'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>%2s</jid></bind></iq>", id, jid);
    }

    public static String GoodResult(String id, String from)
    {
        return String.format("<iq type='result' id='%s' from='%s'/>", id, from);
    }

    public static String ServiceUnavaliableError(String id)
    {
        return String.format("<iq type='error' id='%1s'>" +
                "<error type='cancel'>" +
                "<service-unavaliable xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/>" +
                "</error>" + "</iq>", id);

    }
}
