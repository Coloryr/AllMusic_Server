package Color_yr.ALLMusic.Search;

import java.util.List;

public class DataOBJ {
    private result result;

    public List<songs> getResult() {
        return result.getSongs();
    }
}
class result{
    private List<songs> songs;

    public List<songs> getSongs() {
        return songs;
    }
}
class songs {
    private int id;
    private String name;
    private List<artists> artists;
    private album album;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album.getName();
    }

    public String getArtists() {
        StringBuilder a = new StringBuilder();
        for (artists temp : artists) {
            a.append(temp.getName()).append(",");
        }
        return a.substring(0, a.length() - 1);
    }
}
class artists{
    private String name;

    public String getName() {
        return name;
    }
}
class album{
    private String name;

    public String getName() {
        return name;
    }
}