package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class LogForge implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        net.minecraft.network.chat.Component textComponent = AllMusicServer.parse(data);
        AllMusicServer.server.sendMessage(textComponent, UUID.randomUUID());
    }
}
