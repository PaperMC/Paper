package org.bukkit.craftbukkit.util;

import java.util.concurrent.ExecutionException;


public abstract class Waitable<T> implements Runnable {
    private enum Status {
        WAITING,
        RUNNING,
        FINISHED,
    }
    Throwable t = null;
    T value = null;
    Status status = Status.WAITING;

    public final void run() {
        synchronized (this) {
            if (status != Status.WAITING) {
                throw new IllegalStateException("Invalid state " + status);
            }
            status = Status.RUNNING;
        }
        try {
            value = evaluate();
        } catch (Throwable t) {
            this.t = t;
        } finally {
            synchronized (this) {
                status = Status.FINISHED;
                this.notifyAll();
            }
        }
    }

    protected abstract T evaluate();

    public synchronized T get() throws InterruptedException, ExecutionException {
        while (status != Status.FINISHED) {
            this.wait();
        }
        if (t != null) {
            throw new ExecutionException(t);
        }
        return value;
    }
}
