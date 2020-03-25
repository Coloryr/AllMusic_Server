package Color_yr.ALLMusic.MusicAPI.SongLyric;

import Color_yr.ALLMusic.ALLMusic;

public class ShowOBJ {
    private boolean haveT;
    private String lyric;
    private String tlyric;

    public ShowOBJ(boolean haveT, String lyric, String tlyric) {
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
                data = ALLMusic.Message.getLyric().getTData();
                return data.replace("%Lyric%", lyric)
                        .replace("%TLyric%", tlyric);
            }
        }
        data = ALLMusic.Message.getLyric().getData();
        return data.replace("%Lyric%", lyric);
    }
}
