package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

public class ItemDespawnEvent extends EntityEvent implements Cancellable {
    private boolean canceled;
    private Location location;

    public ItemDespawnEvent(Entity spawnee, Location loc) {
        super(Type.ITEM_DESPAWN, spawnee);
        location = loc;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the location at which the item is despawning.
     *
     * @return The location at which the item is despawning
     */
    public Location getLocation() {
        return location;
    }
}
