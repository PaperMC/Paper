package org.bukkit.craftbukkit.chunkio;

import java.io.IOException;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkRegionLoader;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.Server;
import org.bukkit.craftbukkit.util.AsynchronousExecutor;

import java.util.concurrent.atomic.AtomicInteger;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider<QueuedChunk, Chunk, Runnable, RuntimeException> {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    // async stuff
    public Chunk callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        try {
            ChunkRegionLoader loader = queuedChunk.loader;
            Object[] data = loader.loadChunk(queuedChunk.world, queuedChunk.x, queuedChunk.z, (chunk) -> {});
            
            if (data != null) {
                queuedChunk.compound = (NBTTagCompound) data[1];
                return (Chunk) data[0];
            }

            return null;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // sync stuff
    public void callStage2(QueuedChunk queuedChunk, Chunk chunk) throws RuntimeException {
        if (chunk == null) {
            // If the chunk loading failed just do it synchronously (may generate)
            // queuedChunk.provider.originalGetChunkAt(queuedChunk.x, queuedChunk.z);
            return;
        }

        queuedChunk.loader.loadEntities(queuedChunk.compound.getCompound("Level"), chunk);
        chunk.setLastSaved(queuedChunk.provider.world.getTime());
        queuedChunk.provider.chunks.put(ChunkCoordIntPair.a(queuedChunk.x, queuedChunk.z), chunk);
        chunk.addEntities();
    }

    public void callStage3(QueuedChunk queuedChunk, Chunk chunk, Runnable runnable) throws RuntimeException {
        runnable.run();
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Chunk I/O Executor Thread-" + threadNumber.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
