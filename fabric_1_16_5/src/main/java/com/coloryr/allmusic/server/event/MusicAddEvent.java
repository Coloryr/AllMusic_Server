package com.coloryr.allmusic.server.event;

import com.coloryr.allmusic.server.core.objs.music.PlayerAddMusicObj;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface MusicAddEvent {
    Event<MusicAddEvent> EVENT = EventFactory.createArrayBacked(MusicAddEvent.class,
            (listeners) -> (player, music) -> {
                for (MusicAddEvent listener : listeners) {
                    ActionResult result = listener.interact(player, music);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(ServerPlayerEntity player, PlayerAddMusicObj music);
}
