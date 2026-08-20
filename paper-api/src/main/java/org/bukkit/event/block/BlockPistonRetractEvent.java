package org.bukkit.event.block;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called when a piston retracts
 */
public interface BlockPistonRetractEvent extends BlockPistonEvent {

    /**
     * Gets the location where the possible moving block might be if the
     * retracting piston is sticky.
     *
     * @return The possible location of the possibly moving block.
     */
    @Deprecated(since = "1.8")
    default Location getRetractLocation() {
        return this.getBlock().getRelative(getDirection(), 2).getLocation();
    }

    /**
     * Get an immutable list of the blocks which will be moved by the
     * retracting.
     *
     * @return Immutable list of the moved blocks.
     */
    @Unmodifiable List<Block> getBlocks();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
