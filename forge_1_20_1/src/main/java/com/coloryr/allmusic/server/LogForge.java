package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.MutableComponent;

public class LogForge implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        MutableComponent component = AllMusicServer.parse(data);
        AllMusicServer.server.sendSystemMessage(component);
    }
}
