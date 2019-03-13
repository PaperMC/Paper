package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle collides with a block.
 */
public class VehicleBlockCollisionEvent extends VehicleCollisionEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;

    public VehicleBlockCollisionEvent(@NotNull final Vehicle vehicle, @NotNull final Block block) {
        super(vehicle);
        this.block = block;
    }

    /**
     * Gets the block the vehicle collided with
     *
     * @return the block the vehicle collided with
     */
    @NotNull
    public Block getBlock() {
        return block;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
