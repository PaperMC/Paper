package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.connection.PlayerLoginConnection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
abstract class PlayerConfigurationConnectionEvent extends Event {
    private final PlayerConfigurationConnection loginConnection;

    protected PlayerConfigurationConnectionEvent(final PlayerConfigurationConnection loginConnection) {
        this.loginConnection = loginConnection;
    }

    protected PlayerConfigurationConnectionEvent(final boolean async, final PlayerConfigurationConnection loginConnection) {
        super(async);
        this.loginConnection = loginConnection;
    }

    public PlayerConfigurationConnection getConfigurationConnection() {
        return this.loginConnection;
    }
}
