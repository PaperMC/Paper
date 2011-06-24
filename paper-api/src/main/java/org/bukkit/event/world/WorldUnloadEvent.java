package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.Cancellable;

/**
 * Called when a World is unloaded
 */
public class WorldUnloadEvent extends WorldEvent implements Cancellable {

    private boolean isCancelled;

    public WorldUnloadEvent(World world) {
        super(Type.WORLD_UNLOAD, world);
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return this.isCancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
