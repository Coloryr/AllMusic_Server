package Color_yr.ALLMusic.SongInfo;

import java.util.List;

public class InfoOBJ {
    private List<Songs> songs;
    private int code;

    public String getName() {
        if (songs.size() == 0)
            return null;
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

    public String getAl() {
        return songs.get(0).getAl();
    }

    public int getLength() {
        return songs.get(0).getLength();
    }
}

class Songs {
    private String name;
    private List<ar> ar;
    private List<String> alia;
    private al al;
    private h h;

    public int getLength() {
        return h.getLength();
    }

    public String getName() {
        return name;
    }

    public List<ar> getAr() {
        return ar;
    }

    public List<String> getAlia() {
        return alia;
    }

    public String getAl() {
        return al.getName();
    }
}

class ar {
    private String name;

    public String getName() {
        return name;
    }
}

class al {
    private String name;

    public String getName() {
        return name;
    }
}

class h{
    private int br;
    private int size;

    public int getLength() {
        return size / br * 8000;
    }
}