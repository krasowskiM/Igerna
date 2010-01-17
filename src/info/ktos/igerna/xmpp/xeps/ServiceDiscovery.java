/*
 * Igerna, version 0.2
 *
 * Copyright (C) Marcin Badurowicz 2010
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
 * along with Igerna. If not, see <http://www.gnu.org/licenses/>.
 */
package info.ktos.igerna.xmpp.xeps;

import info.ktos.igerna.xmpp.Iq;

/**
 * Klasa zwracająca Iq odpowiadające na żądanie Service Discovery
 * (XEP-0030)
 */
public class ServiceDiscovery
{
    public static Iq get(String to, String from, String id)
    {
        String children = "<query xmlns='http://jabber.org/protocol/disco#info'>";

        for (String s : new String[] { "http://jabber.org/protocol/disco#info",
                                       "jabber:iq:version"
                                     })
        {
            children += "<feature var='" + s + "' />";
        }

        children += "</query>";
        
        return new Iq(to, from, id, "result", "", children);
    }
}
