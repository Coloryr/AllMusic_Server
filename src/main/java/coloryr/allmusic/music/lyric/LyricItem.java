package coloryr.allmusic.music.lyric;

import coloryr.allmusic.AllMusic;

public class LyricItem {
    protected boolean haveT;
    protected String lyric;
    protected String tlyric;

    public LyricItem(boolean haveT, String lyric, String tlyric) {
        this.haveT = haveT;
        this.lyric = lyric;
        this.tlyric = tlyric;
    }

    public boolean isHaveT() {
        return haveT;
    }

    public String getTlyric() {
        return tlyric;
    }

    public String getLyric() {
        return lyric;
    }

    public String getString() {
        if (lyric == null || lyric.isEmpty())
            return "";
        String data;
        if (haveT) {
            if (tlyric != null && !tlyric.isEmpty()) {
                data = AllMusic.getMessage().getLyric().getTData();
                return data.replace("%Lyric%", lyric)
                        .replace("%TLyric%", tlyric);
            }
        }
        data = AllMusic.getMessage().getLyric().getData();
        return data.replace("%Lyric%", lyric);
    }
}
