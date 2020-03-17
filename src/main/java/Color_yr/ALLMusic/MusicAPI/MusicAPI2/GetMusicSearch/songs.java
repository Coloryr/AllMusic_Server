package Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicSearch;

import java.util.List;

public class songs {
    private int id;
    private String name;
    private List<artists> ar;
    private album al;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return al.getName();
    }

    public String getArtists() {
        StringBuilder a = new StringBuilder();
        for (artists temp : ar) {
            a.append(temp.getName()).append(",");
        }
        return a.substring(0, a.length() - 1);
    }
}
