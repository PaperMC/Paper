package io.papermc.paper.math;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A fine position, which does not hold a y value.
 */
@ApiStatus.Experimental
@NullMarked
public interface ColumnFinePosition {

    double x();
    
    double z();
}
