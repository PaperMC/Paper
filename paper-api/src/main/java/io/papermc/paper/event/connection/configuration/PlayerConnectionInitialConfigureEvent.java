package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class PlayerConnectionInitialConfigureEvent extends PlayerConfigurationConnectionEvent {
    private static final HandlerList handlers = new HandlerList();

    @ApiStatus.Internal
    public PlayerConnectionInitialConfigureEvent(PlayerConfigurationConnection connection) {
        super(!Bukkit.isPrimaryThread(), connection);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
