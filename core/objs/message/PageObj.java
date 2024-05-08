package com.coloryr.allmusic.server.core.objs.message;

public class PageObj {
    public String choice;
    public String next;
    public String last;

    public static PageObj make() {
        PageObj obj = new PageObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        boolean res = choice == null;
        if (next == null)
            res = true;
        if (last == null)
            res = true;

        return res;
    }

    public void init() {
        choice = "§e%Index%->%MusicName% | %MusicAuthor% | %MusicAl%";
        next = "§e[§n点我下一页§r§e]";
        last = "§e[§n点我上一页§r§e]";
    }
}
