package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.music.play.PlayMusic;

public class CommandClearList extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        PlayMusic.clearIdleList();
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2添加空闲音乐列表已清空");
    }
}
