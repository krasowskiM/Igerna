/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.ktos.igerna.xmpp.xeps;

import info.ktos.igerna.xmpp.Iq;

/**
 *
 * @author Marcin
 */
public class SoftwareVersion
{
    public static Iq get(String to, String from, String id)
    {
        return new Iq(to, from, id, "result",
                "", "<query xmlns='jabber:iq:version'>" +
                    "<name>Igerna</name>" +
                    "<version>0.2</version></query>");
    }

}
