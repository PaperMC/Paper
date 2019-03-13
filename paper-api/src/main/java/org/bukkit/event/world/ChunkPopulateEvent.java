package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a new chunk has finished being populated.
 * <p>
 * If your intent is to populate the chunk using this event, please see {@link
 * BlockPopulator}
 */
public class ChunkPopulateEvent extends ChunkEvent {
    private static final HandlerList handlers = new HandlerList();

    public ChunkPopulateEvent(@NotNull final Chunk chunk) {
        super(chunk);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
