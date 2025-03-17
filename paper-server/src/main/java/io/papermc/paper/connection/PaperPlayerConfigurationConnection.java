package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.common.ClientboundTransferPacket;
import net.minecraft.network.protocol.configuration.ClientboundResetChatPacket;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jetbrains.annotations.NotNull;

public class PaperPlayerConfigurationConnection extends CommonCookieConnection implements PlayerConfigurationConnection {

    private final ServerConfigurationPacketListenerImpl serverConfigurationPacketListenerImpl;

    public PaperPlayerConfigurationConnection(final ServerConfigurationPacketListenerImpl serverConfigurationPacketListenerImpl) {
        super(serverConfigurationPacketListenerImpl.connection);
        this.serverConfigurationPacketListenerImpl = serverConfigurationPacketListenerImpl;
    }

    @Override
    public PlayerProfile getProfile() {
        return CraftPlayerProfile.asBukkitCopy(this.serverConfigurationPacketListenerImpl.gameProfile);
    }

    @Override
    public @NotNull InetAddress getAddress() {
        return ((InetSocketAddress) this.serverConfigurationPacketListenerImpl.connection.getRemoteAddress()).getAddress();
    }

    @Override
    public @NotNull InetAddress getRawAddress() {
        return ((InetSocketAddress) this.serverConfigurationPacketListenerImpl.connection.channel.remoteAddress()).getAddress();
    }

    @Override
    public @NotNull String getHostname() {
        return this.serverConfigurationPacketListenerImpl.connection.hostname;
    }

    @Override
    public boolean isTransferred() {
        return this.serverConfigurationPacketListenerImpl.isTransferred();
    }

    @Override
    public void clearChat() {
        this.serverConfigurationPacketListenerImpl.send(ClientboundResetChatPacket.INSTANCE);
    }

    @Override
    public void completeConfiguration() {
        this.serverConfigurationPacketListenerImpl.returnToWorld();
    }

    @Override
    public <T> T getClientOption(final ClientOption<T> type) {
        ServerConfigurationPacketListenerImpl connection = this.serverConfigurationPacketListenerImpl;

        ClientInformation information = connection.clientInformation;
        if (com.destroystokyo.paper.ClientOption.SKIN_PARTS == type) {
            return type.getType().cast(new com.destroystokyo.paper.PaperSkinParts(information.modelCustomisation()));
        } else if (com.destroystokyo.paper.ClientOption.CHAT_COLORS_ENABLED == type) {
            return type.getType().cast(information.chatColors());
        } else if (com.destroystokyo.paper.ClientOption.CHAT_VISIBILITY == type) {
            return type.getType().cast(com.destroystokyo.paper.ClientOption.ChatVisibility.valueOf(information.chatVisibility().name()));
        } else if (com.destroystokyo.paper.ClientOption.LOCALE == type) {
            return type.getType().cast(information.language());
        } else if (com.destroystokyo.paper.ClientOption.MAIN_HAND == type) {
            return type.getType().cast(information.mainHand());
        } else if (com.destroystokyo.paper.ClientOption.VIEW_DISTANCE == type) {
            return type.getType().cast(information.viewDistance());
        } else if (com.destroystokyo.paper.ClientOption.TEXT_FILTERING_ENABLED == type) {
            return type.getType().cast(information.textFilteringEnabled());
        } else if (com.destroystokyo.paper.ClientOption.ALLOW_SERVER_LISTINGS == type) {
            return type.getType().cast(information.allowsListing());
        } else if (com.destroystokyo.paper.ClientOption.PARTICLE_VISIBILITY == type) {
            return type.getType().cast(com.destroystokyo.paper.ClientOption.ParticleVisibility.valueOf(information.particleStatus().name()));
        }
        throw new RuntimeException("Unknown settings type");
    }

    @Override
    public void transfer(final String host, final int port) {
        this.serverConfigurationPacketListenerImpl.sendPacket(new ClientboundTransferPacket(host, port));
    }


    @Override
    public void disconnect(final Component component) {
        this.serverConfigurationPacketListenerImpl.disconnect(PaperAdventure.asVanilla(component), DisconnectionReason.UNKNOWN);
    }
}
