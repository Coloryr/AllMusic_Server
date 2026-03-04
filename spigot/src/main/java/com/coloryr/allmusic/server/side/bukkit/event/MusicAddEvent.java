package com.coloryr.allmusic.server.side.bukkit.event;

import com.coloryr.allmusic.server.core.objs.music.PlayerAddMusicObj;
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
    private final PlayerAddMusicObj music;
    /**
     * 添加者
     */
    private final CommandSender player;
    /**
     * 是否取消添加
     */
    private boolean cancel = false;

    public MusicAddEvent(PlayerAddMusicObj id, CommandSender player) {
        this.music = id;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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

    public PlayerAddMusicObj getMusic() {
        return music;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
