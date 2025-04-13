package org.spigotmc;

import io.papermc.paper.FeatureHooks;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

public class WatchdogThread extends ca.spottedleaf.moonrise.common.util.TickThread { // Paper - rewrite chunk system

    public static final boolean DISABLE_WATCHDOG = Boolean.getBoolean("disable.watchdog"); // Paper - Improved watchdog support
    private static WatchdogThread instance;
    private long timeoutTime;
    private boolean restart;
    private final long earlyWarningEvery; // Paper - Timeout time for just printing a dump but not restarting
    private final long earlyWarningDelay;
    public static volatile boolean hasStarted;
    private long lastEarlyWarning; // Paper - Keep track of short dump times to avoid spamming console with short dumps
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart) {
        super("Paper Watchdog Thread");
        this.timeoutTime = timeoutTime;
        this.restart = restart;
        this.earlyWarningEvery = Math.min(GlobalConfiguration.get().watchdog.earlyWarningEvery, timeoutTime);
        this.earlyWarningDelay = Math.min(GlobalConfiguration.get().watchdog.earlyWarningDelay, timeoutTime);
    }

    private static long monotonicMillis() {
        return System.nanoTime() / 1000000L;
    }

    public static void doStart(int timeoutTime, boolean restart) {
        if (WatchdogThread.instance == null) {
            if (timeoutTime <= 0) timeoutTime = 300;
            WatchdogThread.instance = new WatchdogThread(timeoutTime * 1000L, restart);
            WatchdogThread.instance.start();
        } else {
            WatchdogThread.instance.timeoutTime = timeoutTime * 1000L;
            WatchdogThread.instance.restart = restart;
        }
    }

    public static void tick() {
        WatchdogThread.instance.lastTick = WatchdogThread.monotonicMillis();
    }

    public static void doStop() {
        if (WatchdogThread.instance != null) {
            WatchdogThread.instance.stopping = true;
        }
    }

    @Override
    public void run() {
        while (!this.stopping) {
            Logger logger = Bukkit.getServer().getLogger();
            long currentTime = WatchdogThread.monotonicMillis();
            MinecraftServer server = MinecraftServer.getServer();
            if (this.lastTick != 0 && this.timeoutTime > 0 && WatchdogThread.hasStarted && (!server.isRunning() || (currentTime > this.lastTick + this.earlyWarningEvery && !DISABLE_WATCHDOG))) { // Paper - add property to disable
                boolean isLongTimeout = currentTime > this.lastTick + this.timeoutTime || (!server.isRunning() && !server.hasStopped() && currentTime > this.lastTick + 1000);
                // Don't spam early warning dumps
                if (!isLongTimeout && (this.earlyWarningEvery <= 0 ||
                    !hasStarted || currentTime < this.lastEarlyWarning + this.earlyWarningEvery ||
                    currentTime < this.lastTick + this.earlyWarningDelay))
                    continue;
                if (!isLongTimeout && server.hasStopped())
                    continue; // Don't spam early watchdog warnings during shutdown, we'll come back to this...
                this.lastEarlyWarning = currentTime;
                if (isLongTimeout) {
                    logger.log(Level.SEVERE, "------------------------------");
                    logger.log(Level.SEVERE, "The server has stopped responding! This is (probably) not a Paper bug."); // Paper
                    logger.log(Level.SEVERE, "If you see a plugin in the Server thread dump below, then please report it to that author");
                    logger.log(Level.SEVERE, "\t *Especially* if it looks like HTTP or MySQL operations are occurring");
                    logger.log(Level.SEVERE, "If you see a world save or edit, then it means you did far more than your server can handle at once");
                    logger.log(Level.SEVERE, "\t If this is the case, consider increasing timeout-time in spigot.yml but note that this will replace the crash with LARGE lag spikes");
                    logger.log(Level.SEVERE, "If you are unsure or still think this is a Paper bug, please report this to https://github.com/PaperMC/Paper/issues");
                    logger.log(Level.SEVERE, "Be sure to include ALL relevant console errors and Minecraft crash reports");
                    logger.log(Level.SEVERE, "Paper version: " + Bukkit.getServer().getVersion());

                    if (net.minecraft.world.level.Level.lastPhysicsProblem != null) {
                        logger.log(Level.SEVERE, "------------------------------");
                        logger.log(Level.SEVERE, "During the run of the server, a physics stackoverflow was supressed");
                        logger.log(Level.SEVERE, "near " + net.minecraft.world.level.Level.lastPhysicsProblem);
                    }

                    // Paper start - Warn in watchdog if an excessive velocity was ever set
                    if (CraftServer.excessiveVelEx != null) {
                        logger.log(Level.SEVERE, "------------------------------");
                        logger.log(Level.SEVERE, "During the run of the server, a plugin set an excessive velocity on an entity");
                        logger.log(Level.SEVERE, "This may be the cause of the issue, or it may be entirely unrelated");
                        logger.log(Level.SEVERE, CraftServer.excessiveVelEx.getMessage());
                        for (StackTraceElement stack : CraftServer.excessiveVelEx.getStackTrace()) {
                            logger.log(Level.SEVERE, "\t\t" + stack);
                        }
                    }
                    // Paper end
                } else {
                    logger.log(Level.SEVERE, "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH  - " + Bukkit.getServer().getVersion() + " ---");
                    logger.log(Level.SEVERE, "The server has not responded for " + (currentTime - lastTick) / 1000 + " seconds! Creating thread dump");
                }
                // Paper end - Different message for short timeout
                logger.log(Level.SEVERE, "------------------------------");
                logger.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to Paper!):"); // Paper
                FeatureHooks.dumpAllChunkLoadInfo(MinecraftServer.getServer(), isLongTimeout); // Paper - log detailed tick information
                WatchdogThread.dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServer().serverThread.getId(), Integer.MAX_VALUE), logger);
                logger.log(Level.SEVERE, "------------------------------");

                // Paper start - Only print full dump on long timeouts
                if (isLongTimeout) {
                    logger.log(Level.SEVERE, "Entire Thread Dump:");
                    ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                    for (ThreadInfo thread : threads) {
                        WatchdogThread.dumpThread(thread, logger);
                    }
                } else {
                    logger.log(Level.SEVERE, "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH ---");
                }

                logger.log(Level.SEVERE, "------------------------------");

                if (isLongTimeout) {
                    if (!server.hasStopped()) {
                        server.forceTicks = true;
                        if (this.restart) {
                            RestartCommand.addShutdownHook(SpigotConfig.restartScript);
                        }
                        // try one last chance to safe shutdown on main in case it 'comes back'
                        server.abnormalExit = true;
                        server.safeShutdown(false, this.restart);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!server.hasStopped()) {
                            server.close();
                        }
                    }
                    break;
                }
                // Paper end
            }

            try {
                sleep(1000); // Paper - Reduce check time to every second instead of every ten seconds, more consistent and allows for short timeout
            } catch (InterruptedException ex) {
                this.interrupt();
            }
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger logger) {
        logger.log(Level.SEVERE, "------------------------------");

        logger.log(Level.SEVERE, "Current Thread: " + thread.getThreadName());
        logger.log(Level.SEVERE, "\tPID: " + thread.getThreadId()
            + " | Suspended: " + thread.isSuspended()
            + " | Native: " + thread.isInNative()
            + " | State: " + thread.getThreadState());
        if (thread.getLockedMonitors().length != 0) {
            logger.log(Level.SEVERE, "\tThread is waiting on monitor(s):");
            for (MonitorInfo monitor : thread.getLockedMonitors()) {
                logger.log(Level.SEVERE, "\t\tLocked on:" + monitor.getLockedStackFrame());
            }
        }
        logger.log(Level.SEVERE, "\tStack:");

        for (StackTraceElement stack : io.papermc.paper.util.StacktraceDeobfuscator.INSTANCE.deobfuscateStacktrace(thread.getStackTrace())) { // Paper
            logger.log(Level.SEVERE, "\t\t" + stack);
        }
    }
}
