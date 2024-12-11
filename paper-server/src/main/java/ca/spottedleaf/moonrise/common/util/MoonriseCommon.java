package ca.spottedleaf.moonrise.common.util;

import ca.spottedleaf.concurrentutil.executor.thread.PrioritisedThreadPool;
import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class MoonriseCommon {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    public static final PrioritisedThreadPool WORKER_POOL = new PrioritisedThreadPool(
            new Consumer<>() {
                private final AtomicInteger idGenerator = new AtomicInteger();

                @Override
                public void accept(Thread thread) {
                    thread.setDaemon(true);
                    thread.setName(PlatformHooks.get().getBrand() + " Common Worker #" + this.idGenerator.getAndIncrement());
                    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(final Thread thread, final Throwable throwable) {
                            LOGGER.error("Uncaught exception in thread " + thread.getName(), throwable);
                        }
                    });
                }
            }
    );
    public static final long WORKER_QUEUE_HOLD_TIME = (long)(20.0e6); // 20ms
    public static final int CLIENT_DIVISION = 0;
    public static final PrioritisedThreadPool.ExecutorGroup RENDER_EXECUTOR_GROUP = MoonriseCommon.WORKER_POOL.createExecutorGroup(CLIENT_DIVISION, 0);
    public static final int SERVER_DIVISION = 1;
    public static final PrioritisedThreadPool.ExecutorGroup PARALLEL_GEN_GROUP = MoonriseCommon.WORKER_POOL.createExecutorGroup(SERVER_DIVISION, 0);
    public static final PrioritisedThreadPool.ExecutorGroup RADIUS_AWARE_GROUP = MoonriseCommon.WORKER_POOL.createExecutorGroup(SERVER_DIVISION, 0);
    public static final PrioritisedThreadPool.ExecutorGroup LOAD_GROUP         = MoonriseCommon.WORKER_POOL.createExecutorGroup(SERVER_DIVISION, 0);

    public static void adjustWorkerThreads(final int configWorkerThreads, final int configIoThreads) {
        int defaultWorkerThreads = Runtime.getRuntime().availableProcessors() / 2;
        if (defaultWorkerThreads <= 4) {
            defaultWorkerThreads = defaultWorkerThreads <= 3 ? 1 : 2;
        } else {
            defaultWorkerThreads = defaultWorkerThreads / 2;
        }
        defaultWorkerThreads = Integer.getInteger(PlatformHooks.get().getBrand() + ".WorkerThreadCount", Integer.valueOf(defaultWorkerThreads));

        int workerThreads = configWorkerThreads;

        if (workerThreads <= 0) {
            workerThreads = defaultWorkerThreads;
        }

        final int ioThreads = Math.max(1, configIoThreads);

        WORKER_POOL.adjustThreadCount(workerThreads);
        IO_POOL.adjustThreadCount(ioThreads);

        LOGGER.info(PlatformHooks.get().getBrand() + " is using " + workerThreads + " worker threads, " + ioThreads + " I/O threads");
    }

    public static final PrioritisedThreadPool IO_POOL = new PrioritisedThreadPool(
            new Consumer<>() {
                private final AtomicInteger idGenerator = new AtomicInteger();

                @Override
                public void accept(final Thread thread) {
                    thread.setDaemon(true);
                    thread.setName(PlatformHooks.get().getBrand() + " I/O Worker #" + this.idGenerator.getAndIncrement());
                    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(final Thread thread, final Throwable throwable) {
                            LOGGER.error("Uncaught exception in thread " + thread.getName(), throwable);
                        }
                    });
                }
            }
    );
    public static final long IO_QUEUE_HOLD_TIME = (long)(100.0e6); // 100ms
    public static final PrioritisedThreadPool.ExecutorGroup CLIENT_PROFILER_IO_GROUP = IO_POOL.createExecutorGroup(CLIENT_DIVISION, 0);
    public static final PrioritisedThreadPool.ExecutorGroup SERVER_REGION_IO_GROUP = IO_POOL.createExecutorGroup(SERVER_DIVISION, 0);

    public static void haltExecutors() {
        MoonriseCommon.WORKER_POOL.shutdown(false);
        LOGGER.info("Awaiting termination of worker pool for up to 60s...");
        if (!MoonriseCommon.WORKER_POOL.join(TimeUnit.SECONDS.toMillis(60L))) {
            LOGGER.error("Worker pool did not shut down in time!");
            MoonriseCommon.WORKER_POOL.halt(false);
        }

        MoonriseCommon.IO_POOL.shutdown(false);
        LOGGER.info("Awaiting termination of I/O pool for up to 60s...");
        if (!MoonriseCommon.IO_POOL.join(TimeUnit.SECONDS.toMillis(60L))) {
            LOGGER.error("I/O pool did not shut down in time!");
            MoonriseCommon.IO_POOL.halt(false);
        }
    }

    private MoonriseCommon() {}
}
