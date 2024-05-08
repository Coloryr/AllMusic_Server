package com.coloryr.allmusic.server.core.objs.message;

public class FunObj {
    public String rain;

    public static FunObj make() {
        FunObj obj = new FunObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        boolean res = rain == null;

        return res;
    }

    public void init() {
        rain = "§d[AllMusic3]§e天空开始变得湿润";
    }
}
