package coloryr.allmusic.side.velocity.event;

import coloryr.allmusic.core.objs.music.MusicObj;
import com.velocitypowered.api.command.CommandSource;

/**
 * 音乐添加事件
 */
public class MusicAddEvent {
    /**
     * 添加的音乐
     */
    private final MusicObj music;
    /**
     * 添加者
     */
    private final CommandSource player;
    /**
     * 是否取消添加
     */
    private boolean cancel = false;

    public MusicAddEvent(MusicObj id, CommandSource player) {
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
