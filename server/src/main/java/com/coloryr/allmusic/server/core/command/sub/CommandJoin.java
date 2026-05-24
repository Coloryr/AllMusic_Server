package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;

public class CommandJoin extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        AllMusic.joinPlayNow(name);
    }
}
