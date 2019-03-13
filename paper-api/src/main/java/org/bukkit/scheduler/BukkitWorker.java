package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a worker thread for the scheduler. This gives information about
 * the Thread object for the task, owner of the task and the taskId.
 * <p>
 * Workers are used to execute async tasks.
 */
public interface BukkitWorker {

    /**
     * Returns the taskId for the task being executed by this worker.
     *
     * @return Task id number
     */
    public int getTaskId();

    /**
     * Returns the Plugin that owns this task.
     *
     * @return The Plugin that owns the task
     */
    @NotNull
    public Plugin getOwner();

    /**
     * Returns the thread for the worker.
     *
     * @return The Thread object for the worker
     */
    @NotNull
    public Thread getThread();

}
