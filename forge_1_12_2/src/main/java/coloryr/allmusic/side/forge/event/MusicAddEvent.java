package coloryr.allmusic.side.forge.event;

import coloryr.allmusic.core.objs.music.MusicObj;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class MusicAddEvent extends Event {
    /**
     * 添加的音乐
     */
    private final MusicObj music;
    /**
     * 添加者
     */
    private final ICommandSender player;

    public MusicAddEvent(MusicObj id, ICommandSender player) {
        this.music = id;
        this.player = player;
    }

    public ICommandSender getPlayer() {
        return player;
    }

    public MusicObj getMusic() {
        return music;
    }
}
