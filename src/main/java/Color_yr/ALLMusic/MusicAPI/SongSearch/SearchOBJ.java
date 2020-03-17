package Color_yr.ALLMusic.MusicAPI.SongSearch;

public class SearchOBJ {
    private String ID;
    private String Name;
    private String Author;
    private String Aila;

    public SearchOBJ(String ID, String Name, String Author, String Aila) {
        this.ID = ID;
        this.Name = Name;
        this.Author = Author;
        this.Aila = Aila;
    }

    public String getName() {
        return Name;
    }

    public String getAuthor() {
        return Author;
    }

    public String getID() {
        return ID;
    }

    public String getAila() {
        return Aila;
    }
}
