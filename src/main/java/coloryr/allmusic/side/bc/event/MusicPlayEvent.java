package coloryr.allmusic.side.bc.event;

import coloryr.allmusic.music.api.SongInfo;
import net.md_5.bungee.api.plugin.Event;

public class MusicPlayEvent extends Event {
    private final SongInfo music;
    private boolean cancel = false;

    public MusicPlayEvent(SongInfo music) {
        this.music = music;
    }

    public SongInfo getMusic() {
        return music;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }
}
