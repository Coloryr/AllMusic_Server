package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IMyLogger;

public class LogNeoForge implements IMyLogger {
    @Override
    public void warning(String data) {
        AllMusicServer.LOGGER.warn(data);
    }

    @Override
    public void info(String data) {
        AllMusicServer.LOGGER.info(data);
    }
}
