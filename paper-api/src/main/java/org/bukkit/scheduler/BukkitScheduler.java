package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;

public interface BukkitScheduler {

    /**
     * Schedules a once off task to occur after a delay
     * This task will be executed by the main server thread
     *
     * @param Plugin Plugin that owns the task
     * @param Runnable Task to be executed
     * @param long Delay in server ticks before executing task
     * @return int Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay);

    /**
     * Schedules a once off task to occur as soon as possible
     * This task will be executed by the main server thread
     *
     * @param Plugin Plugin that owns the task
     * @param Runnable Task to be executed 
     * @return int Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task);

    /**
     * Schedules a repeating task
     * This task will be executed by the main server thread
     *
     * @param Plugin Plugin that owns the task
     * @param Runnable Task to be executed 
     * @param long Delay in server ticks before executing first repeat
     * @param long Period in server ticks of the task
     * @return int Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

    /**
     * Schedules a once off task to occur after a delay
     * This task will be executed by a thread managed by the scheduler
     *
     * @param Plugin Plugin that owns the task
     * @param Runnable Task to be executed
     * @param long Delay in server ticks before executing task
     * @return int Task id number (-1 if scheduling failed)
     */
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay);

    /**
     * Schedules a once off task to occur as soon as possible
     * This task will be executed by a thread managed by the scheduler
     *
     * @param Plugin Plugin that owns the task
     * @param Runnable Task to be executed
     * @return int Task id number (-1 if scheduling failed)
     */
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task);

    /**
     * Schedules a repeating task
     * This task will be executed by a thread managed by the scheduler
     *
     * @param Plugin Plugin that owns the task
     * @param Runnable Task to be executed
     * @param long Delay in server ticks before executing first repeat
     * @param long Period in server ticks of the task
     * @return int Task id number (-1 if scheduling failed)
     */
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

    /**
     * Removes task from scheduler
     *
     * @param int Id number of task to be removed
     */
    public void cancelTask(int taskId);

    /**
     * Removes all tasks associated with a particular plugin from the scheduler
     *
     * @param Plugin Owner of tasks to be removed
     */
    public void cancelTasks(Plugin plugin);

    /**
     * Removes all tasks from the scheduler
     */
    public void cancelAllTasks();

}
