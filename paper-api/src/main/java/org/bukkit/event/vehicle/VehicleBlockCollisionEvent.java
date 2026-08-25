package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 * Raised when a vehicle collides with a block.
 */
public interface VehicleBlockCollisionEvent extends VehicleCollisionEvent {

    /**
     * Gets the block the vehicle collided with
     *
     * @return the block the vehicle collided with
     */
    Block getBlock();

    /**
     * Gets velocity at which the vehicle collided with the block
     *
     * @return pre-collision moving velocity
     */
    Vector getVelocity();
}
