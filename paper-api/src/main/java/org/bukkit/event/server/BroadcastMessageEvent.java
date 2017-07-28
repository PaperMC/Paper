package org.bukkit.event.server;

import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event triggered for server broadcast messages such as from
 * {@link org.bukkit.Server#broadcast(String, String)}.
 */
public class BroadcastMessageEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private String message;
    private final Set<CommandSender> recipients;
    private boolean cancelled = false;

    public BroadcastMessageEvent(String message, Set<CommandSender> recipients) {
        this.message = message;
        this.recipients = recipients;
    }

    /**
     * Get the message to broadcast.
     *
     * @return Message to broadcast
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message to broadcast.
     *
     * @param message New message to broadcast
     */
    public void setMessage(String message) {
        this.message = message;
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
     * @return All CommandSenders who will see this chat message
     */
    public Set<CommandSender> getRecipients() {
        return recipients;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
