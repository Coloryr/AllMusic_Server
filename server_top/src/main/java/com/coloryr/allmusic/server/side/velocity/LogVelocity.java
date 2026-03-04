package com.coloryr.allmusic.server.side.velocity;

import com.coloryr.allmusic.server.AllMusicVelocity;
import com.coloryr.allmusic.server.core.side.IAllMusicLogger;
import net.kyori.adventure.text.Component;

public class LogVelocity implements IAllMusicLogger {
    @Override
    public void data(Component data) {
        AllMusicVelocity.plugin.server.sendMessage(data);
    }
}
