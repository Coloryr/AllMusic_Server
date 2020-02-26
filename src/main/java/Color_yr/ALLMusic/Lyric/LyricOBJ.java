package Color_yr.ALLMusic.Lyric;

public class LyricOBJ {
    private boolean sgc;
    private boolean sfy;
    private boolean qfy;
    private lrc lrc;
    private tlyric tlyric;

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