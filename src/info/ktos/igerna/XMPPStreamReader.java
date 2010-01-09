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
package info.ktos.igerna;

import info.ktos.igerna.xmpp.*;
import info.ktos.igerna.xmpp.xeps.SoftwareVersion;
import info.ktos.igerna.xmpp.xeps.Vcard;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    /**
     * Konstruktor, wywoływany przez Workera
     *
     * Tworzy parser XML, wykorzystywany później
     *
     * @param rdr BufferedReader, który odczytuje wejściowe dane
     * @param parent Worker będący rodzicem
     */
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
            System.out.println("Błąd: " + ex.getLocalizedMessage() + ": 78");
            IgernaServer.stop();
        }
    }
    
    /**
     * Metoda wywołująca start wątku, główna pętla klasy
     */
    @Override
    public void run()
    {
        while (!stopped)
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
                        // jeśli klient się pożegnał
                        // to kończymy pracę i wychodzimy z naszej nieskończonej pętli
                        if (cltext.equals(Stream.end()))
                        {
                            disconnectClient();
                            break;
                        }

                        if (parent.clientState.getState() == ClientState.CONNECTING)
                        {
                            // uznajemy, że to co klient wysłał to prawdopodobnie będzie
                            // początek strumienia
                            clientStreamStart = cltext;

                            cltext = cltext + Stream.end();
                            InputStream xmlis = new ByteArrayInputStream(cltext.getBytes());
                            xmldoc = parser.parse(xmlis);
                            
                            // TODO: sprawdzanie wersji!                            

                            serverStreamStart = Stream.start(IgernaServer.getBindHost(), Stream.generateId());

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

                            // TODO: błąd, jeśli klient nie wyśle tego, co trzeba
                            // na przykład wyśle jabber:iq:auth bo nie obsługuje SASL
                            // NullPointerException

                            // wyszukiwanie mechanizmu uwierzytelnienia
                            Node mechanism = xmldoc.getElementsByTagName("auth").item(0).getAttributes().getNamedItem("mechanism");
                            if (mechanism != null)
                                if (IgernaServer.ucp.check(mechanism.getNodeValue(), xmldoc.getElementsByTagName("auth").item(0).getTextContent()))
                                {
                                    // logowanie się powiodło, wyślij <success/>
                                    parent.sendToClient(Stream.SASLsuccess());
                                    
                                    parent.clientJID = new JID(IgernaServer.ucp.lastUsername(), IgernaServer.getBindHost(), "");

                                    // idziemy do następnego stanu
                                    parent.clientState.setState(ClientState.AUTHORIZED);
                                }
                                else
                                {
                                    // tutaj powinny być wyjątki i w zależności od
                                    // wyjątku różne rodzaje błędów SASL
                                    parent.sendImmediately(StreamError.SASLnotauthorized());

                                    // i rozłączamy się
                                    this.stopWorking();
                                    parent.stopWorking();
                                }

                            xmldoc = null;
                            xmlis.close();
                        }
                        else if (parent.clientState.getState() == ClientState.AUTHORIZED)
                        {
                            // tutaj klient wysyła drugiego <stream>, na którego będziemy
                            // odpowiadać naszym drugiem streamem :-)
                            
                            parent.sendToClient(serverStreamStart);
                            parent.sendToClient(Stream.features());

                            // powiodło się? no to klient pewnie się będzie bindował do zasobu
                            parent.clientState.setState(ClientState.BINDING);
                        }
                        // klient się binduje do zasobu
                        else if (parent.clientState.getState() == ClientState.BINDING)
                        {
                            InputStream xmlis = new ByteArrayInputStream(cltext.getBytes());
                            xmldoc = parser.parse(xmlis);
                            
                            if (xmldoc.getElementsByTagName("iq").getLength() == 1)
                            {
                                String bindid = xmldoc.getElementsByTagName("iq").item(0).getAttributes().getNamedItem("id").getTextContent();

                                // ustalanie nowego zasobu
                                // TODO: powinno być brane z XML, a nie generowane przez serwer
                                String newres = "foo";
                                parent.clientJID.setResource(newres);
                                parent.clientResourcePriority = 5;

                                // wysyłanie do klienta potwierdzenia
                                parent.sendToClient(Iq.BindResult(bindid, parent.clientJID));

                                parent.clientState.setState(ClientState.BOUND);
                            }
                            else
                            {
                                // błąd wewnętrzny jeśli klient zechciał coś innnego niż
                                // binding do zasobu
                                parent.sendToClient(StreamError.internalServerError2());
                            }

                            xmldoc = null;
                            xmlis.close();                            
                        }
                        // klient się podłączył do zasobu, będzie chciał ustanowić sesję
                        else if (parent.clientState.getState() == ClientState.BOUND)
                        {
                            InputStream xmlis = new ByteArrayInputStream(cltext.getBytes());
                            xmldoc = parser.parse(xmlis);

                            if (xmldoc.getElementsByTagName("iq").getLength() == 1)
                            {
                                String id = xmldoc.getElementsByTagName("iq").item(0).getAttributes().getNamedItem("id").getTextContent();

                                // wysyłanie do klienta potwierdzenia
                                parent.sendToClient(Iq.GoodResult(id, IgernaServer.getBindHost()));

                                parent.clientState.setState(ClientState.ACTIVE);
                            }
                            else
                            {
                                // błąd wewnętrzny jeśli klient zechciał coś innnego niż
                                // sesja ;-)
                                parent.sendToClient(StreamError.internalServerError2());
                            }

                            xmldoc = null;
                            xmlis.close();
                        }
                        // a jeśli klient jest aktywny i coś wysyła, to my parsujemy żądanie
                        // i robimy co trzeba
                        else if (parent.clientState.getState() == ClientState.ACTIVE)
                        {
                            cltext = clientStreamStart + cltext + Stream.end();

                            InputStream xmlis = new ByteArrayInputStream(cltext.getBytes());
                            xmldoc = parser.parse(xmlis);
                            respondClient();
                        }
                        
                    }
                    catch (org.xml.sax.SAXParseException ex)
                    {
                        // ojej, klient wysłał nam coś, czego nie powinien był
                        System.out.println("Błąd: błąd parsowania XML od klienta: 257");
                        parent.sendImmediately(StreamError.invalidXML());

                        this.stopWorking();
                        parent.stopWorking();
                    }
                    // jakiś inny błąd, na przykład nullpointerexception ;-)
                    catch (Exception ex)
                    {
                        System.out.println("Błąd: " + ex.toString() + ": " + ex.getMessage() + ": 266");
                        ex.printStackTrace();
                        parent.sendImmediately(StreamError.internalServerError2());

                        this.stopWorking();
                        parent.stopWorking();
                    }
                }
            }
            catch (Exception ex)
            {
                // dirty hack, trzeba by to było poprawić pewnie
                if (ex.getLocalizedMessage() != null)
                    System.out.println("Błąd: " + ex.getLocalizedMessage() + ": 279");
                else
                    System.out.println("Debug: klient rozłączony");
                
                this.stopWorking();
                parent.stopWorking();
            }
        }
    }

    /**
     * Wysyłanie odpowiedzi do klienta na różne żądania
     * @throws DOMException
     */
    private void respondClient() throws DOMException
    {
        // klient przysłał <iq>
        if (xmldoc.getElementsByTagName("iq").getLength() > 0)
        {
            for (int i = 0; i < xmldoc.getElementsByTagName("iq").getLength(); i++)
            {
                Node main = xmldoc.getElementsByTagName("iq").item(i);
                // klient wysłał stanzę <iq>
                String id = XmlUtil.getAttributeAsString(main, "id");
                String from = XmlUtil.getAttributeAsString(main, "from");
                String to = XmlUtil.getAttributeAsString(main, "to");

                // jeśli to zostało wysłane do serwera
                if (to.equals(IgernaServer.getBindHost()) || to.equals(""))
                {
                    respondToIq(new Iq(main));
                }
                else
                {
                    JID j = new JID(to);
                    if (j.getResource().equals(""))
                    {
                        // klient przysłał to, gdzie nie określono zasobu
                        // zgodnie z RFC musimy w tym wypadku zrobić coś,
                        // jeśli umiemy i odpowiedzieć błędem jeśli nie rozumiemy
                        // RFC 3921 11.1.4.3
                        respondToIq(new Iq(main));
                    }
                    else
                    {
                        // przesyłamy dalej, do odpowiedniego odbiorcy
                        IgernaServer.sendMessage(j, new Iq(main));
                    }
                }
            }
        }

        // klient przysłał <presence>
        if (xmldoc.getElementsByTagName("presence").getLength() > 0)
        {
            // klient wysłał stanzę <presence>
            for (int i = 0; i < xmldoc.getElementsByTagName("presence").getLength(); i++)
            {
                // hack na Psi, które jest brzydkie i nie wysyła </stream>
                // jak się wyłącza, a tylko presence unavaliable:
                // jeżeli presence jest "unavaliable", to rozłącz klienta
                Node item = xmldoc.getElementsByTagName("presence").item(i);
                Node presType = item.getAttributes().getNamedItem("type");
                if ((presType != null) && (presType.getNodeValue().equals("unavailable")))
                {
                    disconnectClient();
                    // powiedz innym, że się rozłączyłem
                    IgernaServer.sendToAll(new Presence(parent.clientJID.toString(), "unavaliable"));
                    break;
                }
                else
                {
                    // w jakimkolwiek innym wypadku w zasadzie trzeba
                    // <presence /> przekierować dalej

                    // jeśli jest ustawiony atrybut to to wyślij do odpowiedniego
                    // odbiorcy
                    String to = XmlUtil.getAttributeAsString(item, "to");
                    if (!to.equals(""))
                    {
                        IgernaServer.sendMessage(new JID(to), new Presence(item, parent.clientJID));
                    }
                    else
                    {
                        IgernaServer.sendToAll(new Presence(item, parent.clientJID));
                    }
                }
            }
        }

        // klient przysłał <message>
        if (xmldoc.getElementsByTagName("message").getLength() > 0)
        {                        
            //parent.sendToClient(new Message("ktos@127.0.0.1", "127.0.0.1", "", "", "", "<body>Test!</body>"));
            for (int i = 0; i < xmldoc.getElementsByTagName("message").getLength(); i++)
            {                
                // tworzenie obiektu Message i przepisywanie from przez serwer
                Message m = new Message(xmldoc.getElementsByTagName("message").item(i));
                m.setFrom(parent.clientJID);
                System.out.println("XX: " + m);
                // i wysyłanie
                if (IgernaServer.sendMessage(new JID(m.to), m))
                    System.out.println("OK");
                else
                    System.out.println("NIEOK");
            }
        }

        /*
        else
        {
            parent.sendImmediately(StreamError.internalServerError2());
            this.stopWorking();
            parent.stopWorking();
        }*/

        // TODO: jeśli nierozpoznany XML, to co robimy?
    }

    private void disconnectClient()
    {
        this.stopWorking();
        parent.clientState.setState(ClientState.DISCONNECTED);
        parent.stopWorking();
        return;
    }

    /**
     * Analizowanie różnych iq i odpowiadanie na nie, na przykład na zapytanie
     * o vCard, wersję oprogramowania itp.
     */
    private void respondToIq(Iq iq)
    {
        // ignorowanie typu 'result' praktycznie
        if (iq.type.equals("result"))
            return;

        if (iq.getAsNode().hasChildNodes())
        {
            NodeList children = iq.getAsNode().getChildNodes();

            for (int i = 0; i < children.getLength(); i++)
            {
                // vCard (XEP-0054)                
                if (children.item(i).getNodeName().equals("vCard"))
                {
                    // jeśli get to wysyłamy vCarda, na set nasz serwer odpowie,
                    // że ok, ale tak naprawdę nic nie zrobi ;-)
                    if (iq.type.equals("get"))
                    {
                        parent.sendToClient(Vcard.get(iq.from, iq.to, iq.id, new JID(iq.to)));                        
                    }
                    else if (iq.type.equals("set"))
                    {
                        parent.sendToClient(Iq.GoodResult(iq.id, iq.to));                        
                    }
                }                
                else if (children.item(i).getNodeName().equals("query"))
                {
                    String xmlns = children.item(i).getAttributes().getNamedItem("xmlns").getTextContent();

                    if (xmlns.equals("jabber:iq:roster"))
                    {
                        parent.sendToClient(Iq.ServiceUnavaliableError(iq.id));
                    }
                    else if (xmlns.equals("http://jabber.org/protocol/disco#info"))
                    {
                        // service discovery (XEP-0030)
                    }
                    else if (xmlns.equals("jabber:iq:version"))
                    {
                        // software version (XEP-0092)
                        parent.sendToClient(SoftwareVersion.get(iq.from, IgernaServer.getBindHost(), iq.id));
                    }
                    else
                    {
                        // nieobsługiwane zapytanie, wysyłamy błąd
                        parent.sendToClient(Iq.ServiceUnavaliableError(iq.id));
                    }
                }
                else
                {
                    // TODO: nieznane polecenie, powinniśmy wysyłać błąd
                    // ale jeśli getNodeName = #text to nie
                    //return;
                }
            }

            return;
        }
        parent.sendToClient(Iq.ServiceUnavaliableError(iq.id));
    }
}
