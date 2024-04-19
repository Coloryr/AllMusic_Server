package com.coloryr.allmusic.server.side.forge;

import coloryr.allmusic.AllMusicForge;
import com.coloryr.allmusic.server.core.side.IMyLogger;

public class LogForge implements IMyLogger {
    @Override
    public void warning(String data) {
        AllMusicForge.LOGGER.warn(data);
    }

    @Override
    public void info(String data) {
        AllMusicForge.LOGGER.info(data);
    }
}
