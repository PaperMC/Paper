package io.papermc.paper.command.brigadier.argument.position;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A block position, which does not have a y value.
 */
@ApiStatus.Experimental
@NullMarked
public interface ColumnBlockPosition {

    /**
     * Gets the X-coordinate of the block position.
     *
     * @return the block's X-coordinate
     */
    int blockX();

    /**
     * Gets the Z-coordinate of the block position.
     *
     * @return the block's Z-coordinate
     */
    int blockZ();

    /**
     * Converts this column-based position into a full 3D {@link BlockPosition}
     * by supplying a Y-coordinate.
     *
     * @param y the Y-coordinate to include in the new position
     * @return a {@link BlockPosition} representing the full 3D position
     */
    default BlockPosition toPosition(final int y) {
        return Position.block(this.blockX(), y, this.blockZ());
    }

}
