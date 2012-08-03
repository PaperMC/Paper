package org.bukkit.event.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Holds information for player chat and commands
 * @deprecated This event was broken in 1.3, where chat should be asynchronous.
 * It is deprecated because listening for it is detrimental to server performance.
 * {@link AsyncPlayerChatEvent} is the encouraged alternative.
 */
@Deprecated
public class PlayerChatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private String format;
    private final Set<Player> recipients;

    public PlayerChatEvent(final Player player, final String message) {
        super(player);
        this.message = message;
        this.format = "<%1$s> %2$s";
        this.recipients = new HashSet<Player>(Arrays.asList(player.getServer().getOnlinePlayers()));
    }

    public PlayerChatEvent(final Player player, final String message, final String format, final Set<Player> recipients) {
        super(player);
        this.message = message;
        this.format = format;
        this.recipients = recipients;
    }

    public boolean isCancelled() {
        return cancel;
    }

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
        Validate.notNull(player, "Player cannot be null");
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
