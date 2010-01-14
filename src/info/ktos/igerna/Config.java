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

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Konfiguracja serwera
 */
public class Config
{
    private String path;    
    private ArrayList<String> lines;

    /**
     * Konstruktor klasy Config, ustawianie ścieżki pliku
     *
     * @param path
     */
    public Config(String path)
    {
        this.path = path;
    }

    /**
     * Pobieranie wpisu typu string z pliku konfiguracyjnego
     *
     * Metoda pobiera wartość danego wpisu (typu string) z danej
     * sekcji pliku konfiguracyjnego
     *
     * @param section Sekcja pliku
     * @param entry Wpis
     * @param def Wartość zwracana w razie nieznalezienia sekcji/wartości
     * @return Zawartość danego wpisu
     */
    public String getStringEntry(String section, String entry, String def)
    {        
        String reg = String.format("^%s\\.%s([\\s]*)=([\\s]*)\"[a-z0-9.-]+\";$", section, entry);
        Pattern r = Pattern.compile(reg);
        Pattern r2 = Pattern.compile("\"[a-z0-9.-]+\"");
        // TODO: tutaj miała być jakaś optymalizacja, ale zapomniałem jaka
        String result = def;

        for (String s : lines)
        {
            Matcher m = r.matcher(s);
            if (m.find())
            {
                Matcher m2 = r2.matcher(m.group());
                if (m2.find())
                {
                    result = m2.group().replace("\"", "");
                }
            }
        }

        return result;
    }

    /**
     * Pobieranie wpisu typu array z pliku konfiguracyjnego
     *
     * Metoda pobiera wartość danego wpisu (typu array) z danej
     * sekcji pliku konfiguracyjnego
     *
     * @param section Sekcja pliku
     * @param entry Wpis
     * @param def Wartość zwracana w razie nieznalezienia sekcji/wartości
     * @return Zawartość danego wpisu jako tablica stringów
     */
    public String[] getArrayEntry(String section, String entry, String[] def)
    {        
        String reg = String.format("^%1s.%2s(\\s*)=(\\s*)\\((\\s*\".*\",?\\s*)+\\);$", section, entry);
        Pattern r = Pattern.compile(reg);
        Pattern r2 = Pattern.compile("\\(.*\\)");

        String[] result = def;

        for (String s : lines)
        {
            Matcher m = r.matcher(s);
            if (m.find())
            {
                Matcher m2 = r2.matcher(m.group());
                if (m2.find())
                {
                    String t = m2.group().replace("(", "").replace(")", "").trim();
                    result = t.split(",");
                    
                    for (int j = 0; j < result.length; j++)
                    {
                        result[j] = result[j].replace("\"", "").trim();
                    }
                }
            }
        }

        return result;
    }

    /**
     * Ustawianie wpisu w pliku konfiguracyjnym
     *
     * Metoda ustawia daną wartość wpisu w danej sekcji
     * w pliku konfiguracyjnym
     *
     * @param section Sekcja pliku
     * @param entry Nazwa wpisu
     * @param newValue Nowa wartość
     */
    public void setStringEntry(String section, String entry, String newValue)
    {
        String reg = String.format("^%s\\.%s([\\s]*)=([\\s]*)\"[a-z0-9.-]+\";$", section, entry);
        Pattern r = Pattern.compile(reg);

        int i = 0;
        boolean f = false;
        for (String s : lines)
        {
            Matcher m = r.matcher(s);
            if (m.find())
            {
                f = true;
                break;
            }
                
            i++;
        }

        if (f)
        {
            // jeśli znalazło, to zamieniamy
            lines.set(i, String.format("%s.%s = \"%s\";", section, entry, newValue));
        }
        else
        {
            // jeśli nie znalazło, to musimy dodać
            lines.add(String.format("%s.%s = \"%s\";", section, entry, newValue));
        }
    }

    /**
     * Odczytywanie pliku konfiguracyjnego
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void readFile() throws FileNotFoundException, IOException
    {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        lines = new ArrayList<String>();
        String line = null;
        while ((line = br.readLine()) != null)
        {
            // ignorowanie komentarzy i pustych linii
            if (!line.startsWith("#") && !line.trim().equals(""))
                lines.add(line);
        }
        br.close();
        fr.close();
    }

    /**
     * Zapis (zmodyfikowanej) konfiguracji do pliku
     *
     * W trakcie zapisu gubione są niestety komentarze
     */
    public void saveFile() throws IOException
    {
        FileWriter fw = new FileWriter(this.path);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String s : lines)
        {
            bw.write(s);
            bw.newLine();
        }

        bw.close();
        fw.close();
    }

}