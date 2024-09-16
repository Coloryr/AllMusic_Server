package com.coloryr.allmusic.server.core.objs.api.music.info;

import java.util.List;

public class InfoObj {
    private List<Songs> songs;

    public boolean isOk() {
        return (songs != null && !songs.isEmpty());
    }

    public String getName() {
        if (songs == null || songs.isEmpty())
            return "";
        return songs.get(0).getName();
    }

    public String getAuthor() {
        StringBuilder Author = new StringBuilder();
        if (songs.isEmpty())
            return "";
        for (ar ar : songs.get(0).getAr()) {
            Author.append(ar.getName()).append(",");
        }
        if (Author.length() != 0) {
            Author = new StringBuilder(Author.substring(0, Author.length() - 1));
        }
        return Author.toString();
    }

    public String getAlia() {
        StringBuilder Alia = new StringBuilder();
        for (String alia : songs.get(0).getAlia()) {
            Alia.append(alia).append(",");
        }
        if (Alia.length() != 0) {
            Alia = new StringBuilder(Alia.substring(0, Alia.length() - 1));
        }
        return Alia.toString();
    }

    public String getAl() {
        return songs.get(0).getAl();
    }

    public int getLength() {
        if (songs == null)
            return 0;
        return songs.get(0).getLength();
    }

    public String getPicUrl() {
        if (songs == null)
            return null;
        return songs.get(0).getPicUrl();
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
        return name == null ? "" : name;
    }

    public List<ar> getAr() {
        return ar;
    }

    public List<String> getAlia() {
        return alia;
    }

    public String getAl() {
        return al.getName() == null ? "" : al.getName();
    }

    public String getPicUrl() {
        return al.getPicUrl();
    }
}

class ar {
    private String name;

    public String getName() {
        return name == null ? "" : name;
    }
}

class al {
    private String name;
    private String picUrl;

    public String getName() {
        return name == null ? "" : name;
    }

    public String getPicUrl() {
        return picUrl == null ? "" : picUrl;
    }
}

class h {
    private int br;
    private int size;

    public int getLength() {
        return size / br * 8000;
    }
}