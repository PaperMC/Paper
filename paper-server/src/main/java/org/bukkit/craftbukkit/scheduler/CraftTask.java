package org.bukkit.craftbukkit.scheduler;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CraftTask implements BukkitTask, Runnable, Comparable<CraftTask> { // Spigot
    public static final int ERROR = 0;
    public static final int NO_REPEATING = -1;
    public static final int CANCEL = -2;
    public static final int PROCESS_FOR_FUTURE = -3;
    public static final int DONE_FOR_FUTURE = -4;
    /**
     * -1 means no repeating <br>
     * -2 means cancel <br>
     * -3 means processing for Future <br>
     * -4 means done for Future <br>
     * Never 0 <br>
     * >0 means number of ticks to wait between each execution
     */
    private volatile long period;
    private long nextRun;
    private final Consumer<? super BukkitTask> task;
    private final Plugin plugin;
    private final long id;

    CraftTask(final Plugin plugin,
              final Consumer<? super BukkitTask> task,
              final long id,
              final long period) {
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = period;
    }

    @Override
    public final int getTaskId() {
        return Math.floorMod(this.id, Integer.MAX_VALUE) + 1;
    }

    @Override
    public final Plugin getOwner() {
        return this.plugin;
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public void run() {
        if (this.task != null) {
            this.task.accept(this);
        }
    }

    long getPeriod() {
        return this.period;
    }

    void setPeriod(final long period) {
        this.period = period;
    }

    long getNextRun() {
        return this.nextRun;
    }

    void setNextRun(final long nextRun) {
        this.nextRun = nextRun;
    }

    @Override
    public boolean isCancelled() {
        return (this.period == CraftTask.CANCEL);
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.getTaskId());
    }

    /**
     * This method properly sets the status to cancelled, synchronizing when required.
     *
     * @return false if it is a craft future task that has already begun execution, true otherwise
     */
    boolean cancel0() {
        this.setPeriod(CraftTask.CANCEL);
        return true;
    }

    @Override
    public int compareTo(final CraftTask o) {
        if (o == this) {
            return 0;
        }
        final int value = Long.compare(this.getNextRun(), o.getNextRun());
        // If the tasks should run on the same tick they should be run FIFO
        return value != 0 ? value : Long.compare(this.id, o.id);
    }
}
