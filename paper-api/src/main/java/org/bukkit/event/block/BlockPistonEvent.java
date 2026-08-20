package org.bukkit.event.block;

import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Called when a piston block is triggered
 */
@ApiStatus.NonExtendable
public interface BlockPistonEvent extends BlockEvent, Cancellable {

    /**
     * Returns {@code true} if the Piston in the event is sticky.
     *
     * @return stickiness of the piston
     */
    boolean isSticky();

    /**
     * Return the direction in which the piston will operate.
     *
     * @return direction of the piston
     */
    BlockFace getDirection();
}
