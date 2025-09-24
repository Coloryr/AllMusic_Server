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
        if (head == null)
            head = "§d[AllMusic3]§e队列中有歌曲数：" + ARG.count;
        if (item == null)
            item = "§e" + ARG.index + "->" + ARG.musicName + " | " + ARG.musicAuthor
                    + " | " + ARG.musicAl + " | " + ARG.musicAlia + " | by: " + ARG.player;
        if (get == null)
            get = "§d[AllMusic3]§e歌曲列表" + ARG.name + "获取成功";
    }
}
