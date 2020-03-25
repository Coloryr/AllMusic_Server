package Color_yr.ALLMusic.MusicAPI.MusicAPI3.GetMusicLyric;

public class LyricOBJ {
    private lrc lrc;
    private tlyric tlyric;

    public boolean isok() {
        return lrc != null;
    }

    public String getLyric() {
        return lrc.getLyric();
    }

    public String getTlyric() {
        return tlyric.getLyric();
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