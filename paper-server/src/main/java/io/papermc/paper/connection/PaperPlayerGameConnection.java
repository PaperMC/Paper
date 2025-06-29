package io.papermc.paper.connection;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;

public class PaperPlayerGameConnection extends PaperCommonConnection<ServerGamePacketListenerImpl> implements PlayerGameConnection {

    public PaperPlayerGameConnection(final ServerGamePacketListenerImpl serverConfigurationPacketListenerImpl) {
        super(serverConfigurationPacketListenerImpl);
    }

    @Override
    public ClientInformation getClientInformation() {
        return this.handle.player.clientInformation();
    }

    @Override
    public void reenterConfiguration() {
        if (this.handle.connection.savedPlayerForLoginEventLegacy != null) {
            HorriblePlayerLoginEventHack.warnReenterConfiguration();
            return;
        }
        this.handle.switchToConfig();
    }

    @Override
    public Player getPlayer() {
        return this.handle.getCraftPlayer();
    }
}
