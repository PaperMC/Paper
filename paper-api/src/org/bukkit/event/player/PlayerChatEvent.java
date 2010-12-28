
package org.bukkit.event.player;

import org.bukkit.Player;

/**
 * Holds information for player chat and commands
 */
public class PlayerChatEvent extends PlayerEvent {
    private boolean cancel = false;
    private String message;

    public PlayerChatEvent(final Type type, final Player player, final String message) {
        super(type, player);
        this.message = message;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the message that the player is attempting to send
     *
     * @return Message the player is attempting to send
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message that the player will send
     *
     * @param message New message that the player will send
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
