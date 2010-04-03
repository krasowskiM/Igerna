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

import java.util.Random;

/**
 * Klasa odpowiedająca za strumień w XMPP, zawierająca trochę statycznych
 * metod odpowiedzialnych za różne elementy strumienia
 */
public class Stream
{
    /**
     * Generowanie prologu XML w postaci stringa
     * @return
     */
    public static String xmlPrologue()
    {
        return "<?xml version='1.0'?>";
    }

    /**
     * Generowanie początku strumienia
     *
     * @param from Adres serwera
     * @param id Identyfikator strumienia
     * @return
     */
    public static String start(String from, String id)
    {
        return String.format("<stream:stream from='%1s' id='%2s'" +
               " xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'" +
               " version='1.0'>", from, id);
    }

    /**
     * Generowanie znacznika końca strumienia
     * @return
     */
    public static String end()
    {
        return "</stream:stream>";
    }

    /**
     * Generowanie nowego identyfikatora
     * @return
     */
    public static String generateId()
    {
        Random r = new Random();
        return "igerna" + r.nextInt();
    }

    /**
     * Generowanie opisu mechanizmów uwierzytelnienia SASL jako
     * "ficzerów" strumienia
     * 
     * @param mechanisms
     * @return
     */
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

    /**
     * Komunikat o poprawnym uwierzytelnieniu użytkownika
     *
     * @return
     */
    public static String SASLsuccess()
    {
        return "<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";
    }

    /**
     * Zwracanie możliwości, jakie obsługuje serwer, przedstawiane
     * klientowi po uwierzytelnieniu
     *
     * Tutaj powinny znaleźć się obsługiwane XEP-y oraz podstawowe
     * elementy jak <bind /> czy <session />.
     *
     * @return
     */
    public static String features()
    {
        return "<stream:features>" + 
                "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
                "<session xmlns='urn:ietf:params:xml:ns:xmpp-session'/>" +
                "</stream:features>";
    }
    
}
