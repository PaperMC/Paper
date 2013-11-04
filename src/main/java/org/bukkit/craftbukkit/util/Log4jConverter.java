package org.bukkit.craftbukkit.util;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log4jConverter {

    private static final Map<Level, org.apache.logging.log4j.Level> JULTOLOG4J = new HashMap<Level, org.apache.logging.log4j.Level>();
    private static final Map<org.apache.logging.log4j.Level, Level> LOG4JTOJUL = new EnumMap<org.apache.logging.log4j.Level, Level>(org.apache.logging.log4j.Level.class);
    static {
        JULTOLOG4J.put(Level.ALL, org.apache.logging.log4j.Level.ALL);
        JULTOLOG4J.put(Level.FINEST, org.apache.logging.log4j.Level.TRACE);
        JULTOLOG4J.put(Level.FINER, org.apache.logging.log4j.Level.TRACE);
        JULTOLOG4J.put(Level.FINE, org.apache.logging.log4j.Level.TRACE);
        JULTOLOG4J.put(Level.INFO, org.apache.logging.log4j.Level.INFO);
        JULTOLOG4J.put(Level.CONFIG, org.apache.logging.log4j.Level.INFO);
        JULTOLOG4J.put(Level.WARNING, org.apache.logging.log4j.Level.WARN);
        JULTOLOG4J.put(Level.SEVERE, org.apache.logging.log4j.Level.ERROR);
        JULTOLOG4J.put(Level.OFF, org.apache.logging.log4j.Level.OFF);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.ALL, Level.ALL);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.TRACE, Level.FINE);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.INFO, Level.INFO);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.DEBUG, Level.INFO);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.WARN, Level.WARNING);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.ERROR, Level.SEVERE);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.FATAL, Level.SEVERE);
        LOG4JTOJUL.put(org.apache.logging.log4j.Level.OFF, Level.OFF);
    }

    public static org.apache.logging.log4j.Level getLog4jLevel(Level level) {
        org.apache.logging.log4j.Level log4jLevel = JULTOLOG4J.get(level);
        return log4jLevel == null ? org.apache.logging.log4j.Level.INFO : log4jLevel;
    }

    public static Level getJULLevel(org.apache.logging.log4j.Level level) {
        return LOG4JTOJUL.get(level);
    }

    public static Logger createLogger() {
        final Logger logger = Logger.getLogger("Minecraft");
        // Grab a logger.  It doesn't really matter which, so long as it is within net.minecraft.server.
        final org.apache.logging.log4j.core.Logger log4j = (org.apache.logging.log4j.core.Logger) org.apache.logging.log4j.LogManager.getLogger("net.minecraft.server.DedicatedServer");
        logger.setUseParentHandlers(false);
        // Add a handler to our Bukkit Logger so that messages logged to it are sent to log4j.
        Handler log4jHandler = new Handler() {
            @Override
            public void close() throws SecurityException {
            }

            @Override
            public void flush() {
            }

            @Override
            public void publish(LogRecord record) {
                log4j.log(Log4jConverter.getLog4jLevel(record.getLevel()), record.getMessage());

            }
        };
        logger.addHandler(log4jHandler);
        return logger;
    }
}
