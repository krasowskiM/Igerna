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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IgernaServer
{    
    private static Config config;
    private static String configFile = "igerna.conf";
    private static ServerSocket serv;
    private static boolean stopped;

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
        if (args.length == 1)
		{
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
            else
            {
                IgernaServer.showHelp();
            }
		}
        else
        {
            config = new Config(configFile);

            System.out.println("Uruchamianie...");
            try
            {
                serv = new ServerSocket(5222);
            }
            catch (IOException ex)
            {
                System.out.println("Błąd tworzenia serwera, nie mogę utworzyć gniazda!");
                stop();
            }

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

                new Thread(
                        new Worker(clientSocket)
                    ).start();
            }
        }
    }

    /**
     * Pokazywanie informacji o wersji i licencji
     */
    private static void showVersion()
    {
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
}
