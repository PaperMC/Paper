package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Indicates that this player is being configured for the first time, meaning that the connection will start being configured automatically
 */
public interface PlayerConnectionInitialConfigureEvent extends Event {

    PlayerConfigurationConnection getConnection();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
