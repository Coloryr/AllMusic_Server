package com.coloryr.allmusic.server.core.objs.config;

public class EconomyObj {
//    public String mysqlUrl;
    public String backend;
    public boolean vault;

    public static EconomyObj make() {
        EconomyObj obj = new EconomyObj();
        obj.init();

        return obj;
    }

    public boolean check() {
//        boolean res = mysqlUrl == null;

        return false;
    }

    public void init() {
        backend = "Server1";
//        mysqlUrl = "jdbc:mysql://localhost:3306/minecraft?autoReconnect=true&autoReconnectForPools=true";
        vault = true;
    }
}
