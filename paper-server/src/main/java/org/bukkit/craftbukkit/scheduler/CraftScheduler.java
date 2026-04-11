package org.bukkit.craftbukkit.scheduler;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.logging.Level;
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
 *     Another utility method is provided for this, {@link #cancelTask(int)}</li>
 * <li>{@link #runners} provides a moderately up-to-date view of active tasks.
 *     If the linked head to tail set is read, all remaining tasks that were active at the time execution started will be located in runners.</li>
 * <li>Async tasks are responsible for removing themselves from runners</li>
 * <li>Sync tasks are only to be removed from runners on the main thread when coupled with a removal from pending and temp.</li>
 * <li>Most of the design in this scheduler relies on queuing special tasks to perform any data changes on the main thread.
 *     When executed from inside a synchronous method, the scheduler will be updated before next execution by virtue of the frequent {@link #parsePending()} calls.</li>
 * </ul>
 */
public class CraftScheduler implements BukkitScheduler {

    /**
     * The start ID for the counter.
     */
    private static final int START_ID = 1;
    /**
     * Increment the {@link #ids} field and reset it to the {@link #START_ID} if it reaches {@link Integer#MAX_VALUE}
     */
    private static final IntUnaryOperator INCREMENT_IDS = previous -> {
        // We reached the end, go back to the start!
        if (previous == Integer.MAX_VALUE) {
            return CraftScheduler.START_ID;
        }
        return previous + 1;
    };
    /**
     * Counter for IDs. Order doesn't matter, only uniqueness.
     */
    private final AtomicInteger ids = new AtomicInteger(CraftScheduler.START_ID);
    /**
     * Current head of linked-list. This reference is always stale, {@link CraftTask#next} is the live reference.
     */
    private volatile CraftTask head = new CraftTask();
    /**
     * Tail of a linked-list. AtomicReference only matters when adding to queue
     */
    private final AtomicReference<CraftTask> tail = new AtomicReference<CraftTask>(this.head);
    /**
     * Main thread logic only
     */
    final PriorityQueue<CraftTask> pending = new PriorityQueue<CraftTask>(10, // Paper
            new Comparator<CraftTask>() {
                @Override
                public int compare(final CraftTask o1, final CraftTask o2) {
                    int value = Long.compare(o1.getNextRun(), o2.getNextRun());

                    // If the tasks should run on the same tick they should be run FIFO
                    return value != 0 ? value : Long.compare(o1.getCreatedAt(), o2.getCreatedAt());
                }
            });
    /**
     * Main thread logic only
     */
    private final List<CraftTask> temp = new ArrayList<CraftTask>();
    /**
     * These are tasks that are currently active. It's provided for 'viewing' the current state.
     */
    final ConcurrentHashMap<Integer, CraftTask> runners = new ConcurrentHashMap<Integer, CraftTask>(); // Paper
    /**
     * The sync task that is currently running on the main thread.
     */
    private volatile CraftTask currentTask = null;
    // Paper start - Improved Async Task Scheduler
    volatile int currentTick = -1;/*
    private final Executor executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Craft Scheduler Thread - %d").build());
    private CraftAsyncDebugger debugHead = new CraftAsyncDebugger(-1, null, null) {
        @Override
        StringBuilder debugTo(StringBuilder string) {
            return string;
        }
    };
    private CraftAsyncDebugger debugTail = this.debugHead;

    */ // Paper end
    private static final int RECENT_TICKS;

    static {
        RECENT_TICKS = 30;
    }

    // Paper start
    private final CraftScheduler asyncScheduler;
    private final boolean isAsyncScheduler;
    public CraftScheduler() {
        this(false);
    }

    public CraftScheduler(boolean isAsync) {
        this.isAsyncScheduler = isAsync;
        if (isAsync) {
            this.asyncScheduler = this;
        } else {
            this.asyncScheduler = new CraftAsyncScheduler();
        }
    }
    // Paper end

    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleSyncDelayedTask(plugin, task, 0L);
    }

    @Override
    public BukkitTask runTask(Plugin plugin, Runnable runnable) {
        return this.runTaskLater(plugin, runnable, 0L);
    }

    @Override
    public void runTask(Plugin plugin, Consumer<? super BukkitTask> task) throws IllegalArgumentException {
        this.runTaskLater(plugin, task, 0L);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleAsyncDelayedTask(plugin, task, 0L);
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        return this.runTaskLaterAsynchronously(plugin, runnable, 0L);
    }

    @Override
    public void runTaskAsynchronously(Plugin plugin, Consumer<? super BukkitTask> task) throws IllegalArgumentException {
        this.runTaskLaterAsynchronously(plugin, task, 0L);
    }

    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleSyncRepeatingTask(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
        return this.runTaskTimer(plugin, runnable, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public void runTaskLater(Plugin plugin, Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException {
        this.runTaskTimer(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleAsyncRepeatingTask(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public void runTaskLaterAsynchronously(Plugin plugin, Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException {
        this.runTaskTimerAsynchronously(plugin, task, delay, CraftTask.NO_REPEATING);
    }

    @Override
    public void runTaskTimerAsynchronously(Plugin plugin, Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException {
        this.runTaskTimerAsynchronously(plugin, (Object) task, delay, period); // Paper
    }

    @Override
    public int scheduleSyncRepeatingTask(final Plugin plugin, final Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, runnable, delay, period).getTaskId();
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, (Object) runnable, delay, period);
    }

    @Override
    public void runTaskTimer(Plugin plugin, Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException {
        this.runTaskTimer(plugin, (Object) task, delay, period);
    }

    public BukkitTask runTaskTimer(Plugin plugin, Object runnable, long delay, long period) {
        CraftScheduler.validate(plugin, runnable);
        if (delay < 0L) {
            delay = 0;
        }
        if (period == CraftTask.ERROR) {
            period = 1L;
        } else if (period < CraftTask.NO_REPEATING) {
            period = CraftTask.NO_REPEATING;
        }
        return this.handle(new CraftTask(plugin, runnable, this.nextId(), period), delay);
    }

    @Deprecated
    @Override
    public int scheduleAsyncRepeatingTask(final Plugin plugin, final Runnable runnable, long delay, long period) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
        return this.runTaskTimerAsynchronously(plugin, (Object) runnable, delay, period);
    }

    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Object runnable, long delay, long period) {
        CraftScheduler.validate(plugin, runnable);
        if (delay < 0L) {
            delay = 0;
        }
        if (period == CraftTask.ERROR) {
            period = 1L;
        } else if (period < CraftTask.NO_REPEATING) {
            period = CraftTask.NO_REPEATING;
        }
        return this.handle(new CraftAsyncTask(this.asyncScheduler.runners, plugin, runnable, this.nextId(), period), delay); // Paper
    }

    @Override
    public <T> Future<T> callSyncMethod(final Plugin plugin, final Callable<T> task) {
        CraftScheduler.validate(plugin, task);
        final CraftFuture<T> future = new CraftFuture<T>(task, plugin, this.nextId());
        this.handle(future, 0L);
        return future;
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
        CraftTask task = this.runners.get(taskId);
        if (task != null) {
            task.cancel0();
        }
        task = new CraftTask(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!this.check(CraftScheduler.this.temp)) {
                            this.check(CraftScheduler.this.pending);
                        }
                    }
                    private boolean check(final Iterable<CraftTask> collection) {
                        final Iterator<CraftTask> tasks = collection.iterator();
                        while (tasks.hasNext()) {
                            final CraftTask task = tasks.next();
                            if (task.getTaskId() == taskId) {
                                task.cancel0();
                                tasks.remove();
                                if (task.isSync()) {
                                    CraftScheduler.this.runners.remove(taskId);
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                });
        this.handle(task, 0L);
        for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() == taskId) {
                taskPending.cancel0();
            }
        }
    }

    @Override
    public void cancelTasks(final Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Cannot cancel tasks of null plugin");
        // Paper start
        if (!this.isAsyncScheduler) {
            this.asyncScheduler.cancelTasks(plugin);
        }
        // Paper end
        final CraftTask task = new CraftTask(
                new Runnable() {
                    @Override
                    public void run() {
                        this.check(CraftScheduler.this.pending);
                        this.check(CraftScheduler.this.temp);
                    }
                    void check(final Iterable<CraftTask> collection) {
                        final Iterator<CraftTask> tasks = collection.iterator();
                        while (tasks.hasNext()) {
                            final CraftTask task = tasks.next();
                            if (task.getOwner().equals(plugin)) {
                                task.cancel0();
                                tasks.remove();
                                if (task.isSync()) {
                                    CraftScheduler.this.runners.remove(task.getTaskId());
                                }
                            }
                        }
                    }
                });
        this.handle(task, 0L);
        for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                break;
            }
            if (taskPending.getTaskId() != -1 && taskPending.getOwner().equals(plugin)) {
                taskPending.cancel0();
            }
        }
        for (CraftTask runner : this.runners.values()) {
            if (runner.getOwner().equals(plugin)) {
                runner.cancel0();
            }
        }
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
        synchronized (asyncTask.getWorkers()) {
            return !asyncTask.getWorkers().isEmpty();
        }
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
        for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() == taskId) {
                return task.getPeriod() >= CraftTask.NO_REPEATING; // The task will run
            }
        }
        CraftTask task = this.runners.get(taskId);
        return task != null && task.getPeriod() >= CraftTask.NO_REPEATING;
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        // Paper start
        if (!isAsyncScheduler) {
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
            synchronized (task.getWorkers()) {
                // This will never have an issue with stale threads; it's state-safe
                workers.addAll(task.getWorkers());
            }
        }
        return workers;
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        final ArrayList<CraftTask> truePending = new ArrayList<CraftTask>();
        for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() != -1) {
                // -1 is special code
                truePending.add(task);
            }
        }

        final ArrayList<BukkitTask> pending = new ArrayList<BukkitTask>();
        for (CraftTask task : this.runners.values()) {
            if (task.getPeriod() >= CraftTask.NO_REPEATING) {
                pending.add(task);
            }
        }

        for (final CraftTask task : truePending) {
            if (task.getPeriod() >= CraftTask.NO_REPEATING && !pending.contains(task)) {
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
        this.currentTick++;
        // Paper start
        if (!this.isAsyncScheduler) {
            this.asyncScheduler.mainThreadHeartbeat();
        }
        // Paper end
        final List<CraftTask> temp = this.temp;
        this.parsePending();
        while (this.isReady(this.currentTick)) {
            final CraftTask task = this.pending.remove();
            if (task.getPeriod() < CraftTask.NO_REPEATING) {
                if (task.isSync()) {
                    this.runners.remove(task.getTaskId(), task);
                }
                this.parsePending();
                continue;
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
                    org.bukkit.Bukkit.getServer().getPluginManager().callEvent(
                        new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerSchedulerException(logMessage, throwable, task)));
                    // Paper end
                } finally {
                    this.currentTask = null;
                }
                this.parsePending();
            } else {
                // this.debugTail = this.debugTail.setNext(new CraftAsyncDebugger(this.currentTick + CraftScheduler.RECENT_TICKS, task.getOwner(), task.getTaskClass())); // Paper
                task.getOwner().getLogger().log(Level.SEVERE, "Unexpected Async Task in the Sync Scheduler. Report this to Paper"); // Paper
                // We don't need to parse pending
                // (async tasks must live with race-conditions if they attempt to cancel between these few lines of code)
            }
            final long period = task.getPeriod(); // State consistency
            if (period > 0) {
                task.setNextRun(this.currentTick + period);
                temp.add(task);
            } else if (task.isSync()) {
                this.runners.remove(task.getTaskId());
            }
        }
        this.pending.addAll(temp);
        temp.clear();
        //this.debugHead = this.debugHead.getNextHead(this.currentTick); // Paper
    }

    protected void addTask(final CraftTask task) {
        final CraftTask tailTask = this.tail.getAndSet(task);
        tailTask.setNext(task);
    }

    protected CraftTask handle(final CraftTask task, final long delay) { // Paper
        // Paper start
        if (!this.isAsyncScheduler && !task.isSync()) {
            this.asyncScheduler.handle(task, delay);
            return task;
        }
        // Paper end
        task.setNextRun(this.currentTick + delay);
        this.addTask(task);
        return task;
    }

    private static void validate(final Plugin plugin, final Object task) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        Preconditions.checkArgument(task instanceof Runnable || task instanceof Consumer || task instanceof Callable, "Task must be Runnable, Consumer, or Callable");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }
    }

    private int nextId() {
        Preconditions.checkArgument(this.runners.size() < Integer.MAX_VALUE, "There are already %s tasks scheduled! Cannot schedule more", Integer.MAX_VALUE);
        int id;
        do {
            id = this.ids.updateAndGet(CraftScheduler.INCREMENT_IDS);
        } while (this.runners.containsKey(id)); // Avoid generating duplicate IDs
        return id;
    }

    void parsePending() { // Paper
        CraftTask head = this.head;
        CraftTask task = head.getNext();
        CraftTask lastTask = head;
        for (; task != null; task = (lastTask = task).getNext()) {
            if (task.getTaskId() == -1) {
                task.run();
            } else if (task.getPeriod() >= CraftTask.NO_REPEATING) {
                this.pending.add(task);
                this.runners.put(task.getTaskId(), task);
            }
        }
        // We split this because of the way things are ordered for all of the async calls in CraftScheduler
        // (it prevents race-conditions)
        for (task = head; task != lastTask; task = head) {
            head = task.getNext();
            task.setNext(null);
        }
        this.head = lastTask;
    }

    private boolean isReady(final int currentTick) {
        return !this.pending.isEmpty() && this.pending.peek().getNextRun() <= currentTick;
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
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(Plugin, long)");
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
    }

    @Deprecated
    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(Plugin, long, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTask(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskAsynchronously(Plugin)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(Plugin, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLaterAsynchronously(Plugin, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(Plugin, long, long)");
    }

    @Deprecated
    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimerAsynchronously(Plugin, long, long)");
    }

    // Paper start - add getMainThreadExecutor
    @Override
    public Executor getMainThreadExecutor(Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        return command -> {
            Preconditions.checkArgument(command != null, "Command cannot be null");
            this.runTask(plugin, command);
        };
    }
    // Paper end
}
