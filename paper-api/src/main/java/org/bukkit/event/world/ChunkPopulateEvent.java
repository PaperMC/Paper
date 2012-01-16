package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.BlockPopulator;

/**
 * Thrown when a new chunk has finished being populated.
 * <p />
 * If your intent is to populate the chunk using this event, please see {@link BlockPopulator}
 */
@SuppressWarnings("serial")
public class ChunkPopulateEvent extends ChunkEvent {
    private static final HandlerList handlers = new HandlerList();
    public ChunkPopulateEvent(final Chunk chunk) {
        super(Type.CHUNK_POPULATED, chunk);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
