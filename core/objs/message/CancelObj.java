package com.coloryr.allmusic.server.core.objs.message;

public class CancelObj {
    public String done;
    public String err1;
    public String err2;
    public String err3;
    public String err4;

    public boolean check() {
        if (err1 == null)
            return true;
        if (err2 == null)
            return true;
        if (err3 == null)
            return true;
        if (err4 == null)
            return true;
        return done == null;
    }

    public void init() {
        err1 = "§d[AllMusic3]§c你没有点歌";
        err2 = "§d[AllMusic3]§c歌曲%MusicName%-%MusicAuthor%不是你的点歌";
        err3 = "§d[AllMusic3]§c没有找到序号为%Index%的点歌";
        err4 = "§d[AllMusic3]§c输入的序号错误";
        done = "§d[AllMusic3]§e已取消你的点歌";
    }

    public static CancelObj make() {
        CancelObj obj = new CancelObj();
        obj.init();

        return obj;
    }
}
