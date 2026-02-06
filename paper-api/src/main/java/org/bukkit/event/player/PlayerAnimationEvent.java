package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Represents a player animation event
 * @see io.papermc.paper.event.player.PlayerArmSwingEvent
 */
public interface PlayerAnimationEvent extends PlayerEvent, Cancellable {

    /**
     * Get the type of this animation event
     *
     * @return the animation type
     */
    PlayerAnimationType getAnimationType();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
