package io.papermc.paper.util;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Waitable;

/**
 * Utility class providing server-wide functionality for thread management,
 * coordinate conversions, and common operations in the Paper server implementation.
 */
public final class MCUtil {
    /**
     * Executor that runs tasks on the main server thread, or immediately if already on the main thread.
     * This is used to ensure thread safety for operations that must run on the server thread.
     */
    public static final java.util.concurrent.Executor MAIN_EXECUTOR = (run) -> {
        if (!isMainThread()) {
            MinecraftServer.getServer().execute(run);
        } else {
            run.run();
        }
    };

    /**
     * Thread pool for asynchronous task execution.
     * Limited to 2 threads to prevent excessive thread creation.
     */
    public static final ExecutorService ASYNC_EXECUTOR = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
        .setNameFormat("Paper Async Task Handler Thread - %1$d")
        .setUncaughtExceptionHandler(new net.minecraft.DefaultUncaughtExceptionHandlerWithName(MinecraftServer.LOGGER))
        .build()
    );

    private MCUtil() {
        // Prevent instantiation of utility class
    }

    /**
     * Gets a spiral of chunk coordinates outward from a center point.
     * This is useful for expanding outward from a location in a predictable pattern.
     *
     * @param blockposition Center point to spiral out from
     * @param radius Maximum radius in chunks to spiral out to
     * @return List of chunk positions in spiral order
     */
    public static List<ChunkPos> getSpiralOutChunks(BlockPos blockposition, int radius) {
        List<ChunkPos> list = com.google.common.collect.Lists.newArrayList();

        list.add(new ChunkPos(blockposition.getX() >> 4, blockposition.getZ() >> 4));
        for (int r = 1; r <= radius; r++) {
            int x = -r;
            int z = r;

            // Iterates the edge of half of the box; then negates for other half.
            while (x <= r && z > -r) {
                list.add(new ChunkPos((blockposition.getX() + (x << 4)) >> 4, (blockposition.getZ() + (z << 4)) >> 4));
                list.add(new ChunkPos((blockposition.getX() - (x << 4)) >> 4, (blockposition.getZ() - (z << 4)) >> 4));

                if (x < r) {
                    x++;
                } else {
                    z--;
                }
            }
        }
        return list;
    }

    /**
     * Ensures a CompletableFuture's callbacks execute on the main server thread.
     *
     * @param <T> The result type of the CompletableFuture
     * @param future The future to ensure executes on the main thread
     * @return A new CompletableFuture that will complete on the main thread
     */
    public static <T> CompletableFuture<T> ensureMain(CompletableFuture<T> future) {
        return future.thenApplyAsync(r -> r, MAIN_EXECUTOR);
    }

    /**
     * Registers a callback to be executed on the main thread when the future completes.
     *
     * @param <T> The result type of the CompletableFuture
     * @param future The future to attach the callback to
     * @param consumer The callback to execute on the main thread
     */
    public static <T> void thenOnMain(CompletableFuture<T> future, Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "Consumer cannot be null");
        future.thenAcceptAsync(consumer, MAIN_EXECUTOR);
    }

    /**
     * Registers a callback to be executed on the main thread when the future completes or fails.
     *
     * @param <T> The result type of the CompletableFuture
     * @param future The future to attach the callback to
     * @param consumer The callback to execute on the main thread
     */
    public static <T> void thenOnMain(CompletableFuture<T> future, BiConsumer<T, Throwable> consumer) {
        Objects.requireNonNull(consumer, "Consumer cannot be null");
        future.whenCompleteAsync(consumer, MAIN_EXECUTOR);
    }

    /**
     * Checks if the current thread is the server's main thread.
     *
     * @return true if the current thread is the main thread, false otherwise
     */
    public static boolean isMainThread() {
        return MinecraftServer.getServer().isSameThread();
    }

    /**
     * Ensures the target code runs on the main thread.
     *
     * @param run The code to run on the main thread
     */
    public static void ensureMain(Runnable run) {
        ensureMain(null, run);
    }

    /**
     * Ensures the target code runs on the main thread, with optional reason logging.
     *
     * @param reason The reason for needing main thread execution (for logging)
     * @param run The code to run on the main thread
     */
    public static void ensureMain(String reason, Runnable run) {
        Objects.requireNonNull(run, "Runnable cannot be null");

        if (!isMainThread()) {
            if (reason != null) {
                MinecraftServer.LOGGER.warn("Asynchronous " + reason + "!", new IllegalStateException());
            }
            MinecraftServer.getServer().processQueue.add(run);
            return;
        }
        run.run();
    }

    /**
     * Ensures a supplier function runs on the main thread and returns its result.
     *
     * @param <T> The return type of the supplier
     * @param run The supplier to run on the main thread
     * @return The result of the supplier
     */
    public static <T> T ensureMain(Supplier<T> run) {
        return ensureMain(null, run);
    }

    /**
     * Ensures a supplier function runs on the main thread and returns its result,
     * with optional reason logging.
     *
     * @param <T> The return type of the supplier
     * @param reason The reason for needing main thread execution (for logging)
     * @param run The supplier to run on the main thread
     * @return The result of the supplier
     */
    public static <T> T ensureMain(String reason, Supplier<T> run) {
        Objects.requireNonNull(run, "Supplier cannot be null");

        if (!isMainThread()) {
            if (reason != null) {
                MinecraftServer.LOGGER.warn("Asynchronous " + reason + "! Blocking thread until it returns ", new IllegalStateException());
            }
            Waitable<T> wait = new Waitable<>() {
                @Override
                protected T evaluate() {
                    return run.get();
                }
            };
            MinecraftServer.getServer().processQueue.add(wait);
            try {
                return wait.get();
            } catch (InterruptedException | ExecutionException e) {
                MinecraftServer.LOGGER.warn("Encountered exception", e);
                Thread.currentThread().interrupt(); // Reset interrupt flag if interrupted
            }
            return null;
        }
        return run.get();
    }

    /**
     * Calculates the Euclidean distance between two 3D points.
     *
     * @param x1 First point X coordinate
     * @param y1 First point Y coordinate
     * @param z1 First point Z coordinate
     * @param x2 Second point X coordinate
     * @param y2 Second point Y coordinate
     * @param z2 Second point Z coordinate
     * @return The Euclidean distance between the points
     */
    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(distanceSq(x1, y1, z1, x2, y2, z2));
    }

    /**
     * Calculates the squared Euclidean distance between two 3D points.
     * This is more efficient than distance() when only comparing distances.
     *
     * @param x1 First point X coordinate
     * @param y1 First point Y coordinate
     * @param z1 First point Z coordinate
     * @param x2 Second point X coordinate
     * @param y2 Second point Y coordinate
     * @param z2 Second point Z coordinate
     * @return The squared Euclidean distance between the points
     */
    public static double distanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
    }

    public static BlockPos toBlockPos(Position pos) {
        return new BlockPos(pos.blockX(), pos.blockY(), pos.blockZ());
    }

    public static FinePosition toPosition(Vec3 vector) {
        return Position.fine(vector.x, vector.y, vector.z);
    }

    public static BlockPosition toPosition(Vec3i vector) {
        return Position.block(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vec3 toVec3(Position position) {
        return new Vec3(position.x(), position.y(), position.z());
    }

    /**
     * Determines if a position is at the edge of a chunk.
     *
     * @param pos The position to check
     * @return true if the position is at a chunk edge, false otherwise
     */
    public static boolean isEdgeOfChunk(BlockPos pos) {
        final int modX = pos.getX() & 15;
        final int modZ = pos.getZ() & 15;
        return (modX == 0 || modX == 15 || modZ == 0 || modZ == 15);
    }

    /**
     * Schedules a task to run asynchronously using the Paper async executor.
     *
     * @param run The task to run
     */
    public static void scheduleAsyncTask(Runnable run) {
        Objects.requireNonNull(run, "Task cannot be null");
        ASYNC_EXECUTOR.execute(run);
    }

    public static <T> ResourceKey<T> toResourceKey(
        final ResourceKey<? extends net.minecraft.core.Registry<T>> registry,
        final NamespacedKey namespacedKey
    ) {
        return ResourceKey.create(registry, CraftNamespacedKey.toMinecraft(namespacedKey));
    }

    public static NamespacedKey fromResourceKey(final ResourceKey<?> key) {
        return CraftNamespacedKey.fromMinecraft(key.location());
    }

    /**
     * Creates an unmodifiable transformed list from a source list.
     *
     * @param <A> Target element type
     * @param <M> Source element type
     * @param nms Source list
     * @param converter Function to convert from source to target type
     * @return Unmodifiable list of converted elements
     */
    public static <A, M> List<A> transformUnmodifiable(final List<? extends M> nms, final Function<? super M, ? extends A> converter) {
        Objects.requireNonNull(nms, "Source list cannot be null");
        Objects.requireNonNull(converter, "Converter function cannot be null");
        return Collections.unmodifiableList(Lists.transform(nms, converter::apply));
    }

    /**
     * Creates an unmodifiable transformed collection from a source collection.
     *
     * @param <A> Target element type
     * @param <M> Source element type
     * @param nms Source collection
     * @param converter Function to convert from source to target type
     * @return Unmodifiable collection of converted elements
     */
    public static <A, M> Collection<A> transformUnmodifiable(final Collection<? extends M> nms, final Function<? super M, ? extends A> converter) {
        Objects.requireNonNull(nms, "Source collection cannot be null");
        Objects.requireNonNull(converter, "Converter function cannot be null");
        return Collections.unmodifiableCollection(Collections2.transform(nms, converter::apply));
    }

    /**
     * Converts elements from a source collection and adds them to a target collection.
     *
     * @param <A> Source element type
     * @param <M> Target element type
     * @param <C> Target collection type
     * @param target Collection to add converted elements to
     * @param toAdd Collection of elements to convert and add
     * @param converter Function to convert from source to target type
     */
    public static <A, M, C extends Collection<M>> void addAndConvert(final C target, final Collection<A> toAdd, final Function<? super A, ? extends M> converter) {
        Objects.requireNonNull(target, "Target collection cannot be null");
        Objects.requireNonNull(toAdd, "Source collection cannot be null");
        Objects.requireNonNull(converter, "Converter function cannot be null");

        for (final A value : toAdd) {
            target.add(converter.apply(value));
        }
    }
}
