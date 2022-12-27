package coloryr.allmusic.side.bukkit.event;

import coloryr.allmusic.music.api.SongInfo;
import coloryr.allmusic.music.play.MusicObj;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MusicPlayEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final SongInfo music;
    private boolean cancel = false;

    public MusicPlayEvent(SongInfo music) {
        this.music = music;
    }

    public SongInfo getMusic() {
        return music;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
