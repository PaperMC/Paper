package org.bukkit.event.server;

import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered for server broadcast messages such as from
 * {@link org.bukkit.Server#broadcast(net.kyori.adventure.text.Component)} (String, String)}.
 *
 * <b>This event behaves similarly to {@link io.papermc.paper.event.player.AsyncChatEvent} in that it
 * should be async if fired from an async thread. Please see that event for
 * further information.</b>
 */
public class BroadcastMessageEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private net.kyori.adventure.text.Component message; // Paper
    private final Set<CommandSender> recipients;
    private boolean cancelled = false;

    @Deprecated(since = "1.14")
    public BroadcastMessageEvent(@NotNull String message, @NotNull Set<CommandSender> recipients) {
        this(false, message, recipients);
    }

    @Deprecated // Paper
    public BroadcastMessageEvent(boolean isAsync, @NotNull String message, @NotNull Set<CommandSender> recipients) {
        // Paper start
        super(isAsync);
        this.message = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(message);
        this.recipients = recipients;
    }

    @Deprecated
    public BroadcastMessageEvent(net.kyori.adventure.text.@NotNull Component message, @NotNull Set<CommandSender> recipients) {
        this(false, message, recipients);
    }

    public BroadcastMessageEvent(boolean isAsync, net.kyori.adventure.text.@NotNull Component message, @NotNull Set<CommandSender> recipients) {
        // Paper end
        super(isAsync);
        this.message = message;
        this.recipients = recipients;
    }
    // Paper start
    /**
     * Get the broadcast message.
     *
     * @return Message to broadcast
     */
    public net.kyori.adventure.text.@NotNull Component message() {
        return this.message;
    }

    /**
     * Set the broadcast message.
     *
     * @param message New message to broadcast
     */
    public void message(net.kyori.adventure.text.@NotNull Component message) {
        this.message = message;
    }
    // Paper end

    /**
     * Get the message to broadcast.
     *
     * @return Message to broadcast
     * @deprecated in favour of {@link #message()}
     */
    @NotNull
    @Deprecated // Paper
    public String getMessage() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.message); // Paper
    }

    /**
     * Set the message to broadcast.
     *
     * @param message New message to broadcast
     * @deprecated in favour of {@link #message(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setMessage(@NotNull String message) {
        this.message = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(message); // Paper
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
    @NotNull
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
