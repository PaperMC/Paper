package io.papermc.paper.network;

import com.destroystokyo.paper.network.NetworkClient;
import java.net.InetSocketAddress;
import net.minecraft.network.Connection;
import org.jspecify.annotations.Nullable;

public class PaperNetworkClient implements NetworkClient {

    private final Connection connection;

    public PaperNetworkClient(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public InetSocketAddress address() {
        return (InetSocketAddress) this.connection.getRemoteAddress();
    }

    @Override
    public int protocolVersion() {
        return this.connection.protocolVersion;
    }

    @Override
    public @Nullable InetSocketAddress virtualHost() {
        return this.connection.virtualHost;
    }

    public static InetSocketAddress prepareVirtualHost(final String host, final int port) {
        int len = host.length();

        // FML appends a marker to the host to recognize FML clients (\0FML\0)
        final int pos = host.indexOf('\0');
        if (pos >= 0) {
            len = pos;
        }

        // When clients connect with a SRV record, their host contains a trailing '.'
        if (len > 0 && host.charAt(len - 1) == '.') {
            len--;
        }

        return InetSocketAddress.createUnresolved(host.substring(0, len), port);
    }
}
