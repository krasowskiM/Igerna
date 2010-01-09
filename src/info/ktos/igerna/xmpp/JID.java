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
package info.ktos.igerna.xmpp;

/**
 * Klasa będąca reprezentacją tzw. JabberID
 */
public class JID
{
    private String userName;
    private String server;
    private String resource;

    public String getUserName()
    {
        return this.userName;
    }
    
    public String getServer()
    {
        return this.server;
    }

    public String getResource()
    {
        return this.resource;
    }

    public void setResource(String newres)
    {
        this.resource = newres;
    }

    /**
     * Tworzenie JabberID na podstawie trzech parametrów: nazwy użytkownika,
     * adresu serwera oraz zasobu
     *
     * @param name Nazwa użytkownika
     * @param serv Adres serwera
     * @param res Zasób
     */
    public JID(String name, String serv, String res)
    {
        this.userName = name;
        this.server = serv;
        this.resource = res;
    }

    /**
     * Tworzenie JabberID na podstawie jego tekstowej wersji
     *
     * @param s
     */
    public JID(String s)
    {
        int at = s.indexOf("@");
        int slash = s.indexOf("/");
        
        if (slash == -1)
        {
            // jeśli w JID nie ma zasobu
            this.userName = s.substring(0, at);
            this.server = s.substring(at +1);
            this.resource = "";
        }
        else
        {
            this.userName = s.substring(0, at);
            this.server = s.substring(at +1, slash);
            this.resource = s.substring(slash +1);
        }
    }    

    @Override
    public String toString()
    {
        if (!this.resource.equals(""))
            return String.format("%1s@%2s/%3s", this.userName, this.server,
                    this.resource);
        else
            return this.toStringWithoutResource();
    }

    /**
     * Zwraca JID jako tekst, jednakże bez zasobu
     * @return
     */
    public String toStringWithoutResource()
    {
        return String.format("%1s@%2s", this.userName, this.server);
    }


    @Override
    public boolean equals(Object j)
    {
        if (j.getClass() == this.getClass())
            return this.hashCode() == j.hashCode();
        else
            return false;
    }

    /**
     * Porównywanie dwóch JID-ów bez uwzględnienia zasobów
     *
     * @param j
     * @return
     */
    public boolean equalsNoResource(JID j)
    {
        return (
                (this.userName.equals(j.getUserName())) &&
                (this.server.equals(j.getServer()))
               );

    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 83 * hash + (this.userName != null ? this.userName.hashCode() : 0);
        hash = 83 * hash + (this.server != null ? this.server.hashCode() : 0);
        hash = 83 * hash + (this.resource != null ? this.resource.hashCode() : 0);
        return hash;
    }
}
