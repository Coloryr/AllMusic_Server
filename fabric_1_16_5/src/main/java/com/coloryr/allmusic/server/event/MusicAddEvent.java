package com.coloryr.allmusic.server.event;

import com.coloryr.allmusic.server.core.objs.music.PlayerAddMusicObj;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public interface MusicAddEvent {
    Event<MusicAddEvent> EVENT = EventFactory.createArrayBacked(MusicAddEvent.class,
            (listeners) -> (player, music) -> {
                for (MusicAddEvent listener : listeners) {
                    boolean result = listener.interact(player, music);

                    if (result) {
                        return true;
                    }
                }

                return false;
            });

    boolean interact(ServerPlayer player, PlayerAddMusicObj music);
}
