package org.bukkit.event.player;

import java.util.Collection;
import org.bukkit.Warning;
import org.bukkit.event.HandlerList;

/**
 * Called when a player attempts to tab-complete a chat message.
 *
 * @deprecated This event is no longer fired due to client changes
 * @see com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
 */
@Deprecated(since = "1.13")
@Warning(reason = "This event is no longer fired due to client changes")
public interface PlayerChatTabCompleteEvent extends PlayerEvent {

    /**
     * Gets the chat message being tab-completed.
     *
     * @return the chat message
     */
    String getChatMessage();

    /**
     * Gets the last 'token' of the message being tab-completed.
     * <p>
     * The token is the substring starting with the character after the last
     * space in the message.
     *
     * @return The last token for the chat message
     */
    String getLastToken();

    /**
     * This is the collection of completions for this event.
     *
     * @return the current completions
     */
    Collection<String> getTabCompletions();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
