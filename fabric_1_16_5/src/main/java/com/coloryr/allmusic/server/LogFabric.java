package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.side.IAllMusicLogger;

public class LogFabric implements IAllMusicLogger {
    @Override
    public void warning(String data) {
        AllMusicServer.LOGGER.warn(data);
    }

    @Override
    public void info(String data) {
        AllMusicServer.LOGGER.info(data);
    }
}
