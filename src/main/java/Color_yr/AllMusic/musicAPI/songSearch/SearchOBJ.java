package Color_yr.AllMusic.musicAPI.songSearch;

public class SearchOBJ {
    private final String ID;
    private final String Name;
    private final String Author;
    private final String Al;

    public SearchOBJ(String ID, String Name, String Author, String Al) {
        this.ID = ID;
        this.Name = Name;
        this.Author = Author;
        this.Al = Al;
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

    public String getAl() {
        return Al;
    }
}
