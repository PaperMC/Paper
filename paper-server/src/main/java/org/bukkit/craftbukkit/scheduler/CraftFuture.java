package org.bukkit.craftbukkit.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.bukkit.plugin.Plugin;

class CraftFuture<T> extends CraftTask implements Future<T> {

    private final Callable<T> callable;
    private T value;
    private Exception exception = null;

    CraftFuture(final Callable<T> callable, final Plugin plugin, final long id) {
        super(plugin, a -> {}, id, CraftTask.NO_REPEATING);
        this.callable = callable;
    }

    @Override
    public synchronized boolean cancel(final boolean mayInterruptIfRunning) {
        if (this.getPeriod() != CraftTask.NO_REPEATING) {
            return false;
        }
        this.setPeriod(CraftTask.CANCEL);
        return true;
    }

    @Override
    public boolean isDone() {
        final long period = this.getPeriod();
        return period != CraftTask.NO_REPEATING && period != CraftTask.PROCESS_FOR_FUTURE;
    }

    @Override
    public T get() throws CancellationException, InterruptedException, ExecutionException {
        try {
            return this.get(0, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            throw new Error(e);
        }
    }

    @Override
    public synchronized T get(long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        timeout = unit.toMillis(timeout);
        long period = this.getPeriod();
        long timestamp = timeout > 0 ? System.currentTimeMillis() : 0L;
        while (true) {
            if (period == CraftTask.NO_REPEATING || period == CraftTask.PROCESS_FOR_FUTURE) {
                this.wait(timeout);
                period = this.getPeriod();
                if (period == CraftTask.NO_REPEATING || period == CraftTask.PROCESS_FOR_FUTURE) {
                    if (timeout == 0L) {
                        continue;
                    }
                    timeout += timestamp - (timestamp = System.currentTimeMillis());
                    if (timeout > 0) {
                        continue;
                    }
                    throw new TimeoutException();
                }
            }
            if (period == CraftTask.CANCEL) {
                throw new CancellationException();
            }
            if (period == CraftTask.DONE_FOR_FUTURE) {
                if (this.exception == null) {
                    return this.value;
                }
                throw new ExecutionException(this.exception);
            }
            throw new IllegalStateException("Expected " + CraftTask.NO_REPEATING + " to " + CraftTask.DONE_FOR_FUTURE + ", got " + period);
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            if (this.getPeriod() == CraftTask.CANCEL) {
                return;
            }
            this.setPeriod(CraftTask.PROCESS_FOR_FUTURE);
        }
        try {
            this.value = this.callable.call();
        } catch (final Exception e) {
            this.exception = e;
        } finally {
            synchronized (this) {
                this.setPeriod(CraftTask.DONE_FOR_FUTURE);
                this.notifyAll();
            }
        }
    }

    @Override
    synchronized boolean cancel0() {
        if (this.getPeriod() != CraftTask.NO_REPEATING) {
            return false;
        }
        this.setPeriod(CraftTask.CANCEL);
        this.notifyAll();
        return true;
    }
}
