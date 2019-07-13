package com.destroystokyo.paper.io.chunk;

import com.destroystokyo.paper.io.PaperFileIOThread;
import com.destroystokyo.paper.io.PrioritizedTaskQueue;
import net.minecraft.server.WorldServer;

abstract class ChunkTask extends PrioritizedTaskQueue.PrioritizedTask implements Runnable {

    public final WorldServer world;
    public final int chunkX;
    public final int chunkZ;
    public final ChunkTaskManager taskManager;

    public ChunkTask(final WorldServer world, final int chunkX, final int chunkZ, final int priority,
                         final ChunkTaskManager taskManager) {
        super(priority);
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.taskManager = taskManager;
    }

    @Override
    public String toString() {
        return "Chunk task: class:" + this.getClass().getName() + ", for world '" + this.world.getWorld().getName() +
            "', (" + this.chunkX + "," + this.chunkZ + "), hashcode:" + this.hashCode() + ", priority: " + this.getPriority();
    }

    @Override
    public boolean raisePriority(final int priority) {
        PaperFileIOThread.Holder.INSTANCE.bumpPriority(this.world, this.chunkX, this.chunkZ, priority);
        return super.raisePriority(priority);
    }

    @Override
    public boolean updatePriority(final int priority) {
        PaperFileIOThread.Holder.INSTANCE.setPriority(this.world, this.chunkX, this.chunkZ, priority);
        return super.updatePriority(priority);
    }
}
