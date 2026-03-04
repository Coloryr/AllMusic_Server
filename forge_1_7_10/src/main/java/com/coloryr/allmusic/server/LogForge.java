package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;
import net.minecraft.util.IChatComponent;

public class LogForge implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        IChatComponent textComponent = AllMusicServer.parse(data);
        AllMusicServer.server.addChatMessage(textComponent);
    }
}
