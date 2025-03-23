package org.bukkit.event.player;

import java.util.IllegalFormatException;
import java.util.Set;
import com.google.common.base.Preconditions;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

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
 *
 * @deprecated use {@link io.papermc.paper.event.player.AsyncChatEvent} instead
 */
@Deprecated
@Warning(reason = "Don't nag on old event yet") // Paper
public class AsyncPlayerChatEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private String message;
    private String format = "<%1$s> %2$s";
    private final Set<Player> recipients;

    private boolean cancelled;

    @ApiStatus.Internal
    public AsyncPlayerChatEvent(final boolean async, @NotNull final Player player, @NotNull final String message, @NotNull final Set<Player> players) {
        super(player, async);
        this.message = message;
        this.recipients = players;
    }

    /**
     * Gets the message that the player is attempting to send. This message
     * will be used with {@link #getFormat()}.
     *
     * @return Message the player is attempting to send
     */
    @NotNull
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the message that the player will send. This message will be used
     * with {@link #getFormat()}.
     *
     * @param message New message that the player will send
     */
    public void setMessage(@NotNull String message) {
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
    @NotNull
    public String getFormat() {
        return this.format;
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
     * @see String#format(String, Object...)
     */
    public void setFormat(@NotNull final String format) throws IllegalFormatException, NullPointerException {
        Preconditions.checkArgument(format != null, "format cannot be null");
        // Oh for a better way to do this!
        try {
            String.format(format, this.player, this.message);
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
    @NotNull
    public Set<Player> getRecipients() {
        return this.recipients;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
