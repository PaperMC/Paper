package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Called when a brewing stand starts to brew.
 */
@ApiStatus.Experimental // Paper
public class BrewingStartEvent extends InventoryBlockStartEvent {

    private int brewingTime;
    private int recipeBrewTime = 400;

    @ApiStatus.Internal
    public BrewingStartEvent(@NotNull final Block brewingStand, @NotNull ItemStack source, int brewingTime) {
        super(brewingStand, source);
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
        return this.brewingTime;
    }

    /**
     * Sets the total brew time for this event.
     *
     * @param brewTime the new total brew time
     * @deprecated use {@link #setBrewingTime(int)} instead
     */
    @Deprecated(since = "1.21", forRemoval = true) // Paper
    public void setTotalBrewTime(int brewTime) {
        this.setBrewingTime(brewTime);
    }

    /**
     * Gets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTime()}.
     *
     * @return recipe brew time (in ticks)
     */
    @ApiStatus.Experimental
    public @Range(from = 1, to = Integer.MAX_VALUE) int getRecipeBrewTime() {
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
    @ApiStatus.Experimental
    public void setRecipeBrewTime(@Range(from = 1, to = Integer.MAX_VALUE) int recipeBrewTime) {
        Preconditions.checkArgument(recipeBrewTime > 0, "recipeBrewTime must be positive");
        this.recipeBrewTime = recipeBrewTime;
    }

    /**
     * Gets the amount of brewing ticks left.
     *
     * @return The amount of ticks left for the brewing task
     */
    public @Range(from = 0, to = Integer.MAX_VALUE) int getBrewingTime() {
        return this.brewingTime;
    }

    /**
     * Sets the brewing ticks left.
     *
     * @param brewTime the ticks left, which is no less than 0
     * @throws IllegalArgumentException if the ticks are less than 0
     */
    public void setBrewingTime(@Range(from = 0, to = Integer.MAX_VALUE) int brewTime) {
        Preconditions.checkArgument(brewTime >= 0, "brewTime must be non-negative");
        this.brewingTime = brewTime;
    }
}
