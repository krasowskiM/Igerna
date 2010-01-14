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
 * along with Igerna. If not, see <http://www.gnu.org/licenses/>.
 */
package info.ktos.igerna;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa udostępniająca sprawdzanie poprawności danych użytkownika
 */
public class UserCredentialsProvider
{
    private String username;
    private String password;
    private String passwdFile;
    private ArrayList<String[]> users;

    /**
     * Zwraca sumę MD5 stringa
     *
     * Uwaga: funkcja ma błąd, jeżeli suma MD5 zaczyna się od 0,
     * zostanie to 0 zignorowane, przez co funkcja nie jest kompatybilna
     * z innymi implementacjami MD5 we wszystkich przypadkach.
     * Dla nas jednak wystarcza.
     *
     * @param s Tekst do obliczenia sumy
     * @return Suma MD5 wprowadzonego tekstu
     */
    public static String md5(String s)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            return new BigInteger(1, m.digest()).toString(16);
        }
        catch (NoSuchAlgorithmException ex)
        {
            System.out.println("Błąd: MD5 nie jest wspierany, używam PLAIN");
            return s;
        }
    }

    public ArrayList<String[]> getUserData()
    {
        return users;
    }

    public void removeUser(String userName)
    {

    }

    /**
     * Konstruktor, odczyt pliku konfiguracyjnego
     * 
     * @param passwdFile
     */
    public UserCredentialsProvider(String passwdFile) throws FileNotFoundException, IOException
    {
        this.passwdFile = passwdFile;

        FileReader fileReader = new FileReader(passwdFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        users = new ArrayList<String[]>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null)
        {            
            if (!line.trim().equals(""))
                users.add(line.split(":"));
        }
        bufferedReader.close();
    }

    /**
     * Sprawdza czy zakodowane danym mechanizmem hasło jest poprawne
     * 
     * @param mechanism
     * @param data
     * @return
     */
    public boolean check(String mechanism, String data)
    {
        if (mechanism.equals("PLAIN"))
        {
            String d = new String(util.Base64.decodeFast(data));            

            String[] e = d.split("\0");            
            username = e[1];
            password = e[2];

            try
            {                
                return UserCredentialsProvider.md5(password).equals(getPassword(username));
            }
            catch (Exception ex)
            {
                // wyjątek znaczy, że chyba użytkownika nie znaleziono
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Pobiera hasło dla danego użytkownika
     * 
     * @param userName
     * @return
     * @throws Exception
     */
    public String getPassword(String userName) throws Exception
    {
        for (String[] ud : users)
        {
            if (ud[0] != null)
                if (ud[0].equals(userName))
                    return ud[1];
        }

        throw new Exception("User not found");        
    }

    /**
     * Pobiera nazwę użytkownika (geckos) dla danego użytkownika
     * 
     * @param userName
     * @return
     * @throws Exception
     */
    public String getGeckos(String userName) throws Exception
    {        
        for (String[] ud : users)
        {
            if (ud[0] != null)
                if (ud[0].equals(userName))
                    return ud[4];
        }

        throw new Exception("User not found");        
    }

    public String lastUsername()
    {
        return this.username;
    }
}
