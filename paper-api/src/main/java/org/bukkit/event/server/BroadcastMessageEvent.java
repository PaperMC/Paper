package org.bukkit.event.server;

import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event triggered for server broadcast messages such as from
 * {@link org.bukkit.Server#broadcast(Component)}.
 * <p>
 * This event behaves similarly to {@link io.papermc.paper.event.player.AsyncChatEvent} in that it
 * should be async if fired from an async thread. Please see that event for
 * further information.
 */
public interface BroadcastMessageEvent extends ServerEventNew, Cancellable {

    /**
     * Get the broadcast message.
     *
     * @return Message to broadcast
     */
    Component message();

    /**
     * Set the broadcast message.
     *
     * @param message New message to broadcast
     */
    void message(Component message);

    /**
     * Get the message to broadcast.
     *
     * @return Message to broadcast
     * @deprecated in favour of {@link #message()}
     */
    @Deprecated
    String getMessage();

    /**
     * Set the message to broadcast.
     *
     * @param message New message to broadcast
     * @deprecated in favour of {@link #message(Component)}
     */
    @Deprecated
    void setMessage(String message);

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
    Set<CommandSender> getRecipients();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
