package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when a vehicle collides with an entity.
 * 
 * @author sk89q
 */
public class VehicleEntityCollisionEvent extends VehicleCollisionEvent implements Cancellable {
    private Entity entity;
    private boolean cancelled = false;
    private boolean cancelledPickup = false;
    private boolean cancelledCollision = false;
    
    public VehicleEntityCollisionEvent(Vehicle vehicle, Entity entity) {
        super(Type.VEHICLE_COLLISION_ENTITY, vehicle);
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
}
