package org.bukkit.event.vehicle;

import org.bukkit.event.block.BlockEvent;
import org.bukkit.util.Vector;

/**
 * Raised when a vehicle collides with a block.
 */
public interface VehicleBlockCollisionEvent extends VehicleCollisionEvent, BlockEvent {

    /**
     * Gets velocity at which the vehicle collided with the block
     *
     * @return pre-collision moving velocity
     */
    Vector getVelocity();
}
