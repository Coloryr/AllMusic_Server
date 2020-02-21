package Color_yr.ALLMusic.Song;

import java.util.List;

public class InfoOBJ {
    private List<Songs> songs;
    private int code;

    public String getName() {
        return songs.get(0).getName();
    }

    public String getAuthor() {
        String Author = "";
        for (ar ar : songs.get(0).getAr()) {
            Author += ar.getName() + ",";
        }
        if (Author.length() != 0) {
            Author = Author.substring(0, Author.length() - 1);
        }
        return Author;
    }

    public String getAlia() {
        String Alia = "";
        for (String alia : songs.get(0).getAlia()) {
            Alia += alia + ",";
        }
        if (Alia.length() != 0) {
            Alia = Alia.substring(0, Alia.length() - 1);
        }
        return Alia;
    }
}

class Songs {
    private String name;
    private List<ar> ar;
    private List<String> alia;

    public String getName() {
        return name;
    }

    public List<ar> getAr() {
        return ar;
    }

    public List<String> getAlia() {
        return alia;
    }
}

class ar {
    private String name;

    public String getName() {
        return name;
    }
}