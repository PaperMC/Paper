package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

/**
 * Indicates that this player is being reconfigured, meaning that this connection will be held in the configuration
 * stage unless kicked out through {@link PlayerConfigurationConnection#completeConfiguration()}
 */
public class PlayerConnectionReconfigurateEvent extends PlayerConfigurateConnectionEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public PlayerConnectionReconfigurateEvent(final PlayerConfigurationConnection connection) {
        super(!Bukkit.isPrimaryThread(), connection);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
