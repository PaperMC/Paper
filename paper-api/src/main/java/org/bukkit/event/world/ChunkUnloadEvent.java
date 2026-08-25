package org.bukkit.event.world;

import org.bukkit.event.HandlerList;

/**
 * Called when a chunk is unloaded
 */
public interface ChunkUnloadEvent extends ChunkEvent {

    /**
     * Return whether this chunk will be saved to disk.
     *
     * @return chunk save status
     */
    boolean isSaveChunk();

    /**
     * Set whether this chunk will be saved to disk.
     *
     * @param saveChunk chunk save status
     */
    void setSaveChunk(boolean saveChunk);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
