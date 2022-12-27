package coloryr.allmusic.side.velocity;

import coloryr.allmusic.side.IMyLogger;
import org.slf4j.Logger;

public class LogVelocity implements IMyLogger {
    private final Logger logger;

    public LogVelocity(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warning(String data) {
        logger.warn(data);
    }

    @Override
    public void info(String data) {
        logger.info(data);
    }
}
