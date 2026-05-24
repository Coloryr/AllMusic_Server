package com.coloryr.allmusic.server.core.objs.config;

public class CostObj {
    /**
     * 搜歌花费
     */
    public int searchCost;
    /**
     * 点歌花费
     */
    public int addMusicCost;
    /**
     * 开启花钱点歌
     */
    public boolean useCost;

    public static CostObj make() {
        CostObj obj = new CostObj();
        obj.useCost = false;
        obj.searchCost = 20;
        obj.addMusicCost = 10;
        return obj;
    }
}
