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
            clickRun = "§b[§n点我选择§r§b]";
        if (clickCheck == null)
            clickCheck = "§b[§n点我查看§r§b]";
    }
}
