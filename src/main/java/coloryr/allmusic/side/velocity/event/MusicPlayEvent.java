package coloryr.allmusic.side.velocity.event;

import coloryr.allmusic.music.api.SongInfo;

public class MusicPlayEvent {
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
