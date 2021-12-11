package coloryr.allmusic.side.bc;

import coloryr.allmusic.api.IMyLogger;

import java.util.logging.Logger;

public class BCLog implements IMyLogger {
    private final Logger Logger;

    public BCLog(Logger Logger) {
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
