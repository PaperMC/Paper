package com.destroystokyo.paper.network;

import java.net.InetSocketAddress;

import javax.annotation.Nullable;
import net.minecraft.network.Connection;

public class PaperNetworkClient implements NetworkClient {

    private final Connection connection;

    PaperNetworkClient(Connection connection) {
        this.connection = connection;
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) this.connection.getRemoteAddress();
    }

    @Override
    public int getProtocolVersion() {
        return this.connection.protocolVersion;
    }

    @Nullable
    @Override
    public InetSocketAddress getVirtualHost() {
        return this.connection.virtualHost;
    }

    public static InetSocketAddress prepareVirtualHost(String host, int port) {
        int len = host.length();

        // FML appends a marker to the host to recognize FML clients (\0FML\0)
        int pos = host.indexOf('\0');
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
