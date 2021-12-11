package coloryr.allmusic.music.api.obj.music.lyric;

public class WLyricOBJ {
    private lrc lrc;
    private tlyric tlyric;
    private boolean nolyric;
    private boolean uncollected;

    public boolean isok() {
        return lrc != null || isNone();
    }

    public String getLyric() {
        return lrc.getLyric();
    }

    public String getTlyric() {
        if(tlyric == null)
            return null;
        return tlyric.getLyric();
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