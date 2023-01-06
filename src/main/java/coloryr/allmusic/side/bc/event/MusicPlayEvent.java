package coloryr.allmusic.side.bc.event;

import coloryr.allmusic.music.api.SongInfo;
import net.md_5.bungee.api.plugin.Event;

/**
 * 音乐播发事件
 */
public class MusicPlayEvent extends Event {
    /**
     * 音乐内容
     */
    private final SongInfo music;
    /**
     * 是否取消
     */
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
