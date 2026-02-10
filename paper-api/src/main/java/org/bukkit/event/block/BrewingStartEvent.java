package org.bukkit.event.block;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;

/**
 * Called when a brewing stand starts to brew.
 */
@ApiStatus.Experimental
public interface BrewingStartEvent extends InventoryBlockStartEvent {

    /**
     * Gets the total brew time associated with this event.
     *
     * @return the total brew time
     * @deprecated use {@link #getBrewingTime()} instead
     */
    @Deprecated(since = "1.21", forRemoval = true)
    default @NonNegative int getTotalBrewTime() {
        return this.getBrewingTime();
    }

    /**
     * Sets the total brew time for this event.
     *
     * @param brewTime the new total brew time
     * @deprecated use {@link #setBrewingTime(int)} instead
     */
    @Deprecated(since = "1.21", forRemoval = true)
    default void setTotalBrewTime(final @NonNegative int brewTime) {
        this.setBrewingTime(brewTime);
    }

    /**
     * Gets the amount of brewing ticks left.
     *
     * @return The amount of ticks left for the brewing task
     */
    @NonNegative int getBrewingTime();

    /**
     * Sets the brewing ticks left.
     *
     * @param brewTime the ticks left, which is no less than 0
     * @throws IllegalArgumentException if the ticks are less than 0
     */
    void setBrewingTime(@NonNegative int brewTime);

    /**
     * Gets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTime()}.
     *
     * @return recipe brew time (in ticks)
     */
    @ApiStatus.Experimental
    @Positive int getRecipeBrewTime();

    /**
     * Sets the recipe time for the brewing process which is
     * used to compute the progress of the brewing process with
     * {@link #getBrewingTime()}.
     *
     * @param recipeBrewTime recipe brew time (in ticks)
     * @throws IllegalArgumentException if the recipe brew time is non-positive
     */
    @ApiStatus.Experimental
    void setRecipeBrewTime(@Positive int recipeBrewTime);
}
