package coloryr.allmusic.side.forge;

import coloryr.allmusic.AllMusicForge;
import coloryr.allmusic.core.side.IMyLogger;

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
