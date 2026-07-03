package io.papermc.paper.entity;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * Worker pool that runs the expensive A* portion of mob pathfinding
 * ({@link net.minecraft.world.level.pathfinder.PathFinder#findPath}) off the
 * main server thread.
 *
 * <p>This is opt-in per world via {@code entities.behavior.async-pathfinding} and
 * is disabled by default. Only the {@code findPath} computation is offloaded; the
 * request is prepared (validation, {@code EntityPathfindEvent}, region snapshot) on
 * the main thread and the finished {@link net.minecraft.world.level.pathfinder.Path}
 * is applied back on the main thread, so the world's mutation model is unchanged.</p>
 *
 * <p>Backpressure: the queue is bounded and uses a caller-runs policy, so when the
 * pool is saturated the pathfinding simply runs synchronously on the main thread as
 * it would in vanilla — latency never regresses past the vanilla baseline.</p>
 */
public final class AsyncPathProcessor {

    // Reserve most cores for the main tick + chunk workers; pathfinding gets a small slice.
    private static final int THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() / 4);

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
        THREADS, THREADS,
        60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(256),
        new ThreadFactoryBuilder()
            .setNameFormat("Paper Async Pathfinding Thread #%d")
            .setDaemon(true)
            .setPriority(Thread.NORM_PRIORITY - 2)
            .build(),
        new ThreadPoolExecutor.CallerRunsPolicy() // saturated -> run on caller (main) = safe vanilla fallback
    );

    static {
        EXECUTOR.allowCoreThreadTimeOut(true);
    }

    private AsyncPathProcessor() {
    }

    public static void execute(final @NotNull Runnable task) {
        EXECUTOR.execute(task);
    }

    public static int threadCount() {
        return THREADS;
    }
}
