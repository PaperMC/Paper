package io.papermc.paper.log;

public final class LogManagerShutdownThread extends Thread {

    static LogManagerShutdownThread INSTANCE = new LogManagerShutdownThread();

    public static void hook() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot re-hook after being unhooked");
        }
        Runtime.getRuntime().addShutdownHook(INSTANCE);
    }

    public static void unhook() {
        Runtime.getRuntime().removeShutdownHook(INSTANCE);
        INSTANCE = null;
    }

    private LogManagerShutdownThread() {
        super("Log4j2 Shutdown Thread");
    }

    @Override
    public void run() {
        io.papermc.paper.log.CustomLogManager.forceReset(); // Paper - Reset loggers after shutdown
        LoggerShutdown.shutdownLogging(); // Flushes the async appender
        try {
            net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Paper - Use TerminalConsoleAppender
        } catch (final Exception ignored) {
        }
    }
}
