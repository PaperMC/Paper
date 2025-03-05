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
    private final int id;
    private final long createdAt = System.nanoTime();

    CraftTask(final Runnable task) {
        this(null,
            t -> task.run(),
            CraftTask.NO_REPEATING,
            CraftTask.NO_REPEATING
        );
    }

    CraftTask(final Plugin plugin,
              final Consumer<? super BukkitTask> task,
              final int id,
              final long period) {
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = period;
    }

    @Override
    public final int getTaskId() {
        return this.id;
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
        if (task != null) {
            task.accept(this);
        }
    }

    long getCreatedAt() {
        return this.createdAt;
    }

    long getPeriod() {
        return this.period;
    }

    void setPeriod(long period) {
        this.period = period;
    }

    long getNextRun() {
        return this.nextRun;
    }

    void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    @Override
    public boolean isCancelled() {
        return (this.period == CraftTask.CANCEL);
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.id);
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
        int value = Long.compare(getNextRun(), o.getNextRun());
        // If the tasks should run on the same tick they should be run FIFO
        return value != 0 ? value : Long.compare(createdAt, o.createdAt);
    }
}
