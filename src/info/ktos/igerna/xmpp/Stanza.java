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

    public Stanza(Node n)
    {
        this.xmlnode = n;
        
        this.id = XmlUtil.getAttributeAsString(n, "id");
        this.id = XmlUtil.getAttributeAsString(n, "from");
        this.id = XmlUtil.getAttributeAsString(n, "to");
        this.id = XmlUtil.getAttributeAsString(n, "type");
        this.id = XmlUtil.getAttributeAsString(n, "lang");
    }

    public NodeList getChildItems()
    {
        return xmlnode.getChildNodes();
    }

    public Node getAsNode()
    {
        return xmlnode;
    }

    public Stanza()
    {
        
    }

    public Stanza(String to, String from, String id, String type)
    {
        this(to, from, id, type, "en", "");
    }

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


    
}
