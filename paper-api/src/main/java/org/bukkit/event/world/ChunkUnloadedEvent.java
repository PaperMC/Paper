
package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;

/**
 * Called when a chunk is unloaded
 */
public class ChunkUnloadedEvent extends ChunkLoadedEvent implements Cancellable {
    private boolean cancel = false;

    public ChunkUnloadedEvent(final Type type, final Chunk chunk) {
        super(type, chunk);
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
