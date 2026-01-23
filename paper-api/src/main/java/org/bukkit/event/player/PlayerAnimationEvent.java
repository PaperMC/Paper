package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a player animation event
 * @see io.papermc.paper.event.player.PlayerArmSwingEvent
 */
@NullMarked
public interface PlayerAnimationEvent extends PlayerEventNew, Cancellable {

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
