package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.core.side.IMyLogger;

import java.util.logging.Logger;

public class LogBukkit implements IMyLogger {
    private final Logger Logger;

    public LogBukkit(Logger Logger) {
        this.Logger = Logger;
    }

    @Override
    public void warning(String data) {
        Logger.warning(data);
    }

    @Override
    public void info(String data) {
        Logger.info(data);
    }
}
