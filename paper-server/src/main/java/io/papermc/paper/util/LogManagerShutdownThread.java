package io.papermc.paper.util;

import org.apache.logging.log4j.LogManager;

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
        LogManager.shutdown();
    }
}
