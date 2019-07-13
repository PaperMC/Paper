package com.destroystokyo.paper.io.chunk;

import co.aikar.timings.Timing;
import com.destroystokyo.paper.io.PaperFileIOThread;
import com.destroystokyo.paper.io.IOUtil;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkRegionLoader;
import net.minecraft.server.PlayerChunkMap;
import net.minecraft.server.WorldServer;

import java.util.ArrayDeque;
import java.util.function.Consumer;

public final class ChunkLoadTask extends ChunkTask {

    public boolean cancelled;

    Consumer<ChunkRegionLoader.InProgressChunkHolder> onComplete;
    public PaperFileIOThread.ChunkData chunkData;

    private boolean hasCompleted;

    public ChunkLoadTask(final WorldServer world, final int chunkX, final int chunkZ, final int priority,
                         final ChunkTaskManager taskManager,
                         final Consumer<ChunkRegionLoader.InProgressChunkHolder> onComplete) {
        super(world, chunkX, chunkZ, priority, taskManager);
        this.onComplete = onComplete;
    }

    private static final ArrayDeque<Runnable> EMPTY_QUEUE = new ArrayDeque<>();

    private static ChunkRegionLoader.InProgressChunkHolder createEmptyHolder() {
        return new ChunkRegionLoader.InProgressChunkHolder(null, EMPTY_QUEUE);
    }

    @Override
    public void run() {
        try {
            this.executeTask();
        } catch (final Throwable ex) {
            PaperFileIOThread.LOGGER.error("Failed to execute chunk load task: " + this.toString(), ex);
            if (!this.hasCompleted) {
                this.complete(ChunkLoadTask.createEmptyHolder());
            }
        }
    }

    private boolean checkCancelled() {
        if (this.cancelled) {
            // IntelliJ does not understand writes may occur to cancelled concurrently.
            return this.taskManager.chunkLoadTasks.compute(Long.valueOf(IOUtil.getCoordinateKey(this.chunkX, this.chunkZ)), (final Long keyInMap, final ChunkLoadTask valueInMap) -> {
                if (valueInMap != ChunkLoadTask.this) {
                    throw new IllegalStateException("Expected this task to be scheduled, but another was! Other: " + valueInMap + ", current: " + ChunkLoadTask.this);
                }

                if (valueInMap.cancelled) {
                    return null;
                }
                return valueInMap;
            }) == null;
        }
        return false;
    }

    public void executeTask() {
        if (this.checkCancelled()) {
            return;
        }

        // either executed synchronously or asynchronously
        final PaperFileIOThread.ChunkData chunkData = this.chunkData;

        if (chunkData.poiData == PaperFileIOThread.FAILURE_VALUE || chunkData.chunkData == PaperFileIOThread.FAILURE_VALUE) {
            PaperFileIOThread.LOGGER.error("Could not load chunk for task: " + this.toString() + ", file IO thread has dumped the relevant exception above");
            this.complete(ChunkLoadTask.createEmptyHolder());
            return;
        }

        if (chunkData.chunkData == null) {
            // not on disk
            this.complete(ChunkLoadTask.createEmptyHolder());
            return;
        }

        final ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(this.chunkX, this.chunkZ);

        final PlayerChunkMap chunkManager = this.world.getChunkProvider().playerChunkMap;

        try (Timing ignored = this.world.timings.chunkLoadLevelTimer.startTimingIfSync()) {
            final ChunkRegionLoader.InProgressChunkHolder chunkHolder;

            // apply fixes

            try {
                chunkData.chunkData = chunkManager.getChunkData(this.world.getTypeKey(),
                    chunkManager.getWorldPersistentDataSupplier(), chunkData.chunkData, chunkPos, this.world); // clone data for safety, file IO thread does not clone
            } catch (final Throwable ex) {
                PaperFileIOThread.LOGGER.error("Could not apply datafixers for chunk task: " + this.toString(), ex);
                this.complete(ChunkLoadTask.createEmptyHolder());
            }

            if (this.checkCancelled()) {
                return;
            }

            try {
                this.world.getChunkProvider().playerChunkMap.updateChunkStatusOnDisk(chunkPos, chunkData.chunkData);
            } catch (final Throwable ex) {
                PaperFileIOThread.LOGGER.warn("Failed to update chunk status cache for task: " + this.toString(), ex);
                // non-fatal, continue
            }

            try {
                chunkHolder = ChunkRegionLoader.loadChunk(this.world,
                    chunkManager.definedStructureManager, chunkManager.getVillagePlace(), chunkPos,
                    chunkData.chunkData, true);
            } catch (final Throwable ex) {
                PaperFileIOThread.LOGGER.error("Could not de-serialize chunk data for task: " + this.toString(), ex);
                this.complete(ChunkLoadTask.createEmptyHolder());
                return;
            }

            this.complete(chunkHolder);
        }
    }

    private void complete(final ChunkRegionLoader.InProgressChunkHolder holder) {
        this.hasCompleted = true;
        holder.poiData = this.chunkData == null ? null : this.chunkData.poiData;

        this.taskManager.chunkLoadTasks.compute(Long.valueOf(IOUtil.getCoordinateKey(this.chunkX, this.chunkZ)), (final Long keyInMap, final ChunkLoadTask valueInMap) -> {
            if (valueInMap != ChunkLoadTask.this) {
                throw new IllegalStateException("Expected this task to be scheduled, but another was! Other: " + valueInMap + ", current: " + ChunkLoadTask.this);
            }
            if (valueInMap.cancelled) {
                return null;
            }
            try {
                ChunkLoadTask.this.onComplete.accept(holder);
            } catch (final Throwable thr) {
                PaperFileIOThread.LOGGER.error("Failed to complete chunk data for task: " + this.toString(), thr);
            }
            return null;
        });
    }
}
