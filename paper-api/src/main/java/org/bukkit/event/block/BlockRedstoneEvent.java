package org.bukkit.event.block;

import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

/**
 * Called when a redstone current changes
 */
public interface BlockRedstoneEvent extends BlockEventNew {

    /**
     * Gets the old current of this block
     *
     * @return The previous current
     */
    @IntRange(from = 0, to = 15) int getOldCurrent();

    /**
     * Gets the new current of this block
     *
     * @return The new current
     */
    @IntRange(from = 0, to = 15) int getNewCurrent();

    /**
     * Sets the new current of this block
     *
     * @param newCurrent The new current to set
     */
    void setNewCurrent(@IntRange(from = 0, to = 15) int newCurrent);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
