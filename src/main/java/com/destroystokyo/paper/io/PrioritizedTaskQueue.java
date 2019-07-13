package com.destroystokyo.paper.io;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PrioritizedTaskQueue<T extends PrioritizedTaskQueue.PrioritizedTask> {

    // lower numbers are a higher priority (except < 0)
    // higher priorities are always executed before lower priorities

    /**
     * Priority value indicating the task has completed or is being completed.
     */
    public static final int COMPLETING_PRIORITY   = -1;

    /**
     * Highest priority, should only be used for main thread tasks or tasks that are blocking the main thread.
     */
    public static final int HIGHEST_PRIORITY      = 0;

    /**
     * Should be only used in an IO task so that chunk loads do not wait on other IO tasks.
     * This only exists because IO tasks are scheduled before chunk load tasks to decrease IO waiting times.
     */
    public static final int HIGHER_PRIORITY       = 1;

    /**
     * Should be used for scheduling chunk loads/generation that would increase response times to users.
     */
    public static final int HIGH_PRIORITY         = 2;

    /**
     * Default priority.
     */
    public static final int NORMAL_PRIORITY       = 3;

    /**
     * Use for tasks not at all critical and can potentially be delayed.
     */
    public static final int LOW_PRIORITY          = 4;

    /**
     * Use for tasks that should "eventually" execute.
     */
    public static final int LOWEST_PRIORITY       = 5;

    private static final int TOTAL_PRIORITIES     = 6;

    final ConcurrentLinkedQueue<T>[] queues = (ConcurrentLinkedQueue<T>[])new ConcurrentLinkedQueue[TOTAL_PRIORITIES];

    private final AtomicBoolean shutdown = new AtomicBoolean();

    {
        for (int i = 0; i < TOTAL_PRIORITIES; ++i) {
            this.queues[i] = new ConcurrentLinkedQueue<>();
        }
    }

    /**
     * Returns whether the specified priority is valid
     */
    public static boolean validPriority(final int priority) {
        return priority >= 0 && priority < TOTAL_PRIORITIES;
    }

    /**
     * Queues a task.
     * @throws IllegalStateException If the task has already been queued. Use {@link PrioritizedTask#raisePriority(int)} to
     *                               raise a task's priority.
     *                               This can also be thrown if the queue has shutdown.
     */
    public void add(final T task) throws IllegalStateException {
        int priority = task.getPriority();
        if (priority != COMPLETING_PRIORITY) {
            task.setQueue(this);
            this.queues[priority].add(task);
        }
        if (this.shutdown.get()) {
            // note: we're not actually sure at this point if our task will go through
            throw new IllegalStateException("Queue has shutdown, refusing to execute task " + IOUtil.genericToString(task));
        }
    }

    /**
     * Polls the highest priority task currently available. {@code null} if none.
     */
    public T poll() {
        T task;
        for (int i = 0; i < TOTAL_PRIORITIES; ++i) {
            final ConcurrentLinkedQueue<T> queue = this.queues[i];

            while ((task = queue.poll()) != null) {
                final int prevPriority = task.tryComplete(i);
                if (prevPriority != COMPLETING_PRIORITY && prevPriority <= i) {
                    // if the prev priority was greater-than or equal to our current priority
                    return task;
                }
            }
        }

        return null;
    }

    /**
     * Returns whether this queue may have tasks queued.
     * <p>
     * This operation is not atomic, but is MT-Safe.
     * </p>
     * @return {@code true} if tasks may be queued, {@code false} otherwise
     */
    public boolean hasTasks() {
        for (int i = 0; i < TOTAL_PRIORITIES; ++i) {
            final ConcurrentLinkedQueue<T> queue = this.queues[i];

            if (queue.peek() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prevent further additions to this queue. Attempts to add after this call has completed (potentially during) will
     * result in {@link IllegalStateException} being thrown.
     * <p>
     *     This operation is atomic with respect to other shutdown calls
     * </p>
     * <p>
     *     After this call has completed, regardless of return value, this queue will be shutdown.
     * </p>
     * @return {@code true} if the queue was shutdown, {@code false} if it has shut down already
     */
    public boolean shutdown() {
        return this.shutdown.getAndSet(false);
    }

    public abstract static class PrioritizedTask {

        protected final AtomicReference<PrioritizedTaskQueue> queue = new AtomicReference<>();

        protected final AtomicInteger priority;

        protected PrioritizedTask() {
            this(PrioritizedTaskQueue.NORMAL_PRIORITY);
        }

        protected PrioritizedTask(final int priority) {
            if (!PrioritizedTaskQueue.validPriority(priority)) {
                throw new IllegalArgumentException("Invalid priority " + priority);
            }
            this.priority = new AtomicInteger(priority);
        }

        /**
         * Returns the current priority. Note that {@link PrioritizedTaskQueue#COMPLETING_PRIORITY} will be returned
         * if this task is completing or has completed.
         */
        public final int getPriority() {
            return this.priority.get();
        }

        /**
         * Returns whether this task is scheduled to execute, or has been already executed.
         */
        public boolean isScheduled() {
            return this.queue.get() != null;
        }

        final int tryComplete(final int minPriority) {
            for (int curr = this.getPriorityVolatile();;) {
                if (curr == COMPLETING_PRIORITY) {
                    return COMPLETING_PRIORITY;
                }
                if (curr > minPriority) {
                    // curr is lower priority
                    return curr;
                }

                if (curr == (curr = this.compareAndExchangePriorityVolatile(curr, COMPLETING_PRIORITY))) {
                    return curr;
                }
                continue;
            }
        }

        /**
         * Forces this task to be completed.
         * @return {@code true} if the task was cancelled, {@code false} if the task has already completed or is being completed.
         */
        public boolean cancel() {
            return this.exchangePriorityVolatile(PrioritizedTaskQueue.COMPLETING_PRIORITY) != PrioritizedTaskQueue.COMPLETING_PRIORITY;
        }

        /**
         * Attempts to raise the priority to the priority level specified.
         * @param priority Priority specified
         * @return {@code true} if successful, {@code false} otherwise.
         */
        public boolean raisePriority(final int priority) {
            if (!PrioritizedTaskQueue.validPriority(priority)) {
                throw new IllegalArgumentException("Invalid priority");
            }

            for (int curr = this.getPriorityVolatile();;) {
                if (curr == COMPLETING_PRIORITY) {
                    return false;
                }
                if (priority >= curr) {
                    return true;
                }

                if (curr == (curr = this.compareAndExchangePriorityVolatile(curr, priority))) {
                    PrioritizedTaskQueue queue = this.queue.get();
                    if (queue != null) {
                        //noinspection unchecked
                        queue.queues[priority].add(this); // silently fail on shutdown
                    }
                    return true;
                }
                continue;
            }
        }

        /**
         * Attempts to set this task's priority level to the level specified.
         * @param priority Specified priority level.
         * @return {@code true} if successful, {@code false} if this task is completing or has completed.
         */
        public boolean updatePriority(final int priority) {
            if (!PrioritizedTaskQueue.validPriority(priority)) {
                throw new IllegalArgumentException("Invalid priority");
            }

            for (int curr = this.getPriorityVolatile();;) {
                if (curr == COMPLETING_PRIORITY) {
                    return false;
                }
                if (curr == priority) {
                    return true;
                }

                if (curr == (curr = this.compareAndExchangePriorityVolatile(curr, priority))) {
                    PrioritizedTaskQueue queue = this.queue.get();
                    if (queue != null) {
                        //noinspection unchecked
                        queue.queues[priority].add(this); // silently fail on shutdown
                    }
                    return true;
                }
                continue;
            }
        }

        void setQueue(final PrioritizedTaskQueue queue) {
            this.queue.set(queue);
        }

        /* priority */

        protected final int getPriorityVolatile() {
            return this.priority.get();
        }

        protected final int compareAndExchangePriorityVolatile(final int expect, final int update) {
            if (this.priority.compareAndSet(expect, update)) {
                return expect;
            }
            return this.priority.get();
        }

        protected final int exchangePriorityVolatile(final int value) {
            return this.priority.getAndSet(value);
        }
    }
}
