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

import java.util.Random;

/**
 *
 */
public class Stream
{
    public static String xmlPrologue()
    {
        return "<?xml version='1.0'?>";
    }

    public static String start(String from, String id)
    {
        return String.format("<stream:stream from='%1s' id='%2s'" +
               " xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'" +
               " version='1.0'>", from, id);
    }

    public static String end()
    {
        return "</stream:stream>";
    }

    public static String streamId()
    {
        Random r = new Random();
        return "igerna" + r.nextInt();
    }

    public static String SASLfeatures(String[] mechanisms)
    {
        String m = "";
        for (String s : mechanisms)
        {
            m += String.format("<mechanism>%1s</mechanism>", s);
        }

        return String.format("<stream:features>" +
                "<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
                "%1s" +
                "</mechanisms>" +
                "</stream:features>", m);

    }

    public static String SASLsuccess()
    {
        return "<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";
    }

    public static String features()
    {
        return "<stream:features>" + 
                "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
                "<session xmlns='urn:ietf:params:xml:ns:xmpp-session'/>" +
                "</stream:features>";
    }
    
}
