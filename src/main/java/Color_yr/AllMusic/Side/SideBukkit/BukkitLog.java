package Color_yr.AllMusic.Side.SideBukkit;

import Color_yr.AllMusic.IMyLogger;

import java.util.logging.Logger;

public class BukkitLog implements IMyLogger {
    private final Logger Logger;

    public BukkitLog(Logger Logger) {
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
