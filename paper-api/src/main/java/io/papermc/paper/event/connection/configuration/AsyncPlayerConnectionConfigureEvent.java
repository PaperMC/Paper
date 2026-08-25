package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.ConnectionEvent;
import org.bukkit.event.HandlerList;

/**
 * An event that allows you to configure the player.
 * This is async and allows you to run configuration code on the player.
 * Once this event has finished execution, the player connection will continue.
 * <p>
 * This occurs after configuration, but before the player has entered the world.
 */
public interface AsyncPlayerConnectionConfigureEvent extends ConnectionEvent {

    @Override
    PlayerConfigurationConnection getConnection();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
