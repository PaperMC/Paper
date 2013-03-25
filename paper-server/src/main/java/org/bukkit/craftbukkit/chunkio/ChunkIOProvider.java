package org.bukkit.craftbukkit.chunkio;

import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkRegionLoader;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.Server;
import org.bukkit.craftbukkit.util.AsynchronousExecutor;
import org.bukkit.craftbukkit.util.LongHash;

import java.util.concurrent.atomic.AtomicInteger;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider<QueuedChunk, Chunk, Runnable, RuntimeException> {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    // async stuff
    public Chunk callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        ChunkRegionLoader loader = queuedChunk.loader;
        Object[] data = loader.loadChunk(queuedChunk.world, LongHash.msw(queuedChunk.coords), LongHash.lsw(queuedChunk.coords));

        if (data != null) {
            queuedChunk.compound = (NBTTagCompound) data[1];
            return (Chunk) data[0];
        }

        return null;
    }

    // sync stuff
    public void callStage2(QueuedChunk queuedChunk, Chunk chunk) throws RuntimeException {
        if(chunk == null) {
            // If the chunk loading failed just do it synchronously (may generate)
            queuedChunk.provider.getChunkAt(LongHash.msw(queuedChunk.coords), LongHash.lsw(queuedChunk.coords));
            return;
        }

        int x = LongHash.msw(queuedChunk.coords);
        int z = LongHash.lsw(queuedChunk.coords);

        // See if someone already loaded this chunk while we were working on it (API, etc)
        if (queuedChunk.provider.chunks.containsKey(queuedChunk.coords)) {
            // Make sure it isn't queued for unload, we need it
            queuedChunk.provider.unloadQueue.remove(queuedChunk.coords);
            return;
        }

        queuedChunk.loader.loadEntities(chunk, queuedChunk.compound.getCompound("Level"), queuedChunk.world);
        chunk.n = queuedChunk.provider.world.getTime();
        queuedChunk.provider.chunks.put(queuedChunk.coords, chunk);
        chunk.addEntities();

        if (queuedChunk.provider.chunkProvider != null) {
            queuedChunk.provider.chunkProvider.recreateStructures(x, z);
        }

        Server server = queuedChunk.provider.world.getServer();
        if (server != null) {
            server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(chunk.bukkitChunk, false));
        }

        chunk.a(queuedChunk.provider, queuedChunk.provider, x, z);
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
