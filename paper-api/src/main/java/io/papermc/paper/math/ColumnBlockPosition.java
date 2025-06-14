package io.papermc.paper.math;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@link BlockPosition}, which does not hold
 * a y value, always returning {@code 0} for y.
 */
@ApiStatus.Experimental
@NullMarked
public interface ColumnBlockPosition extends BlockPosition {

    @Override
    default int blockY() {
        return 0;
    }

    default ColumnBlockPosition offset(final int x, final int z) {
        return x == 0 && z == 0 ? this : new ColumnBlockPositionImpl(this.blockX() + x, this.blockZ() + z);
    }

    @Override
    default ColumnBlockPosition offset(final int x, final int y, final int z) {
        return offset(x, z);
    }

    @Override
    default ColumnFinePosition offset(final double x, final double y, final double z) {
        return offset(x, z);
    }

    default ColumnFinePosition offset(final double x, final double z) {
        return new ColumnFinePositionImpl(this.blockX() + x, this.blockZ() + z);
    }
}
