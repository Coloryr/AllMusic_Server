package Color_yr.ALLMusic.MusicAPI.SongSearch;

public class SearchOBJ {
    private String ID;
    private String Name;
    private String Author;
    private String Al;

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
