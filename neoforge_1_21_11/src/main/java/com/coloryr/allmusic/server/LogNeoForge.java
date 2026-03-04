package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.side.IAllMusicLogger;

public class LogNeoForge implements IAllMusicLogger {
    @Override
    public void warning(String data) {
        AllMusicServer.audiences.console().sendMessage(AllMusic.miniMessage(data));
    }

    @Override
    public void info(String data) {
        AllMusicServer.audiences.console().sendMessage(AllMusic.miniMessage(data));
    }
}
