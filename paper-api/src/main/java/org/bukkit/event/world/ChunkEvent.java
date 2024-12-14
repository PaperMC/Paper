package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Chunk related event
 *
 * @since 1.0.0 R1
 */
public abstract class ChunkEvent extends WorldEvent {
    protected Chunk chunk;

    protected ChunkEvent(@NotNull final Chunk chunk) {
        super(chunk.getWorld());
        this.chunk = chunk;
    }

    /**
     * Gets the chunk being loaded/unloaded
     *
     * @return Chunk that triggered this event
     */
    @NotNull
    public Chunk getChunk() {
        return chunk;
    }
}
