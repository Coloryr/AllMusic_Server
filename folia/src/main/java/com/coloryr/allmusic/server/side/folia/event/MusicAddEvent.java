package com.coloryr.allmusic.server.side.folia.event;

import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 音乐添加事件
 */
public class MusicAddEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    /**
     * 添加的音乐
     */
    private final MusicObj music;
    /**
     * 添加者
     */
    private final CommandSender player;
    /**
     * 是否取消添加
     */
    private boolean cancel = false;

    public MusicAddEvent(MusicObj id, CommandSender player) {
        this.music = id;
        this.player = player;
    }

    /**
     * 事件是否被取消
     * @return 是否被取消
     * @deprecated 请使用 {@link #isCancelled()}
     */
    @Deprecated(since = "3.3.1")
    public boolean isCancel() {
        return cancel;
    }

    /**
     * 设置事件是否被取消
     * @param cancel 是否取消事件
     * @deprecated 请使用 {@link #setCancelled(boolean)}
     */
    @Deprecated(since = "3.3.1")
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public CommandSender getPlayer() {
        return player;
    }

    public MusicObj getMusic() {
        return music;
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
