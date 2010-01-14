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

import info.ktos.igerna.xmpp.JID;
import info.ktos.igerna.xmpp.Presence;
import info.ktos.igerna.xmpp.Stanza;
import java.io.*;
import java.net.Socket;


/**
 * Klasa-wątek, główna klasa odpowiedzialna za komunikację z klientem
 */
class Worker extends Thread
{
    private Socket clientSocket;    
    private XMPPStreamReader xsr;
    private MessageBuffer mbuf;
    private PrintWriter output;    

    protected boolean stopped = false;

    public JID clientJID;
    public ClientState clientState;
    public int clientResourcePriority = 0;    

    /**
     * Zatrzymywanie pracy wątku
     */
    public void stopWorking()
    {
        stopped = true;
    }

    /**
     * Tworzenie wątku opartego na gniazdku klienta
     * 
     * @param clientSocket
     */
    public Worker(Socket clientSocket)
    {
        super();
        this.clientSocket = clientSocket;
        this.mbuf = new MessageBuffer();

        // status podłączonego klienta
        // skoro już tworzony jest ten wątek, znaczy, że klient się podłączył
        this.clientState = new ClientState();
        this.clientState.setState(ClientState.CONNECTING);

        this.clientJID = null;

        try
        {
            // tworzenie strumieni wejścia i wyjścia
            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();

            // tworzenie readerów i writerów opartych na tych strumieniach
            xsr = new XMPPStreamReader(new BufferedReader(new InputStreamReader(is)), this);
            output = new PrintWriter(os, true);
        }
        catch (Exception ex)
        {
            System.out.println("Błąd: " + ex.getLocalizedMessage());
            IgernaServer.stop();
        }
        
        System.out.println("Debug: tworzenie wątku");
    }

    /**
     * Główna pętla operacyjna
     */
    @Override
    public void run()
    {
        try
        {
            xsr.start();
            while (!stopped && (clientSocket.getRemoteSocketAddress() != null))
            {
                /* sprawdzaj okresowo bufor, jeśli jest coś w buforze do wysłania, to wyślij
                   do klienta */

                if (!mbuf.isClean())
                {
                    output.println(mbuf.getBuffer());
                    mbuf.clearBuffer();
                }

                Thread.sleep(500);
                //System.out.println("Debug: Czytam bufor...");
                Thread.yield();
            }

            // gdy kończymy pracę albo klient się rozłączył, to
            // zamykamy readera, gniazdko i cały wątek
            xsr.stopWorking();
            clientSocket.close();
            System.out.println("Debug: wątek zamykany");
        }
        catch (Exception ex)
        {
            System.out.println("Krytyczny błąd: " + ex.getLocalizedMessage());
            IgernaServer.stop();
            System.exit(1);
        }
    }

    /**
     * Dodaje tekst do bufora, który to zostanie przy następnym
     * "obrocie" pętli do niego wysłany
     *
     * @param xml
     */
    public void sendToClient(String xml)
    {
        System.out.println("To: " + xml);
        mbuf.addToBuffer(xml);
    }

    /**
     * Wysyłanie do klienta jakiejś stanzy
     *
     * @param st
     */
    public void sendToClient(Stanza st)
    {
        sendToClient(st.toString());
    }

    /**
     * Wysyła do klienta informację natychmiast, bez żadnego "szemrania",
     * co jest użyteczne na przykład przy krytycznych błędach strumienia
     *
     * Wysyła po prostu od razu do strumienia wyjściowego, z pominięciem
     * buforowania.
     *
     * @param xml
     */
    public void sendImmediately(String xml)
    {
        System.out.println("To! " + xml);
        output.println(xml);
    }

}
