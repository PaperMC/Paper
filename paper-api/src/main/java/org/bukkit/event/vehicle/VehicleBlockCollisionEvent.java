package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle collides with a block.
 */
public class VehicleBlockCollisionEvent extends VehicleCollisionEvent {

    private final Block block;
    private final Vector velocity;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public VehicleBlockCollisionEvent(@NotNull final Vehicle vehicle, @NotNull final Block block) {
        this(vehicle, block, vehicle.getVelocity());
    }

    @ApiStatus.Internal
    public VehicleBlockCollisionEvent(@NotNull final Vehicle vehicle, @NotNull final Block block, @NotNull final Vector velocity) {
        super(vehicle);
        this.block = block;
        this.velocity = velocity;
    }

    /**
     * Gets the block the vehicle collided with
     *
     * @return the block the vehicle collided with
     */
    @NotNull
    public Block getBlock() {
        return this.block;
    }

    /**
     * Gets velocity at which the vehicle collided with the block
     *
     * @return pre-collision moving velocity
     */
    @NotNull
    public Vector getVelocity() {
        return this.velocity.clone();
    }
}
