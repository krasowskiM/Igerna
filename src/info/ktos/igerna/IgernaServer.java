/*
 * Copyright (C) Marcin Badurowicz 2009
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

/**
 * IgernaServer
 *
 * Główna, uruchomieniowa klasa aplikacji, którazajmuje się bądź uruchomieniem
 * serwera, bądź pokazaniem pewnych dodatkowych informacji.
 *
 * @category    Igerna
 * @author      Marcin Badurowicz <ktos at ktos dot info>
 * @version     0.1
 */
public class IgernaServer
{

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
        if ((args.length == 1) && (args[0].equals("--version")))
            IgernaServer.showVersion();
    }

    private static void showVersion()
    {
        System.out.println("Igerna version 0.1.0.0");
        System.out.println("Copyright (C) Marcin Badurowicz 2009");
        System.out.println("");
    }

}
