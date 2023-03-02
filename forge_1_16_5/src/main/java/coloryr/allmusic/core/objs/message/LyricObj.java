package coloryr.allmusic.core.objs.message;

public class LyricObj {
    public String Data;
    public String TData;

    public LyricObj() {
        Data = "%Lyric%";
        TData = "%Lyric%(%TLyric%)";
    }

    public boolean check() {
        if (Data == null)
            return true;
        return TData == null;
    }
}
