package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

/**
 * An event that allows you to configure the player.
 * This is async and allows you to run configuration code on the player.
 * Once this event has finished execution, the player connection will continue.
 * <p>
 * This occurs after configuration, but before the player has entered the world.
 */
public class AsyncPlayerConnectionConfigureEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerConfigurationConnection connection;

    @ApiStatus.Internal
    public AsyncPlayerConnectionConfigureEvent(final PlayerConfigurationConnection connection) {
        super(true);
        this.connection = connection;
    }

    public PlayerConfigurationConnection getConnection() {
        return this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
