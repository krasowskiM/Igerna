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

    public JID(String name, String serv, String res)
    {
        this.userName = name;
        this.server = serv;
        this.resource = res;
    }

    public void setResource(String newres)
    {
        this.resource = newres;
    }

    @Override
    public String toString()
    {
        return String.format("%1s@%2s/%3s", this.userName, this.server,
                this.resource);
    }

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
