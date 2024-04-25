package com.coloryr.allmusic.server.side.forge.event;

import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class MusicAddEvent extends Event {
    /**
     * 添加的音乐
     */
    private final MusicObj music;
    /**
     * 添加者
     */
    private final CommandSource player;

    public MusicAddEvent(MusicObj id, CommandSource player) {
        this.music = id;
        this.player = player;
    }

    public CommandSource getPlayer() {
        return player;
    }

    public MusicObj getMusic() {
        return music;
    }
}
