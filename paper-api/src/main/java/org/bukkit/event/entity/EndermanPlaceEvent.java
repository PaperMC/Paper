package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

@SuppressWarnings("serial")
public class EndermanPlaceEvent extends EntityEvent implements Cancellable {

    private boolean cancel;
    private Location location;

    public EndermanPlaceEvent(Entity what, Location location) {
        super(Type.ENDERMAN_PLACE, what);
        this.location = location;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Get the location that is target of the enderman's placement.
     *
     * @return location where the enderman will place its block
     */
    public Location getLocation() {
        return location;
    }
}
