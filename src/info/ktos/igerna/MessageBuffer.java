package info.ktos.igerna;

/**
 * Klasa będąca odpowiedzialna za bufory wiadomości dla określonego
 * klienta
 *
 * Zasadniczo, zamiast operowania na stringach jak teraz, tymczasowo,
 * to można by ją wykorzystać do odczytywania informacji z plików,
 * co jednocześnie załatwiłoby przechowywanie wiadomości off-line
 */
class MessageBuffer
{
    private String buffer;

    public MessageBuffer()
    {
        // odczytywanie pliku z buforem i takie tam zabawy
        clearBuffer();
    }

    public String getBuffer()
    {
        return buffer;
    }

    public void addToBuffer(String newbuf)
    {
        buffer += newbuf;
    }

    public void clearBuffer()
    {
        buffer = "";
    }

    public boolean isClean()
    {
        return buffer.equals("");
    }
}
