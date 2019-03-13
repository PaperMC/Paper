package org.bukkit.block.data.type;

import java.util.Set;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to the 'has_bottle_0', 'has_bottle_1', 'has_bottle_2' flags on a
 * brewing stand which indicate which bottles are rendered on the outside.
 * <br>
 * Stand may have 0, 1... {@link #getMaximumBottles()}-1 bottles.
 */
public interface BrewingStand extends BlockData {

    /**
     * Checks if the stand has the following bottle
     *
     * @param bottle to check
     * @return if bottle is present
     */
    boolean hasBottle(int bottle);

    /**
     * Set whether the stand has this bottle present.
     *
     * @param bottle to set
     * @param has bottle
     */
    void setBottle(int bottle, boolean has);

    /**
     * Get the indexes of all the bottles present on this block.
     *
     * @return set of all bottles
     */
    @NotNull
    Set<Integer> getBottles();

    /**
     * Get the maximum amount of bottles present on this stand.
     *
     * @return maximum bottle count
     */
    int getMaximumBottles();
}
