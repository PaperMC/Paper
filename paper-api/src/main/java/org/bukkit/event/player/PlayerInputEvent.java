package org.bukkit.event.player;

import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * This event is called when a player sends updated input to the server.
 *
 * @see Player#getCurrentInput()
 */
public interface PlayerInputEvent extends PlayerEvent {

    /**
     * Gets the new input received from this player.
     *
     * @return the new input
     */
    Input getInput();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
