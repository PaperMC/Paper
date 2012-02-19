package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

/**
 * Stores data for entities standing inside a portal block
 */
public class EntityPortalEnterEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location location;

    public EntityPortalEnterEvent(final Entity entity, final Location location) {
        super(entity);
        this.location = location;
    }

    /**
     * Gets the portal block the entity is touching
     *
     * @return The portal block the entity is touching
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
