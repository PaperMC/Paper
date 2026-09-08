package io.papermc.paper.event.network.connection;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.connection.PlayerConnection;
import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.players.PlayerList;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerConnectionValidateLoginEvent extends PaperConnectionEvent implements PlayerConnectionValidateLoginEvent {

    private @Nullable Component kickMessage;

    public PaperPlayerConnectionValidateLoginEvent(final PlayerConnection connection, final @Nullable Component kickMessage) {
        super(connection);
        this.kickMessage = kickMessage;
    }

    public PaperPlayerConnectionValidateLoginEvent(final ServerPacketListener packetListener, final PlayerList.LoginResult result) {
        this(packetListener.paperConnection(), result.isAllowed() ? null : PaperAdventure.asAdventure(result.message()));
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
