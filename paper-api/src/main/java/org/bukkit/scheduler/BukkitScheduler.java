package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.List;

public interface BukkitScheduler {

    /**
     * Schedules a once off task to occur after a delay
     * This task will be executed by the main server thread
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay);

    /**
     * Schedules a once off task to occur as soon as possible
     * This task will be executed by the main server thread
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task);

    /**
     * Schedules a repeating task
     * This task will be executed by the main server thread
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

    /**
     * Schedules a once off task to occur after a delay
     * This task will be executed by a thread managed by the scheduler
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay);

    /**
     * Schedules a once off task to occur as soon as possible
     * This task will be executed by a thread managed by the scheduler
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task);

    /**
     * Schedules a repeating task
     * This task will be executed by a thread managed by the scheduler
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

    /**
     * Calls a method on the main thread and returns a Future object
     * This task will be executed by the main server thread
     * <p />
     * Note: The Future.get() methods must NOT be called from the main thread
     * Note2: There is at least an average of 10ms latency until the isDone() method returns true
     *
     * @param <T> The callable's return type
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Future Future object related to the task
     */
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task);

    /**
     * Removes task from scheduler
     *
     * @param taskId Id number of task to be removed
     */
    public void cancelTask(int taskId);

    /**
     * Removes all tasks associated with a particular plugin from the scheduler
     *
     * @param plugin Owner of tasks to be removed
     */
    public void cancelTasks(Plugin plugin);

    /**
     * Removes all tasks from the scheduler
     */
    public void cancelAllTasks();

    /**
     * Check if the task currently running.
     * <p />
     * A repeating task might not be running currently, but will be running in the future.
     * A task that has finished, and does not repeat, will not be running ever again.
     * <p />
     * Explicitly, a task is running if there exists a thread for it, and that thread is alive.
     *
     * @param taskId The task to check.
     * <p />
     * @return If the task is currently running.
     */
    public boolean isCurrentlyRunning(int taskId);

    /**
     * Check if the task queued to be run later.
     * <p />
     * If a repeating task is currently running, it might not be queued now but could be in the future.
     * A task that is not queued, and not running, will not be queued again.
     *
     * @param taskId The task to check.
     * <p />
     * @return If the task is queued to be run.
     */
    public boolean isQueued(int taskId);

    /**
     * Returns a list of all active workers.
     * <p />
     * This list contains asynch tasks that are being executed by separate threads.
     *
     * @return Active workers
     */
    public List<BukkitWorker> getActiveWorkers();

    /**
     * Returns a list of all pending tasks. The ordering of the tasks is not related to their order of execution.
     *
     * @return Active workers
     */
    public List<BukkitTask> getPendingTasks();

}
