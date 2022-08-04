package coloryr.allmusic.music.lyric;

public class TopLyricItem extends LyricItem {
    public TopLyricItem() {
        super(false, null, null);
    }

    public void setTlyric(String data){
        tlyric = data;
    }

    public void setLyric(String data){
        lyric = data;
    }

    public void setHaveT(boolean data){
        haveT = data;
    }
}
