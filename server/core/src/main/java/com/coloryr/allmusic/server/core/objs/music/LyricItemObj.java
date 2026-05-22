package com.coloryr.allmusic.server.core.objs.music;

public class LyricItemObj {
    public String lyric;
    public String tlyric;

    public long start;

    public LyricItemObj(String lyric, String tlyric, long start) {
        this.lyric = lyric;
        this.tlyric = tlyric;
        this.start = start;
    }

    public LyricItemObj copy() {
        return new LyricItemObj(this.lyric, this.tlyric, this.start);
    }
}
