package coloryr.allmusic.message;

public class PAPIOBJ {
    public String NoMusic;

    public PAPIOBJ() {
        NoMusic = "没有播放的音乐";
    }

    public boolean check() {
        if (NoMusic == null)
            return true;

        return false;
    }
}
