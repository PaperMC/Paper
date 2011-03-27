package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * Raised when a vehicle collides with a block.
 * 
 * @author sk89q
 */
public class VehicleBlockCollisionEvent extends VehicleCollisionEvent {
    private Block block;
    
    public VehicleBlockCollisionEvent(Vehicle vehicle, Block block) {
        super(Type.VEHICLE_COLLISION_BLOCK, vehicle);
        this.block = block;
    }
    
    public Block getBlock() {
        return block;
    }
}
