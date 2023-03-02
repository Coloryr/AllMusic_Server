package coloryr.allmusic.core.music.play;

public class TopLyricSave extends LyricSave {

    public void setTlyric(String data) {
        tly = data;
    }

    public void setLyric(String data) {
        lly = data;
    }

    public void setKly(String data) {
        kly = data;
    }

    public void setHaveK(boolean data) {
        if (!data) {
            kly = null;
        }
    }

    public void setHaveT(boolean data) {
        if (!data) {
            tly = null;
        }
    }
}
