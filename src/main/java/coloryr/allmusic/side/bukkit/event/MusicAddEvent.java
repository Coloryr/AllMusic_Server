package coloryr.allmusic.side.bukkit.event;

import coloryr.allmusic.music.play.MusicObj;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MusicAddEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
