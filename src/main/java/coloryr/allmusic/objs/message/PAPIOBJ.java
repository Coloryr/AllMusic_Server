package coloryr.allmusic.objs.message;

public class PAPIOBJ {
    public String NoMusic;

    public PAPIOBJ() {
        NoMusic = "没有播放的音乐";
    }

    public boolean check() {
        return NoMusic == null;
    }
}
