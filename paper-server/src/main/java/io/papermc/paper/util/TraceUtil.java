package io.papermc.paper.util;

import org.bukkit.Bukkit;

public final class TraceUtil {

    public static void dumpTraceForThread(Thread thread, String reason) {
        Bukkit.getLogger().warning(thread.getName() + ": " + reason);
        StackTraceElement[] trace = thread.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            Bukkit.getLogger().warning("\tat " + traceElement);
        }
    }

    public static void dumpTraceForThread(String reason) {
        final Throwable thr = new Throwable(reason);
        thr.printStackTrace();
    }

    public static void printStackTrace(Throwable thr) {
        thr.printStackTrace();
    }
}
