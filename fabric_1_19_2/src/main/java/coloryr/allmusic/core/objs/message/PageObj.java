package coloryr.allmusic.core.objs.message;

public class PageObj {
    public String Choice;
    public String Next;
    public String Last;

    public PageObj() {
        Choice = "§e[§n点我选择§r§e]§2%index%->%MusicName% | %MusicAuthor% | %MusicAl%";
        Next = "§d[AllMusic]§e[§n点我下一页§r§e]";
        Last = "§d[AllMusic]§e[§n点我上一页§r§e]";
    }

    public boolean check() {
        boolean res = Choice == null;
        if (Next == null)
            res = true;
        if (Last == null)
            res = true;

        return res;
    }
}
