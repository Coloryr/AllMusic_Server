package Color_yr.AllMusic.MusicAPI.SongLyric;

import java.util.HashMap;
import java.util.Map;

public class LyricSave {
    private Map<Integer, ShowOBJ> Lyric;
    private boolean haveLyric;

    public LyricSave() {
        haveLyric = false;
        Lyric = new HashMap<>();
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
