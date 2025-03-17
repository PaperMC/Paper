package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperPlayerGameConnection extends CommonCookieConnection implements PlayerGameConnection {

    private final ServerGamePacketListenerImpl serverConfigurationPacketListenerImpl;

    public PaperPlayerGameConnection(final ServerGamePacketListenerImpl serverConfigurationPacketListenerImpl) {
        super(serverConfigurationPacketListenerImpl.connection);
        this.serverConfigurationPacketListenerImpl = serverConfigurationPacketListenerImpl;
    }

    @Override
    public void configurate() {
        this.serverConfigurationPacketListenerImpl.switchToConfig();
    }

    @Override
    public Player getPlayer() {
        return this.serverConfigurationPacketListenerImpl.getCraftPlayer();
    }

    @Override
    public void disconnect(final Component component) {
        this.serverConfigurationPacketListenerImpl.disconnect(component);
    }
}
