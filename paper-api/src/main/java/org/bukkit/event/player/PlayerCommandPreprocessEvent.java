package org.bukkit.event.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called early in the command handling process. This event is only
 * for very exceptional cases and you should not normally use it.
 */
public class PlayerCommandPreprocessEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private String format = "<%1$s> %2$s";
    private final Set<Player> recipients;

    public PlayerCommandPreprocessEvent(final Player player, final String message) {
        super(player);
        this.recipients = new HashSet<Player>(Arrays.asList(player.getServer().getOnlinePlayers()));
        this.message = message;
    }

    public PlayerCommandPreprocessEvent(final Player player, final String message, final Set<Player> recipients) {
        super(player);
        this.recipients = recipients;
        this.message = message;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the command that the player is attempting to send.
     * All commands begin with a special character; implementations do not consider the first character when executing the content.
     *
     * @return Message the player is attempting to send
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the command that the player will send.
     * All commands begin with a special character; implementations do not consider the first character when executing the content.
     *
     * @param command New message that the player will send
     * @throws IllegalArgumentException if command is null or empty
     */
    public void setMessage(String command) throws IllegalArgumentException {
        Validate.notNull(command, "Command cannot be null");
        Validate.notEmpty(command, "Command cannot be empty");
        this.message = command;
    }

    /**
     * Sets the player that this command will be executed as.
     *
     * @param player New player which this event will execute as
     * @throws IllegalArgumentException if the player provided is null
     */
    public void setPlayer(final Player player) throws IllegalArgumentException {
        Validate.notNull(player, "Player cannot be null");
        this.player = player;
    }

    /**
     * Gets the format to use to display this chat message
     *
     * @deprecated This method is provided for backward compatibility with no guarantee to the use of the format.
     * @return String.Format compatible format string
     */
    @Deprecated
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format to use to display this chat message
     *
     * @deprecated This method is provided for backward compatibility with no guarantee to the effect of modifying the format.
     * @param format String.Format compatible format string
     */
    @Deprecated
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
     * Gets a set of recipients that this chat message will be displayed to.
     * The set returned is not guaranteed to be mutable and may auto-populate on access.
     * Any listener accessing the returned set should be aware that it may reduce performance for a lazy set implementation.
     * Listeners should be aware that modifying the list may throw {@link UnsupportedOperationException} if the event caller provides an unmodifiable set.
     * @deprecated This method is provided for backward compatibility with no guarantee to the effect of viewing or modifying the set.
     * @return All Players who will see this chat message
     */
    @Deprecated
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
