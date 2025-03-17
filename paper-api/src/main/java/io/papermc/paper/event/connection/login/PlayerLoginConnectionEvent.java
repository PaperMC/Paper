package io.papermc.paper.event.connection.login;

import io.papermc.paper.connection.PlayerLoginConnection;
import org.bukkit.event.Event;

abstract class PlayerLoginConnectionEvent extends Event {
    private final PlayerLoginConnection loginConnection;

    protected PlayerLoginConnectionEvent(final PlayerLoginConnection loginConnection) {
        this.loginConnection = loginConnection;
    }

    protected PlayerLoginConnectionEvent(final boolean async, final PlayerLoginConnection loginConnection) {
        super(async);
        this.loginConnection = loginConnection;
    }

    public PlayerLoginConnection getLoginConnection() {
        return this.loginConnection;
    }
}
