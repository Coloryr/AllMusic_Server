package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IMyLogger;

public class LogFabric implements IMyLogger {
    @Override
    public void warning(String data) {
        AllMusicFabric.LOGGER.warn(data);
    }

    @Override
    public void info(String data) {
        AllMusicFabric.LOGGER.info(data);
    }
}
