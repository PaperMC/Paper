package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a brewing stand starts to brew.
 */
@org.jetbrains.annotations.ApiStatus.Experimental // Paper
public class BrewingStartEvent extends InventoryBlockStartEvent {

    // Paper - remove HandlerList
    private int brewingTime;

    public BrewingStartEvent(@NotNull final Block furnace, @NotNull ItemStack source, int brewingTime) {
        super(furnace, source);
        this.brewingTime = brewingTime;
    }

    /**
     * Gets the total brew time associated with this event.
     *
     * @return the total brew time
     */
    public int getTotalBrewTime() {
        return brewingTime;
    }

    /**
     * Sets the total brew time for this event.
     *
     * @param brewTime the new total brew time
     */
    public void setTotalBrewTime(int brewTime) {
        this.brewingTime = brewTime;
    }

    // Paper - remove HandlerList
}
