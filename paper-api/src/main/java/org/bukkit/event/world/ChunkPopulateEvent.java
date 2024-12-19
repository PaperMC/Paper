package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a newly generated chunk has finished being populated.
 * <p>
 * <b>Note:</b> Do not use this to generated blocks in a newly generated chunk.
 * Use a {@link BlockPopulator} instead.
 *
 * @since 1.0.0 R1
 */
public class ChunkPopulateEvent extends ChunkEvent {
    private static final HandlerList handlers = new HandlerList();

    public ChunkPopulateEvent(@NotNull final Chunk chunk) {
        super(chunk);
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
