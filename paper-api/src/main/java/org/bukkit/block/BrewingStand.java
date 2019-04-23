package org.bukkit.block;

import org.bukkit.inventory.BrewerInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a brewing stand.
 */
public interface BrewingStand extends Container {

    /**
     * How much time is left in the brewing cycle.
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

    /**
     * Get the level of current fuel for brewing.
     *
     * @return The fuel level
     */
    int getFuelLevel();

    /**
     * Set the level of current fuel for brewing.
     *
     * @param level fuel level
     */
    void setFuelLevel(int level);

    @NotNull
    @Override
    BrewerInventory getInventory();

    @NotNull
    @Override
    BrewerInventory getSnapshotInventory();
}
