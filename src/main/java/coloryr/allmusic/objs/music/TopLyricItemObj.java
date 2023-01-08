package coloryr.allmusic.objs.music;

import coloryr.allmusic.objs.music.LyricItemObj;

public class TopLyricItemObj extends LyricItemObj {
    public TopLyricItemObj() {
        super(0, false, null, null, null, false);
    }

    public void setTlyric(String data) {
        tlyric = data;
    }

    public void setLyric(String data) {
        lyric = data;
    }

    public void setKly(String data) {
        kly = data;
    }

    public void setHaveK(boolean data) {
        haveK = data;
        if (!haveK) {
            kly = null;
        }
    }

    public void setHaveT(boolean data) {
        haveT = data;
        if (!haveT) {
            tlyric = null;
        }
    }
}
