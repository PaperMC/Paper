package org.bukkit.event.world;

import org.bukkit.Chunk;

public class ChunkEvent extends WorldEvent {
    protected Chunk chunk;

    protected ChunkEvent(Type type, Chunk chunk) {
        super(type, chunk.getWorld());
        this.chunk = chunk;
    }

    /**
     * Gets the chunk being loaded/unloaded
     *
     * @return Chunk that triggered this event
     */
    public Chunk getChunk() {
        return chunk;
    }
}
