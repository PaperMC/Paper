package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.Location;

/**
 * Stores data for entities standing inside a portal block
 */
@SuppressWarnings("serial")
public class EntityPortalEnterEvent extends EntityEvent {

    private Location location;

    public EntityPortalEnterEvent(Entity entity, Location location) {
        super(Type.ENTITY_PORTAL_ENTER, entity);
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
}
