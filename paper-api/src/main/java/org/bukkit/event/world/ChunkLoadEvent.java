package org.bukkit.event.world;

import org.bukkit.event.HandlerList;
import org.bukkit.generator.BlockPopulator;

/**
 * Called when a chunk is loaded
 */
public interface ChunkLoadEvent extends ChunkEvent {

    /**
     * Gets if this chunk was newly created or not.
     * <p>
     * <b>Note:</b> Do not use this to generated blocks in a newly generated chunk.
     * Use a {@link BlockPopulator} instead.
     *
     * @return {@code true} if the chunk is new, otherwise {@code false}
     */
    boolean isNewChunk();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
