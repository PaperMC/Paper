package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
/**
 * Called when an ItemStack is successfully burned as fuel in a furnace.
 */
public class FurnaceBurnEvent extends Event implements Cancellable {
    private Block furnace;
    private ItemStack fuel;
    private int burnTime;
    private boolean cancelled;

    public FurnaceBurnEvent(Block furnace, ItemStack fuel, int burnTime) {
        super(Type.FURNACE_BURN);

        this.furnace = furnace;
        this.fuel = fuel;
        this.burnTime = burnTime;
        this.cancelled = false;
    }

    /**
     * Gets the block for the furnace involved in this event
     *
     * @return the block of the furnace
     */
    public Block getFurnace() {
        return furnace;
    }

    /**
     * Gets the fuel ItemStack for this event
     *
     * @return the fuel ItemStack
     */
    public ItemStack getFuel() {
        return fuel;
    }

    /**
     * Gets the default burn time for this fuel
     *
     * @return the default burn time for this fuel
     */
    public int getBurnTime() {
        return burnTime;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
