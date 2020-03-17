package Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicInfo;

import java.util.List;

public class InfoOBJ {
    private data data;

    public boolean isok() {
        return data != null;
    }

    public String getName() {
        if (data.getSongs().size() == 0)
            return null;
        return data.getSongs().get(0).getName();
    }

    public String getAuthor() {
        StringBuilder Author = new StringBuilder();
        for (ar ar : data.getSongs().get(0).getAr()) {
            Author.append(ar.getName()).append(",");
        }
        if (Author.length() != 0) {
            Author = new StringBuilder(Author.substring(0, Author.length() - 1));
        }
        return Author.toString();
    }

    public String getAlia() {
        StringBuilder Alia = new StringBuilder();
        for (String alia : data.getSongs().get(0).getAlia()) {
            Alia.append(alia).append(",");
        }
        if (Alia.length() != 0) {
            Alia = new StringBuilder(Alia.substring(0, Alia.length() - 1));
        }
        return Alia.toString();
    }

    public String getAl() {
        return data.getSongs().get(0).getAl();
    }

    public int getLength() {
        if (data.getSongs() == null)
            return 0;
        return data.getSongs().get(0).getLength();
    }
}

class data {
    private List<Songs> songs;

    public List<Songs> getSongs() {
        return songs;
    }
}

class Songs {
    private String name;
    private List<ar> ar;
    private List<String> alia;
    private al al;
    private h l;
    private h m;
    private h h;

    public int getLength() {
        if (l != null)
            return l.getLength();
        if (m != null)
            return m.getLength();
        if (h != null)
            return h.getLength();
        return 0;
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

class h {
    private int br;
    private int size;

    public int getLength() {
        return size / br * 8000;
    }
}