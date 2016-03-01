package com.destroystokyo.paper.exception;

import org.bukkit.scheduler.BukkitTask;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Thrown when a plugin's scheduler fails with an exception
 */
public class ServerSchedulerException extends ServerPluginException {

    private final BukkitTask task;

    public ServerSchedulerException(String message, Throwable cause, BukkitTask task) {
        super(message, cause, task.getOwner());
        this.task = checkNotNull(task, "task");
    }

    public ServerSchedulerException(Throwable cause, BukkitTask task) {
        super(cause, task.getOwner());
        this.task = checkNotNull(task, "task");
    }

    protected ServerSchedulerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, BukkitTask task) {
        super(message, cause, enableSuppression, writableStackTrace, task.getOwner());
        this.task = checkNotNull(task, "task");
    }

    /**
     * Gets the task which threw the exception
     *
     * @return exception throwing task
     */
    public BukkitTask getTask() {
        return task;
    }
}
