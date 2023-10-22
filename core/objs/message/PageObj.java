package coloryr.allmusic.core.objs.message;

public class PageObj {
    public String Choice;
    public String Next;
    public String Last;

    public boolean check() {
        boolean res = Choice == null;
        if (Next == null)
            res = true;
        if (Last == null)
            res = true;

        return res;
    }

    public void init() {
        Choice = "§e%Index%->%MusicName% | %MusicAuthor% | %MusicAl%";
        Next = "§e[§n点我下一页§r§e]";
        Last = "§e[§n点我上一页§r§e]";
    }

    public static PageObj make() {
        PageObj obj = new PageObj();
        obj.init();

        return obj;
    }
}
