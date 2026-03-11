package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class LogFolia implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        Bukkit.getConsoleSender().sendMessage(data);
    }
}
