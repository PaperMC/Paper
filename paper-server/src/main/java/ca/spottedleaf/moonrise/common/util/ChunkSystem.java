package ca.spottedleaf.moonrise.common.util;

import ca.spottedleaf.concurrentutil.util.Priority;
import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.slf4j.Logger;
import java.util.List;
import java.util.function.Consumer;

public final class ChunkSystem {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final net.minecraft.world.level.chunk.status.ChunkStep FULL_CHUNK_STEP = net.minecraft.world.level.chunk.status.ChunkPyramid.GENERATION_PYRAMID.getStepTo(ChunkStatus.FULL);

    private static int getDistance(final ChunkStatus status) {
        return FULL_CHUNK_STEP.getAccumulatedRadiusOf(status);
    }

    public static void scheduleChunkTask(final ServerLevel level, final int chunkX, final int chunkZ, final Runnable run) {
        scheduleChunkTask(level, chunkX, chunkZ, run, Priority.NORMAL);
    }

    public static void scheduleChunkTask(final ServerLevel level, final int chunkX, final int chunkZ, final Runnable run, final Priority priority) {
        level.chunkSource.mainThreadProcessor.execute(run);
    }

    public static void scheduleChunkLoad(final ServerLevel level, final int chunkX, final int chunkZ, final boolean gen,
                                         final ChunkStatus toStatus, final boolean addTicket, final Priority priority,
                                         final Consumer<ChunkAccess> onComplete) {
        if (gen) {
            scheduleChunkLoad(level, chunkX, chunkZ, toStatus, addTicket, priority, onComplete);
            return;
        }
        scheduleChunkLoad(level, chunkX, chunkZ, ChunkStatus.EMPTY, addTicket, priority, (final ChunkAccess chunk) -> {
            if (chunk == null) {
                if (onComplete != null) {
                    onComplete.accept(null);
                }
            } else {
                if (chunk.getPersistedStatus().isOrAfter(toStatus)) {
                    scheduleChunkLoad(level, chunkX, chunkZ, toStatus, addTicket, priority, onComplete);
                } else {
                    if (onComplete != null) {
                        onComplete.accept(null);
                    }
                }
            }
        });
    }

    static final net.minecraft.server.level.TicketType<Long> CHUNK_LOAD = net.minecraft.server.level.TicketType.create("chunk_load", Long::compareTo);

    private static long chunkLoadCounter = 0L;
    public static void scheduleChunkLoad(final ServerLevel level, final int chunkX, final int chunkZ, final ChunkStatus toStatus,
                                         final boolean addTicket, final Priority priority, final Consumer<ChunkAccess> onComplete) {
        if (!org.bukkit.Bukkit.isOwnedByCurrentRegion(level.getWorld(), chunkX, chunkZ)) {
            scheduleChunkTask(level, chunkX, chunkZ, () -> {
                scheduleChunkLoad(level, chunkX, chunkZ, toStatus, addTicket, priority, onComplete);
            }, priority);
            return;
        }

        final int minLevel = 33 + getDistance(toStatus);
        final Long chunkReference = addTicket ? Long.valueOf(++chunkLoadCounter) : null;
        final net.minecraft.world.level.ChunkPos chunkPos = new net.minecraft.world.level.ChunkPos(chunkX, chunkZ);

        if (addTicket) {
            level.chunkSource.addTicketAtLevel(CHUNK_LOAD, chunkPos, minLevel, chunkReference);
        }
        level.chunkSource.runDistanceManagerUpdates();

        final Consumer<ChunkAccess> loadCallback = (final ChunkAccess chunk) -> {
            try {
                if (onComplete != null) {
                    onComplete.accept(chunk);
                }
            } catch (final Throwable thr) {
                LOGGER.error("Exception handling chunk load callback", thr);
                com.destroystokyo.paper.util.SneakyThrow.sneaky(thr);
            } finally {
                if (addTicket) {
                    level.chunkSource.addTicketAtLevel(net.minecraft.server.level.TicketType.UNKNOWN, chunkPos, minLevel, chunkPos);
                    level.chunkSource.removeTicketAtLevel(CHUNK_LOAD, chunkPos, minLevel, chunkReference);
                }
            }
        };

        final ChunkHolder holder = level.chunkSource.chunkMap.updatingChunkMap.get(CoordinateUtils.getChunkKey(chunkX, chunkZ));

        if (holder == null || holder.getTicketLevel() > minLevel) {
            loadCallback.accept(null);
            return;
        }

        final java.util.concurrent.CompletableFuture<net.minecraft.server.level.ChunkResult<net.minecraft.world.level.chunk.ChunkAccess>> loadFuture = holder.scheduleChunkGenerationTask(toStatus, level.chunkSource.chunkMap);

        if (loadFuture.isDone()) {
            loadCallback.accept(loadFuture.join().orElse(null));
            return;
        }

        loadFuture.whenCompleteAsync((final net.minecraft.server.level.ChunkResult<net.minecraft.world.level.chunk.ChunkAccess> result, final Throwable thr) -> {
            if (thr != null) {
                loadCallback.accept(null);
                return;
            }
            loadCallback.accept(result.orElse(null));
        }, (final Runnable r) -> {
            scheduleChunkTask(level, chunkX, chunkZ, r, Priority.HIGHEST);
        });
    }

