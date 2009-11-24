package info.ktos.igerna;

class Config
{
    private String path;

    /**
     * Tworzenie klasy Config, odczytywanie pliku konfiguracyjnego ze wskazanej
     * ścieżki.
     *
     * @param path
     */
    public Config(String path)
    {
        this.path = path;
    }

    public String getEntry(String section, String entry)
    {
        return "";
    }

    public void setEntry(String section, String entry)
    {

    }
    
}