package org.bukkit.block.data.type;

import java.util.Set;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/// Interface to the 'has\_bottle\_0', 'has\_bottle\_1', 'has\_bottle\_2' flags on a
/// brewing stand which indicate which bottles are rendered on the outside.
///
/// Stand may have 0, 1... [#getMaximumBottles()]-1 bottles.
public interface BrewingStand extends BlockData {

    /// Checks if the stand has the following bottle
    ///
    /// @param bottle to check
    /// @return if bottle is present
    boolean hasBottle(int bottle);

    /// Set whether the stand has this bottle present.
    ///
    /// @param bottle to set
    /// @param has bottle
    void setBottle(int bottle, boolean has);

    /// Get the indexes of all the bottles present on this block.
    ///
    /// @return set of all bottles
    @NotNull
    Set<Integer> getBottles();

    /// Get the maximum amount of bottles present on this stand.
    ///
    /// @return maximum bottle count
    int getMaximumBottles();
}
