package org.bukkit.event.player;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * This event is called whenever a player harvests a block.
 * <br>
 * A 'harvest' is when a block drops an item (usually some sort of crop) and
 * changes state, but is not broken in order to drop the item.
 * <br>
 * This event is not called for when a block is broken, to handle that, listen
 * for {@link org.bukkit.event.block.BlockBreakEvent} and
 * {@link org.bukkit.event.block.BlockDropItemEvent}.
 */
public interface PlayerHarvestBlockEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the block that is being harvested.
     *
     * @return The block that is being harvested
     */
    Block getHarvestedBlock();

    /**
     * Get the hand used to harvest the block.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    /**
     * Gets a list of items that are being harvested from this block.
     *
     * @return A list of items that are being harvested from this block
     */
    List<ItemStack> getItemsHarvested();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
