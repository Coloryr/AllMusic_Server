package Color_yr.AllMusic.music.lyric;

import java.util.HashMap;
import java.util.Map;

public class LyricSave {
    private Map<Integer, ShowOBJ> lyric;
    private boolean haveLyric;

    public LyricSave() {
        haveLyric = false;
        lyric = new HashMap<>();
    }

    public void setLyric(Map<Integer, ShowOBJ> lyric) {
        this.lyric = lyric;
    }

    public boolean isHaveLyric() {
        return haveLyric;
    }

    public void setHaveLyric(boolean haveLyric) {
        this.haveLyric = haveLyric;
    }

    public ShowOBJ checkTime(int playNow) {
        if (lyric == null)
            return null;
        if (lyric.containsKey(playNow))
            return lyric.get(playNow);
        return null;
    }
}
