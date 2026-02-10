package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.Nullable;

/**
 * Event that gets called each time a Hopper attempts to find its
 * source/attached containers.
 */
public interface HopperInventorySearchEvent extends BlockEventNew {

    /**
     * Gets the {@link Inventory} that the Hopper will use for its
     * source/attached Container.
     *
     * @return the inventory which will be used
     */
    @Nullable Inventory getInventory();

    /**
     * Set the {@link Inventory} that the Hopper will use for its
     * source/attached Container.
     *
     * @param inventory the inventory to use
     */
    void setInventory(@Nullable Inventory inventory);

    /**
     * Gets the Container type the Hopper is searching for.
     *
     * @return the container type being searched for
     */
    ContainerType getContainerType();

    /**
     * Gets the Block that is being searched for an inventory.
     *
     * @return block being searched for an inventory
     */
    Block getSearchBlock();

    enum ContainerType {

        /**
         * The source container the hopper is looking for.
         *
         * This is the Inventory above the Hopper where it extracts items from.
         */
        SOURCE,
        /**
         * The container the hopper is attached to.
         *
         * This is the Inventory the Hopper pushes items into.
         */
        DESTINATION
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
