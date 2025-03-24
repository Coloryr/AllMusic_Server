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
        if (choice == null)
            choice = "§e" + PAL.index + "->" + PAL.musicName + " | " + PAL.musicAuthor + " | " + PAL.musicAl;
        if (next == null)
            next = "§e[§n点我下一页§r§e]";
        if (last == null)
            last = "§e[§n点我上一页§r§e]";
    }
}
