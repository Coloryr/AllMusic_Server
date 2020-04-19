package Color_yr.AllMusic.Message;

public class LyricOBJ {
    private final String Data;
    private final String TData;

    public LyricOBJ() {
        Data = "%Lyric%";
        TData = "%Lyric%(%TLyric%)";
    }

    public String getData() {
        return Data;
    }

    public String getTData() {
        return TData;
    }
}
