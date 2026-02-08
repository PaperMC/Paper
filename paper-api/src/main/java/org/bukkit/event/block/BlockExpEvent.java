package org.bukkit.event.block;

import org.bukkit.event.HandlerList;

/**
 * An event that's called when a block yields experience.
 */
public interface BlockExpEvent extends BlockEventNew {

    /**
     * Get the experience dropped by the block after the event has processed
     *
     * @return The experience to drop
     */
    int getExpToDrop();

    /**
     * Set the amount of experience dropped by the block after the event has
     * processed
     *
     * @param exp 1 or higher to drop experience, else nothing will drop
     */
    void setExpToDrop(int exp);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
