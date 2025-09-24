package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IMyLogger;

public class LogForge implements IMyLogger {
    @Override
    public void warning(String data) {
        AllMusicNeoForge.LOGGER.warn(data);
    }

    @Override
    public void info(String data) {
        AllMusicNeoForge.LOGGER.info(data);
    }
}
