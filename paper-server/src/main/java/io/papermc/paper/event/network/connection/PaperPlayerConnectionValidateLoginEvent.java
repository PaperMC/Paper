package io.papermc.paper.event.network.connection;

import io.papermc.paper.connection.PlayerConnection;
import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerConnectionValidateLoginEvent extends PaperConnectionEvent implements PlayerConnectionValidateLoginEvent {

    private @Nullable Component kickMessage;

    public PaperPlayerConnectionValidateLoginEvent(final PlayerConnection connection, final @Nullable Component kickMessage) {
        super(connection);
        this.kickMessage = kickMessage;
    }

    @Override
    public @Nullable Component getKickMessage() {
        return this.kickMessage;
    }

    @Override
    public void kickMessage(final Component message) {
        this.kickMessage = message;
    }

    @Override
    public boolean isAllowed() {
        return this.kickMessage == null;
    }

    @Override
    public void allow() {
        this.kickMessage = null;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerConnectionValidateLoginEvent.getHandlerList();
    }
}
