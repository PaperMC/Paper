package org.bukkit.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface BukkitScheduler {

    /**
     * Schedules a once off task to occur after a delay.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(@NotNull Plugin plugin, @NotNull Runnable task, long delay);

    /**
     * Schedules a once off task to occur after a delay.
     * <p>
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in {@link java.time.Duration} before executing task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(@NotNull Plugin plugin, @NotNull Runnable task, @NotNull Duration delay);

    /**
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing task
     * @return Task id number (-1 if scheduling failed)
     * @deprecated Use {@link BukkitRunnable#runTaskLater(Plugin, long)}
     */
    @Deprecated(since = "1.7.10")
    public int scheduleSyncDelayedTask(@NotNull Plugin plugin, @NotNull BukkitRunnable task, long delay);

    /**
     * Schedules a once off task to occur as soon as possible.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(@NotNull Plugin plugin, @NotNull Runnable task);

    /**
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     * @deprecated Use {@link BukkitRunnable#runTask(Plugin)}
     */
    @Deprecated(since = "1.7.10")
    public int scheduleSyncDelayedTask(@NotNull Plugin plugin, @NotNull BukkitRunnable task);

    /**
     * Schedules a repeating task.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncRepeatingTask(@NotNull Plugin plugin, @NotNull Runnable task, long delay, long period);

    /**
     * Schedules a repeating task.
     * <p>
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in {@link java.time.Duration} before executing first repeat
     * @param period Period in {@link java.time.Duration} of the task execution
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncRepeatingTask(@NotNull Plugin plugin, @NotNull Runnable task, @NotNull Duration delay, @NotNull Duration period);

    /**
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     * @deprecated Use {@link BukkitRunnable#runTaskTimer(Plugin, long, long)}
     */
    @Deprecated(since = "1.7.10")
    public int scheduleSyncRepeatingTask(@NotNull Plugin plugin, @NotNull BukkitRunnable task, long delay, long period);

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules a once off task to occur after a delay. This task will be
     * executed by a thread managed by the scheduler.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing task
     * @return Task id number (-1 if scheduling failed)
     * @deprecated This name is misleading, as it does not schedule "a sync"
     *     task, but rather, "an async" task
     */
    @Deprecated(since = "1.4.5")
    public int scheduleAsyncDelayedTask(@NotNull Plugin plugin, @NotNull Runnable task, long delay);

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules a once off task to occur as soon as possible. This task will
     * be executed by a thread managed by the scheduler.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     * @deprecated This name is misleading, as it does not schedule "a sync"
     *     task, but rather, "an async" task
     */
    @Deprecated(since = "1.4.5")
    public int scheduleAsyncDelayedTask(@NotNull Plugin plugin, @NotNull Runnable task);

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules a repeating task. This task will be executed by a thread
     * managed by the scheduler.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     * @deprecated This name is misleading, as it does not schedule "a sync"
     *     task, but rather, "an async" task
     */
    @Deprecated(since = "1.4.5")
    public int scheduleAsyncRepeatingTask(@NotNull Plugin plugin, @NotNull Runnable task, long delay, long period);

    /**
     * Calls a method on the main thread and returns a Future object. This
     * task will be executed by the main server thread.
     * <ul>
     * <li>Note: The Future.get() methods must NOT be called from the main
     *     thread.
     * <li>Note2: There is at least an average of 10ms latency until the
     *     isDone() method returns true.
     * </ul>
     * @param <T> The callable's return type
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Future Future object related to the task
     */
    @NotNull
    public <T> Future<T> callSyncMethod(@NotNull Plugin plugin, @NotNull Callable<T> task);

    /**
     * Removes task from scheduler.
     *
     * @param taskId Id number of task to be removed
     */
    public void cancelTask(int taskId);

    /**
     * Removes all tasks associated with a particular plugin from the
     * scheduler.
     *
     * @param plugin Owner of tasks to be removed
     */
    public void cancelTasks(@NotNull Plugin plugin);

    /**
     * Check if the task currently running.
     * <p>
     * A repeating task might not be running currently, but will be running in
     * the future. A task that has finished, and does not repeat, will not be
     * running ever again.
     * <p>
     * Explicitly, a task is running if there exists a thread for it, and that
     * thread is alive.
     *
     * @param taskId The task to check.
     * <p>
     * @return If the task is currently running.
     */
    public boolean isCurrentlyRunning(int taskId);

    /**
     * Check if the task queued to be run later.
     * <p>
     * If a repeating task is currently running, it might not be queued now
     * but could be in the future. A task that is not queued, and not running,
     * will not be queued again.
     *
     * @param taskId The task to check.
     * <p>
     * @return If the task is queued to be run.
     */
    public boolean isQueued(int taskId);

    /**
     * Returns a list of all active workers.
     * <p>
     * This list contains asynch tasks that are being executed by separate
     * threads.
     *
     * @return Active workers
     */
    @NotNull
    public List<BukkitWorker> getActiveWorkers();

    /**
     * Returns a list of all pending tasks. The ordering of the tasks is not
     * related to their order of execution.
     *
     * @return Active workers
     */
    @NotNull
    public List<BukkitTask> getPendingTasks();

    /**
     * Returns a task that will run on the next server tick.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    @NotNull
    public BukkitTask runTask(@NotNull Plugin plugin, @NotNull Runnable task) throws IllegalArgumentException;

    /**
     * Returns a task that will run on the next server tick.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public void runTask(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task) throws IllegalArgumentException;

    /**
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @deprecated Use {@link BukkitRunnable#runTask(Plugin)}
     */
    @Deprecated(since = "1.7.10")
    @NotNull
    public BukkitTask runTask(@NotNull Plugin plugin, @NotNull BukkitRunnable task) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    @NotNull
    public BukkitTask runTaskAsynchronously(@NotNull Plugin plugin, @NotNull Runnable task) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public void runTaskAsynchronously(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task) throws IllegalArgumentException;

    /**
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @deprecated Use {@link BukkitRunnable#runTaskAsynchronously(Plugin)}
     */
    @Deprecated(since = "1.7.10")
    @NotNull
    public BukkitTask runTaskAsynchronously(@NotNull Plugin plugin, @NotNull BukkitRunnable task) throws IllegalArgumentException;

    /**
     * Returns a task that will run after the specified number of server
     * ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    @NotNull
    public BukkitTask runTaskLater(@NotNull Plugin plugin, @NotNull Runnable task, long delay) throws IllegalArgumentException;

    @NotNull
    public BukkitTask runTaskLater(@NotNull Plugin plugin, @NotNull Runnable task, @NotNull Duration delay) throws IllegalArgumentException;

    /**
     * Returns a task that will run after the specified number of server
     * ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public void runTaskLater(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException;

    /**
     * Returns a task that will run after the specified duration.
     * <p>
     * The time given in {@link java.time.Duration} is converted to number of ticks.
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay duration to wait before running the task
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @throws IllegalArgumentException if delay is null
     */
    public void runTaskLater(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull Duration delay) throws IllegalArgumentException;

    /**
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @deprecated Use {@link BukkitRunnable#runTaskLater(Plugin, long)}
     */
    @Deprecated(since = "1.7.10")
    @NotNull
    public BukkitTask runTaskLater(@NotNull Plugin plugin, @NotNull BukkitRunnable task, long delay) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously after the specified number
     * of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    @NotNull
    public BukkitTask runTaskLaterAsynchronously(@NotNull Plugin plugin, @NotNull Runnable task, long delay) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously after the specified duration.
     * <p>
     * The time given in {@link java.time.Duration} is converted to number of ticks.
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the duration to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @throws IllegalArgumentException if delay is null
     */
    @NotNull
    public BukkitTask runTaskLaterAsynchronously(@NotNull Plugin plugin, @NotNull Runnable task, @NotNull Duration delay) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously after the specified number
     * of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public void runTaskLaterAsynchronously(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously after the specified duration.
     * <p>
     * The time given in {@link java.time.Duration} is converted to number of ticks.
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the duration to wait before running the task
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @throws IllegalArgumentException if delay is null
     */
    public void runTaskLaterAsynchronously(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull Duration delay) throws IllegalArgumentException;

    /**
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @deprecated Use {@link BukkitRunnable#runTaskLaterAsynchronously(Plugin, long)}
     */
    @Deprecated(since = "1.7.10")
    @NotNull
    public BukkitTask runTaskLaterAsynchronously(@NotNull Plugin plugin, @NotNull BukkitRunnable task, long delay) throws IllegalArgumentException;

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    @NotNull
    public BukkitTask runTaskTimer(@NotNull Plugin plugin, @NotNull Runnable task, long delay, long period) throws IllegalArgumentException;

    /**
     * Returns a task that will repeatedly run until cancelled,
     * starting after the specified duration.
     * <p>
     * The time given in {@link java.time.Duration} is converted to number of ticks.
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the duration to wait before running the task
     * @param period the duration to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @throws IllegalArgumentException if delay is null
     * @throws IllegalArgumentException if period is null
     */
    @NotNull
    public BukkitTask runTaskTimer(@NotNull Plugin plugin, @NotNull Runnable task, @NotNull Duration delay, @NotNull Duration period) throws IllegalArgumentException;

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public void runTaskTimer(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException;

    /**
     * Returns a task that will repeatedly run until cancelled,
     * starting after the specified duration.
     * <p>
     * The time given in {@link java.time.Duration} is converted to number of ticks.
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the duration to wait before running the task
     * @param period the duration to wait between runs
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @throws IllegalArgumentException if delay is null
     * @throws IllegalArgumentException if period is null
     */
    public void runTaskTimer(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull Duration delay, @NotNull Duration period) throws IllegalArgumentException;

    /**
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @deprecated Use {@link BukkitRunnable#runTaskTimer(Plugin, long, long)}
     */
    @Deprecated(since = "1.7.10")
    @NotNull
    public BukkitTask runTaskTimer(@NotNull Plugin plugin, @NotNull BukkitRunnable task, long delay, long period) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task for the first
     *     time
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    @NotNull
    public BukkitTask runTaskTimerAsynchronously(@NotNull Plugin plugin, @NotNull Runnable task, long delay, long period) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified duration.
     * <p>
     * The time given in {@link java.time.Duration} is converted to number of ticks.
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the duration to wait before running the task for the
     *     first time
     * @param period the duration to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @throws IllegalArgumentException if delay is null
     * @throws IllegalArgumentException if period is null
     */
    @NotNull
    public BukkitTask runTaskTimerAsynchronously(@NotNull Plugin plugin, @NotNull Runnable task, @NotNull Duration delay, @NotNull Duration period) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task for the first
     *     time
     * @param period the ticks to wait between runs
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public void runTaskTimerAsynchronously(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified duration.
     * <p>
     * The time given in {@link java.time.Duration} is converted to number of ticks.
     * Note that the smallest unit for {@link org.bukkit.scheduler.BukkitScheduler} is still 1 tick (50ms).
     * Values smaller than 50ms will be rounded down.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the duration to wait before running the task for the
     *     first time
     * @param period the duration to wait between runs
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @throws IllegalArgumentException if delay is null
     * @throws IllegalArgumentException if period is null
     */
    public void runTaskTimerAsynchronously(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull Duration delay, @NotNull Duration period) throws IllegalArgumentException;

    /**
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task for the first
     *     time
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     * @deprecated Use {@link BukkitRunnable#runTaskTimerAsynchronously(Plugin, long, long)}
     */
    @Deprecated(since = "1.7.10")
    @NotNull
    public BukkitTask runTaskTimerAsynchronously(@NotNull Plugin plugin, @NotNull BukkitRunnable task, long delay, long period) throws IllegalArgumentException;

    /**
     * Returns a task that will run at a specified date time.
     * <p>
     * The LocalDateTime used is the local time, appropriate
     * time zone adjustments are made automatically.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param runnable the task to be run
     * @param localDateTime the time to run this task at
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if runnable is null
     * @throws IllegalArgumentException if localDateTime is nll
     */
    @NotNull
    public BukkitTask runTaskAtTime(@NotNull Plugin plugin, @NotNull Runnable runnable, @NotNull LocalDateTime localDateTime) throws IllegalArgumentException;

    /**
     * Returns a task that will run at a specified date time.
     * <p>
     * The LocalDateTime used is the local time, appropriate
     * time zone adjustments are made automatically.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param localDateTime the time to run this task at
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if runnable is null
     * @throws IllegalArgumentException if localDateTime is nll
     */
    @NotNull
    public void runTaskAtTime(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull LocalDateTime localDateTime) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously
     * at a specified date time.
     * <p>
     * The LocalDateTime used is the local time, appropriate
     * time zone adjustments are made automatically.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param runnable the task to be run
     * @param localDateTime the time to run this task at
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if runnable is null
     * @throws IllegalArgumentException if localDateTime is nll
     */
    @NotNull
    public BukkitTask runTaskAtTimeAsynchronously(@NotNull Plugin plugin, @NotNull Runnable runnable, @NotNull LocalDateTime localDateTime) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously
     * at a specified date time.
     * <p>
     * The LocalDateTime used is the local time, appropriate
     * time zone adjustments are made automatically.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param localDateTime the time to run this task at
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if runnable is null
     * @throws IllegalArgumentException if localDateTime is nll
     */
    @NotNull
    public void runTaskAtTimeAsynchronously(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull LocalDateTime localDateTime) throws IllegalArgumentException;

    /**
     * Returns a task that will run every day at
     * a specified time (hour, minute and second).
     * <p>
     * The LocalTime used is the local time, appropriate
     * time zone adjustments are made automatically.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param runnable the task to be run
     * @param localTime the time to run this task at
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if runnable is null
     * @throws IllegalArgumentException if LocalTime is nll
     */
    @NotNull
    public BukkitTask runRepeatedTaskAtTime(@NotNull Plugin plugin, @NotNull Runnable runnable, @NotNull LocalTime localTime) throws IllegalArgumentException;

    /**
     * Returns a task that will run every day at
     * a specified time (hour, minute and second).
     * <p>
     * The LocalTime used is the local time, appropriate
     * time zone adjustments are made automatically.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param localTime the time to run this task at
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if runnable is null
     * @throws IllegalArgumentException if LocalTime is nll
     */
    @NotNull
    public void runRepeatedTaskAtTime(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull LocalTime localTime) throws IllegalArgumentException;

    /*
    @NotNull
    public BukkitTask runRepeatedTaskAtTimeAsynchronously(@NotNull Plugin plugin, @NotNull Runnable runnable, @NotNull LocalTime localTime) throws IllegalArgumentException;

    @NotNull
    public void runRepeatedTaskAtTimeAsynchronously(@NotNull Plugin plugin, @NotNull Consumer<? super BukkitTask> task, @NotNull LocalTime localTime) throws IllegalArgumentException;
    */

    // Paper start - add getMainThreadExecutor
    /**
     * Returns an executor that will run tasks on the next server tick.
     *
     * @param plugin the reference to the plugin scheduling tasks
     * @return an executor associated with the given plugin
     */
    @NotNull
    public java.util.concurrent.Executor getMainThreadExecutor(@NotNull Plugin plugin);
    // Paper end
}
