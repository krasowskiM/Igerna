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

/**
 * Klasa będąca reprezentacją tzw. JabberID
 */
class JID
{
    private String userName;
    private String server;
    private String resource;

    public JID(String name, String serv, String res)
    {
        this.userName = name;
        this.server = serv;
        this.resource = res;
    }

    @Override
    public String toString()
    {
        return String.format("%1s@%2s/%3s", this.userName, this.server,
                this.resource);
    }
}
