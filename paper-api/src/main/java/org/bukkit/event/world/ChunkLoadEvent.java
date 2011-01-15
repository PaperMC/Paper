
package org.bukkit.event.world;

import org.bukkit.Chunk;

/**
 * Called when a chunk is loaded
 */
public class ChunkLoadEvent extends WorldEvent {
    private final Chunk chunk;

    public ChunkLoadEvent(final Type type, final Chunk chunk) {
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
