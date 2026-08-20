package io.papermc.paper.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called when a player is marked as loaded.
 * <p>
 * This either happens when the player notifies the server after loading the world (closing the downloading terrain screen)
 * or when the player has not done so for 60 ticks after joining the server or respawning.
 */
public interface PlayerClientLoadedWorldEvent extends PlayerEvent {

    /**
     * True if the event was triggered because the server has not been notified by the player
     * for 60 ticks after the player joined the server or respawned.
     *
     * @return true if the event was triggered because of a timeout
     */
    boolean isTimeout();

    HandlerList getHandlers();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
