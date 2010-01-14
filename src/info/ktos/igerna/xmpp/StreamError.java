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

import info.ktos.igerna.IgernaServer;


/**
 * Klasa zwracająca różne stringi będące odpowiednikami błędów serwera
 * na poziomie całego strumienia komunikacji
 */
public class StreamError
{
    /**
     * Błąd złego XML wysłanego przez klienta, przesyłany w momencie
     * gdy np. nie powiedzie się parsowanie XML
     * @return
     */
    public static String invalidXML()
    {
        return err("<invalid-xml />");
    }

    /**
     * Wewnętrzny błąd serwera, razem z prologiem XML i początkiem strumienia
     * używany na początku komunikacji
     * @return
     */
    public static String internalServerError()
    {
        return err("<internal-server-error />");
    }

    /**
     * Wewnętrzny błąd serwera
     * @return
     */
    public static String internalServerError2()
    {
        return err2("<internal-server-error />");
    }

    /**
     * Komunikat o niepowodzeniu uwierzytelnienia SASL
     * @return
     */
    public static String SASLnotauthorized()
    {
        return SASLerror("<not-authorized/>");
    }

    /**
     * Nieobsługiwana stanza przez serwer
     * 
     * @return
     */
    public static String unsupportedStanzaType()
    {
        return err2("<unsupported-stanza-type />");
    }

    private static String err(String err)
    {
        return String.format("%s%s<stream:error>%s</stream:error>%s",
                Stream.xmlPrologue(),
                Stream.start(IgernaServer.getBindHost(), Stream.generateId()),
                err,
                Stream.end());
    }

    private static String err2(String err)
    {
        return String.format("<stream:error>%s</stream:error>%s",
                err,
                Stream.end());
    }

    private static String SASLerror(String err)
    {
        return String.format("<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>%1s</failure>%2s", err, Stream.end());
    }

    public static String resourceConflict()
    {
        return "<error type='cancel'><conflict xmlns='urn:ietf:params:xml:ns:xmpp-stanzas' /></error>";
    }
}
