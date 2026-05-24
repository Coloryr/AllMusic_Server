package com.coloryr.allmusic.server.core.objs.config;

public class FunConfigObj {
    public boolean rain;
    public int rainRate;

    public static FunConfigObj make() {
        FunConfigObj obj = new FunConfigObj();
        obj.init();

        return obj;
    }

    public void init() {
        rain = true;
        rainRate = 10;
    }
}
