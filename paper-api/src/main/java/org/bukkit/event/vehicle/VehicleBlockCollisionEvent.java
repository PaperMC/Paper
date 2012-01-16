package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle collides with a block.
 */
@SuppressWarnings("serial")
public class VehicleBlockCollisionEvent extends VehicleCollisionEvent {
    private static final HandlerList handlers = new HandlerList();
    private Block block;

    public VehicleBlockCollisionEvent(Vehicle vehicle, Block block) {
        super(Type.VEHICLE_COLLISION_BLOCK, vehicle);
        this.block = block;
    }

    /**
     * Gets the block the vehicle collided with
     *
     * @return the block the vehicle collided with
     */
    public Block getBlock() {
        return block;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
