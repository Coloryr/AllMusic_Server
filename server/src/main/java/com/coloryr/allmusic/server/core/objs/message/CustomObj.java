package com.coloryr.allmusic.server.core.objs.message;

public class CustomObj {
    public String info;
    public String idle;

    public static CustomObj make() {
        CustomObj obj = new CustomObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        boolean res = idle == null;
        if (info == null) {
            res = true;
        }
        return res;
    }

    public void init() {
        if (info == null)
            info = "自定义音乐";
        if (idle == null)
            idle = "空闲列表";
    }
}
