package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.AllMusicBukkit;
import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;

public class LogBukkit implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        AllMusicBukkit.adventure.console().sendMessage(data);
    }
}
