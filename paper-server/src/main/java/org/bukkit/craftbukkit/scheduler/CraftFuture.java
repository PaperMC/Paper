package org.bukkit.craftbukkit.scheduler;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public final class CraftFuture<T> extends CraftTask implements Future<T> {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Callable<T> callable;
    private T value;
    private Exception exception = null;

    CraftFuture(final Callable<T> callable, final Plugin plugin, final long id) {
        super(plugin, a -> {}, id, NO_REPEATING);
        this.callable = callable;
    }

    @Override
    public void run() {
        if (getState() == NO_REPEATING && casState(NO_REPEATING, COMPLETING)) {
            try {
                this.value = this.callable.call();
            } catch (final Exception ex) {
                this.exception = ex;
            } finally {
                callable = null; // free
                lock.lock();
                try {
                    setState(DONE);
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (getState() == NO_REPEATING && casState(NO_REPEATING, CANCEL)) {
            lock.lock();
            try {
                condition.signalAll();
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean tryCancel() {
        return cancel(true);
    }

    @Override
    public boolean isDone() {
        return getState() <= DONE;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(Long.MAX_VALUE, NANOSECONDS);
        } catch (final TimeoutException ex) {
            throw new Error(ex);
        }
    }

    @Override
    public T get(final long timeout, @NotNull final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long nanos = unit.toNanos(timeout);
        long state = getState();
        if (state > DONE) {
            lock.lock();
            try {
                while ((state = getState()) > DONE) {
                    if (nanos <= 0L) {
                        throw new TimeoutException();
                    }
                    nanos = condition.awaitNanos(nanos);
                }
            } finally {
                lock.unlock();
            }
        }
        if (state == CANCEL) {
            throw new CancellationException();
        } else if (state == DONE) {
            if (this.exception == null) {
                return this.value;
            }
            throw new ExecutionException(this.exception);
        }
        throw new IllegalStateException("Expected " + NO_REPEATING + " to " + DONE + ", got " + state);
    }
}
