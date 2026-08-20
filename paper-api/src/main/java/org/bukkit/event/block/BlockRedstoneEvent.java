package org.bukkit.event.block;

import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

/**
 * Called when a redstone current changes.
 * <p>
 * It includes the relevant mutation of the {@code powered} and {@code power}
 * properties even if the block is not able to produce a redstone signal.
 * For the {@code powered} property, a high state will be considered as
 * a current of 15 and a low state as 0. Setting the new current to a different
 * value will prevent most action in this case.
 */
public interface BlockRedstoneEvent extends BlockEventNew {

    /**
     * Gets the old current of this block.
     *
     * @return the previous current
     */
    @IntRange(from = 0, to = 15) int getOldCurrent();

    /**
     * Gets the new current of this block.
     *
     * @return the new current
     */
    @IntRange(from = 0, to = 15) int getNewCurrent();

    /**
     * Sets the new current of this block.
     *
     * @param newCurrent the new current to set
     */
    void setNewCurrent(@IntRange(from = 0, to = 15) int newCurrent);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
