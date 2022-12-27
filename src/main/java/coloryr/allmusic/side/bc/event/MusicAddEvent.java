package coloryr.allmusic.side.bc.event;

import coloryr.allmusic.music.play.MusicObj;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

public class MusicAddEvent extends Event {
    private final MusicObj music;
    private final CommandSender player;
    private boolean cancel = false;

    public MusicAddEvent(MusicObj id, CommandSender player){
        this.music = id;
        this.player = player;
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

    public MusicObj getMusic() {
        return music;
    }
}
