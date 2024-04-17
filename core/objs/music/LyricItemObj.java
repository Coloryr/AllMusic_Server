package coloryr.allmusic.core.objs.music;

import coloryr.allmusic.core.AllMusic;

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
