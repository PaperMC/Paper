package io.papermc.paper.command.brigadier.argument.position;

import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A fine position, which does not hold a y value.
 */
@ApiStatus.Experimental
@NullMarked
public interface ColumnFinePosition {

    /**
     * Gets the X-coordinate of this position.
     *
     * @return the X-coordinate as a double
     */
    double x();

    /**
     * Gets the Z-coordinate of this position.
     *
     * @return the Z-coordinate as a double
     */
    double z();

    /**
     * Converts this column-based position into a full 3D {@link FinePosition}
     * by supplying a Y-coordinate.
     *
     * @param y the Y-coordinate to include in the new position
     * @return a {@link FinePosition} representing the full 3D position
     */
    default FinePosition toPosition(final double y) {
        return Position.fine(this.x(), y, this.z());
    }
}
