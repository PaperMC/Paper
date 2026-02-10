package org.bukkit.event.block;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a sponge absorbs water from the world.
 * <br>
 * The world will be in its previous state, and {@link #getBlocks()} will
 * represent the changes to be made to the world, if the event is not cancelled.
 * <br>
 * As this is a physics based event it may be called multiple times for "the
 * same" changes.
 */
public interface SpongeAbsorbEvent extends BlockEventNew, Cancellable {

    /**
     * Get a list of all blocks to be cleared by the sponge.
     * <br>
     * This list is mutable and contains the blocks in their removed state, i.e.
     * having a type of {@link Material#AIR} or not waterlogged.
     *
     * @return list of the cleared blocks.
     */
    List<BlockState> getBlocks();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
