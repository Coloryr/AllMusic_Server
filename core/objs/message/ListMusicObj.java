package com.coloryr.allmusic.server.core.objs.message;

public class ListMusicObj {
    public String head;
    public String item;
    public String get;

    public static ListMusicObj make() {
        ListMusicObj obj = new ListMusicObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        boolean res = head == null;
        if (item == null)
            res = true;
        if (get == null)
            res = true;

        return res;
    }

    public void init() {
        head = "§d[AllMusic3]§e队列中有歌曲数：" + PAL.count;
        item = "§e" + PAL.index + "->" + PAL.musicName + " | " + PAL.musicAuthor
                + " | " + PAL.musicAl + " | " + PAL.musicAlia + " | by: " + PAL.player;
        get = "§d[AllMusic3]§e歌曲列表" + PAL.name + "获取成功";
    }
}
