package coloryr.allmusic.side.fabric.event;

import coloryr.allmusic.core.objs.music.MusicObj;
import coloryr.allmusic.core.objs.music.SongInfoObj;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface MusicPlayEvent {
    Event<MusicPlayEvent> EVENT = EventFactory.createArrayBacked(MusicPlayEvent.class,
            (listeners) -> (music) -> {
                for (MusicPlayEvent listener : listeners) {
                    ActionResult result = listener.interact(music);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(SongInfoObj music);
}
