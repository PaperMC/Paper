package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle collides with an entity.
 */
public class VehicleEntityCollisionEvent extends VehicleCollisionEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;
    private boolean cancelled = false;
    private boolean cancelledPickup = false;
    private boolean cancelledCollision = false;

    public VehicleEntityCollisionEvent(final Vehicle vehicle, final Entity entity) {
        super(vehicle);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isPickupCancelled() {
        return cancelledPickup;
    }

    public void setPickupCancelled(boolean cancel) {
        cancelledPickup = cancel;
    }

    public boolean isCollisionCancelled() {
        return cancelledCollision;
    }

    public void setCollisionCancelled(boolean cancel) {
        cancelledCollision = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
