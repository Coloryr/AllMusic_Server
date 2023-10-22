package coloryr.allmusic.core.objs.message;

public class LyricObj {
    public String Data;
    public String TData;
    public String Empty1;
    public String Empty2;
    public String Empty3;

    public boolean check() {
        if (Data == null)
            return true;
        if (TData == null)
            return true;
        if (Empty1 == null)
            return true;
        if (Empty2 == null)
            return true;
        return Empty3 == null;
    }

    public void init() {
        Data = "%Lyric%";
        TData = "%Lyric%(%TLyric%)";
        Empty1 = "没有歌词";
        Empty2 = "没有翻译歌词";
        Empty3 = "没有KTV歌词";
    }

    public static LyricObj make() {
        LyricObj obj = new LyricObj();
        obj.init();

        return obj;
    }
}
