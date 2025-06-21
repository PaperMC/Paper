package io.papermc.paper.connection;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class PaperPlayerLoginConnection extends ReadablePlayerCookieConnectionImpl implements PlayerLoginConnection {

    private final ServerLoginPacketListenerImpl handle;

    public PaperPlayerLoginConnection(final ServerLoginPacketListenerImpl serverLoginPacketListener) {
        super(serverLoginPacketListener.connection);
        this.handle = serverLoginPacketListener;
    }

    @Override
    public @Nullable PlayerProfile getAuthenticatedProfile() {
        return this.handle.authenticatedProfile == null ? null : CraftPlayerProfile.asBukkitCopy(this.handle.authenticatedProfile);
    }

    @Override
    public @Nullable PlayerProfile getUnsafeProfile() {
        // TODO: may violate the player profile contract -- split out
        return new CraftPlayerProfile(this.handle.requestedUuid,  this.handle.requestedUsername);
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
    public @org.jspecify.annotations.Nullable InetSocketAddress getVirtualHost() {
        return this.handle.connection.virtualHost;
    }

    @Override
    public @org.jspecify.annotations.Nullable InetSocketAddress getHAProxyAddress() {
        return this.handle.connection.haProxyAddress instanceof final InetSocketAddress inetSocketAddress ? inetSocketAddress : null;
    }

    @Override
    public boolean isTransferred() {
        return this.handle.transferred;
    }

    @Override
    public void disconnect(final Component component) {
        this.handle.disconnect(PaperAdventure.asVanilla(component));
    }
}
