package io.papermc.paper.event.network.connection.configuration;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.configuration.PlayerCodeOfConductSendEvent;
import io.papermc.paper.event.network.connection.PaperConnectionEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerCodeOfConductSendEvent extends PaperConnectionEvent implements PlayerCodeOfConductSendEvent {

    private @Nullable String codeOfConduct;

    public PaperPlayerCodeOfConductSendEvent(final PlayerConfigurationConnection connection, final @Nullable String codeOfConduct) {
        super(connection);
        this.codeOfConduct = codeOfConduct;
    }

    @Override
    public PlayerCommonConnection getConnection() {
        return (PlayerCommonConnection) this.connection;
    }

    @Override
    public @Nullable String getCodeOfConduct() {
        return this.codeOfConduct;
    }

    @Override
    public void setCodeOfConduct(final @Nullable String codeOfConduct) {
        this.codeOfConduct = codeOfConduct;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerCodeOfConductSendEvent.getHandlerList();
    }
}
