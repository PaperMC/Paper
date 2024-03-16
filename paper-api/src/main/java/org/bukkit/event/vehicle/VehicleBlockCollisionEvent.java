package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle collides with a block.
 */
public class VehicleBlockCollisionEvent extends VehicleCollisionEvent {
    private final Block block;
    private final org.bukkit.util.Vector velocity; // Paper

    // Paper start - Add pre-collision velocity
    @Deprecated
    public VehicleBlockCollisionEvent(@NotNull final Vehicle vehicle, @NotNull final Block block) {
        this(vehicle, block, vehicle.getVelocity());
    }

    public VehicleBlockCollisionEvent(@NotNull final Vehicle vehicle, @NotNull final Block block, @NotNull final org.bukkit.util.Vector velocity) { // Paper - Added velocity
        super(vehicle);
        this.block = block;
        this.velocity = velocity;
    }

    /**
     * Gets velocity at which the vehicle collided with the block
     *
     * @return pre-collision moving velocity
     */
    @NotNull
    public org.bukkit.util.Vector getVelocity() {
        return velocity.clone();
    }
    // Paper end

    /**
     * Gets the block the vehicle collided with
     *
     * @return the block the vehicle collided with
     */
    @NotNull
    public Block getBlock() {
        return block;
    }
}
