package Color_yr.AllMusic.music.api.song.search;

public class SearchOBJ {
    private final String id;
    private final String name;
    private final String author;
    private final String al;

    public SearchOBJ(String ID, String Name, String Author, String Al) {
        this.id = ID;
        this.name = Name;
        this.author = Author;
        this.al = Al;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public String getAl() {
        return al;
    }
}
