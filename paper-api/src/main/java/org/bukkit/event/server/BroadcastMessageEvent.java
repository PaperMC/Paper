package org.bukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.Set;

/**
 * Event triggered for {@link org.bukkit.Server#broadcast(String, String)}
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
     * Get the message to broadcast
     *
     * @return Message to broadcast
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message to broadcast
     *
     * @param message Message to broadcast
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets a set of recipients that this broadcast message will be displayed to.
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
