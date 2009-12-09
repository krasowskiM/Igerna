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
 * Klasa określająca stan podłączonego klienta
 */
class ClientState
{
    public static final int DISCONNECTED = 0;
    public static final int CONNECTED = 1;
    public static final int AUTHORIZED = 2;
    public static final int BOUND = 3;
    public static final int ACTIVE = 4;

    private int state = 0;

    /**
     * Pobiera aktualny stan
     */
    public int getState()
    {
        return this.state;
    }

    /**
     * Ustawia nowy stan klienta
     * @param newState Nowy stan
     */
    public void setState(int newState)
    {
        this.state = newState;
    }
}
