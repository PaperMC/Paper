package io.papermc.paper.math;

import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A position represented with doubles.
 * <p>
 * <b>May see breaking changes until Experimental annotation is removed.</b>
 *
 * @see BlockPosition
 */
@ApiStatus.Experimental
@NullMarked
public interface FinePosition extends Position {

    @Override
    default int blockX() {
        return NumberConversions.floor(this.x());
    }

    @Override
    default int blockY() {
        return NumberConversions.floor(this.y());
    }

    @Override
    default int blockZ() {
        return NumberConversions.floor(this.z());
    }

    @Override
    default boolean isBlock() {
        return false;
    }

    @Override
    default boolean isFine() {
        return true;
    }

    @Override
    default BlockPosition toBlock() {
        return new BlockPositionImpl(this.blockX(), this.blockY(), this.blockZ());
    }

    @Override
    default FinePosition offset(final int x, final int y, final int z) {
        return this.offset((double) x, y, z);
    }

    @Override
    default FinePosition offset(final double x, final double y, final double z) {
        return x == 0.0 && y == 0.0 && z == 0.0 ? this : new FinePositionImpl(this.x() + x, this.y() + y, this.z() + z);
    }
}
