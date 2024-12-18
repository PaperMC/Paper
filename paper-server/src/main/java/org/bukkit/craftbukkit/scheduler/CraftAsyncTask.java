package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitWorker;

class CraftAsyncTask extends CraftTask {

    private final LinkedList<BukkitWorker> workers = new LinkedList<BukkitWorker>();
    private final Map<Integer, CraftTask> runners;

    CraftAsyncTask(final Map<Integer, CraftTask> runners, final Plugin plugin, final Object task, final int id, final long delay) {
        super(plugin, task, id, delay);
        this.runners = runners;
    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        // Paper start - name threads according to running plugin
        final String nameBefore = thread.getName();
        thread.setName(nameBefore + " - " + this.getOwner().getName());
        try { synchronized (this.workers) { // Paper end - name threads according to running plugin
            if (this.getPeriod() == CraftTask.CANCEL) {
                // Never continue running after cancelled.
                // Checking this with the lock is important!
                return;
            }
            this.workers.add(
                new BukkitWorker() {
                    @Override
                    public Thread getThread() {
                        return thread;
                    }

                    @Override
                    public int getTaskId() {
                        return CraftAsyncTask.this.getTaskId();
                    }

                    @Override
                    public Plugin getOwner() {
                        return CraftAsyncTask.this.getOwner();
                    }
                });
        }
        Throwable thrown = null;
        try {
            super.run();
        } catch (final Throwable t) {
            thrown = t;
            this.getOwner().getLogger().log(
                    Level.WARNING,
                    String.format(
                        "Plugin %s generated an exception while executing task %s",
                        this.getOwner().getDescription().getFullName(),
                        this.getTaskId()),
                    thrown);
        } finally {
            // Cleanup is important for any async task, otherwise ghost tasks are everywhere
            synchronized (this.workers) {
                try {
                    final Iterator<BukkitWorker> workers = this.workers.iterator();
                    boolean removed = false;
                    while (workers.hasNext()) {
                        if (workers.next().getThread() == thread) {
                            workers.remove();
                            removed = true; // Don't throw exception
                            break;
                        }
                    }
                    if (!removed) {
                        throw new IllegalStateException(
                                String.format(
                                    "Unable to remove worker %s on task %s for %s",
                                    thread.getName(),
                                    this.getTaskId(),
                                    this.getOwner().getDescription().getFullName()),
                                thrown); // We don't want to lose the original exception, if any
                    }
                } finally {
                    if (this.getPeriod() < 0 && this.workers.isEmpty()) {
                        // At this spot, we know we are the final async task being executed!
                        // Because we have the lock, nothing else is running or will run because delay < 0
                        this.runners.remove(this.getTaskId());
                    }
                }
            }
        }
        } finally { thread.setName(nameBefore); } // Paper - name threads according to running plugin
    }

    LinkedList<BukkitWorker> getWorkers() {
        return this.workers;
    }

    @Override
    boolean cancel0() {
        synchronized (this.workers) {
            // Synchronizing here prevents race condition for a completing task
            this.setPeriod(CraftTask.CANCEL);
            if (this.workers.isEmpty()) {
                this.runners.remove(this.getTaskId());
            }
        }
        return true;
    }
}
