package coloryr.allmusic.core.objs.api.music.search;

import java.util.List;

public class songs {
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
