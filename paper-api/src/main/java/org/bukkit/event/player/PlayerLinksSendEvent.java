package org.bukkit.event.player;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.event.connection.ConnectionEvent;
import org.bukkit.ServerLinks;
import org.bukkit.event.HandlerList;

/**
 * This event is called when the list of links is sent to the player.
 */
public interface PlayerLinksSendEvent extends ConnectionEvent {

    @Override
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
