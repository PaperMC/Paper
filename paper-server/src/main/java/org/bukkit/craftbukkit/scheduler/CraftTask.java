package org.bukkit.craftbukkit.scheduler;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CraftTask implements BukkitTask, Runnable, Comparable<CraftTask> { // Spigot
    public static final long NO_REPEATING = -1;
    public static final long COMPLETING = -2;
    public static final long DONE = -3;
    public static final long CANCEL = -4;

    private final AtomicLong period;
    private final Consumer<? super BukkitTask> task;
    private final Plugin plugin;
    private final long id;
    private long nextRun;

    CraftTask(final Consumer<? super BukkitTask> task) {
        this(null, task, -1, NO_REPEATING);
    }

    CraftTask(final Plugin plugin,
              final Consumer<? super BukkitTask> task,
              final long id,
              final long period) {
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = new AtomicLong(period);
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
        this.task.accept(this);
    }

    long getNextRun() {
        return this.nextRun;
    }

    void setNextRun(final long nextRun) {
        this.nextRun = nextRun;
    }

    @Override
    public boolean isCancelled() {
        return this.getState() == CraftTask.CANCEL;
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.getTaskId());
    }

    boolean casState(long p, long x) {
        return this.period.compareAndSet(p, x);
    }

    void setState(final long state) {
        this.period.setRelease(state);
    }

    long getState() {
        return this.period.get();
    }

    long getPeriod() {
        return this.period.get();
    }

    /**
     * This method properly sets the status to cancelled, synchronizing when required.
     *
     * @return false if it is a craft future task that has already begun execution, true otherwise
     */
    boolean tryCancel() {
        return getState() != CANCEL && this.period.getAndSet(CANCEL) != CANCEL;
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
