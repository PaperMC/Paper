package org.bukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called when a piston extends
 */
public interface BlockPistonExtendEvent extends BlockPistonEvent {

    /**
     * Get the amount of blocks which will be moved while extending.
     *
     * @return the amount of moving blocks
     * @deprecated slime blocks make the value of this method
     *          inaccurate due to blocks being pushed at the side
     */
    @Deprecated(since = "1.8")
    default int getLength() {
        return this.getBlocks().size();
    }

    /**
     * Get an immutable list of the blocks which will be moved by the
     * extending.
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
