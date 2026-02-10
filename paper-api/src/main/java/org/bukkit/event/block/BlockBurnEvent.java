package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a block is destroyed as a result of being burnt by fire.
 * <p>
 * If this event is cancelled, the block will not be destroyed as a
 * result of being burnt by fire.
 */
public interface BlockBurnEvent extends BlockEvent, Cancellable {

    /**
     * Gets the block which ignited this block.
     *
     * @return The Block that ignited and burned this block, or {@code null} if no
     * source block exists
     */
    @Nullable Block getIgnitingBlock();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
