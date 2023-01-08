package coloryr.allmusic.objs.music;

import coloryr.allmusic.AllMusic;

import java.util.Map;

public class LyricItemObj {
    protected boolean haveT;
    protected boolean haveK;
    protected String lyric;
    protected String tlyric;
    protected Map<Integer, Character> klyric;
    protected int start;
    protected int last;
    protected String kly;
    protected String lly;
    protected int lastIndex = 0;

    public LyricItemObj(int start, boolean haveT, String lyric, String tlyric,
                        Map<Integer, Character> klyric, boolean haveK) {
        this.start = start;
        this.haveT = haveT;
        this.lyric = lyric;
        this.tlyric = tlyric;
        this.klyric = klyric;
        this.haveK = haveK;
        this.lly = lyric;
        if (haveK) {
            kly = "";
        }
    }

    public boolean ktv(int time) {
        if (last == time || klyric == null)
            return false;

        Character temp = klyric.get(time - start);
        if (temp != null) {
            if (lastIndex >= lyric.length()) {
                kly = lyric;
                lly = "";
            } else {
                int index = lyric.indexOf(temp, lastIndex);
                if (index == lyric.length() - 1) {
                    kly = lyric;
                    lly = "";
                    lastIndex = lyric.length();
                } else if (index != -1) {
                    lastIndex = index + 1;

                    kly = lyric.substring(0, index + 1);
                    lly = lyric.substring(index + 1);
                }
            }
            last = time;
            return true;
        }

        return false;
    }

    public String getKly() {
        return kly;
    }

    public boolean isHaveK() {
        return haveK;
    }

    public String getTlyric() {
        return tlyric;
    }

    public String getLyric() {
        return lly;
    }

    public String getString() {
        if (lyric == null || lyric.isEmpty())
            return "";
        String data;
        if (haveT) {
            if (tlyric != null && !tlyric.isEmpty()) {
                data = AllMusic.getMessage().Lyric.TData;
                return data.replace("%Lyric%", lyric)
                        .replace("%TLyric%", tlyric);
            }
        }
        data = AllMusic.getMessage().Lyric.Data;
        return data.replace("%Lyric%", lyric);
    }
}
