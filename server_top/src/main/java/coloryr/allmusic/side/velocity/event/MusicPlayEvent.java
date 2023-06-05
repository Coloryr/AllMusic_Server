package coloryr.allmusic.side.velocity.event;

import coloryr.allmusic.core.objs.music.SongInfoObj;

/**
 * 音乐播发事件
 */
public class MusicPlayEvent {
    /**
     * 音乐内容
     */
    private final SongInfoObj music;
    /**
     * 是否取消
     */
    private boolean cancel = false;

    public MusicPlayEvent(SongInfoObj music) {
        this.music = music;
    }

    public SongInfoObj getMusic() {
        return music;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
