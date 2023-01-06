package coloryr.allmusic.side.bukkit.event;

import coloryr.allmusic.objs.music.SongInfoObj;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 音乐播发事件
 */
public class MusicPlayEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
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

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
