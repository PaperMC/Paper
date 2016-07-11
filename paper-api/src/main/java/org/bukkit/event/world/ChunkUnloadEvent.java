package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a chunk is unloaded
 */
public class ChunkUnloadEvent extends ChunkEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private boolean saveChunk;

    public ChunkUnloadEvent(final Chunk chunk) {
        this(chunk, true);
    }

    public ChunkUnloadEvent(Chunk chunk, boolean save) {
        super(chunk);
        this.saveChunk = save;
    }

    /**
     * Return whether this chunk will be saved to disk.
     *
     * @return chunk save status
     */
    public boolean isSaveChunk() {
        return saveChunk;
    }

    /**
     * Set whether this chunk will be saved to disk.
     *
     * @param saveChunk chunk save status
     */
    public void setSaveChunk(boolean saveChunk) {
        this.saveChunk = saveChunk;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
