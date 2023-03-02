package coloryr.allmusic.side.bc;

import coloryr.allmusic.core.side.IMyLogger;

import java.util.logging.Logger;

public class LogBC implements IMyLogger {
    private final Logger Logger;

    public LogBC(Logger Logger) {
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
