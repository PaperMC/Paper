package org.bukkit.craftbukkit.scheduler;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerSchedulerException;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

/**
 * The fundamental concepts for this implementation:
 * <ul>
 * <li>Main thread owns {@link #head} and {@link #currentTick}, but it may be read from any thread</li>
 * <li>Main thread exclusively controls {@link #temp} and {@link #pending}.
 *     They are never to be accessed outside of the main thread; alternatives exist to prevent locking.</li>
 * <li>{@link #head} to {@link #tail} act as a linked list/queue, with 1 consumer and infinite producers.
 *     Adding to the tail is atomic and very efficient; utility method is {@link #handle(CraftTask, long)} or {@link #addTask(CraftTask)}. </li>
 * <li>Changing the period on a task is delicate.
 *     Any future task needs to notify waiting threads.
 *     Async tasks must be synchronized to make sure that any thread that's finishing will remove itself from {@link #runners}.
 *     Another utility method is provided for this, {@link ask(int)}</li>
 * <li>{@link #runners} provides a moderately up-to-date view of active tasks.
 *     If the linked head to tail set is read, all remaining tasks that were active at the time execution started will be located in runners.</li>
 * <li>Async tasks are responsible for removing themselves from runners</li>
 * <li>Sync tasks are only to be removed from runners on the main thread when coupled with a removal from pending and temp.</li>
 * <li>Most of the design in this scheduler relies on queuing special tasks to perform any data changes on the main thread.
 *     When executed from inside a synchronous method, the scheduler will be updated before next execution by virtue of the frequent {@link #parsePending()} calls.</li>
 * </ul>
 */
public class CraftScheduler implements BukkitScheduler {
    private static volatile int currentTick = -1;
    /**
     * Counter for IDs. Order doesn't matter, only uniqueness.
     */
    private final AtomicLong ids = new AtomicLong();

    final WorkQueue external = new LinkedWorkQueue();

    final WorkQueue pending = new DelayedQueue();
    /**
     * These are tasks that are currently active. It's provided for 'viewing' the current state.
     */
    final ConcurrentHashMap<Integer, CraftTask> runners = new ConcurrentHashMap<>(); // Paper
    /**
     * The sync task that is currently running on the main thread.
     */
    private volatile CraftTask currentTask = null;
    // Paper start
    private final CraftScheduler asyncScheduler;
    private final boolean isAsyncScheduler;

    public CraftScheduler() {
        this(false);
    }

