package coloryr.allmusic.message;

public class LyricOBJ {
    private String Data;
    private String TData;

    public LyricOBJ() {
        Data = "%Lyric%";
        TData = "%Lyric%(%TLyric%)";
    }

    public boolean check(){
        if(Data == null)
            return true;
        if(TData == null)
            return true;

        return false;
    }

    public String getData() {
        return Data;
    }

    public String getTData() {
        return TData;
    }
}
