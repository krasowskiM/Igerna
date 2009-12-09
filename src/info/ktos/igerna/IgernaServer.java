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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import info.ktos.igerna.xmpp.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class IgernaServer
{    
    private static Config config;
    private static String configFile = "igerna.conf";
    private static ServerSocket serv;
    private static boolean stopped;
    private static ArrayList<Worker> workerPool;
    private static String bindHost;
    private static int bindPort;

    public static UserCredentialsProvider ucp;

    public static String getBindHost()
    {
        return bindHost;
    }

    public static int getBindPort()
    {
        return bindPort;
    }

    public static String[] getSASLMechanisms()
    {
        return config.getArrayEntry("server", "sasl", new String[] { "PLAIN" });
    }

    /**
     * Sprawdza czy serwer nie jest zatrzymany
     * @return
     */
    public static boolean isStopped()
    {
        return stopped;
    }

    /**
     * Zatrzymuje pracę serwera
     */
    public static void stop()
    {
        stopped = true;
        System.out.println("Status: Zatrzymywanie...");
    }

    /**
     * Główna metoda uruchomieniowa
     *
     * Analizuje podane parametry i od tego uruchamia pewne inne, dodatkowe
     * funkcje lub po prostu startuje serwer.
     *
     * @param Argumenty linii poleceń
     */
    public static void main(String[] args)
    {
        // jeśli podano jakiś parametr, to musimy sprawdzić jaki
        if (args.length == 1)
	{
            // jeśli --version - pokaż informacje o wersji, inaczej help, inaczej
            // załaduj konfigurację ze wskazanego pliku
            if (args[0].equals("--version"))
            {
                IgernaServer.showVersion();
            }
            else if (args[0].equals("--help"))
            {
                IgernaServer.showHelp();
            }
            else if (args[0].startsWith("-C"))
            {
                System.out.println("Not implemented");
            }
            // jeżeli coś, czego nie rozpoznajemy, to pokaż pomoc
            else
            {
                IgernaServer.showHelp();
            }
	}
        else
        {
            // ładowanie pliku konfiguracyjnego
            // i wczytywanie go do lokalnej tablicy w klasie Config
            try
            {
                config = new Config(configFile);
                config.readFile();
            }
            catch (FileNotFoundException ex)
            {
                System.out.println("Błąd: nie znaleziono pliku konfiguracji, przyjmuję wartości domyślne");
            }
            catch (IOException ex)
            {
                System.out.println("Błąd: nie mogę wczytać konfiguracji, przyjmuję wartości domyślne");
            }            

            System.out.println("Status: Uruchamianie...");
            try
            {
                // odczyt hosta i portu do którego mam zbindować socket                                
                bindPort = Integer.parseInt(config.getStringEntry("bind", "port", "5222"));
                bindHost = config.getStringEntry("bind", "host", "localhost");
                // podłączanie gniazda
                SocketAddress sa = new InetSocketAddress(bindHost, bindPort);
                serv = new ServerSocket();
                serv.bind(sa);
            }
            catch (IOException ex)
            {
                System.out.println("Błąd: nie mogę utworzyć gniazda serwera, sprawdź czy nie jest uruchomiona inna instancja");
                stop();
            }
            catch (NumberFormatException ex)
            {
                System.out.println("Błąd: podano niepoprawny port do zbindowania");
                stop();
            }

            try
            {
                ucp = new UserCredentialsProvider();
            }
            catch (Exception ex)
            {
                System.out.println("Błąd: nie mogę utworzyć providera danych użytkowników");
            }

            // tworzenie "puli" moich wątków
            workerPool = new ArrayList<Worker>();

            // główna pętla aplikacji
            // jeśli ktoś się podłączył, to stwórz nowy wątek typu Worker
            // i czekaj na połączenia dalej
            while(!isStopped())
            {
                Socket clientSocket = null;
                try
                {
                    clientSocket = serv.accept();
                }
                catch (IOException e)
                {
                    if(isStopped())
                    {
                        //System.out.println("Server Stopped.") ;
                        return;
                    }
                }

                // dodawanie nowego wątku klasy Worker do puli i uruchamianie go
                workerPool.add(new Worker(clientSocket));
                workerPool.get(workerPool.size() -1).start();
            }
        }
    }

    /**
     * Pokazywanie informacji o wersji i licencji
     */
    private static void showVersion()
    {
        Test();
        System.exit(0);
        
        System.out.println("Igerna version 0.1.0.0");
	System.out.println("");
        System.out.println("Copyright (C) Marcin Badurowicz 2009");
        System.out.println("Licencja GPLv3+: GNU GPL w wersji 3 lub późniejszej");
	System.out.println("<http://www.gnu.org/licenses/gpl.html>.");
        System.out.println("");
	System.out.println("Niniejszy program jest oprogramowaniem wolnodostępnym: można go");
	System.out.println("modyfikować i rozpowszechniać.");
	System.out.println("Nie ma ŻADNEJ GWARANCJI w zakresie dopuszczalnym przez prawo.");
    }

    /**
     * Pokazywanie informacji o użyciu i opcjach
     */
    private static void showHelp()
    {
        System.out.println("Igerna version 0.1.0.0");
	System.out.println("");
        System.out.println("Użycie: java IgernaServer.class [--version] [--help] [-C <plik>]");
        System.out.println("");
        System.out.println("  --version - pokazuje informacje o wersji serwera");
        System.out.println("  --help - pokazuje tą informację");
        System.out.println("  -C <plik> - ładuje konfigurację z podanego pliku, domyślnie");
        System.out.println("              jest to plik igerna.conf");
        System.out.println("");
    }

    /**
     * Wysyłanie wiadomości do danego podłączonego klienta
     */
    public static boolean sendMessage(JID recipient, Stanza st)
    {
        // iterowanie po poszczególnych wątkach roboczych
        for (Worker w : workerPool)
        {
            // jeśli klient jest podłączony i aktywny
            if (w.clientState.getState() == ClientState.ACTIVE)
            {
                // jeśli jego JID odpowiada JIDowi odbiorcy
                if (w.clientJID.equals(recipient))
                {
                    w.sendToClient(st.toString());
                    return true;
                }
            }
        }
        return false;
    }

    private static void Test()
    {
        try {
            String n = "";
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = dbf.newDocumentBuilder();
            Document xmldoc = parser.parse("test.xml");

            Iq i = new Iq(xmldoc.getElementsByTagName("iq").item(0));
            System.out.println(i.getAsNode().toString());

            xmldoc = null;
            parser.reset();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
