package org.bukkit.event.inventory;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an item is about to increase the fuel level of a brewing
 * stand.
 */
public interface BrewingStandFuelEvent extends BlockEvent, Cancellable {

    /**
     * Gets the item of the fuel before the amount was subtracted.
     *
     * @return the fuel item
     */
    ItemStack getFuel();

    /**
     * Gets the fuel power for this fuel. Each unit of power can fuel one
     * brewing operation.
     *
     * @return the fuel power for this fuel
     */
    int getFuelPower();

    /**
     * Sets the fuel power for this fuel. Each unit of power can fuel one
     * brewing operation.
     *
     * @param fuelPower the fuel power for this fuel
     */
    void setFuelPower(int fuelPower);

    /**
     * Gets whether the brewing stand's fuel will be reduced / consumed or not.
     *
     * @return whether the fuel will be reduced or not
     */
    boolean isConsuming();

    /**
     * Sets whether the brewing stand's fuel will be reduced / consumed or not.
     *
     * @param consuming whether the fuel will be reduced or not
     */
    void setConsuming(boolean consuming);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
