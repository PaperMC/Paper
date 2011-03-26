
package org.bukkit.event.world;

import org.bukkit.Chunk;

/**
 * Called when a chunk is loaded
 */
public class ChunkLoadEvent extends ChunkEvent {
    public ChunkLoadEvent(final Chunk chunk) {
        super(Type.CHUNK_LOAD, chunk);
    }
}
