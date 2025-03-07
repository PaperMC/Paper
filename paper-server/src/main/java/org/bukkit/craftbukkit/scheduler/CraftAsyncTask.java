package org.bukkit.craftbukkit.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.jetbrains.annotations.NotNull;

class CraftAsyncTask extends CraftTask {
    private final Lock lock = new ReentrantLock();
    private final Map<Long, BukkitWorker> workers = new HashMap<>();
    private final Map<Integer, CraftTask> runners;

    CraftAsyncTask(final Map<Integer, CraftTask> runners,
                   final Plugin plugin,
                   final Consumer<? super BukkitTask> task,
                   final long id, final long delay) {
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
        try {
            lock.lock();
            try {
                if (this.getState() != CraftTask.CANCEL) {
                    this.workers.put(thread.threadId(), new Worker(thread, this));
                }
            } finally {
                lock.unlock();
            }
            try {
                super.run();
            } catch (final Throwable thrown) {
                this.getOwner().getLogger().log(
                    Level.WARNING,
                    String.format(
                        "Plugin %s generated an exception while executing task %s",
                        this.getOwner().getDescription().getFullName(),
                        this.getTaskId()),
                    thrown);
            } finally {
                removeWorker();
            }
        } finally {
            thread.setName(nameBefore); // Paper - name threads according to running plugin
        }
    }

    void forEach(Consumer<BukkitWorker> consumer) {
        lock.lock();
        try {
            workers.values().forEach(consumer);
        } finally {
            lock.unlock();
        }
    }

    int workers() {
        lock.lock();
        try {
            return workers.size();
        } finally {
            lock.unlock();
        }
    }

    private void removeWorker() {
        final Thread thread = Thread.currentThread();
        lock.lock();
        try {
            if (workers.remove(thread.threadId()) == null) {
                throw new IllegalStateException(
                    String.format(
                        "Unable to remove worker %s on task %s for %s",
                        thread.getName(),
                        this.getTaskId(),
                        this.getOwner().getDescription().getFullName())
                    );
            }
        } finally {
            if (this.getState() < 0 && this.workers.isEmpty()) {
                // At this spot, we know we are the final async task being executed!
                // Because we have the lock, nothing else is running or will run because delay < 0
                this.runners.remove(this.getTaskId());
            }
            lock.unlock();
        }
    }

    @Override
    boolean cancel0() {
        lock.lock();
        try {
            if (super.cancel0()) {
                if (this.workers.isEmpty()) {
                    this.runners.remove(this.getTaskId());
                }
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private static final class Worker implements BukkitWorker {
        private final Thread thread;
        private final BukkitTask task;

        private Worker(final BukkitTask task) {
            this(Thread.currentThread(), task);
        }

        private Worker(final Thread thread, final BukkitTask task) {
            this.thread = thread;
            this.task = task;
        }

        @Override
        public int getTaskId() {
            return this.task.getTaskId();
        }

        @Override
        public @NotNull Plugin getOwner() {
            return this.task.getOwner();
        }

        @Override
        public @NotNull Thread getThread() {
            return this.thread;
        }
    }

}
