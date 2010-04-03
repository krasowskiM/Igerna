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
package info.ktos.igerna;

/**
 * Klasa będąca odpowiedzialna za bufory wiadomości dla określonego
 * klienta
 *
 * Zasadniczo, zamiast operowania na stringach jak teraz, tymczasowo,
 * to można by ją wykorzystać do odczytywania informacji z plików,
 * co jednocześnie załatwiłoby przechowywanie wiadomości off-line
 */
class MessageBuffer
{
    private String buffer;

    public MessageBuffer()
    {
        // odczytywanie pliku z buforem i takie tam zabawy
        clearBuffer();
    }

    /**
     * Odczytywanie bufora
     *
     * @return
     */
    public synchronized String getBuffer()
    {
        return buffer;
    }

    /**
     * Dopisywanie do bufora
     *
     * @param newbuf
     */
    public synchronized void addToBuffer(String newbuf)
    {
        buffer += newbuf;
    }

    /**
     * Czysczenie bufora
     */
    public synchronized void clearBuffer()
    {
        buffer = "";
    }

    /**
     * Sprawdza czy bufor jest czysty
     * 
     * @return
     */
    public synchronized boolean isClean()
    {
        return buffer.equals("");
    }
}
