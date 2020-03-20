package Color_yr.ALLMusic.MusicAPI.SongLyric;

import java.util.Map;

public class LyricDo {
    private Map<Integer, ShowOBJ> Lyric;
    private boolean haveLyric = false;

    public LyricDo() {
    }

    public void setLyric(Map<Integer, ShowOBJ> lyric) {
        Lyric = lyric;
    }

    public boolean isHaveLyric() {
        return haveLyric;
    }

    public void setHaveLyric(boolean haveLyric) {
        this.haveLyric = haveLyric;
    }

    public ShowOBJ checkTime(int playNow) {
        if (Lyric == null)
            return null;
        if (Lyric.containsKey(playNow))
            return Lyric.get(playNow);
        return null;
    }
}
