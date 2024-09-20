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
    private int recipeBrewTime = 400; // Paper - Add recipeBrewTime

    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public BrewingStartEvent(@NotNull final Block furnace, @NotNull ItemStack source, int brewingTime) {
        super(furnace, source);
        this.brewingTime = brewingTime;
    }

    /**
     * Gets the total brew time associated with this event.
     *
     * @return the total brew time
     * @deprecated use {@link #getBrewingTime()} instead
     */
    @Deprecated(since = "1.21", forRemoval = true) // Paper
    public int getTotalBrewTime() {
        return brewingTime;
    }

    /**
     * Sets the total brew time for this event.
     *
     * @param brewTime the new total brew time
     * @deprecated use {@link #setBrewingTime(int)} instead
     */
    @Deprecated(since = "1.21", forRemoval = true) // Paper
    public void setTotalBrewTime(int brewTime) {
        this.setBrewingTime(brewTime); // Paper - delegate to new method
    }

    // Paper - remove HandlerList

    // Paper start - add recipeBrewTime
    /**
     * Gets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTime()}.
     *
     * @return recipe brew time (in ticks)
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public @org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) int getRecipeBrewTime() {
        return this.recipeBrewTime;
    }

    /**
     * Sets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTime()}.
     *
     * @param recipeBrewTime recipe brew time (in ticks)
     * @throws IllegalArgumentException if the recipe brew time is non-positive
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public void setRecipeBrewTime(@org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) int recipeBrewTime) {
        com.google.common.base.Preconditions.checkArgument(recipeBrewTime > 0, "recipeBrewTime must be positive");
        this.recipeBrewTime = recipeBrewTime;
    }

    /**
     * Gets the amount of brewing ticks left.
     *
     * @return The amount of ticks left for the brewing task
     */
    public @org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE) int getBrewingTime() {
        return this.brewingTime;
    }

    /**
     * Sets the brewing ticks left.
     *
     * @param brewTime the ticks left, which is no less than 0
     * @throws IllegalArgumentException if the ticks are less than 0
     */
    public void setBrewingTime(@org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE) int brewTime) {
        com.google.common.base.Preconditions.checkArgument(brewTime >= 0, "brewTime must be non-negative");
        this.brewingTime = brewTime;
    }
    // Paper end - add recipeBrewTime
}
