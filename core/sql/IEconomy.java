package com.coloryr.allmusic.server.core.sql;

public interface IEconomy {
    /**
     * 检查有没有足够的钱
     * @param name 玩家名字
     * @param cost 数量
     * @return true为有足够的钱
     */
    boolean check(String name, int cost);

    boolean cost(String name, int cost);
}
