package io.papermc.paper.log;

import org.apache.logging.log4j.LogManager;
import org.jline.utils.ShutdownHooks;

public final class LoggerShutdown {
    private LoggerShutdown() {
    }

    public static void shutdownLogging() {
        io.papermc.paper.log.CustomLogManager.forceReset(); // Reset loggers after shutdown
        LogManager.shutdown(); // Flushes the async appender

        // Call JLine's shutdown hooks now that all pending logs have been flushed
        ShutdownHooks.runTasks();

        try {
            // This may end up calling AbstractTerminal#close twice which doesn't have any guards for that, consider patching TCA if it becomes an issue
            net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Use TerminalConsoleAppender
        } catch (final Exception ignored) {
        }
    }
}
