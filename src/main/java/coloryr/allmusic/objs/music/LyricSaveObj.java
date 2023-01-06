package coloryr.allmusic.objs.music;

import coloryr.allmusic.objs.music.LyricItemObj;

import java.util.HashMap;
import java.util.Map;

public class LyricSaveObj {
    private Map<Integer, LyricItemObj> lyric;
    private boolean haveLyric;

    public LyricSaveObj() {
        haveLyric = false;
        lyric = new HashMap<>();
    }

    public void setLyric(Map<Integer, LyricItemObj> lyric) {
        this.lyric = lyric;
    }

    public boolean isHaveLyric() {
        return haveLyric;
    }

    public void setHaveLyric(boolean haveLyric) {
        this.haveLyric = haveLyric;
    }

    public LyricItemObj checkTime(int playNow) {
        if (lyric == null)
            return null;
        if (lyric.containsKey(playNow))
            return lyric.get(playNow);
        return null;
    }
}
