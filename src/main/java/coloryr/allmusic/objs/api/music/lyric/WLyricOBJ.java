package coloryr.allmusic.objs.api.music.lyric;

public class WLyricOBJ {
    private lrc lrc;
    private tlyric tlyric;
    private klyric klyric;
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

class klyric
{
    private String lyric;

    public String getLyric() {
        return lyric == null ? "" : lyric;
    }
}