package io.papermc.paper.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utilities for scaling thread pools.
 *
 * @see <a href="https://medium.com/@uditharosha/java-scale-first-executorservice-4245a63222df">Java Scale First ExecutorService — A myth or a reality</a>
 */
public final class ScalingThreadPool {
    private ScalingThreadPool() {
    }

    public static RejectedExecutionHandler defaultReEnqueuePolicy() {
        return reEnqueuePolicy(new ThreadPoolExecutor.AbortPolicy());
    }

    public static RejectedExecutionHandler reEnqueuePolicy(final RejectedExecutionHandler original) {
        return new ReEnqueuePolicy(original);
    }

    public static <E> BlockingQueue<E> createUnboundedQueue() {
        return new Queue<>();
    }

    public static <E> BlockingQueue<E> createQueue(final int capacity) {
        return new Queue<>(capacity);
    }

    private static final class Queue<E> extends LinkedBlockingQueue<E> {
        private final AtomicInteger idleThreads = new AtomicInteger(0);

        private Queue() {
            super();
        }

        private Queue(final int capacity) {
            super(capacity);
        }

        @Override
        public boolean offer(final E e) {
            return this.idleThreads.get() > 0 && super.offer(e);
        }

        @Override
        public E take() throws InterruptedException {
            this.idleThreads.incrementAndGet();
            try {
                return super.take();
            } finally {
                this.idleThreads.decrementAndGet();
            }
        }

        @Override
        public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
            this.idleThreads.incrementAndGet();
            try {
                return super.poll(timeout, unit);
            } finally {
                this.idleThreads.decrementAndGet();
            }
        }

        @Override
        public boolean add(final E e) {
            return super.offer(e);
        }
    }

    private record ReEnqueuePolicy(RejectedExecutionHandler originalHandler) implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
            if (!executor.getQueue().add(r)) {
                this.originalHandler.rejectedExecution(r, executor);
            }
        }
    }
}
