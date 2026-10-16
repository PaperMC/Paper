package org.bukkit.event.server;

import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered for server broadcast messages such as from
 * {@link org.bukkit.Server#broadcast(Component)}.
 * <p>
 * This event behaves similarly to {@link io.papermc.paper.event.player.AsyncChatEvent} in that it
 * should be async if fired from an async thread. Please see that event for
 * further information.
 */
public class BroadcastMessageEvent extends ServerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Set<CommandSender> recipients;
    private Component message;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(since = "1.14", forRemoval = true)
    public BroadcastMessageEvent(@NotNull String message, @NotNull Set<CommandSender> recipients) {
        this(false, message, recipients);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public BroadcastMessageEvent(boolean isAsync, @NotNull String message, @NotNull Set<CommandSender> recipients) {
        super(isAsync);
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
        this.recipients = recipients;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public BroadcastMessageEvent(@NotNull Component message, @NotNull Set<CommandSender> recipients) {
        this(false, message, recipients);
    }

    @ApiStatus.Internal
    public BroadcastMessageEvent(boolean isAsync, @NotNull Component message, @NotNull Set<CommandSender> recipients) {
        super(isAsync);
        this.message = message;
        this.recipients = recipients;
    }

    /**
     * Get the broadcast message.
     *
     * @return Message to broadcast
     */
    public @NotNull Component message() {
        return this.message;
    }

    /**
     * Set the broadcast message.
     *
     * @param message New message to broadcast
     */
    public void message(@NotNull Component message) {
        this.message = message;
    }

    /**
     * Get the message to broadcast.
     *
     * @return Message to broadcast
     * @deprecated in favour of {@link #message()}
     */
    @NotNull
    @Deprecated
    public String getMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.message);
    }

    /**
     * Set the message to broadcast.
     *
     * @param message New message to broadcast
     * @deprecated in favour of {@link #message(Component)}
     */
    @Deprecated // Paper
    public void setMessage(@NotNull String message) {
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
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
        return this.recipients;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
