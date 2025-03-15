package io.papermc.paper.connection;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;

public class PaperPlayerGameConnection extends PaperCommonConnection<ServerGamePacketListenerImpl> implements PlayerGameConnection {

    public PaperPlayerGameConnection(final ServerGamePacketListenerImpl serverConfigurationPacketListenerImpl) {
        super(serverConfigurationPacketListenerImpl);
    }

    @Override
    public void reenterConfiguration() {
        this.handle.switchToConfig();
    }

    @Override
    public Player getPlayer() {
        return this.handle.getCraftPlayer();
    }
}
