package io.papermc.paper.connection;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.adventure.PaperAdventure;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.kyori.adventure.text.Component;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperPlayerLoginConnection extends ReadablePlayerCookieConnectionImpl implements PlayerLoginConnection {

    private final ServerLoginPacketListenerImpl packetListener;

    public PaperPlayerLoginConnection(final ServerLoginPacketListenerImpl packetListener) {
        super(packetListener.connection);
        this.packetListener = packetListener;
    }

    @Override
    public @Nullable PlayerProfile getAuthenticatedProfile() {
        return this.packetListener.authenticatedProfile == null ? null : CraftPlayerProfile.asBukkitCopy(this.packetListener.authenticatedProfile);
    }

    @Override
    public @Nullable PlayerProfile getUnsafeProfile() {
        // This can possibly error as requestedUsername can have wacky stuff. But its fine I doubt mojang will support this
        // much longer.
        return new CraftPlayerProfile(this.packetListener.requestedUuid, this.packetListener.requestedUsername);
    }

    @Override
    public SocketAddress getAddress() {
        return this.packetListener.connection.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getClientAddress() {
        return (InetSocketAddress) this.packetListener.connection.channel.remoteAddress();
    }

    @Override
    public @org.jspecify.annotations.Nullable InetSocketAddress getVirtualHost() {
        return this.packetListener.connection.virtualHost;
    }

    @Override
    public @org.jspecify.annotations.Nullable InetSocketAddress getHAProxyAddress() {
        return this.packetListener.connection.haProxyAddress instanceof final InetSocketAddress inetSocketAddress ? inetSocketAddress : null;
    }

    @Override
    public boolean isTransferred() {
        return this.packetListener.transferred;
    }

    @Override
    public void disconnect(final Component component) {
        this.packetListener.disconnect(PaperAdventure.asVanilla(component));
    }

    @Override
    public boolean isConnected() {
        return this.packetListener.connection.isConnected();
    }
}
