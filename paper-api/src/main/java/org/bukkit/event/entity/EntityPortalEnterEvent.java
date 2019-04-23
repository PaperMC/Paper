package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity comes into contact with a portal
 */
public class EntityPortalEnterEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location location;

    public EntityPortalEnterEvent(@NotNull final Entity entity, @NotNull final Location location) {
        super(entity);
        this.location = location;
    }

    /**
     * Gets the portal block the entity is touching
     *
     * @return The portal block the entity is touching
     */
    @NotNull
    public Location getLocation() {
        return location;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
