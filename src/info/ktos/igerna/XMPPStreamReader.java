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

import info.ktos.igerna.xmpp.Stream;
import info.ktos.igerna.xmpp.StreamError;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
    private char[] cbuf;
    private String clientStreamStart;
    private String serverStreamStart;

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
        while (true)//!stopped)
        {
            try
            {
                // czyszczenie bufora i odczyt z niego
                cbuf = null; cbuf = new char[4096];

                int res = input.read(cbuf);
                cltext = new String(cbuf).trim();

                if ((res != -1) && (!cltext.equals("")))
                {
                    
                    System.out.println("Fr: " + cltext);
                    
                    try
                    {                        
                        if (parent.clientState.getState() == ClientState.CONNECTING)
                        {
                            // uznajemy, że to co klient wysłał to prawdopodobnie będzie
                            // początek strumienia
                            clientStreamStart = cltext;

                            cltext = cltext + Stream.end();
                            InputStream xmlis = new ByteArrayInputStream(cltext.getBytes());
                            xmldoc = parser.parse(xmlis);
                            
                            // TODO: sprawdzanie wersji!                            

                            serverStreamStart = Stream.start(IgernaServer.getBindHost(), Stream.streamId());

                            // jeżeli jest podłączony i coś wysyła, to pewnie początek streamu
                            // zatem odpowiadamy naszym streamem oraz SASLem
                            parent.sendToClient(serverStreamStart);
                            parent.sendToClient(Stream.SASLfeatures(IgernaServer.getSASLMechanisms()));

                            // i ustawiamy, że najwyraźniej klient jest w trakcie autoryzacji
                            parent.clientState.setState(ClientState.AUTHORIZING);

                            xmldoc = null;
                            xmlis.close();
                        }
                        // jeśli klient jest w trakcie autoryzacji, to czekamy na jego <auth>
                        else if (parent.clientState.getState() == ClientState.AUTHORIZING)
                        {
                            InputStream xmlis = new ByteArrayInputStream(cltext.getBytes());
                            xmldoc = parser.parse(xmlis);

                            // wyszukiwanie mechanizmu uwierzytelnienia
                            Node mechanism = xmldoc.getElementsByTagName("auth").item(0).getAttributes().getNamedItem("mechanism");
                            if (mechanism != null)
                                if (IgernaServer.ucp.check(mechanism.getNodeValue(), xmldoc.getElementsByTagName("auth").item(0).getNodeValue()))
                                {
                                    // logowanie się powiodło, wyślij roster
                                    parent.clientState.setState(ClientState.BINDING);
                                }
                                else
                                {
                                    parent.sendImmediately(StreamError.SASLnotauthorized());
                                }

                            xmldoc = null;
                            xmlis.close();
                        }
                        // jeśli klient jest aktywny i coś wysyła, to my parsujemy żądanie
                        // i robimy co trzeba
                        else if (parent.clientState.getState() > ClientState.AUTHORIZING)
                        {                            
                            // TODO: tutaj rozpozwanie tagów i odpowiednie akcje podejmowane
                            // dla różnych głupich pomysłów

                            // tymczasowo: wysyłamy wewnętrzny błąd w wersji "w trakcie"
                            // ustanawiania streamu
                            parent.sendToClient(StreamError.internalServerError2());
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