    public CraftScheduler(final boolean isAsync) {
        this.isAsyncScheduler = isAsync;
        if (isAsync) {
            this.asyncScheduler = this;
        } else {
            this.asyncScheduler = new CraftAsyncScheduler();
        }
    }
    public static int now() {
        return currentTick;
    }
    // Paper end
    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleSyncDelayedTask(plugin, task, 0L);
    }

    @Override
    public BukkitTask runTask(final Plugin plugin, final Runnable runnable) {
        return this.runTaskLater(plugin, runnable, 0L);
    }

    @Override
    public void runTask(final Plugin plugin, final Consumer<? super BukkitTask> task) throws IllegalArgumentException {
        this.runTaskLater(plugin, task, 0L);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleAsyncDelayedTask(plugin, task, 0L);
    }

    @Override
    public BukkitTask runTaskAsynchronously(final Plugin plugin, final Runnable runnable) {
        return this.runTaskLaterAsynchronously(plugin, runnable, 0L);
    }

    @Override
    public void runTaskAsynchronously(final Plugin plugin, final Consumer<? super BukkitTask> task) throws IllegalArgumentException {
        this.runTaskLaterAsynchronously(plugin, task, 0L);
    }

    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleSyncRepeatingTask(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public BukkitTask runTaskLater(final Plugin plugin, final Runnable runnable, final long delay) {
        return this.runTaskTimer(plugin, runnable, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public void runTaskLater(final Plugin plugin, final Consumer<? super BukkitTask> task, final long delay) throws IllegalArgumentException {
        this.runTaskTimer(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleAsyncRepeatingTask(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(final Plugin plugin, final Runnable runnable, final long delay) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public void runTaskLaterAsynchronously(final Plugin plugin, final Consumer<? super BukkitTask> task, final long delay) throws IllegalArgumentException {
        this.runTaskTimerAsynchronously(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public void runTaskTimerAsynchronously(final Plugin plugin, final Consumer<? super BukkitTask> task, final long delay, final long period) throws IllegalArgumentException {
        this.submit(plugin, task, delay, period, true); // Paper
    }

    @Override
    public int scheduleSyncRepeatingTask(final Plugin plugin, final Runnable runnable, final long delay, final long period) {
        return this.runTaskTimer(plugin, runnable, delay, period).getTaskId();
    }

    @Override
    public BukkitTask runTaskTimer(final Plugin plugin, final Runnable runnable, final long delay, final long period) {
        return this.submit(plugin, runnable, delay, period, false);
    }

    @Override
    public void runTaskTimer(final Plugin plugin, final Consumer<? super BukkitTask> task, final long delay, final long period) throws IllegalArgumentException {
        this.submit(plugin, task, delay, period, false);
    }

    @Deprecated
    @Override
    public int scheduleAsyncRepeatingTask(final Plugin plugin, final Runnable runnable, final long delay, final long period) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(final Plugin plugin, final Runnable runnable, final long delay, final long period) {
        return this.submit(plugin, runnable, delay, period, true);
    }

    private BukkitTask submit(final Plugin plugin,
                              final Runnable runnable,
                              final long delay, final long period,
                              final boolean async) {
        return this.submit(plugin, task -> runnable.run(), delay, period, async);
    }

    private BukkitTask submit(final Plugin plugin,
                              final Consumer<? super BukkitTask> consumer,
                              long delay, long period,
                              final boolean async) {
        CraftScheduler.validate(plugin);
        if (delay < 0L) {
            delay = 0;
        }
        if (period == 0) {
            period = 1L;
        } else if (period < CraftTask.NO_REPEATING) {
            period = CraftTask.NO_REPEATING;
        }
        final long id = this.nextId();
        return this.handle(
            async
                ? new CraftAsyncTask(this.asyncScheduler, this.asyncScheduler.runners, plugin, consumer, id, period)
                : new CraftTask(this, plugin, consumer, id, period),
            delay);
    }

    @Override
    public <T> Future<T> callSyncMethod(final Plugin plugin, final Callable<T> task) {
        CraftScheduler.validate(plugin);
        final CraftFuture<T> future = new CraftFuture<>(this, task, plugin, this.nextId());
        this.handle(future, 0L);
        return future;
    }


    private CraftScheduler scheduler() {
        return isAsyncScheduler ? asyncScheduler : this;
    }

    @Override
    public void cancelTask(final int taskId) {
        if (taskId <= 0) {
            return;
        }
        // Paper start
        if (!this.isAsyncScheduler) {
            this.asyncScheduler.cancelTask(taskId);
        }
        // Paper end
        final CraftTask task = this.runners.get(taskId);
        if (task != null) {
            task.cancel();
        } else {
            for (CraftTask t : this.external) {
                if (t.getTaskId() == taskId) {
                    t.cancel();
                    return;
                }
            }
            handle(
                new CraftTask(
                    scheduler(),
                    t -> {
                        for (final CraftTask pend : this.pending) {
                            if (pend.getTaskId() == taskId) {
                                pend.cancel();
                                return;
                            }
                        }
                    }
                ),
                0
            );
        }
    }

    @Override
    public void cancelTasks(final Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Cannot cancel tasks of null plugin");
        // Paper start
        if (!this.isAsyncScheduler) {
            this.asyncScheduler.cancelTasks(plugin);
        }
        handle(
            new CraftTask(
                scheduler(),
                t -> {
                    for (final CraftTask pend : this.pending) {
                        if (!pend.isInternal() && pend.getOwner().equals(plugin)) {
                            pend.cancel();
                        }
                    }
                }
            ),
            0
        );
        // Paper end
    }

    @Override
    public boolean isCurrentlyRunning(final int taskId) {
        // Paper start
        if (!this.isAsyncScheduler) {
            if (this.asyncScheduler.isCurrentlyRunning(taskId)) {
                return true;
            }
        }
        // Paper end
        final CraftTask task = this.runners.get(taskId);
        if (task == null) {
            return false;
        }
        if (task.isSync()) {
            return (task == this.currentTask);
        }
        final CraftAsyncTask asyncTask = (CraftAsyncTask) task;
        return asyncTask.workers() > 0;
    }

    @Override
    public boolean isQueued(final int taskId) {
        if (taskId <= 0) {
            return false;
        }
        // Paper start
        if (!this.isAsyncScheduler && this.asyncScheduler.isQueued(taskId)) {
            return true;
        }
        // Paper end
        for (final CraftTask task : this.external) {
            if (task.getTaskId() == taskId) {
                return task.getState() >= CraftTask.NO_REPEATING; // The task will run
            }
        }
        final CraftTask task = this.runners.get(taskId);
        return task != null && task.getState() >= CraftTask.NO_REPEATING;
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        // Paper start
        if (!this.isAsyncScheduler) {
            //noinspection TailRecursion
            return this.asyncScheduler.getActiveWorkers();
        }
        // Paper end
        final ArrayList<BukkitWorker> workers = new ArrayList<BukkitWorker>();
        for (final CraftTask taskObj : this.runners.values()) {
            // Iterator will be a best-effort (may fail to grab very new values) if called from an async thread
            if (taskObj.isSync()) {
                continue;
            }
            final CraftAsyncTask task = (CraftAsyncTask) taskObj;
            task.forEach(workers::add);
        }
        return workers;
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        final ArrayList<CraftTask> truePending = new ArrayList<CraftTask>();
        for (final CraftTask task : this.external) {
            if (!task.isInternal() && !task.isCancelled()) {
                truePending.add(task);
            }
        }

        final ArrayList<BukkitTask> pending = new ArrayList<BukkitTask>();
        for (final CraftTask task : this.runners.values()) {
            if (task.getState() >= CraftTask.NO_REPEATING) {
                pending.add(task);
            }
        }

        for (final CraftTask task : truePending) {
            if (task.getState() >= CraftTask.NO_REPEATING && !pending.contains(task)) {
                pending.add(task);
            }
        }
        // Paper start
        if (!this.isAsyncScheduler) {
            pending.addAll(this.asyncScheduler.getPendingTasks());
        }
        // Paper end
        return pending;
    }

    /**
     * This method is designed to never block or wait for locks; an immediate execution of all current tasks.
     */
    public void mainThreadHeartbeat() {
        currentTick++;
        // Paper start
        if (!this.isAsyncScheduler) {
            this.asyncScheduler.mainThreadHeartbeat();
        }
        // Paper end
        this.parsePending();
        this.pending.dropAll(task -> {
            if (task.getState() < CraftTask.COMPLETING) {
                this.runners.remove(task.getTaskId(), task);
                return;
            }
            if (task.isSync()) {
                this.currentTask = task;
                try {
                    task.run();
                } catch (final Throwable throwable) {
                    // Paper start
                    final String logMessage = String.format(
                        "Task #%s for %s generated an exception",
                        task.getTaskId(),
                        task.getOwner().getDescription().getFullName());
                    task.getOwner().getLogger().log(
                        Level.WARNING,
                        logMessage,
                        throwable);
                    Bukkit.getServer().getPluginManager().callEvent(
                        new ServerExceptionEvent(
                            new ServerSchedulerException(logMessage, throwable, task))
                    );
                    // Paper end
                } finally {
                    this.currentTask = null;
                }
            } else {
                // this.debugTail = this.debugTail.setNext(new CraftAsyncDebugger(this.currentTick + CraftScheduler.RECENT_TICKS, task.getOwner(), task.getTaskClass())); // Paper
                task.getOwner().getLogger().log(Level.SEVERE, "Unexpected Async Task in the Sync Scheduler. Report this to Paper"); // Paper
                // We don't need to parse pending
                // (async tasks must live with race-conditions if they attempt to cancel between these few lines of code)
            }
            final long period = task.getPeriod(); // State consistency
            if (period > 0) {
                this.handle(task, period);
            } else if (task.isSync()) {
                this.runners.remove(task.getTaskId());
            }
            this.parsePending();
        });
        //this.debugHead = this.debugHead.getNextHead(this.currentTick); // Paper
    }

    protected CraftTask handle(final CraftTask task, final long delay) { // Paper
        // Paper start
        if (!this.isAsyncScheduler && !task.isSync()) {
            this.asyncScheduler.handle(task, delay);
            return task;
        }
        // Paper end
        task.setNextRun(now() + delay);
        this.external.push(task);
        return task;
    }

    private static void validate(final Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }
    }

    private long nextId() {
        Preconditions.checkArgument(this.runners.size() < Integer.MAX_VALUE, "There are already %s tasks scheduled! Cannot schedule more", Integer.MAX_VALUE);
        long id;
        do {
            id = this.ids.getAndIncrement();
        } while (this.runners.containsKey(Math.floorMod(id, Integer.MAX_VALUE) + 1)); // Avoid generating duplicate IDs
        return id;
    }

    void parsePending() { // Paper
        this.external.dropAll(task -> {
            if (task.getState() >= CraftTask.NO_REPEATING) {
                this.pending.push(task);
                // todo:
                if (!task.isInternal()) {
                    this.runners.put(task.getTaskId(), task);
                }
            } else {
                this.pending.remove(task);
                if (task.isSync()) {
                    this.runners.remove(task.getTaskId());
                }
            }
        });
    }

    @Override
    public String toString() {
        // Paper start
        return "";
        /*
        int debugTick = this.currentTick;
        StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - CraftScheduler.RECENT_TICKS).append('-').append(debugTick).append('{');
        this.debugHead.debugTo(string);
        return string.append('}').toString();
        */
        // Paper end
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final BukkitRunnable task, final long delay) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(Plugin, long)");
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final BukkitRunnable task) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
    }

    @Deprecated
    @Override
    public int scheduleSyncRepeatingTask(final Plugin plugin, final BukkitRunnable task, final long delay, final long period) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(Plugin, long, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTask(final Plugin plugin, final BukkitRunnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskAsynchronously(final Plugin plugin, final BukkitRunnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskAsynchronously(Plugin)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskLater(final Plugin plugin, final BukkitRunnable task, final long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(Plugin, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskLaterAsynchronously(final Plugin plugin, final BukkitRunnable task, final long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLaterAsynchronously(Plugin, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskTimer(final Plugin plugin, final BukkitRunnable task, final long delay, final long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(Plugin, long, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskTimerAsynchronously(final Plugin plugin, final BukkitRunnable task, final long delay, final long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimerAsynchronously(Plugin, long, long)");
    }

    // Paper start - add getMainThreadExecutor
    @Override
    public Executor getMainThreadExecutor(final Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        return command -> {
            Preconditions.checkArgument(command != null, "Command cannot be null");
            this.runTask(plugin, command);
        };
    }
    // Paper end
}
