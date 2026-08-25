package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

public class CraftVehicleEntityCollisionEvent extends CraftVehicleCollisionEvent implements VehicleEntityCollisionEvent {

    private final Entity entity;
    private boolean cancelledPickup;
    private boolean cancelledCollision;

    private boolean cancelled;

    public CraftVehicleEntityCollisionEvent(final Vehicle vehicle, final Entity entity) {
        super(vehicle);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    @Deprecated(forRemoval = true)
    public boolean isPickupCancelled() {
        return this.cancelledPickup;
    }

    @Override
    @Deprecated(forRemoval = true)
    public void setPickupCancelled(final boolean cancel) {
        this.cancelledPickup = cancel;
    }

    @Override
    @Deprecated(forRemoval = true)
    public boolean isCollisionCancelled() {
        return this.cancelledCollision;
    }

    @Override
    @Deprecated(forRemoval = true)
    public void setCollisionCancelled(final boolean cancel) {
        this.cancelledCollision = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
