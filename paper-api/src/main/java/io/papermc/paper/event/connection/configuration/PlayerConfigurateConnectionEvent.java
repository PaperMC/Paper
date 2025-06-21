package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
abstract class PlayerConfigurateConnectionEvent extends Event {
    private final PlayerConfigurationConnection loginConnection;

    protected PlayerConfigurateConnectionEvent(final PlayerConfigurationConnection loginConnection) {
        this.loginConnection = loginConnection;
    }

    protected PlayerConfigurateConnectionEvent(final boolean async, final PlayerConfigurationConnection loginConnection) {
        super(async);
        this.loginConnection = loginConnection;
    }

    public PlayerConfigurationConnection getConfigurationConnection() {
        return this.loginConnection;
    }
}
