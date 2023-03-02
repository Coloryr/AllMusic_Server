package coloryr.allmusic.core.objs.message;

public class PAPIObj {
    public String NoMusic;

    public PAPIObj() {
        NoMusic = "没有播放的音乐";
    }

    public boolean check() {
        return NoMusic == null;
    }
}
