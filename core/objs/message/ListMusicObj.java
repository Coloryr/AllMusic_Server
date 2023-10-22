package coloryr.allmusic.core.objs.message;

public class ListMusicObj {
    public String Head;
    public String Item;
    public String Get;

    public boolean check() {
        boolean res = Head == null;
        if (Item == null)
            res = true;
        if (Get == null)
            res = true;

        return res;
    }

    public void init() {
        Head = "§d[AllMusic]§e队列中有歌曲数：%Count%";
        Item = "§e%Index%->%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia%";
        Get = "§d[AllMusic]§e歌曲列表%ListName%获取成功";
    }

    public static ListMusicObj make() {
        ListMusicObj obj = new ListMusicObj();
        obj.init();

        return obj;
    }
}
