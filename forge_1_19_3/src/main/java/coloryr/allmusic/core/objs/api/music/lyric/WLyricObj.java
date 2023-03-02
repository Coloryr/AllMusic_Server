package coloryr.allmusic.core.objs.api.music.lyric;

public class WLyricObj {
    private lrc lrc;
    private tlyric tlyric;
    private klyric klyric;
    private yrc yrc;
    private boolean nolyric;
    private boolean uncollected;

    public boolean isOk() {
        return lrc != null || isNone();
    }

    public String getLyric() {
        return lrc.getLyric();
    }

    public String getTlyric() {
        if (tlyric == null)
            return null;
        return tlyric.getLyric();
    }

    public String getKlyric() {
        if (klyric == null)
            return null;
        return klyric.getLyric();
    }

    public String getYrc() {
        if (yrc == null)
            return null;
        return yrc.getLyric();
    }

    public boolean getVersion() {
        return yrc != null;
    }

    public boolean isNone() {
        return nolyric || uncollected;
    }
}

class lrc {
    private String lyric;

    public String getLyric() {
        return lyric == null ? "" : lyric;
    }
}

class tlyric {
    private String lyric;

    public String getLyric() {
        return lyric;
    }
}

class klyric {
    private String lyric;
    private int version;

    public int getVersion() {
        return version;
    }

    public String getLyric() {
        return lyric == null ? "" : lyric;
    }
}

class yrc {
    private int version;
    private String lyric;

    public int getVersion() {
        return version;
    }

    public String getLyric() {
        return lyric == null ? "" : lyric;
    }
}