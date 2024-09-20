package org.bukkit.inventory.view;

import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * brewing stand view data.
 */
public interface BrewingStandView extends InventoryView {

    @NotNull
    @Override
    BrewerInventory getTopInventory();

    /**
     * Gets the fuel level of this brewing stand.
     * <p>
     * The default maximum fuel level in minecraft is 20.
     *
     * @return The amount of fuel level left
     */
    int getFuelLevel();

    /**
     * Gets the amount of brewing ticks left.
     *
     * @return The amount of ticks left for the brewing task
     */
    int getBrewingTicks();

    /**
     * Sets the fuel level left.
     *
     * @param level the level of the fuel, which is no less than 0
     * @throws IllegalArgumentException if the level is less than 0
     */
    void setFuelLevel(final int level) throws IllegalArgumentException;

    /**
     * Sets the brewing ticks left.
     *
     * @param ticks the ticks left, which is no less than 0
     * @throws IllegalArgumentException if the ticks are less than 0
     */
    void setBrewingTicks(final int ticks) throws IllegalArgumentException;

    // Paper start - Add recipeBrewTime
    /**
     * Sets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTicks()}.
     *
     * @param recipeBrewTime recipe brew time (in ticks)
     * @throws IllegalArgumentException if the recipe brew time is non-positive
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    void setRecipeBrewTime(@org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) int recipeBrewTime);

    /**
     * Gets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTicks()}.
     *
     * @return recipe brew time (in ticks)
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    @org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) int getRecipeBrewTime();
    // Paper end - Add recipeBrewTime
}
