package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

/**
 * Called when a block spreads based on world conditions.
 * <p>
 * Use {@link BlockFormEvent} to catch blocks that "randomly" form instead of
 * actually spread.
 * <p>
 * Examples:
 * <ul>
 * <li>Mushrooms spreading.
 * <li>Fire spreading.
 * </ul>
 * <p>
 * If this event is cancelled, the block will not spread.
 *
 * @see BlockFormEvent
 */
public interface BlockSpreadEvent extends BlockFormEvent {

    /**
     * Gets the source block involved in this event.
     *
     * @return the Block for the source block involved in this event.
     */
    Block getSource();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
