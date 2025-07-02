package com.coloryr.allmusic.server.side.folia.event;

import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 音乐播发事件
 */
public class MusicPlayEvent extends Event implements Cancellable {
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

    @Deprecated(since = "3.4.5")
    public boolean isCancel() {
        return cancel;
    }

    @Deprecated(since = "3.4.5")
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
