package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerCodeOfConductSendEvent extends CraftEvent implements PlayerCodeOfConductSendEvent {

    private @Nullable String codeOfConduct;
    private final PlayerCommonConnection connection;

    public PaperPlayerCodeOfConductSendEvent(final PlayerConfigurationConnection connection, final @Nullable String codeOfConduct) {
        this.connection = connection;
        this.codeOfConduct = codeOfConduct;
    }

    @Override
    public PlayerCommonConnection getConnection() {
        return this.connection;
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
