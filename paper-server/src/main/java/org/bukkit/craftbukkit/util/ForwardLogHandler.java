package org.bukkit.craftbukkit.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForwardLogHandler extends ConsoleHandler {
    private final Map<String, Logger> cachedLoggers = new ConcurrentHashMap<>();

    private Logger getLogger(String name) {
        Logger logger = this.cachedLoggers.get(name);
        if (logger == null) {
            logger = LogManager.getLogger(name);
            this.cachedLoggers.put(name, logger);
        }

        return logger;
    }

    @Override
    public void publish(LogRecord record) {
        Logger logger = this.getLogger(String.valueOf(record.getLoggerName())); // See SPIGOT-1230
        Throwable exception = record.getThrown();
        Level level = record.getLevel();
        String message = this.getFormatter().formatMessage(record);

        if (level == Level.SEVERE) {
            logger.error(message, exception);
        } else if (level == Level.WARNING) {
            logger.warn(message, exception);
        } else if (level == Level.INFO) {
            logger.info(message, exception);
        } else if (level == Level.CONFIG) {
            logger.debug(message, exception);
        } else {
            logger.trace(message, exception);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
