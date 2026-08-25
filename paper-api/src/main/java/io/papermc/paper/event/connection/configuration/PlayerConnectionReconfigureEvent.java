package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Indicates that this player is being reconfigured, meaning that this connection will be held in the configuration
 * stage unless kicked out through {@link PlayerConfigurationConnection#completeReconfiguration()}
 */
public interface PlayerConnectionReconfigureEvent extends Event {

    PlayerConfigurationConnection getConnection();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
