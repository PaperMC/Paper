package org.spigotmc;

import java.io.PrintStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.defaults.TimingsCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides custom timing sections for /timings merged.
 */
public class CustomTimingsHandler {

    private static Queue<CustomTimingsHandler> HANDLERS = new ConcurrentLinkedQueue<CustomTimingsHandler>();
    /*========================================================================*/
    private final String name;
    private final CustomTimingsHandler parent;
    private long count = 0;
    private long start = 0;
    private long timingDepth = 0;
    private long totalTime = 0;
    private long curTickTotal = 0;
    private long violations = 0;

    public CustomTimingsHandler(@NotNull String name) {
        this(name, null);
    }

    public CustomTimingsHandler(@NotNull String name, @Nullable CustomTimingsHandler parent) {
        this.name = name;
        this.parent = parent;
        HANDLERS.add(this);
    }

    /**
     * Prints the timings and extra data to the given stream.
     *
     * @param printStream output stream
     */
    public static void printTimings(@NotNull PrintStream printStream) {
        printStream.println("Minecraft");
        for (CustomTimingsHandler timings : HANDLERS) {
            long time = timings.totalTime;
            long count = timings.count;
            if (count == 0) {
                continue;
            }
            long avg = time / count;

            printStream.println("    " + timings.name + " Time: " + time + " Count: " + count + " Avg: " + avg + " Violations: " + timings.violations);
        }
        printStream.println("# Version " + Bukkit.getVersion());
        int entities = 0;
        int livingEntities = 0;
        for (World world : Bukkit.getWorlds()) {
            entities += world.getEntities().size();
            livingEntities += world.getLivingEntities().size();
        }
        printStream.println("# Entities " + entities);
        printStream.println("# LivingEntities " + livingEntities);
    }

    /**
     * Resets all timings.
     */
    public static void reload() {
        if (Bukkit.getPluginManager().useTimings()) {
            for (CustomTimingsHandler timings : HANDLERS) {
                timings.reset();
            }
        }
        TimingsCommand.timingStart = System.nanoTime();
    }

    /**
     * Ticked every tick by CraftBukkit to count the number of times a timer
     * caused TPS loss.
     */
    public static void tick() {
        if (Bukkit.getPluginManager().useTimings()) {
            for (CustomTimingsHandler timings : HANDLERS) {
                if (timings.curTickTotal > 50000000) {
                    timings.violations += Math.ceil(timings.curTickTotal / 50000000);
                }
                timings.curTickTotal = 0;
                timings.timingDepth = 0; // incase reset messes this up
            }
        }
    }

    /**
     * Starts timing to track a section of code.
     */
    public void startTiming() {
        // If second condtion fails we are already timing
        if (Bukkit.getPluginManager().useTimings() && ++timingDepth == 1) {
            start = System.nanoTime();
            if (parent != null && ++parent.timingDepth == 1) {
                parent.start = start;
            }
        }
    }

    /**
     * Stops timing a section of code.
     */
    public void stopTiming() {
        if (Bukkit.getPluginManager().useTimings()) {
            if (--timingDepth != 0 || start == 0) {
                return;
            }
            long diff = System.nanoTime() - start;
            totalTime += diff;
            curTickTotal += diff;
            count++;
            start = 0;
            if (parent != null) {
                parent.stopTiming();
            }
        }
    }

    /**
     * Reset this timer, setting all values to zero.
     */
    public void reset() {
        count = 0;
        violations = 0;
        curTickTotal = 0;
        totalTime = 0;
        start = 0;
        timingDepth = 0;
    }
}
