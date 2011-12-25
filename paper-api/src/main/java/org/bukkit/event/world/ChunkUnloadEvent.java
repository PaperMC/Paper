package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;

/**
 * Called when a chunk is unloaded
 */
@SuppressWarnings("serial")
public class ChunkUnloadEvent extends ChunkEvent implements Cancellable {
    private boolean cancel = false;

    public ChunkUnloadEvent(final Chunk chunk) {
        super(Type.CHUNK_UNLOAD, chunk);
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
