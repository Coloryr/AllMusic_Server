package com.coloryr.allmusic.server.event;

import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface MusicPlayEvent {
    Event<MusicPlayEvent> EVENT = EventFactory.createArrayBacked(MusicPlayEvent.class,
            (listeners) -> (music) -> {
                for (MusicPlayEvent listener : listeners) {
                    if (listener.interact(music)) {
                        return true;
                    }
                }

                return false;
            });

    boolean interact(SongInfoObj music);
}
