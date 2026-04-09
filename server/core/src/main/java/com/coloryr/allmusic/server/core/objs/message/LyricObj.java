package com.coloryr.allmusic.server.core.objs.message;

public class LyricObj {
    public String data;
    public String tdata;
    public String empty1;
    public String empty2;
    public String empty3;

    public static LyricObj make() {
        LyricObj obj = new LyricObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (data == null)
            return true;
        if (tdata == null)
            return true;
        if (empty1 == null)
            return true;
        if (empty2 == null)
            return true;
        return empty3 == null;
    }

    public void init() {
        if (data == null)
            data = ARG.lyric;
        if (tdata == null)
            tdata = ARG.lyric + "(" + ARG.tlyric + ")";
        if (empty1 == null)
            empty1 = "没有歌词";
        if (empty2 == null)
            empty2 = "没有翻译歌词";
        if (empty3 == null)
            empty3 = "没有KTV歌词";
    }
}
