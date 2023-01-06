package coloryr.allmusic.objs.message;

public class LyricOBJ {
    public String Data;
    public String TData;

    public LyricOBJ() {
        Data = "%Lyric%";
        TData = "%Lyric%(%TLyric%)";
    }

    public boolean check() {
        if (Data == null)
            return true;
        if (TData == null)
            return true;

        return false;
    }
}
