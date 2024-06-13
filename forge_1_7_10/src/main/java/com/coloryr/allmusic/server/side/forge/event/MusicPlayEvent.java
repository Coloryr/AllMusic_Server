package com.coloryr.allmusic.server.side.forge.event;

import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@Cancelable
public class MusicPlayEvent extends Event {
    private final SongInfoObj music;

    public MusicPlayEvent(SongInfoObj music) {
        this.music = music;
    }

    public SongInfoObj getMusic() {
        return music;
    }
}
