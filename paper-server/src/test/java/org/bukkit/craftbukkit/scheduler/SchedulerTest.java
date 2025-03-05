package org.bukkit.craftbukkit.scheduler;

import io.papermc.paper.plugin.PaperTestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class SchedulerTest {
    private static volatile boolean active = false;

    @BeforeAll
    public static void start() {
        final CraftScheduler scheduler = (CraftScheduler) Bukkit.getServer().getScheduler();
        active = true;
        new Thread(() -> {
            while (active) {
                scheduler.mainThreadHeartbeat();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @AfterAll
    public static void stop() {
        active = false;
    }

    @Test
    public void testAsyncTimeout() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> d = new CompletableFuture<>();
        CraftScheduler scheduler = (CraftScheduler) Bukkit.getServer().getScheduler();
        scheduler.runTaskLaterAsynchronously(
            new PaperTestPlugin("foo/bar"),
            () -> d.complete(1),
            20
        );
        assertEquals(1,
            (int) d.completeOnTimeout(0, 3, TimeUnit.SECONDS).get(),
            "The task should have been completed in 1 second, or at least completed");
    }

    @Test
    public void testAsyncCancel() throws InterruptedException {
        final int tasks = 1 << 6;
        CraftScheduler scheduler = (CraftScheduler) Bukkit.getServer().getScheduler();
        Set<BukkitTask> submitted = new HashSet<>(tasks);
        LongAdder executed = new LongAdder();
        for (int i = 0; i < tasks; ++i) {
            BukkitTask task = scheduler.runTaskLaterAsynchronously(
                new PaperTestPlugin("foo/bar"),
                () -> {},
                5
            );
            task.cancel();
            submitted.add(task);
        }
        while (!scheduler.getPendingTasks().isEmpty()) {
            Thread.sleep(50);
        }
        for (BukkitTask task : submitted) {
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
}
