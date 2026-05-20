package com.coloryr.allmusic.server.core.music;

import com.coloryr.allmusic.server.core.objs.music.LyricItemObj;

import java.util.HashMap;
import java.util.Map;

public class LyricSave {
    protected boolean haveLyric;
    protected float kly = 0;
    protected LyricItemObj now;
    private Map<Long, LyricItemObj> lyric;
    private Map<Long, KtvLyric> klyric;
    private KtvLyric ktvNow;

    public LyricSave() {
        haveLyric = false;
        lyric = new HashMap<>();
    }

    public void setKlyric(Map<Long, KtvLyric> klyric) {
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

    public float getKly() {
        return kly;
    }

    public void kUpdate(long playNow) {
        if (now == null || now.lyric == null || now.lyric.isEmpty()
                || ktvNow == null || ktvNow.items == null || ktvNow.items.isEmpty()) {
            kly = 0.0f;
            return;
        }

        if (playNow < ktvNow.start) {
            kly = 0.0f;
            return;
        }

        if (playNow >= ktvNow.start + ktvNow.time) {
            kly = 1.0f;
            return;
        }

        for (int i = 0; i < ktvNow.items.size(); i++) {
            KtvLyric.KtvItem item = ktvNow.items.get(i);
            if (playNow >= item.start && playNow < item.start + item.time) {
                float progressInChar = (float) (playNow - item.start) / item.time;
                kly = (i + progressInChar) / ktvNow.items.size();
                return;
            }
        }

        kly = 0.0f;
    }

    private void ktvGetNext(long time) {
        if (klyric == null)
            return;

        KtvLyric temp = klyric.get(time);
        if (temp != null) {
            ktvNow = temp;
            kly = 0.0f;
        }

        kUpdate(time);
    }

    public boolean lyricGetNext(long playNow) {
        if (lyric == null)
            return false;

        LyricItemObj temp = lyric.get(playNow);
        if (temp != null && now != temp) {
            now = temp.copy();
            if (now.tlyric == null) {
                now.tlyric = "";
            }
            kly = 0.0f;
            return true;
        }

        return false;
    }

    public void updateKtv(long playNow) {
        ktvGetNext(playNow);
    }
}
