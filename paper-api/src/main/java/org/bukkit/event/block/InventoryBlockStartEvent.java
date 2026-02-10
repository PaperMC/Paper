package org.bukkit.event.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Used when:
 * <ul>
 * <li>A Furnace starts smelting {@link FurnaceStartSmeltEvent}</li>
 * <li>A Brewing-Stand starts brewing {@link BrewingStartEvent}</li>
 * <li>A Campfire starts cooking {@link CampfireStartEvent}</li>
 * </ul>
 */
public interface InventoryBlockStartEvent extends BlockEvent {

    /**
     * Gets the source ItemStack for this event.
     *
     * @return the source ItemStack
     */
    ItemStack getSource();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
