package org.bukkit.craftbukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.world.ChunkEvent;

public abstract class CraftChunkEvent extends CraftWorldEvent implements ChunkEvent {

    protected Chunk chunk;

    protected CraftChunkEvent(final Chunk chunk) {
        super(chunk.getWorld());
        this.chunk = chunk;
    }

    @Override
    public Chunk getChunk() {
        return this.chunk;
    }
}
