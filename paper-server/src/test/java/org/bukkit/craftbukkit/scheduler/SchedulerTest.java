package org.bukkit.craftbukkit.scheduler;

import io.papermc.paper.plugin.PaperTestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class SchedulerTest {
    private static final AtomicBoolean active = new AtomicBoolean(false);
    private static final Plugin plugin = new PaperTestPlugin("foo/bar");

    @BeforeAll
    public static void start() {
        final CraftScheduler scheduler = (CraftScheduler) Bukkit.getServer().getScheduler();
        active.set(true);
        new Thread(() -> {
            while (active.get()) {
                scheduler.mainThreadHeartbeat();
                try {
                    Thread.sleep(50);
                } catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "main").start();
    }

    @AfterAll
    public static void stop() {
        active.set(false);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testTimeout(final boolean async) throws InterruptedException, ExecutionException {
        final CompletableFuture<Integer> d = new CompletableFuture<>();
        final CraftScheduler scheduler = (CraftScheduler) Bukkit.getServer().getScheduler();
        final Runnable action = () -> d.complete(1);
        if (async) {
            scheduler.runTaskLaterAsynchronously(plugin, action, 20);
        } else {
            scheduler.runTaskLater(plugin, action, 20);
        }
        assertEquals(1,
            (int) d.completeOnTimeout(0, 3, TimeUnit.SECONDS).get(),
            "The task should have been completed in 1 second, or at least completed");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testCancel(final boolean async) throws InterruptedException {
        final int tasks = 1 << 8;
        final CraftScheduler scheduler = (CraftScheduler) Bukkit.getServer().getScheduler();
        final Set<BukkitTask> submitted = ConcurrentHashMap.newKeySet(tasks);
        final LongAdder executed = new LongAdder();
        try (final ExecutorService executor = Executors.newWorkStealingPool()) {
            for (int i = 0; i < tasks; ++i) {
                executor.execute(() -> {
                    final BukkitTask task = async
                        ? scheduler.runTaskLaterAsynchronously(plugin, () -> { }, 20)
                        : scheduler.runTaskLater(plugin, () -> {}, 20);
                    executor.execute(task::cancel);
                    submitted.add(task);
                });
            }
        }
        for (final BukkitTask task : submitted) {
            assertTrue(
                task.isCancelled(),
                String.format(
                    "Task %s scheduled after cancellation",
                    task.getTaskId()
                )
            );
        }
        assertEquals(0, executed.intValue());
    }

    @Test
    public void testFuture() throws ExecutionException, InterruptedException, TimeoutException {
        final ReentrantLock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        final AtomicInteger result = new AtomicInteger();
        final Future<Integer> future = Bukkit.getScheduler().callSyncMethod(plugin, () -> {
            lock.lock();
            try {
                while (result.get() < 1) {
                    condition.await();
                }
            } finally {
                lock.unlock();
            }
            return result.get();
        });
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            lock.lock();
            try {
                result.set(1);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }, 1);
        assertEquals(1, future.get(2, TimeUnit.SECONDS));
    }
}
