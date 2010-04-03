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
package info.ktos.igerna.xmpp.xeps;

import info.ktos.igerna.xmpp.Iq;
import info.ktos.igerna.xmpp.JID;

/**
 * Klasa odpowiadająca za XEP-0054: vcard-tmp, generowanie Iq
 * odpowiedniego, zawierającego vCard użytkownika
 *
 * Zasadniczo to vCardy użytkowników powinny być gdzieś przechowywane
 */
public class Vcard
{
    public static Iq get(String to, String from, String id, JID owner, String nickname)
    {
        String vcard = "<vCard xmlns='vcard-temp'><NICKNAME>" + nickname + "</NICKNAME></vCard>";
        return new Iq(to, from, id, "result", "", vcard);
    }
}
