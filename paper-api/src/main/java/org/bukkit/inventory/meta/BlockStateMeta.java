
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

    // Paper start - add method to clear block state
    /**
     * Clears the block state currently attached to this item.
     */
    void clearBlockState();
    // Paper end - add method to clear block state

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
     *
     * @apiNote As of 1.20.5 the block state carries a copy of the item's data deviations.
     * As such, setting the block state via this method will reset secondary deviations of the item meta.
     * This can manifest in the addition to an existing lore failing or a change of a previously added display name.
     * It is hence recommended to first mutate the block state, set it back, and then mutate the item meta.
     */
    void setBlockState(@NotNull BlockState blockState);
}
