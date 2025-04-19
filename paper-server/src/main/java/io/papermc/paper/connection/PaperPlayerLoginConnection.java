package io.papermc.paper.connection;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class PaperPlayerLoginConnection extends CommonCookieConnection implements PlayerLoginConnection {

    private final ServerLoginPacketListenerImpl serverLoginPacketListener;

    public PaperPlayerLoginConnection(final ServerLoginPacketListenerImpl serverLoginPacketListener) {
        super(serverLoginPacketListener.connection);
        this.serverLoginPacketListener = serverLoginPacketListener;
    }

    @Override
    public @Nullable PlayerProfile getAuthenticatedProfile() {
        return this.serverLoginPacketListener.authenticatedProfile == null ? null : CraftPlayerProfile.asBukkitCopy(this.serverLoginPacketListener.authenticatedProfile);
    }

    @Override
    public @Nullable PlayerProfile getUnsafeProfile() {
        // TODO: may violate the player profile contract -- split out
        return new CraftPlayerProfile(this.serverLoginPacketListener.requestedUuid,  this.serverLoginPacketListener.requestedUsername);
    }

    @Override
    public @NotNull InetAddress getAddress() {
        return ((java.net.InetSocketAddress) this.serverLoginPacketListener.connection.getRemoteAddress()).getAddress();
    }

    @Override
    public @NotNull InetAddress getRawAddress() {
        return ((InetSocketAddress) this.serverLoginPacketListener.connection.channel.remoteAddress()).getAddress();
    }

    @Override
    public @NotNull String getHostname() {
        return this.serverLoginPacketListener.connection.hostname;
    }

    @Override
    public boolean isTransferred() {
        return this.serverLoginPacketListener.transferred;
    }

    @Override
    public void disconnect(final Component component) {
        this.serverLoginPacketListener.disconnect(PaperAdventure.asVanilla(component));
    }
}
