package org.bukkit.craftbukkit.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CraftFuture<T> implements Runnable, Future<T> {

    private final CraftScheduler craftScheduler;
    private final Callable<T> callable;
    private final ObjectContainer<T> returnStore = new ObjectContainer<T>();
    private boolean done = false;
    private boolean running = false;
    private boolean cancelled = false;
    private Exception e = null;
    private int taskId = -1;

    CraftFuture(CraftScheduler craftScheduler, Callable<T> callable) {
        this.callable = callable;
        this.craftScheduler = craftScheduler;
    }

    public void run() {
        synchronized (this) {
            if (cancelled) {
                return;
            }
            running = true;
        }
        try {
            returnStore.setObject(callable.call());
        } catch (Exception e) {
            this.e = e;
        }
        synchronized (this) {
            running = false;
            done = true;
            this.notify();
        }
    }

    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(0L, TimeUnit.MILLISECONDS);
        } catch (TimeoutException te) {}
        return null;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (this) {
            if (isDone()) {
                return getResult();
            }
            this.wait(TimeUnit.MILLISECONDS.convert(timeout, unit));
            return getResult();
        }
    }

    public T getResult() throws ExecutionException {
        if (cancelled) {
            throw new CancellationException();
        }
        if (e != null) {
            throw new ExecutionException(e);
        }
        return returnStore.getObject();
    }

    public boolean isDone() {
        synchronized (this) {
            return done;
        }
    }

    public boolean isCancelled() {
        synchronized (this) {
            return cancelled;
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (cancelled) {
                return false;
            }
            cancelled = true;
            if (taskId != -1) {
                craftScheduler.cancelTask(taskId);
            }
            if (!running && !done) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void setTaskId(int taskId) {
        synchronized (this) {
            this.taskId = taskId;
        }
    }
}
