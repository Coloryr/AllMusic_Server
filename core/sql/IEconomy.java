package com.coloryr.allmusic.server.core.sql;

public interface IEconomy {
    boolean check(String name, int cost);

    boolean cost(String name, int cost);
}
