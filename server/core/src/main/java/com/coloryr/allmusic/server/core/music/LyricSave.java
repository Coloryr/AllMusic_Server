package com.coloryr.allmusic.server.core.music;

import com.coloryr.allmusic.server.core.objs.music.LyricItemObj;

import java.util.HashMap;
import java.util.Map;

public class LyricSave {
    public int lastIndex = 0;
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
        }

        // 查找当前时间落在哪个 KTV 字的时间段内
        for (int i = 0; i < ktvNow.items.size(); i++) {
            KtvLyric.KtvItem item = ktvNow.items.get(i);
            if (playNow >= item.start && playNow < item.start + item.time) {
                // 当前字内的进度
                float progressInChar = (float) (playNow - item.start) / item.time;
                // 整体进度 = (已完成的字 + 当前字的进度) / 总字数
                kly = (i + progressInChar) / ktvNow.items.size();
                lastIndex = i;  // 记录当前字索引，可选
            }
        }

        // 如果时间超过了最后一个字
        if (playNow >= ktvNow.start + ktvNow.time) {
            kly = 1.0f;
            lastIndex = ktvNow.items.size();
        }

        // 还没到第一个字的时间
        kly = 0.0f;
        lastIndex = 0;
    }

    private void ktvGetNext(long time) {
        if (klyric == null)
            return;

        KtvLyric temp = klyric.get(time);
        if (temp != null) {
            ktvNow = temp;
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
            lastIndex = 0;
            ktvGetNext(playNow);
            return true;
        }

        return false;
    }

    public void updateKtv(long playNow) {
        if (now != null) {
            kUpdate(playNow);
        }
    }
}
