package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;

public class LogVelocity implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        AllMusicVelocity.plugin.server.getConsoleCommandSource().sendMessage(data);
    }
}
