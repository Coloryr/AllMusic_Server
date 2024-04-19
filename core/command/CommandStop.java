package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.utils.HudUtils;

public class CommandStop extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.side.clearHud(name);
        AllMusic.side.sendStop(name);
        HudUtils.clearHud(name);
        AllMusic.removeNowPlayPlayer(name);
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.stopPlaying);
    }
}
