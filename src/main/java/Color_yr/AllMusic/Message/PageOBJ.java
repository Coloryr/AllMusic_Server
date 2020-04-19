package Color_yr.AllMusic.Message;

public class PageOBJ {
    private final String Choice;
    private final String Next;
    private final String Last;

    public PageOBJ() {
        Choice = "§e[§n点我选择§r§e]§2%index%->%MusicName% | %MusicAuthor% | %MusicAl%";
        Next = "§d[AllMusic]§e[§n点我下一页§r§e]";
        Last = "§d[AllMusic]§e[§n点我上一页§r§e]";
    }

    public String getLast() {
        return Last;
    }

    public String getNext() {
        return Next;
    }

    public String getChoice() {
        return Choice;
    }
}
