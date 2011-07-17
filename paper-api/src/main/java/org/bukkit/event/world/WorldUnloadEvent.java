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

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
