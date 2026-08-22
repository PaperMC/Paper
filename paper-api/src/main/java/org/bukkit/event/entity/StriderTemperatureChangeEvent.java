package org.bukkit.event.entity;

import org.bukkit.entity.Strider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link Strider}'s temperature has changed as a result of
 * entering or exiting blocks it considers warm.
 */
public interface StriderTemperatureChangeEvent extends EntityEvent, Cancellable {

    @Override
    Strider getEntity();

    /**
     * Get the Strider's new shivering state.
     *
     * @return the new shivering state
     */
    boolean isShivering();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
