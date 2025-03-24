package com.coloryr.allmusic.server.core.objs.message;

public class CommandObj {
    public String error;
    public String noPer;

    public static CommandObj make() {
        CommandObj obj = new CommandObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (error == null)
            return true;
        return noPer == null;
    }

    public void init() {
        if (error == null)
            error = "§d[AllMusic3]§c参数错误，请输入/music help获取帮助";
        if (noPer == null)
            noPer = "§d[AllMusic3]§c你没有权限执行这个操作";
    }
}
