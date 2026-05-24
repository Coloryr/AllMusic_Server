package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.music.PlayMusic;

public class CommandClearList extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        PlayMusic.clearIdleList();
        AllMusic.side.sendMessage(sender, "<light_purple>[AllMusic3]<dark_green>空闲音乐列表已清空");
    }
}
