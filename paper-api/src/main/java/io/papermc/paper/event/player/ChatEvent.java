package io.papermc.paper.event.player;

import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * An event fired when a {@link Player} sends a chat message to the server.
 *
 * @deprecated Listening to this event forces chat to wait for the main thread, delaying chat messages.
 * It is recommended to use {@link AsyncChatEvent} instead, wherever possible.
 */
@Deprecated
@Warning(reason = "Listening to this event forces chat to wait for the main thread, delaying chat messages.")
public interface ChatEvent extends AbstractChatEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
