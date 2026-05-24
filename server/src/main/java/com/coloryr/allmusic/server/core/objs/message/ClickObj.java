package com.coloryr.allmusic.server.core.objs.message;

public class ClickObj {
    public String clickRun;
    public String clickCheck;

    public static ClickObj make() {
        ClickObj obj = new ClickObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (clickRun == null)
            return true;
        return clickCheck == null;
    }

    public void init() {
        if (clickRun == null)
            clickRun = "<aqua>[<underlined>点我选择</underlined>]";
        if (clickCheck == null)
            clickCheck = "<aqua>[<underlined>点我查看</underlined>]";
    }
}
