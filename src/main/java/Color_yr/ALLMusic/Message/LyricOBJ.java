package Color_yr.ALLMusic.Message;

public class LyricOBJ {
    private String Data;
    private String TData;

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
