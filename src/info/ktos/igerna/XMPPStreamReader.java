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
package info.ktos.igerna;

import info.ktos.igerna.xmpp.StreamError;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * Klasa odpowiedzialna za odczytywanie z wejściowego strumienia XMPP
 * i robienie z pobranymi od klienta danymi jakiś rzeczy.
 */
class XMPPStreamReader extends Thread
{
    private Document xmldoc;
    private DocumentBuilder parser;
    private BufferedReader input;
    private boolean stopped = false;
    private String cltext;
    private Worker parent;

    public void stopWorking()
    {
        this.stopped = true;
    }

    public XMPPStreamReader(BufferedReader rdr, Worker parent)
    {
        super();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            // tworzenie parsera XML
            parser = dbf.newDocumentBuilder();

            // przepisywanie strumienia wejściowego
            this.input = rdr;

            this.parent = parent;
        }
        catch (Exception ex)
        {
            System.out.println("Błąd: " + ex.getLocalizedMessage());
            IgernaServer.stop();
        }
    }

    @Override
    public void run()
    {
        while (!stopped)
        {
            try
            {                
                cltext = input.readLine();

                if (!cltext.startsWith("<?xml") && !cltext.equals(""))
                {

                    cltext = cltext + "</stream:stream>";

                    InputStream xmlis = new ByteArrayInputStream(cltext.getBytes());
                    try
                    {
                        xmldoc = parser.parse(xmlis);

                        // jeśli stan klienta jest jakikolwiek byle nie "rozłączony",
                        // i klient coś do nas wyśle, to my parsujemy żądanie
                        // i wysyłamy odpowiedź
                        if (parent.clientState.getState() >= ClientState.CONNECTED)
                        {

                            // TODO: tutaj rozpozwanie tagów i odpowiednie akcje podejmowane
                            // dla różnych głupich pomysłów

                            parent.sendToClient(StreamError.internalServerError());
                        }
                    }
                    catch (Exception ex)
                    {
                        // ojej, klient wysłał nam coś, czego nie powinien był
                        System.out.println("Błąd: błąd parsowania XML od klienta");
                        parent.sendImmediately(StreamError.invalidXML());

                        this.stopWorking();
                        parent.stopWorking();
                    }
                }
            }
            catch (Exception ex)
            {
                // dirty hack, trzeba by to było poprawić pewnie
                if (ex.getLocalizedMessage() != null)
                    System.out.println("Błąd: " + ex.getLocalizedMessage());
                else
                    System.out.println("Debug: klient rozłączony");
                
                this.stopWorking();
                parent.stopWorking();
            }
        }
    }
}
