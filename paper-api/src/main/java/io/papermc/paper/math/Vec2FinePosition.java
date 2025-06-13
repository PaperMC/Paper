package io.papermc.paper.math;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@link FinePosition}, which does not hold
 * a y value, always returning {@code 0} for y.
 */
@ApiStatus.Experimental
@NullMarked
public interface Vec2FinePosition extends FinePosition {

    @Override
    default double y() {
        return 0;
    }

    @Override
    default Vec2FinePosition offset(int x, int y, int z) {
        return offset(x, z);
    }

    @Override
    default Vec2FinePosition offset(double x, double y, double z) {
        return offset(x, z);
    }

    default Vec2FinePosition offset(final int x, final int z) {
        return this.offset((double) x, z);
    }

    default Vec2FinePosition offset(final double x, final double z) {
        return x == 0.0 && z == 0.0 ? this : new Vec2FinePositionImpl(this.x() + x, this.z() + z);
    }
}
