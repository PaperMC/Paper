package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a chunk is unloaded
 */
public class ChunkUnloadEvent extends ChunkEvent {
    private static final HandlerList handlers = new HandlerList();
    private boolean saveChunk;

    public ChunkUnloadEvent(@NotNull final Chunk chunk) {
        this(chunk, true);
    }

    public ChunkUnloadEvent(@NotNull Chunk chunk, boolean save) {
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
