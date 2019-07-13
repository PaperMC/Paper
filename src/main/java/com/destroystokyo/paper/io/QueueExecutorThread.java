package com.destroystokyo.paper.io;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class QueueExecutorThread<T extends PrioritizedTaskQueue.PrioritizedTask & Runnable> extends Thread {

    private static final Logger LOGGER = MinecraftServer.LOGGER;

    protected final PrioritizedTaskQueue<T> queue;
    protected final long spinWaitTime;

    protected volatile boolean closed;

    protected final AtomicBoolean parked = new AtomicBoolean();

    protected volatile ConcurrentLinkedQueue<Thread> flushQueue = new ConcurrentLinkedQueue<>();
    protected volatile long flushCycles;

    public QueueExecutorThread(final PrioritizedTaskQueue<T> queue) {
        this(queue, (int)(1.e6)); // 1.0ms
    }

    public QueueExecutorThread(final PrioritizedTaskQueue<T> queue, final long spinWaitTime) { // in ms
        this.queue = queue;
        this.spinWaitTime = spinWaitTime;
    }

    @Override
    public void run() {
        final long spinWaitTime = this.spinWaitTime;
        main_loop:
        for (;;) {
            this.pollTasks(true);

            // spinwait

            final long start = System.nanoTime();

            for (;;) {
                // If we are interrpted for any reason, park() will always return immediately. Clear so that we don't needlessly use cpu in such an event.
                Thread.interrupted();
                LockSupport.parkNanos("Spinwaiting on tasks", 1000L); // 1us

                if (this.pollTasks(true)) {
                    // restart loop, found tasks
                    continue main_loop;
                }

                if (this.handleClose()) {
                    return; // we're done
                }

                if ((System.nanoTime() - start) >= spinWaitTime) {
                    break;
                }
            }

            if (this.handleClose()) {
                return;
            }

            this.parked.set(true);

            // We need to parse here to avoid a race condition where a thread queues a task before we set parked to true
            // (i.e it will not notify us)
            if (this.pollTasks(true)) {
                this.parked.set(false);
                continue;
            }

            if (this.handleClose()) {
                return;
            }

            // we don't need to check parked before sleeping, but we do need to check parked in a do-while loop
            // LockSupport.park() can fail for any reason
            do {
                Thread.interrupted();
                LockSupport.park("Waiting on tasks");
            } while (this.parked.get());
        }
    }

    protected boolean handleClose() {
        if (this.closed) {
            this.pollTasks(true); // this ensures we've emptied the queue
            this.handleFlushThreads(true);
            return true;
        }
        return false;
    }

    protected boolean pollTasks(boolean flushTasks) {
        Runnable task;
        boolean ret = false;

        while ((task = this.queue.poll()) != null) {
            ret = true;
            try {
                task.run();
            } catch (final Throwable throwable) {
                if (throwable instanceof ThreadDeath) {
                    throw (ThreadDeath)throwable;
                }
                LOGGER.fatal("Exception thrown from prioritized runnable task in thread '" + this.getName() + "': " + IOUtil.genericToString(task), throwable);
            }
        }

        if (flushTasks) {
            this.handleFlushThreads(false);
        }

        return ret;
    }

    protected void handleFlushThreads(final boolean shutdown) {
        Thread parking;
        ConcurrentLinkedQueue<Thread> flushQueue = this.flushQueue;
        do {
            ++flushCycles; // may be plain read opaque write
            while ((parking = flushQueue.poll()) != null) {
                LockSupport.unpark(parking);
            }
        } while (this.pollTasks(false));

        if (shutdown) {
            this.flushQueue = null;

            // defend against a race condition where a flush thread double-checks right before we set to null
            while ((parking = flushQueue.poll()) != null) {
                LockSupport.unpark(parking);
            }
        }
    }

    /**
     * Notify's this thread that a task has been added to its queue
     * @return {@code true} if this thread was waiting for tasks, {@code false} if it is executing tasks
     */
    public boolean notifyTasks() {
        if (this.parked.get() && this.parked.getAndSet(false)) {
            LockSupport.unpark(this);
            return true;
        }
        return false;
    }

    protected void queueTask(final T task) {
        this.queue.add(task);
        this.notifyTasks();
    }

    /**
     * Waits until this thread's queue is empty.
     *
     * @throws IllegalStateException If the current thread is {@code this} thread.
     */
    public void flush() {
        final Thread currentThread = Thread.currentThread();

        if (currentThread == this) {
            // avoid deadlock
            throw new IllegalStateException("Cannot flush the queue executor thread while on the queue executor thread");
        }

        // order is important

        int successes = 0;
        long lastCycle = -1L;

        do {
            final ConcurrentLinkedQueue<Thread> flushQueue = this.flushQueue;
            if (flushQueue == null) {
                return;
            }

            flushQueue.add(currentThread);

            // double check flush queue
            if (this.flushQueue == null) {
                return;
            }

            final long currentCycle = this.flushCycles; // may be opaque read

            if (currentCycle == lastCycle) {
                Thread.yield();
                continue;
            }

            // force response
            this.parked.set(false);
            LockSupport.unpark(this);

            LockSupport.park("flushing queue executor thread");

            // returns whether there are tasks queued, does not return whether there are tasks executing
            // this is why we cycle twice twice through flush (we know a pollTask call is made after a flush cycle)
            // we really only need to guarantee that the tasks this thread has queued has gone through, and can leave
            // tasks queued concurrently that are unsychronized with this thread as undefined behavior
            if (this.queue.hasTasks()) {
                successes = 0;
            } else {
                ++successes;
            }

        } while (successes != 2);

    }

    /**
     * Closes this queue executor's queue and optionally waits for it to empty.
     * <p>
     *     If wait is {@code true}, then the queue will be empty by the time this call completes.
     * </p>
     * <p>
     *     This function is MT-Safe.
     * </p>
     * @param wait If this call is to wait until the queue is empty
     * @param killQueue Whether to shutdown this thread's queue
     * @return whether this thread shut down the queue
     */
    public boolean close(final boolean wait, final boolean killQueue) {
        boolean ret = !killQueue ? false : this.queue.shutdown();
        this.closed = true;

        // force thread to respond to the shutdown
        this.parked.set(false);
        LockSupport.unpark(this);

        if (wait) {
            this.flush();
        }
        return ret;
    }
}
