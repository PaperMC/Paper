package org.bukkit.block;

import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a brewing stand.
 */
public interface BrewingStand extends BlockState, InventoryHolder {

    /**
     * How much time is left in the brewing cycle
     *
     * @return Brew Time
     */
    int getBrewingTime();

    /**
     * Set the time left before brewing completes.
     *
     * @param brewTime Brewing time
     */
    void setBrewingTime(int brewTime);

    public BrewerInventory getInventory();
}
