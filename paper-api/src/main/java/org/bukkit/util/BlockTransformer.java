package org.bukkit.util;

import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A BlockTransformer is used to modify blocks that are placed by structure.
 */
@FunctionalInterface
@ApiStatus.Experimental
public interface BlockTransformer {

    /**
     * The TransformationState allows access to the original block state and the
     * block state of the block that was at the location of the transformation
     * in the world before the transformation started.
     */
    public static interface TransformationState {

        /**
         * Creates a clone of the original block state that a structure wanted
         * to place and caches it for the current transformer.
         *
         * @return a clone of the original block state.
         */
        @NotNull
        BlockState getOriginal();

        /**
         * Creates a clone of the block state that was at the location of the
         * currently modified block at the start of the transformation process
         * and caches it for the current transformer.
         *
         * @return a clone of the world block state.
         */
        @NotNull
        BlockState getWorld();

    }

    /**
     * Transforms a block in a structure.
     *
     * NOTE: The usage of {@link BlockData#createBlockState()} can provide even
     * more flexibility to return the exact block state you might want to
     * return.
     *
     * @param region the accessible region
     * @param x the x position of the block
     * @param y the y position of the block
     * @param z the z position of the block
     * @param current the state of the block that should be placed
     * @param state the state of this transformation.
     *
     * @return the new block state
     */
    @NotNull
    BlockState transform(@NotNull LimitedRegion region, int x, int y, int z, @NotNull BlockState current, @NotNull TransformationState state);
}
