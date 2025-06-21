package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

/**
 * An event that allows you to configurate the player.
 * This is async and allows you to run configuration code on the player.
 * Once this event has finished execution, the player connection will continue.
 * <p>
 * This occurs after configuration, but before the player has entered the world.
 * <p>
 */
public class AsyncPlayerConnectionConfigureEvent extends PlayerConfigurateConnectionEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public AsyncPlayerConnectionConfigureEvent(final PlayerConfigurationConnection connection) {
        super(true, connection);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
