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
        info = "自定义音乐";
        idle = "空闲列表";
    }
}
