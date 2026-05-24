package com.coloryr.allmusic.server.core.objs.message;

public class CancelObj {
    public String done;
    public String err1;
    public String err2;
    public String err3;
    public String err4;

    public static CancelObj make() {
        CancelObj obj = new CancelObj();
        obj.init();

        return obj;
    }

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
        if (err1 == null)
            err1 = "<light_purple>[AllMusic3]<red>你没有点歌";
        if (err2 == null)
            err2 = "<light_purple>[AllMusic3]<red>歌曲" + ARG.musicName + "-" + ARG.musicAuthor + "不是你的点歌";
        if (err3 == null)
            err3 = "<light_purple>[AllMusic3]<red>没有找到序号为" + ARG.index + "的点歌";
        if (err4 == null)
            err4 = "<light_purple>[AllMusic3]<red>输入的序号错误";
        if (done == null)
            done = "<light_purple>[AllMusic3]<yellow>已取消你的点歌";
    }
}
