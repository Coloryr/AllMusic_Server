package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;

public class CommandReload extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.side.reload();
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2已重读配置文件");
    }
}
