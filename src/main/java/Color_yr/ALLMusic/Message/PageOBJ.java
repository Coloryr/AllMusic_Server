package Color_yr.ALLMusic.Message;

public class PageOBJ {
    private String Choice;
    private String Next;
    private String Last;

    public PageOBJ() {
        Choice = "§b[§n点我选择§r§b]§2%index%->%MusicName% | %MusicAuthor% | %MusicAl%";
        Next = "§d[ALLMusic]§e[§n点我下一页§r§b]";
        Last = "§d[ALLMusic]§e[§n点我上一页§r§b]";
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
