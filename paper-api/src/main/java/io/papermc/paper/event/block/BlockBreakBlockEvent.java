package io.papermc.paper.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a block forces another block to break and drop items.
 * <p>
 * Currently called for piston's and liquid flows.
 */
public interface BlockBreakBlockEvent extends BlockExpEvent {

    /**
     * Gets the block that cause this (e.g. a piston, or adjacent liquid)
     *
     * @return the source
     */
    Block getSource();

    /**
     * Gets a mutable list of drops for this event
     *
     * @return the drops
     */
    List<ItemStack> getDrops();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
