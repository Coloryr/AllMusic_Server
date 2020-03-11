package Color_yr.ALLMusic.SongLyric;

public class LyricOBJ {
    private boolean sgc;
    private boolean sfy;
    private boolean qfy;
    private lrc lrc;
    private tlyric tlyric;

    public boolean isok()
    {
        return lrc!=null;
    }

    public String getLyric() {
        return lrc.getLyric();
    }

    public String getTlyric() {
        return tlyric.getLyric();
    }
}

class lrc {
    private int version;
    private String lyric;

    public String getLyric() {
        return lyric == null ? "" : lyric;
    }
}

class tlyric {
    private int version;
    private String lyric;

    public String getLyric() {
        return lyric;
    }
}