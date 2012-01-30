package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 *
 * @deprecated Use EntityChangeBlockEvent instead
 *
 */
@SuppressWarnings("serial")
@Deprecated
public class EndermanPlaceEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel;
    private final Location location;

    public EndermanPlaceEvent(final Entity what, final Location location) {
        super(what);
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
