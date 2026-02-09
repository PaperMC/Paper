package org.bukkit.event.block;

import org.bukkit.GameEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a Sculk sensor receives a game event and hence might activate.
 * <br>
 * Will be called cancelled if the block's default behavior is to ignore the
 * event.
 */
public interface BlockReceiveGameEvent extends BlockEventNew, Cancellable {

    /**
     * Get the underlying game event.
     *
     * @return the game event
     */
    GameEvent getEvent();

    /**
     * Get the entity which triggered this game event, if present.
     *
     * @return triggering entity or {@code null}
     */
    @Nullable Entity getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
