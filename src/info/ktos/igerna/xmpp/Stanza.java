package info.ktos.igerna.xmpp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Klasa abstrakcyjna reprezentująca poszczególne "stanza" protokołu XMPP
 */
public abstract class Stanza
{
    protected String xml;
    protected DocumentBuilder parser;
    protected BufferedReader input;
    protected Document xmldoc;

    public JID from;
    public JID to;
    public String id;
    public String type;
    public String lang;

    public Stanza(String xml) throws ParserConfigurationException, IOException, SAXException
    {
        this.xml = xml;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // tworzenie parsera XML
        parser = dbf.newDocumentBuilder();
        InputStream xmlis = new ByteArrayInputStream(xml.getBytes());
        xmldoc = parser.parse(xmlis);
    }
    
    public abstract String toXML();

    public String getRawData()
    {
        return this.xml;
    }
}
