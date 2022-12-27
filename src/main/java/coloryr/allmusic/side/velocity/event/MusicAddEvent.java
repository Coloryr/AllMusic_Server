package coloryr.allmusic.side.velocity.event;

import coloryr.allmusic.music.play.MusicObj;
import com.velocitypowered.api.command.CommandSource;

public class MusicAddEvent {
    private final MusicObj music;
    private final CommandSource player;
    private boolean cancel = false;

    public MusicAddEvent(MusicObj id, CommandSource player){
        this.music = id;
        this.player = player;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public CommandSource getPlayer() {
        return player;
    }

    public MusicObj getMusic() {
        return music;
    }
}
