package coloryr.allmusic.side.bc.event;

import coloryr.allmusic.core.objs.music.MusicObj;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

/**
 * 音乐添加事件
 */
public class MusicAddEvent extends Event {
    /**
     * 添加的音乐
     */
    private final MusicObj music;
    /**
     * 添加者
     */
    private final CommandSender player;
    /**
     * 是否取消添加
     */
    private boolean cancel = false;

    public MusicAddEvent(MusicObj id, CommandSender player) {
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
