package org.bukkit.event.block;

import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block grows naturally in the world.
 * <p>
 * Examples:
 * <ul>
 * <li>Wheat
 * <li>Sugar Cane
 * <li>Cactus
 * <li>Watermelon
 * <li>Pumpkin
 * <li>Turtle Egg
 * </ul>
 * <p>
 * If this event is cancelled, the block will not grow.
 */
public interface BlockGrowEvent extends BlockEvent, Cancellable {

    /**
     * Gets the state of the block where it will form or spread to.
     *
     * @return The block state for this events block
     */
    BlockState getNewState();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
