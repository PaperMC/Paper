package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle collides with an entity.
 */
public class VehicleEntityCollisionEvent extends VehicleCollisionEvent implements Cancellable {

    private final Entity entity;
    private boolean cancelledPickup;
    private boolean cancelledCollision;

    private boolean cancelled;

    @ApiStatus.Internal
    public VehicleEntityCollisionEvent(@NotNull final Vehicle vehicle, @NotNull final Entity entity) {
        super(vehicle);
        this.entity = entity;
    }

    @NotNull
    public Entity getEntity() {
        return this.entity;
    }

    @Deprecated(forRemoval = true)
    public boolean isPickupCancelled() {
        return this.cancelledPickup;
    }

    @Deprecated(forRemoval = true)
    public void setPickupCancelled(boolean cancel) {
        this.cancelledPickup = cancel;
    }

    @Deprecated(forRemoval = true)
    public boolean isCollisionCancelled() {
        return this.cancelledCollision;
    }

    @Deprecated(forRemoval = true)
    public void setCollisionCancelled(boolean cancel) {
        this.cancelledCollision = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
