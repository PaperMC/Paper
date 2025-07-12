package io.papermc.paper.math;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A block position, which does not have a y value.
 */
@ApiStatus.Experimental
@NullMarked
public interface ColumnBlockPosition {
    
    int blockX();
    
    int blockZ();
}
