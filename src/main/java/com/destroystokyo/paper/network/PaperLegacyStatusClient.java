package com.destroystokyo.paper.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

import java.net.InetSocketAddress;

import javax.annotation.Nullable;

public final class PaperLegacyStatusClient implements StatusClient {

    private final InetSocketAddress address;
    private final int protocolVersion;
    @Nullable private final InetSocketAddress virtualHost;

    private PaperLegacyStatusClient(InetSocketAddress address, int protocolVersion, @Nullable InetSocketAddress virtualHost) {
        this.address = address;
        this.protocolVersion = protocolVersion;
        this.virtualHost = virtualHost;
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.address;
    }

    @Override
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Nullable
    @Override
    public InetSocketAddress getVirtualHost() {
        return this.virtualHost;
    }

    @Override
    public boolean isLegacy() {
        return true;
    }

    public static PaperServerListPingEvent processRequest(MinecraftServer server,
            InetSocketAddress address, int protocolVersion, @Nullable InetSocketAddress virtualHost) {

        PaperServerListPingEvent event =  new PaperServerListPingEventImpl(server,
                new PaperLegacyStatusClient(address, protocolVersion, virtualHost), Byte.MAX_VALUE, null);
        server.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return null;
        }

        return event;
    }

    public static String getMotd(PaperServerListPingEvent event) {
        return getFirstLine(event.getMotd());
    }

    public static String getUnformattedMotd(PaperServerListPingEvent event) {
        // Strip color codes and all other occurrences of the color char (because it's used as delimiter)
        return getFirstLine(StringUtils.remove(ChatColor.stripColor(event.getMotd()), ChatColor.COLOR_CHAR));
    }

    private static String getFirstLine(String s) {
        int pos = s.indexOf('\n');
        return pos >= 0 ? s.substring(0, pos) : s;
    }

}
