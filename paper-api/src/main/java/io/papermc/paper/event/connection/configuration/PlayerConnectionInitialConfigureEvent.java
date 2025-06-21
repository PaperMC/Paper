package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

public class PlayerConnectionInitialConfigureEvent extends PlayerConfigurateConnectionEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public PlayerConnectionInitialConfigureEvent(final PlayerConfigurationConnection connection) {
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