    public static void scheduleTickingState(final ServerLevel level, final int chunkX, final int chunkZ,
                                            final FullChunkStatus toStatus, final boolean addTicket,
                                            final Priority priority, final Consumer<LevelChunk> onComplete) {
        // This method goes unused until the chunk system rewrite
        if (toStatus == FullChunkStatus.INACCESSIBLE) {
            throw new IllegalArgumentException("Cannot wait for INACCESSIBLE status");
        }

        if (!org.bukkit.Bukkit.isOwnedByCurrentRegion(level.getWorld(), chunkX, chunkZ)) {
            scheduleChunkTask(level, chunkX, chunkZ, () -> {
                scheduleTickingState(level, chunkX, chunkZ, toStatus, addTicket, priority, onComplete);
            }, priority);
            return;
        }

        final int minLevel = 33 - (toStatus.ordinal() - 1);
        final int radius = toStatus.ordinal() - 1;
        final Long chunkReference = addTicket ? Long.valueOf(++chunkLoadCounter) : null;
        final net.minecraft.world.level.ChunkPos chunkPos = new net.minecraft.world.level.ChunkPos(chunkX, chunkZ);

        if (addTicket) {
            level.chunkSource.addTicketAtLevel(CHUNK_LOAD, chunkPos, minLevel, chunkReference);
        }
        level.chunkSource.runDistanceManagerUpdates();

        final Consumer<LevelChunk> loadCallback = (final LevelChunk chunk) -> {
            try {
                if (onComplete != null) {
                    onComplete.accept(chunk);
                }
            } catch (final Throwable thr) {
                LOGGER.error("Exception handling chunk load callback", thr);
                com.destroystokyo.paper.util.SneakyThrow.sneaky(thr);
            } finally {
                if (addTicket) {
                    level.chunkSource.addTicketAtLevel(net.minecraft.server.level.TicketType.UNKNOWN, chunkPos, minLevel, chunkPos);
                    level.chunkSource.removeTicketAtLevel(CHUNK_LOAD, chunkPos, minLevel, chunkReference);
                }
            }
        };

        final ChunkHolder holder = level.chunkSource.chunkMap.updatingChunkMap.get(CoordinateUtils.getChunkKey(chunkX, chunkZ));

        if (holder == null || holder.getTicketLevel() > minLevel) {
            loadCallback.accept(null);
            return;
        }

        final java.util.concurrent.CompletableFuture<net.minecraft.server.level.ChunkResult<net.minecraft.world.level.chunk.LevelChunk>> tickingState;
        switch (toStatus) {
            case FULL: {
                tickingState = holder.getFullChunkFuture();
                break;
            }
            case BLOCK_TICKING: {
                tickingState = holder.getTickingChunkFuture();
                break;
            }
            case ENTITY_TICKING: {
                tickingState = holder.getEntityTickingChunkFuture();
                break;
            }
            default: {
                throw new IllegalStateException("Cannot reach here");
            }
        }

        if (tickingState.isDone()) {
            loadCallback.accept(tickingState.join().orElse(null));
            return;
        }

        tickingState.whenCompleteAsync((final net.minecraft.server.level.ChunkResult<net.minecraft.world.level.chunk.LevelChunk> result, final Throwable thr) -> {
            if (thr != null) {
                loadCallback.accept(null);
                return;
            }
            loadCallback.accept(result.orElse(null));
        }, (final Runnable r) -> {
            scheduleChunkTask(level, chunkX, chunkZ, r, Priority.HIGHEST);
        });
    }

