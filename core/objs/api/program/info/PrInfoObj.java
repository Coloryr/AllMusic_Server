package com.coloryr.allmusic.server.core.objs.api.program.info;

public class PrInfoObj {

    private program program;

    public boolean isOk() {
        return (program != null && program.getMainSong().getName() != null);
    }

    public String getId() {

        return String.valueOf(program.getMainSong().getId());
    }

    public String getName() {

        return program.getMainSong().getName();
    }

    public String getAlia() {
        if (program.getDj() == null)
            return null;
        return program.getDj().getBrand();
    }

    public long getLength() {
        return program.getMainSong().getLength();
    }

    public String getAuthor() {
        return program.getDj().getNickname();
    }
}

class mainSong {

    private String name;
    private long id;
    private hMusic hMusic;
    private hMusic mMusic;
    private hMusic lMusic;
    private hMusic bMusic;

    public long getLength() {
        if (hMusic != null)
            return hMusic.getLength();
        else if (mMusic != null)
            return mMusic.getLength();
        else if (lMusic != null)
            return lMusic.getLength();
        else if (bMusic != null)
            return bMusic.getLength();
        return 0;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class program {
    private dj dj;
    private mainSong mainSong;

    public dj getDj() {
        return dj;
    }

    public mainSong getMainSong() {
        return mainSong;
    }
}

class dj {
    private String brand;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public String getBrand() {
        return brand;
    }
}

class hMusic {
    private long size;
    private long bitrate;

    public long getLength() {
        return size / bitrate * 8000;
    }
}