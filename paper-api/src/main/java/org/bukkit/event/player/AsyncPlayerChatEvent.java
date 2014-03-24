package org.bukkit.event.player;

import java.util.IllegalFormatException;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * This event will sometimes fire synchronously, depending on how it was
 * triggered.
 * <p>
 * The constructor provides a boolean to indicate if the event was fired
 * synchronously or asynchronously. When asynchronous, this event can be
 * called from any thread, sans the main thread, and has limited access to the
 * API.
 * <p>
 * If a player is the direct cause of this event by an incoming packet, this
 * event will be asynchronous. If a plugin triggers this event by compelling a
 * player to chat, this event will be synchronous.
 * <p>
 * Care should be taken to check {@link #isAsynchronous()} and treat the event
 * appropriately.
 */
public class AsyncPlayerChatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private String format = "<%1$s> %2$s";
    private final Set<Player> recipients;

    /**
     *
     * @param async This changes the event to a synchronous state.
     * @param who the chat sender
     * @param message the message sent
     * @param players the players to receive the message. This may be a lazy
     *     or unmodifiable collection.
     */
    public AsyncPlayerChatEvent(final boolean async, final Player who, final String message, final Set<Player> players) {
        super(who, async);
        this.message = message;
        recipients = players;
    }

    /**
     * Gets the message that the player is attempting to send. This message
     * will be used with {@link #getFormat()}.
     *
     * @return Message the player is attempting to send
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message that the player will send. This message will be used
     * with {@link #getFormat()}.
     *
     * @param message New message that the player will send
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the format to use to display this chat message.
     * <p>
     * When this event finishes execution, the first format parameter is the
     * {@link Player#getDisplayName()} and the second parameter is {@link
     * #getMessage()}
     *
     * @return {@link String#format(String, Object...)} compatible format
     *     string
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format to use to display this chat message.
     * <p>
     * When this event finishes execution, the first format parameter is the
     * {@link Player#getDisplayName()} and the second parameter is {@link
     * #getMessage()}
     *
     * @param format {@link String#format(String, Object...)} compatible
     *     format string
     * @throws IllegalFormatException if the underlying API throws the
     *     exception
     * @throws NullPointerException if format is null
     * @see String#format(String, Object...)
     */
    public void setFormat(final String format) throws IllegalFormatException, NullPointerException {
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
     * <p>
     * The set returned is not guaranteed to be mutable and may auto-populate
     * on access. Any listener accessing the returned set should be aware that
     * it may reduce performance for a lazy set implementation.
     * <p>
     * Listeners should be aware that modifying the list may throw {@link
     * UnsupportedOperationException} if the event caller provides an
     * unmodifiable set.
     *
     * @return All Players who will see this chat message
     */
    public Set<Player> getRecipients() {
        return recipients;
    }

    public boolean isCancelled() {
        return cancel ;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
