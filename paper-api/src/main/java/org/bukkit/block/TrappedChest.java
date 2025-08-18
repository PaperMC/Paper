package org.bukkit.block;

import org.checkerframework.common.value.qual.IntRange;

/**
 * Represents a captured state of a Trapped Chest.
 */
public interface TrappedChest extends Chest {
    /**
     * Gets the current redstone power level emitted by this trapped chest.
     *
     * @return the redstone power level (0–15)
     */
    int getPower();

    /**
     * Checks if the power level is being forced rather than determined
     * dynamically (for example by player interaction).
     *
     * @return {@code true} if the power is forced, {@code false} otherwise
     */
    boolean isForcedPower();

    /**
     * Sets a forced redstone power level for this trapped chest.
     * <p>
     * When a non-null value is provided, the trapped chest will emit
     * the given power level regardless of its normal behavior.
     * When {@code null} is passed, the chest will revert to its default
     * dynamic behavior (power depending on open state and players nearby).
     *
     * @param power the forced power level (0–15), or {@code null} to disable forcing
     */
    void setForcedPower(@org.jspecify.annotations.Nullable @IntRange(from = 0, to = 15) Integer power);
}
