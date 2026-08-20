package io.papermc.paper.event.block;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

/**
 * Called when the progress of a block break is updated.
 */
public interface BlockBreakProgressUpdateEvent extends BlockEvent {

    /**
     * The progress of the block break
     * <p>
     * The progress ranges from 0.0 - 1.0, where 0 is no damage and
     * 1.0 is the most damaged
     *
     * @return The progress of the block break
     */
    float getProgress();

    /**
     * The entity breaking the block.
     *
     * @return The entity breaking the block
     */
    Entity getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
