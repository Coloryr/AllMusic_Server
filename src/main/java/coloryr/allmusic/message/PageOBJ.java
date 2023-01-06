package coloryr.allmusic.message;

public class PageOBJ {
    public String Choice;
    public String Next;
    public String Last;

    public PageOBJ() {
        Choice = "§e[§n点我选择§r§e]§2%index%->%MusicName% | %MusicAuthor% | %MusicAl%";
        Next = "§d[AllMusic]§e[§n点我下一页§r§e]";
        Last = "§d[AllMusic]§e[§n点我上一页§r§e]";
    }

    public boolean check() {
        boolean res = false;
        if (Choice == null)
            res = true;
        if (Next == null)
            res = true;
        if (Last == null)
            res = true;

        return res;
    }
}
