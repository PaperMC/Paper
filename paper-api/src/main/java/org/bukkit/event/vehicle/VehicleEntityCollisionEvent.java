package org.bukkit.event.vehicle;

import org.bukkit.Entity;
import org.bukkit.Vehicle;

/**
 * Raised when a vehicle collides with an entity.
 * 
 * @author sk89q
 */
public class VehicleEntityCollisionEvent extends VehicleCollisionEvent {
    private Entity entity;
    
    public VehicleEntityCollisionEvent(Type type, Vehicle vehicle, Entity entity) {
        super(type, vehicle);
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }
}
