package io.papermc.paper.log;

import org.apache.logging.log4j.LogManager;

public final class LoggerShutdown {
    private LoggerShutdown() {
    }

    public static void shutdownLogging() {
        io.papermc.paper.log.CustomLogManager.forceReset(); // Reset loggers after shutdown
        LogManager.shutdown(); // Flushes the async appender
        try {
            net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Use TerminalConsoleAppender
        } catch (final Exception ignored) {
        }
    }
}
