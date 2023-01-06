package coloryr.allmusic.objs.music;

import coloryr.allmusic.objs.music.LyricItemObj;

public class TopLyricItemObj extends LyricItemObj {
    public TopLyricItemObj() {
        super(false, null, null);
    }

    public void setTlyric(String data) {
        tlyric = data;
    }

    public void setLyric(String data) {
        lyric = data;
    }

    public void setHaveT(boolean data) {
        haveT = data;
    }
}
