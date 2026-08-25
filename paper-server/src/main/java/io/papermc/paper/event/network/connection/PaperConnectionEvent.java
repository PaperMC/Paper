package io.papermc.paper.event.network.connection;

import io.papermc.paper.connection.PlayerConnection;
import io.papermc.paper.event.connection.ConnectionEvent;
import org.bukkit.craftbukkit.event.CraftEvent;

public abstract class PaperConnectionEvent extends CraftEvent implements ConnectionEvent {

    protected final PlayerConnection connection;

    public PaperConnectionEvent(final PlayerConnection connection) {
        this(connection, false);
    }

    public PaperConnectionEvent(final PlayerConnection connection, final boolean isAsync) {
        super(isAsync);
        this.connection = connection;
    }

    @Override
    public PlayerConnection getConnection() {
        return this.connection;
    }
}
