package org.bukkit.event.inventory;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

/**
 * Called when an ItemStack is successfully burned as fuel in a furnace-like block such as a
 * {@link org.bukkit.block.Furnace}, {@link org.bukkit.block.Smoker}, or
 * {@link org.bukkit.block.BlastFurnace}.
 */
public interface FurnaceBurnEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the fuel ItemStack for this event
     *
     * @return the fuel ItemStack
     */
    ItemStack getFuel();

    /**
     * Gets the burn time for this fuel
     *
     * @return the burn time for this fuel
     */
    int getBurnTime();

    /**
     * Sets the burn time for this fuel
     *
     * @param burnTime the burn time for this fuel
     */
    void setBurnTime(@Range(from = Short.MIN_VALUE, to = Short.MAX_VALUE) int burnTime);

    /**
     * Gets whether the furnace's fuel is burning or not.
     *
     * @return whether the furnace's fuel is burning or not.
     */
    boolean isBurning();

    /**
     * Sets whether the furnace's fuel is burning or not.
     *
     * @param burning {@code true} if the furnace's fuel is burning
     */
    void setBurning(boolean burning);

    /**
     * Gets whether the furnace's fuel will be consumed or not.
     *
     * @return whether the furnace's fuel will be consumed
     */
    boolean willConsumeFuel();

    /**
     * Sets whether the furnace's fuel will be consumed or not.
     *
     * @param consumeFuel {@code true} to consume the fuel
     */
    void setConsumeFuel(boolean consumeFuel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
