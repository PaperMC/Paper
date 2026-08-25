package org.bukkit.event.world;

import org.bukkit.Chunk;

/**
 * Represents a Chunk related event
 */
public interface ChunkEvent extends WorldEventNew {

    /**
     * Gets the chunk being loaded/unloaded
     *
     * @return Chunk that triggered this event
     */
    Chunk getChunk();
}
