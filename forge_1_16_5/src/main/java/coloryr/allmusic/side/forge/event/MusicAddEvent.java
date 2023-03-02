package coloryr.allmusic.side.forge.event;

import coloryr.allmusic.core.objs.music.MusicObj;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class MusicAddEvent extends Event {
    /**
     * 添加的音乐
     */
    private final MusicObj music;
    /**
     * 添加者
     */
    private final CommandSource player;

    public MusicAddEvent(MusicObj id, CommandSource player) {
        this.music = id;
        this.player = player;
    }

    public CommandSource getPlayer() {
        return player;
    }

    public MusicObj getMusic() {
        return music;
    }
}
