package io.papermc.paper.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player has slept long enough
 * to count as passing the night/storm.
 * <p>
 * Cancelling this event will prevent the player from being counted as deeply sleeping
 * unless they exit and re-enter the bed.
 */
@NullMarked
public interface PlayerDeepSleepEvent extends PlayerEventNew, Cancellable {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
