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

import java.io.*;
import java.net.Socket;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * Klasa-wątek, główna klasa odpowiedzialna za komunikację z klientem
 */
class Worker implements Runnable
{
    private Socket clientSocket;
    private Document xmldoc;
    private DocumentBuilder parser;
    private BufferedReader input;
    private PrintWriter output;
    protected boolean stopped = false;

    private String stream;

    public void stop()
    {
        stopped = true;
    }

    public Worker(Socket clientSocket)
    {
        this.clientSocket = clientSocket;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            // tworzenie parsera XML
            parser = dbf.newDocumentBuilder();            

            // tworzenie strumieni wejścia i wyjścia
            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();

            input = new BufferedReader(new InputStreamReader(is));
            output = new PrintWriter(os, true);
        }
        catch (Exception ex)
        {
            System.out.println("Błąd: " + ex.getLocalizedMessage());
            IgernaServer.stop();
        }

        System.out.println("Debug: tworzenie wątku");
    }

    public void run()
    {
        try
        {
            String cltext;
            while (!stopped)
            {
                cltext = input.readLine();                
                
                // oczekiwanie na początek strumienia
                if (cltext.equals("<?xml version=\"1.0\"?>"))
                {

                }
                else
                {                    
                    stream = "<?xml version='1.0'?>" + cltext + "</stream:stream>";
                    InputStream xmlis = new ByteArrayInputStream(stream.getBytes());
                    try
                    {
                        xmldoc = parser.parse(xmlis);
                        /*if (xmldoc.getElementsByTagName("stream:stream").item(0).getNodeName() == null)
                        {
                            output.println("<?xml version='1.0'?><stream:stream from='127.0.0.1' id='foo' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>");
                            output.println("<stream:error><invalid-xml /></stream>");
                            output.println("</stream:stream>");
                        }*/

                        /*String version = xmldoc.getElementsByTagName("stream:stream").item(0).getAttributes().getNamedItem("version").getNodeValue();
                        if (version.equals("1.0"))
                        {*/

                            String ot =
                                "<?xml version=\"1.0\"?><stream:stream from=\"127.0.0.1\" id=\"foo\" xmlns=\"jabber:client\" xmlns:stream=\"http://etherx.jabber.org/streams\" version=\"1.0\">"
                                + "<stream:error><internal-server-error /></stream:error>"
                                + "</stream:stream>";
                            
                            output.println(ot);
                        //}

                            stop();

                    }
                    catch (Exception ex)
                    {
                        /*output.println("<?xml version='1.0'?><stream:stream from='127.0.0.1' id='foo' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>");
                        output.println("<stream:error><invalid-xml /></stream:error>");
                        output.println("</stream:stream>");*/
                        stop();
                    }                    

                }                
            }

            clientSocket.close();
            System.out.println("Debug: wątek zamykany");
        }
        catch (Exception ex)
        {
            System.out.println("Błąd: " + ex.getLocalizedMessage());
            IgernaServer.stop();
            System.exit(1);
        }
    }

}
