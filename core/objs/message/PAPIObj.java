package com.coloryr.allmusic.server.core.objs.message;

public class PAPIObj {
    public String emptyMusic;

    public boolean check() {
        return emptyMusic == null;
    }

    public void init() {
        emptyMusic = "没有播放的音乐";
    }

    public static PAPIObj make() {
        PAPIObj obj = new PAPIObj();
        obj.init();

        return obj;
    }
}
