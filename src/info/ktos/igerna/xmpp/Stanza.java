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

import info.ktos.igerna.XmlUtil;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 * Klasa abstrakcyjna reprezentująca poszczególne "stanza" protokołu XMPP
 */
public abstract class Stanza
{
    protected String innerXML;
    protected Node xmlnode;
    protected Document xmldoc;
    protected DocumentBuilder parser;

    public String from;
    public String to;
    public String id;
    public String type;
    public String lang;
    public String childXML;

    /**
     * Tworzenie obiektu na podstawie węzła XML-owego
     *
     * @param n
     */
    public Stanza(Node n)
    {
        this.xmlnode = n;
        
        this.id = XmlUtil.getAttributeAsString(n, "id");
        this.id = XmlUtil.getAttributeAsString(n, "from");
        this.id = XmlUtil.getAttributeAsString(n, "to");
        this.id = XmlUtil.getAttributeAsString(n, "type");
        this.id = XmlUtil.getAttributeAsString(n, "lang");
    }

    /**
     * Pobieranie "dzieci" danej stanzy
     * @return
     */
    public NodeList getChildItems()
    {
        return xmlnode.getChildNodes();
    }

    /**
     * Pobieranie stanzy w postaci węzła XML-owego
     * @return
     */
    public Node getAsNode()
    {
        return xmlnode;
    }

    /**
     * Konstruktor bezparametrowy, w zasadzie nie robi nic
     */
    public Stanza()
    {
        
    }

    /**
     * Konstruktor tworzący stanzę i wymagający podstawowych atrybutów
     *
     * @param to
     * @param from
     * @param id
     * @param type
     */
    public Stanza(String to, String from, String id, String type)
    {
        this(to, from, id, type, "en", "");
    }

    /**
     * Pełne tworzenie stanzy na podstawie tekstu
     *
     * @param to
     * @param from
     * @param id
     * @param type
     * @param lang
     * @param childXML Tekstowy XML zawierający dzieci obiektu
     */
    public Stanza(String to, String from, String id, String type, String lang, String childXML)
    {
        this.to = to;
        this.from = from;
        this.id = id;
        this.type = type;
        this.lang = lang;
        this.childXML = childXML;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            // tworzenie parsera XML
            parser = dbf.newDocumentBuilder();
            xmldoc = parser.newDocument();

            // tworzenie noda XML i przepisywanie własności w klasach potomnych dopiero
            // bo tutaj to nie ma sensu
        }
        catch (Exception ex)
        {

        }
    }

    /**
     * Mała, użyteczna funkcja tworząca XML-owy atrybut wraz z wartością, owoc
     * refaktoryzacji
     * @param name
     * @param value
     * @return
     * @throws DOMException
     */
    protected Node createAttributeValue(String name, String value) throws DOMException
    {
        Node nfrom = xmldoc.createAttribute(name);
        nfrom.setTextContent(value);
        return nfrom;
    }


    
}
