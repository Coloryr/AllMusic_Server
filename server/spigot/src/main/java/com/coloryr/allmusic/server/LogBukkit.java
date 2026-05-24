package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;

public class LogBukkit implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        AllMusicBukkit.adventure.console().sendMessage(data);
    }
}
