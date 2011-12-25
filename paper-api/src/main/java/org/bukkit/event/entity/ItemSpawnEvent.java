package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

/**
 * Called when an item is spawned into a world
 */
@SuppressWarnings("serial")
public class ItemSpawnEvent extends EntityEvent implements Cancellable {

    private Location location;
    private boolean canceled;

    public ItemSpawnEvent(Entity spawnee, Location loc) {
        super(Type.ITEM_SPAWN, spawnee);
        this.location = loc;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the location at which the item is spawning.
     *
     * @return The location at which the item is spawning
     */
    public Location getLocation() {
        return location;
    }
}
