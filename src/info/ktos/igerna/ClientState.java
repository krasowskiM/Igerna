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
 * Klasa określająca stan podłączonego klienta
 */
class ClientState
{
    /**
     * Klient jest rozłączony
     */
    public static final int DISCONNECTED = 0;

    /**
     * Klient się podłączył fizycznie, ale nic jeszcze nie wysłał
     */
    public static final int CONNECTING = 1;

    /**
     * Klient wysłał już <stream>, a my mu powiedzieliśmy, by się zautoryzował
     * i przesłaliśmy mechanizmy autoryzacji
     */
    public static final int AUTHORIZING = 2;

    /**
     * Klient się zautoryzował, przedstawiamy mu inne możliwości
     */
    public static final int AUTHORIZED = 3;

    /**
     * Klient jest w trakcie podłączana do zasobu i ustanawiania sesji
     */
    public static final int BINDING = 4;

    /**
     * Klient się podłączył, ustanawia sesję
     */
    public static final int BOUND = 5;

    /**
     * Klient się podłączył do zasobu, ustanowił sesję, jest w trakcie pracy
     * i gotowy do działania
     */
    public static final int ACTIVE = 6;

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
