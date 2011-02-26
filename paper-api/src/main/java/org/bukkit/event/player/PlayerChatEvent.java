
package org.bukkit.event.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Holds information for player chat and commands
 */
public class PlayerChatEvent extends PlayerEvent implements Cancellable {
    private boolean cancel = false;
    private String message;
    private String format = "<%1$s> %2$s";
    private final Set<Player> recipients;

    public PlayerChatEvent(final Type type, final Player player, final String message) {
        super(type, player);
        this.message = message;

        recipients = new HashSet<Player>(Arrays.asList(player.getServer().getOnlinePlayers()));
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

    /**
     * Sets the player that this message will display as, or command will be
     * executed as
     *
     * @param player New player which this event will execute as
     */
    public void setPlayer(final Player player) {
        this.player = player;
    }

    /**
     * Gets the format to use to display this chat message
     *
     * @return String.Format compatible format string
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format to use to display this chat message
     *
     * @param format String.Format compatible format string
     */
    public void setFormat(final String format) {
        // Oh for a better way to do this!
        try {
            String.format(format, player, message);
        } catch (RuntimeException ex) {
            ex.fillInStackTrace();
            throw ex;
        }

        this.format = format;
    }

    /**
     * Gets a set of recipients that this chat message will be displayed to
     *
     * @return All Players who will see this chat message
     */
    public Set<Player> getRecipients() {
        return recipients;
    }
}
