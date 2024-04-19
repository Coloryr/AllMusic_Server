package com.coloryr.allmusic.server.core.music.play;

import com.coloryr.allmusic.server.core.objs.music.LyricItemObj;

import java.util.*;

public class LyricSave {
    public int lastIndex = 0;
    protected boolean haveLyric;
    protected String lly = "";
    protected String kly = "";
    protected String tly = "";
    private Map<Integer, LyricItemObj> lyric;
    private Map<Integer, String> klyric;
    private LyricItemObj now;
    private int last;
    private String kNow;

    public LyricSave() {
        haveLyric = false;
        lyric = new HashMap<>();
    }

    public void setKlyric(Map<Integer, String> klyric) {
        this.klyric = klyric;
    }

    public boolean isHaveLyric() {
        return haveLyric;
    }

    public void setHaveLyric(boolean haveLyric) {
        this.haveLyric = haveLyric;
    }

    public String getLyric() {
        return lly;
    }

    public void setLyric(Map<Integer, LyricItemObj> lyric) {
        this.lyric = lyric;
    }

    public String getTlyric() {
        return tly;
    }

    public String getKly() {
        return kly;
    }

    public boolean kUpdate() {
        if (lastIndex >= now.lyric.length()) {
            kly = now.lyric;
            lly = "";
        } else if (kNow != null) {
            int index = now.lyric.toLowerCase(Locale.ROOT).indexOf(kNow, lastIndex);
            if (index != -1) {
                lastIndex = index + kNow.length();

                kly = now.lyric.substring(0, lastIndex);
                lly = now.lyric.substring(lastIndex);
                return true;
            }
        } else {
            kly = "";
        }

        return false;
    }

    public boolean ktv(int time) {
        if (last == time || klyric == null)
            return false;

        String temp = klyric.get(time);
        if (temp != null) {
            kNow = temp.toLowerCase(Locale.ROOT);
            last = time;
            return kUpdate();
        }

        return false;
    }

    public boolean checkTime(int playNow, boolean ktv) {
        if (lyric == null)
            return false;

        boolean res = false;
        LyricItemObj temp = lyric.get(playNow);
        if (temp != null) {
            now = temp;
            lly = now.lyric;
            tly = now.tlyric;
            if (tly == null)
                tly = "";
            kly = null;
            lastIndex = 0;
            kUpdate();
            return true;
        }

        if (ktv && now != null) {
            res = ktv(playNow);
        }

        return res;
    }
}
