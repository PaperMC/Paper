package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity combusts.
 * <p>
 * If this event is cancelled, the entity will not combust.
 */
public interface EntityCombustEvent extends EntityEventNew, Cancellable {

    /**
     * @return the amount of time (in seconds) the combustee should be alight for
     */
    float getDuration();

    /**
     * The number of seconds the combustee should be alight for.
     * <p>
     * This value will only ever increase the combustion time, not decrease
     * existing combustion times.
     *
     * @param duration the time in seconds to be alight for.
     */
    void setDuration(float duration);

    /**
     * The number of seconds the combustee should be alight for.
     * <p>
     * This value will only ever increase the combustion time, not decrease
     * existing combustion times.
     *
     * @param duration the time in seconds to be alight for.
     * @see #setDuration(float)
     * @deprecated duration is now a float
     */
    @Deprecated(since = "1.21", forRemoval = true)
    default void setDuration(final int duration) {
        this.setDuration((float) duration);
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