    public static List<ChunkHolder> getVisibleChunkHolders(final ServerLevel level) {
        return new java.util.ArrayList<>(level.chunkSource.chunkMap.visibleChunkMap.values());
    }

    public static List<ChunkHolder> getUpdatingChunkHolders(final ServerLevel level) {
        return new java.util.ArrayList<>(level.chunkSource.chunkMap.updatingChunkMap.values());
    }

    public static int getVisibleChunkHolderCount(final ServerLevel level) {
        return level.chunkSource.chunkMap.visibleChunkMap.size();
    }

    public static int getUpdatingChunkHolderCount(final ServerLevel level) {
        return level.chunkSource.chunkMap.updatingChunkMap.size();
    }

    public static boolean hasAnyChunkHolders(final ServerLevel level) {
        return getUpdatingChunkHolderCount(level) != 0;
    }

    public static boolean screenEntity(final ServerLevel level, final Entity entity, final boolean fromDisk, final boolean event) {
        if (!PlatformHooks.get().screenEntity(level, entity, fromDisk, event)) {
            return false;
        }
        return true;
    }

    public static void onChunkHolderCreate(final ServerLevel level, final ChunkHolder holder) {

    }

    public static void onChunkHolderDelete(final ServerLevel level, final ChunkHolder holder) {

    }

    public static void onChunkBorder(final LevelChunk chunk, final ChunkHolder holder) {

    }

    public static void onChunkNotBorder(final LevelChunk chunk, final ChunkHolder holder) {

    }

    public static void onChunkTicking(final LevelChunk chunk, final ChunkHolder holder) {

    }

    public static void onChunkNotTicking(final LevelChunk chunk, final ChunkHolder holder) {

    }

    public static void onChunkEntityTicking(final LevelChunk chunk, final ChunkHolder holder) {

    }

    public static void onChunkNotEntityTicking(final LevelChunk chunk, final ChunkHolder holder) {

    }

    public static ChunkHolder getUnloadingChunkHolder(final ServerLevel level, final int chunkX, final int chunkZ) {
        return level.chunkSource.chunkMap.getUnloadingChunkHolder(chunkX, chunkZ);
    }

    public static int getSendViewDistance(final ServerPlayer player) {
        return getViewDistance(player);
    }

    public static int getViewDistance(final ServerPlayer player) {
        final ServerLevel level = player.serverLevel();
        if (level == null) {
            return org.bukkit.Bukkit.getViewDistance();
        }
        return level.chunkSource.chunkMap.serverViewDistance;
    }

    public static int getTickViewDistance(final ServerPlayer player) {
        final ServerLevel level = player.serverLevel();
        if (level == null) {
            return org.bukkit.Bukkit.getSimulationDistance();
        }
        return level.chunkSource.chunkMap.distanceManager.simulationDistance;
    }

    private ChunkSystem() {}
}
