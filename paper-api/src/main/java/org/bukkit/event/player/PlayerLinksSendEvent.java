package org.bukkit.event.player;

import io.papermc.paper.connection.PlayerCommonConnection;
import org.bukkit.ServerLinks;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

/**
 * This event is called when the list of links is sent to the player.
 */
@ApiStatus.Experimental
public interface PlayerLinksSendEvent extends Event {

    /**
     * Gets the connection that received the links.
     *
     * @return connection
     */
    PlayerCommonConnection getConnection();

    /**
     * Gets the links to be sent, for modification.
     *
     * @return the links
     */
    ServerLinks getLinks();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
