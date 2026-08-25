package io.papermc.paper.event.connection;

import io.papermc.paper.connection.PlayerConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerConnectionValidateLoginEvent extends CraftEvent implements PlayerConnectionValidateLoginEvent {

    private final PlayerConnection connection;
    private @Nullable Component kickMessage;

    public PaperPlayerConnectionValidateLoginEvent(final PlayerConnection connection, final @Nullable Component kickMessage) {
        super(false);
        this.connection = connection;
        this.kickMessage = kickMessage;
    }

    @Override
    public PlayerConnection getConnection() {
        return this.connection;
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
