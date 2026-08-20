package org.bukkit.event.block;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a bell is being rung.
 */
public interface BellRingEvent extends BlockEventNew, Cancellable {

    /**
     * Get the direction in which the bell was rung.
     *
     * @return the direction
     */
    BlockFace getDirection();

    /**
     * Get the {@link Entity} that rang the bell (if there was one).
     *
     * @return the entity
     */
    @Nullable Entity getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
