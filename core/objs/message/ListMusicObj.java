package coloryr.allmusic.core.objs.message;

public class ListMusicObj {
    public String head;
    public String item;
    public String get;

    public boolean check() {
        boolean res = head == null;
        if (item == null)
            res = true;
        if (get == null)
            res = true;

        return res;
    }

    public void init() {
        head = "§d[AllMusic3]§e队列中有歌曲数：%Count%";
        item = "§e%Index%->%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia%";
        get = "§d[AllMusic3]§e歌曲列表%ListName%获取成功";
    }

    public static ListMusicObj make() {
        ListMusicObj obj = new ListMusicObj();
        obj.init();

        return obj;
    }
}
