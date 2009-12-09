package info.ktos.igerna.xmpp;

/**
 * Klasa abstrakcyjna reprezentująca poszczególne "stanza" protokołu XMPP
 */
public abstract class Stanza
{
    protected String xml;

    public JID from;
    public JID to;
    public String id;
    public String type;
    public String lang;

    public Stanza(String xml)
    {

    }
    
    public abstract String toXML();
}
