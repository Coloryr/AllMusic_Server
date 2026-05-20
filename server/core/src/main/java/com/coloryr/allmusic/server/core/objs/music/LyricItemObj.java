package com.coloryr.allmusic.server.core.objs.music;

public class LyricItemObj {
    public String lyric;
    public String tlyric;

    public LyricItemObj(String lyric, String tlyric) {
        this.lyric = lyric;
        this.tlyric = tlyric;
    }

    public LyricItemObj copy() {
        return new LyricItemObj(this.lyric, this.tlyric);
    }
}
