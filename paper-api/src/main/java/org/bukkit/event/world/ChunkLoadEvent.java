package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a chunk is loaded
 */
public class ChunkLoadEvent extends ChunkEvent {
    private static final HandlerList handlers = new HandlerList();
    private final boolean newChunk;

    public ChunkLoadEvent(@NotNull final Chunk chunk, final boolean newChunk) {
        super(chunk);
        this.newChunk = newChunk;
    }

    /**
     * Gets if this chunk was newly created or not.
     * <p>
     * Note that if this chunk is new, it will not be populated at this time.
     *
     * @return true if the chunk is new, otherwise false
     */
    public boolean isNewChunk() {
        return newChunk;
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
