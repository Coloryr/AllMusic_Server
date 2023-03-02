package coloryr.allmusic.side.forge.event;

import coloryr.allmusic.core.objs.music.SongInfoObj;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

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
