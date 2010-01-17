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

    /**
     * Pobranie listy wszystkich użytkowników
     * 
     * @return
     */
    public ArrayList<String[]> getUserData()
    {
        return users;
    }

    /**
     * Usunięcie użytkownika z listy
     * 
     * @param userName
     */
    public void removeUser(String userName)
    {        
        removeUser(findUser(userName));
    }

    public void removeUser(int i)
    {
        users.remove(i);
    }

    private int findUser(String userName)
    {
        int i = 0;
        for (String[] strings : users)
        {
            if (strings[0] != null && strings[0].equals(userName))
            {
                break;
            }
            i++;
        }
        return i;
    }

    /**
     * Zmiana istniejącego użytkownika na liście
     *
     * @param userName
     * @param newUserName
     * @param newPassword
     * @param newGeckos
     */
    public void changeUser(String userName, String newUserName, String newPassword, String newGeckos)
    {
        int i = findUser(userName);
        changeUser(i, newUserName, newPassword, newGeckos);
    }

    public void changeUser(int i, String newUserName, String newPassword, String newGeckos)
    {
        String[] dat = users.get(i);

        if (!newUserName.equals(""))
        {
            dat[0] = newUserName;
            dat[5] = "/home/" + newUserName;
        }

        if (!newPassword.equals(""))
            dat[1] = newPassword;
        if (!newGeckos.equals(""))
            dat[4] = newGeckos;

        users.set(i, dat);
    }

    /**
     * Dodanie nowego użytkownika do listy
     * 
     * @param userName
     * @param newPassword
     * @param newGeckos
     */
    public void addUser(String userName, String newPassword, String newGeckos)
    {        
        users.add(new String[] { userName, md5(newPassword), "0", "0", newGeckos, "/home/" + userName, "/bin/bash" });
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

    private String arrayjoin(String[] arr, String separator)
    {
        String result = "";

        for (String s : arr)
        {
            result = result + s + separator;
        }

        return result.substring(0, result.length() - separator.length());        
    }

    /**
     * Zapis zmodyfikowanej listy użytkowników do pliku
     */
    public void saveToFile() throws IOException
    {
        FileWriter fw = new FileWriter(this.passwdFile);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String[] strings : users)
        {
            bw.write(arrayjoin(strings, ":"));
            bw.newLine();
        }

        bw.close();
        fw.close();
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
