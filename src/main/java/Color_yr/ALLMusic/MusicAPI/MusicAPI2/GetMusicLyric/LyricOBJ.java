package Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicLyric;

import Color_yr.ALLMusic.MusicAPI.SongLyric.Lyric;

public class LyricOBJ implements Lyric {
    private lrc lrc;
    private tlyric tlyric;
    private boolean nolyric;
    private boolean uncollected;

    public boolean isok() {
        return lrc != null;
    }

    public String getLyric() {
        return lrc.getLyric();
    }

    public String getTlyric() {
        return tlyric.getLyric();
    }

    public boolean isNone() {
        return nolyric || uncollected;
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