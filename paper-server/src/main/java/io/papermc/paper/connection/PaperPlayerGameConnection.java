package io.papermc.paper.connection;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.Set;

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
        if (HorriblePlayerLoginEventHack.warnReenterConfiguration(this.handle.connection)) {
            return;
        }
        this.handle.switchToConfig();
    }

    @Override
    public Player getPlayer() {
        return this.handle.getCraftPlayer();
    }

    @Override
    public void sendPluginMessage(final Plugin source, final String channel, final byte[] message) {
        getPlayer().sendPluginMessage(source, channel, message);
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return getPlayer().getListeningPluginChannels();
    }

    @Override
    public boolean isConnected() {
        return getPlayer().isConnected();
    }
}
