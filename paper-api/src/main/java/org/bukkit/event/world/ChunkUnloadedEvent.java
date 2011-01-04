
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

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
