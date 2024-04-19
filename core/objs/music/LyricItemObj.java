package com.coloryr.allmusic.server.core.objs.music;

import com.coloryr.allmusic.server.core.AllMusic;

public class LyricItemObj {
    public String lyric;
    public String tlyric;

    public LyricItemObj(String lyric, String tlyric) {
        this.lyric = lyric;
        this.tlyric = tlyric;
    }

    public String getString() {
        if (lyric == null || lyric.isEmpty())
            return "";
        String data;
        if (tlyric != null && !tlyric.isEmpty()) {
            data = AllMusic.getMessage().lyric.tdata;
            return data.replace("%Lyric%", lyric)
                    .replace("%TLyric%", tlyric);
        }

        data = AllMusic.getMessage().lyric.data;
        return data.replace("%Lyric%", lyric);
    }
}
