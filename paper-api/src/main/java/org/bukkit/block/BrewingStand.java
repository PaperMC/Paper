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

    // Paper start - Add recipeBrewTime
    /**
     * Sets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTime()}.
     *
     * @param recipeBrewTime recipe brew time (in ticks)
     * @throws IllegalArgumentException if the recipe brew time is non-positive
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    void setRecipeBrewTime(@org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) int recipeBrewTime);

    /**
     * Gets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTime()}.
     *
     * @return recipe brew time (in ticks)
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    @org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) int getRecipeBrewTime();
    // Paper end - Add recipeBrewTime

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
