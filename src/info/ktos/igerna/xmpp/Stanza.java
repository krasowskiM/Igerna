package info.ktos.igerna.xmpp;

import org.w3c.dom.*;

/**
 * Klasa abstrakcyjna reprezentująca poszczególne "stanza" protokołu XMPP
 */
public abstract class Stanza
{
    protected String xml;
    protected Node xmlnode;

    public String from;
    public String to;
    public String id;
    public String type;
    public String lang;

    public Stanza(Node n)
    {
        this.xmlnode = n;
        this.xml = n.getTextContent();
        // TODO: parsowanie XML i przepisywanie odpowiednich własności

        NamedNodeMap att = n.getAttributes();
        if (att.getNamedItem("id") != null) this.id = att.getNamedItem("id").getNodeValue(); else this.id = "";
        if (att.getNamedItem("from") != null) this.from = att.getNamedItem("from").getNodeValue(); else this.from = "";
        if (att.getNamedItem("to") != null) this.to = att.getNamedItem("to").getNodeValue(); else this.to = "";
        if (att.getNamedItem("type") != null) this.type = att.getNamedItem("type").getNodeValue(); else this.type = "";
        if (att.getNamedItem("xml:lang") != null) this.lang = att.getNamedItem("xml:lang").getNodeValue(); else this.lang = "";
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

    public Stanza(String to, String from, String id, String type, String lang)
    {
        this.to = to;
        this.from = from;
        this.id = id;
        this.type = type;
        this.lang = lang;
    }
}
