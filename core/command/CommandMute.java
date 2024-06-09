package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;

public class CommandMute extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.side.sendStop(name);
        AllMusic.side.clearHud(name);
        AllMusic.getConfig().addNoMusicPlayer(name);
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute);
    }
}
