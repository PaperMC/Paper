package org.bukkit.craftbukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


class CraftTask implements BukkitTask, Runnable {

    private volatile CraftTask next = null;
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
    private final Runnable task;
    private final Plugin plugin;
    private final int id;

    CraftTask() {
        this(null, null, -1, -1);
    }

    CraftTask(final Runnable task) {
        this(null, task, -1, -1);
    }

    CraftTask(final Plugin plugin, final Runnable task, final int id, final long period) {
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = period;
    }

    public final int getTaskId() {
        return id;
    }

    public final Plugin getOwner() {
        return plugin;
    }

    public boolean isSync() {
        return true;
    }

    public void run() {
        task.run();
    }

    long getPeriod() {
        return period;
    }

    void setPeriod(long period) {
        this.period = period;
    }

    long getNextRun() {
        return nextRun;
    }

    void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    CraftTask getNext() {
        return next;
    }

    void setNext(CraftTask next) {
        this.next = next;
    }

    Class<? extends Runnable> getTaskClass() {
        return task.getClass();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    /**
     * This method properly sets the status to cancelled, synchronizing when required.
     *
     * @return false if it is a craft future task that has already begun execution, true otherwise
     */
    boolean cancel0() {
        setPeriod(-2l);
        return true;
    }
}
