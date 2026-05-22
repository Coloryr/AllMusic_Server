package com.coloryr.allmusic.server.core.music;

import com.coloryr.allmusic.server.core.objs.music.LyricItemObj;
import com.coloryr.allmusic.codec.KtvLyricObj;

import java.util.HashMap;
import java.util.Map;

public class LyricSave {
    protected boolean haveLyric;
    protected LyricItemObj now;
    private Map<Long, LyricItemObj> lyric;
    private Map<Long, KtvLyricObj> klyric;
    private KtvLyricObj ktvNow;

    public LyricSave() {
        haveLyric = false;
        lyric = new HashMap<>();
    }

    public KtvLyricObj getKtvNow() {
        return ktvNow;
    }

    public LyricItemObj getNow() {
        return now;
    }

    public void setKlyric(Map<Long, KtvLyricObj> klyric) {
        this.klyric = klyric;
    }

    public boolean isHaveLyric() {
        return haveLyric;
    }

    public void setHaveLyric(boolean haveLyric) {
        this.haveLyric = haveLyric;
    }

    public String getLyric() {
        return now.lyric;
    }

    public void setLyric(Map<Long, LyricItemObj> lyric) {
        this.lyric = lyric;
    }

    public String getTlyric() {
        return now.tlyric;
    }

    public boolean ktvGetNext(long time) {
        if (klyric == null)
            return false;

        KtvLyricObj temp = klyric.get(time);
        if (temp != null) {
            ktvNow = temp;
            return true;
        }

        return false;
    }

    public boolean lyricGetNext(long playNow) {
        if (lyric == null)
            return false;

        LyricItemObj temp = lyric.get(playNow);
        if (temp != null && now != temp) {
            if (ktvNow != null) {
                long time = Math.abs(ktvNow.start - temp.start);
                KtvLyricObj.KtvItem start = ktvNow.items.get(0);
                if (time > 500 && (start != null && !temp.lyric.startsWith(start.key))) {
                    ktvNow = null;
                }
            }

            now = temp.copy();
            if (now.tlyric == null) {
                now.tlyric = "";
            }
            return true;
        }

        return false;
    }
}
