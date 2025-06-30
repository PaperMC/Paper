package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.PaperSkinParts;
import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.common.ClientboundCustomReportDetailsPacket;
import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
import net.minecraft.network.protocol.common.ClientboundTransferPacket;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.ServerLinks;
import org.bukkit.craftbukkit.CraftServerLinks;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jspecify.annotations.Nullable;

public abstract class PaperCommonConnection<T extends ServerCommonPacketListenerImpl> extends ReadablePlayerCookieConnectionImpl implements PlayerCommonConnection {

    protected final T handle;

    public PaperCommonConnection(final T serverConfigurationPacketListenerImpl) {
        super(serverConfigurationPacketListenerImpl.connection);
        this.handle = serverConfigurationPacketListenerImpl;
    }

    @Override
    public void sendReportDetails(final Map<String, String> details) {
        this.handle.send(new ClientboundCustomReportDetailsPacket(details));
    }

    @Override
    public void sendLinks(final ServerLinks links) {
        this.handle.send(new ClientboundServerLinksPacket(((CraftServerLinks) links).getServerLinks().untrust()));
    }

    @Override
    public void transfer(final String host, final int port) {
        this.handle.send(new ClientboundTransferPacket(host, port));
    }

    @Override
    public <T> T getClientOption(ClientOption<T> type) {
        ClientInformation information = this.getClientInformation();

        if (ClientOption.SKIN_PARTS == type) {
            return type.getType().cast(new PaperSkinParts(information.modelCustomisation()));
        } else if (ClientOption.CHAT_COLORS_ENABLED == type) {
            return type.getType().cast(information.chatColors());
        } else if (ClientOption.CHAT_VISIBILITY == type) {
            return type.getType().cast(ClientOption.ChatVisibility.valueOf(information.chatVisibility().name()));
        } else if (ClientOption.LOCALE == type) {
            return type.getType().cast(information.language());
        } else if (ClientOption.MAIN_HAND == type) {
            return type.getType().cast(information.mainHand());
        } else if (ClientOption.VIEW_DISTANCE == type) {
            return type.getType().cast(information.viewDistance());
        } else if (ClientOption.TEXT_FILTERING_ENABLED == type) {
            return type.getType().cast(information.textFilteringEnabled());
        } else if (ClientOption.ALLOW_SERVER_LISTINGS == type) {
            return type.getType().cast(information.allowsListing());
        } else if (ClientOption.PARTICLE_VISIBILITY == type) {
            return type.getType().cast(ClientOption.ParticleVisibility.valueOf(information.particleStatus().name()));
        }
        throw new RuntimeException("Unknown settings type");
    }

    @Override
    public void disconnect(final Component component) {
        this.handle.disconnect(PaperAdventure.asVanilla(component), DisconnectionReason.UNKNOWN);
    }

    @Override
    public boolean isTransferred() {
        return this.handle.isTransferred();
    }

    @Override
    public SocketAddress getAddress() {
        return this.handle.connection.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getClientAddress() {
        return (InetSocketAddress) this.handle.connection.channel.remoteAddress();
    }

    @Override
    public @Nullable InetSocketAddress getVirtualHost() {
        return this.handle.connection.virtualHost;
    }

    @Override
    public @Nullable InetSocketAddress getHAProxyAddress() {
        return this.handle.connection.haProxyAddress instanceof final InetSocketAddress inetSocketAddress ? inetSocketAddress : null;
    }

    @Override
    public void storeCookie(final NamespacedKey key, final byte[] value) {
        Preconditions.checkArgument(key != null, "Cookie key cannot be null");
        Preconditions.checkArgument(value != null, "Cookie value cannot be null");
        Preconditions.checkArgument(value.length <= 5120, "Cookie value too large, must be smaller than 5120 bytes");
        Preconditions.checkState(this.canStoreCookie(), "Can only store cookie in CONFIGURATION or PLAY protocol.");

        this.handle.send(new ClientboundStoreCookiePacket(CraftNamespacedKey.toMinecraft(key), value));
    }

    public abstract ClientInformation getClientInformation();
}
