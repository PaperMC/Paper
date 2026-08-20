package io.papermc.paper.event.block;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the progress of a block break is updated.
 */
@NullMarked
public interface BlockBreakProgressUpdateEvent extends BlockEventNew {

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
