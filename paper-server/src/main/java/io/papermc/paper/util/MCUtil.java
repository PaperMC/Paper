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
import net.minecraft.world.phys.Vec3;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Waitable;

public final class MCUtil {
    public static final java.util.concurrent.Executor MAIN_EXECUTOR = (run) -> {
        if (!isMainThread()) {
            MinecraftServer.getServer().execute(run);
        } else {
            run.run();
        }
    };
    public static final ExecutorService ASYNC_EXECUTOR = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
        .setNameFormat("Paper Async Task Handler Thread - %1$d")
        .setUncaughtExceptionHandler(new net.minecraft.DefaultUncaughtExceptionHandlerWithName(MinecraftServer.LOGGER))
        .build()
    );

    private MCUtil() {
    }

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

    public static <T> CompletableFuture<T> ensureMain(CompletableFuture<T> future) {
        return future.thenApplyAsync(r -> r, MAIN_EXECUTOR);
    }

    public static <T> void thenOnMain(CompletableFuture<T> future, Consumer<T> consumer) {
        future.thenAcceptAsync(consumer, MAIN_EXECUTOR);
    }

    public static <T> void thenOnMain(CompletableFuture<T> future, BiConsumer<T, Throwable> consumer) {
        future.whenCompleteAsync(consumer, MAIN_EXECUTOR);
    }

    public static boolean isMainThread() {
        return MinecraftServer.getServer().isSameThread();
    }

    public static void ensureMain(Runnable run) {
        ensureMain(null, run);
    }

    /**
     * Ensures the target code is running on the main thread.
     */
    public static void ensureMain(String reason, Runnable run) {
        if (!isMainThread()) {
            if (reason != null) {
                MinecraftServer.LOGGER.warn("Asynchronous " + reason + "!", new IllegalStateException());
            }
            MinecraftServer.getServer().processQueue.add(run);
            return;
        }
        run.run();
    }

    public static double sanitizeNanInf(final double value, final double defaultValue) {
        return Double.isNaN(value) || Double.isInfinite(value) ? defaultValue : value;
    }

    public static Vec3 sanitizeNanInf(final Vec3 vec3, final double defaultValue) {
        return new Vec3(
            sanitizeNanInf(vec3.x, defaultValue),
            sanitizeNanInf(vec3.y, defaultValue),
            sanitizeNanInf(vec3.z, defaultValue)
        );
    }

    public static <T> T ensureMain(Supplier<T> run) {
        return ensureMain(null, run);
    }

    /**
     * Ensures the target code is running on the main thread.
     */
    public static <T> T ensureMain(String reason, Supplier<T> run) {
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
            }
            return null;
        }
        return run.get();
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(distanceSq(x1, y1, z1, x2, y2, z2));
    }

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

    public static boolean isEdgeOfChunk(BlockPos pos) {
        final int modX = pos.getX() & 15;
        final int modZ = pos.getZ() & 15;
        return (modX == 0 || modX == 15 || modZ == 0 || modZ == 15);
    }

    public static void scheduleAsyncTask(Runnable run) {
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

    public static <A, M> List<A> transformUnmodifiable(final List<? extends M> nms, final Function<? super M, ? extends A> converter) {
        return Collections.unmodifiableList(Lists.transform(nms, converter::apply));
    }

    public static <A, M> Collection<A> transformUnmodifiable(final Collection<? extends M> nms, final Function<? super M, ? extends A> converter) {
        return Collections.unmodifiableCollection(Collections2.transform(nms, converter::apply));
    }

    public static <A, M, C extends Collection<M>> void addAndConvert(final C target, final Collection<A> toAdd, final Function<? super A, ? extends M> converter) {
        for (final A value : toAdd) {
            target.add(converter.apply(value));
        }
    }
}
