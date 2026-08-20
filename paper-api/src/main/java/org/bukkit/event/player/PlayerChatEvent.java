package org.bukkit.event.player;

import java.util.Set;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Holds information for player chat and commands
 *
 * @deprecated Listening to this event forces chat to wait for the main thread, delaying chat messages. It is recommended to use {@link io.papermc.paper.event.player.AsyncChatEvent} instead, wherever possible.
 */
@Deprecated(since = "1.3.1")
@Warning(reason = "Listening to this event forces chat to wait for the main thread, delaying chat messages.")
public interface PlayerChatEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the message that the player is attempting to send
     *
     * @return Message the player is attempting to send
     */
    String getMessage();

    /**
     * Sets the message that the player will send
     *
     * @param message New message that the player will send
     */
    void setMessage(String message);

    /**
     * Sets the player that this message will display as, or command will be
     * executed as
     *
     * @param player New player which this event will execute as
     */
    void setPlayer(Player player);

    /**
     * Gets the format to use to display this chat message
     *
     * @return String.Format compatible format string
     */
    String getFormat();

    /**
     * Sets the format to use to display this chat message
     *
     * @param format String.Format compatible format string
     */
    void setFormat(String format);

    /**
     * Gets a set of recipients that this chat message will be displayed to
     *
     * @return All Players who will see this chat message
     */
    Set<Player> getRecipients();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
