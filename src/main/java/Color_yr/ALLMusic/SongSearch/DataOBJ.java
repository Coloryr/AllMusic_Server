package Color_yr.ALLMusic.SongSearch;

import java.util.List;

public class DataOBJ {
    private data data;

    public boolean isok() {
        return data != null;
    }

    public List<songs> getResult() {
        return data.getSongs();
    }
}

class data {
    private List<songs> songs;

    public List<songs> getSongs() {
        return songs;
    }
}

class songs {
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

class artists {
    private String name;

    public String getName() {
        return name;
    }
}

class album {
    private String name;

    public String getName() {
        return name;
    }
}