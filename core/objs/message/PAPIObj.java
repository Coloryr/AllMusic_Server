package coloryr.allmusic.core.objs.message;

public class PAPIObj {
    public String NoMusic;

    public boolean check() {
        return NoMusic == null;
    }

    public void init() {
        NoMusic = "没有播放的音乐";
    }

    public static PAPIObj make() {
        PAPIObj obj = new PAPIObj();
        obj.init();

        return obj;
    }
}
