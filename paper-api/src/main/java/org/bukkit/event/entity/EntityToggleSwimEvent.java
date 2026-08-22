package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Sent when an entity's swimming status is toggled.
 */
public interface EntityToggleSwimEvent extends EntityEvent, Cancellable {

    /**
     * Returns {@code true} if the entity is now swims or
     * {@code false} if the entity stops swimming.
     *
     * @return new swimming state
     */
    boolean isSwimming();

    /**
     * @deprecated This does nothing, the server and the client doesn't work
     * correctly when the server try to bypass this. A current workaround
     * exists. If you want to cancel the switch from the ground state to the
     * swimming state you need to disable the sprinting flag for the player after
     * the cancel action.
     */
    @Deprecated
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
