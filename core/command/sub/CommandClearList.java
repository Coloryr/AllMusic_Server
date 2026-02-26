package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.music.PlayMusic;

public class CommandClearList extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        PlayMusic.clearIdleList();
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2空闲音乐列表已清空");
    }
}
