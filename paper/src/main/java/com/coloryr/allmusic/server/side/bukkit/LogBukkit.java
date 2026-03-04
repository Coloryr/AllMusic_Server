package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class LogBukkit implements IAllMusicLogger {
    @Override
    public void warning(String data) {
        Component data1 = AllMusic.miniMessage(data);
        Bukkit.getConsoleSender().sendMessage(data1);
    }

    @Override
    public void info(String data) {
        Component data1 = AllMusic.miniMessage(data);
        Bukkit.getConsoleSender().sendMessage(data1);
    }
}
