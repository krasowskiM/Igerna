/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.ktos.igerna;

import org.w3c.dom.Node;

/**
 * Klasa zawierająca pewne dodatkowe funkcje do obsługi XML, używane
 * zarówno przez klasy z pakietu xmpp, jak i XMPPStreamReader 
 */
public class XmlUtil
{

    /**
     * Pobiera atrybut danego węzła jako string, w przeciwnym razie
     * zwraca pustego stringa.
     *
     * Metoda refaktoryzowana z obsługi zdarzeń XMPPStreamReadera
     * żeby mi było nieco łatwiej, ale okazała się potrzebna też
     * bezpośrednio w Stanza
     *
     * @param n Węzeł
     * @param att Tekstowa nazwa atrybutu
     * @return Tekstowa zawartość atrybutu albo pusty string jeśli atrybut nie istnieje
     */
    public static String getAttributeAsString(Node n, String att)
    {
        if (n.getAttributes().getNamedItem(att) == null)
        {
            return "";
        }
        else
        {
            return n.getAttributes().getNamedItem(att).getTextContent();
        }
    }

}
