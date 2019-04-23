
package org.bukkit.inventory.meta;

import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

public interface BlockStateMeta extends ItemMeta {

    /**
     * Returns whether the item has a block state currently
     * attached to it.
     *
     * @return whether a block state is already attached
     */
    boolean hasBlockState();

    /**
     * Returns the currently attached block state for this
     * item or creates a new one if one doesn't exist.
     *
     * The state is a copy, it must be set back (or to another
     * item) with {@link #setBlockState(org.bukkit.block.BlockState)}
     *
     * @return the attached state or a new state
     */
    @NotNull
    BlockState getBlockState();

    /**
     * Attaches a copy of the passed block state to the item.
     *
     * @param blockState the block state to attach to the block.
     * @throws IllegalArgumentException if the blockState is null
     *         or invalid for this item.
     */
    void setBlockState(@NotNull BlockState blockState);
}
