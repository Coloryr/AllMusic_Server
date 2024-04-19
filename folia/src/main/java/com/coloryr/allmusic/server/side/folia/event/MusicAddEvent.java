package com.coloryr.allmusic.server.side.folia.event;

import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 音乐添加事件
 */
public class MusicAddEvent extends Event {
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

    public boolean isCancel() {
        return cancel;
    }

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
}
