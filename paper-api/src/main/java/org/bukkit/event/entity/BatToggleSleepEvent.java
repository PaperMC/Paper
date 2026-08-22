package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a bat attempts to sleep or wake up from its slumber.
 * <p>
 * If this event is cancelled, the Bat will not toggle its sleep
 * state.
 */
public interface BatToggleSleepEvent extends EntityEvent, Cancellable {

    /**
     * Get whether the bat is attempting to awaken.
     *
     * @return {@code true} if trying to awaken, {@code false} otherwise
     */
    boolean isAwake();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
