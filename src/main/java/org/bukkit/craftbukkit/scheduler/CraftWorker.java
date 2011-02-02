package org.bukkit.craftbukkit.scheduler;

import org.bukkit.plugin.Plugin;

public class CraftWorker implements Runnable {

    private static int hashIdCounter = 1;
    private static Object hashIdCounterSync = new Object();

    private final int hashId;

    private final Plugin owner;
    private final int taskId;

    private final Thread t;
    private final CraftThreadManager parent;

    private final Runnable task;

    CraftWorker(CraftThreadManager parent, Runnable task, Plugin owner, int taskId) {
        this.parent = parent;
        this.taskId = taskId;
        this.task = task;
        this.owner = owner;
        this.hashId = CraftWorker.getNextHashId();
        t = new Thread(this);
        t.start();
    }

    public void run() {

        try {
            task.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (parent.workers) {
            parent.workers.remove(this);
        }

    }

    public int getTaskId() {
        return taskId;
    }

    public Plugin getOwner() {
        return owner;
    }

    public void interrupt() {
        t.interrupt();
    }

    private static int getNextHashId() {
        synchronized (hashIdCounterSync) {
            return hashIdCounter++;
        }
    }

    @Override
    public int hashCode() {
        return hashId;
    }

    @Override
    public boolean equals( Object other ) {

        if (other == null) {
            return false;
        }

        if (!(other instanceof CraftWorker)) {
            return false;
        }

        CraftWorker otherCraftWorker = (CraftWorker) other;
        return otherCraftWorker.hashCode() == hashId;
    }

}
