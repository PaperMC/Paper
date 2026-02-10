package org.bukkit.event.block;

import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block fades, melts or disappears based on world conditions
 * <p>
 * Examples:
 * <ul>
 * <li>Snow melting due to being near a light source.
 * <li>Ice melting due to being near a light source.
 * <li>Fire burning out after time, without destroying fuel block.
 * <li>Coral fading to dead coral due to lack of water</li>
 * <li>Turtle Egg bursting when a turtle hatches</li>
 * </ul>
 * <p>
 * If this event is cancelled, the block will not fade, melt or
 * disappear.
 */
public interface BlockFadeEvent extends BlockEvent, Cancellable {

    /**
     * Gets the state of the new block that will replace the block
     * fading, melting or disappearing.
     *
     * @return The block state of the new block that replaces the block
     *     fading, melting or disappearing
     */
    BlockState getNewState();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
