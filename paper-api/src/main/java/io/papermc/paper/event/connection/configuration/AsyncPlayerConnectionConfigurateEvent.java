package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A task that allows you to configurate the player.
 * This is async and allows you to run configuration code on the player.
 * Once this event has finished execution, the player connection will continue.
 * <p>
 * This occurs after configuration, but before the player has entered the world.
 */
public class AsyncPlayerConnectionConfigurateEvent extends PlayerConfigurationConnectionEvent {
    private static final HandlerList handlers = new HandlerList();

    @ApiStatus.Internal
    public AsyncPlayerConnectionConfigurateEvent(PlayerConfigurationConnection connection) {
        super(true, connection);
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
