package com.destroystokyo.paper.network;

import net.minecraft.server.NetworkManager;

import java.net.InetSocketAddress;

import javax.annotation.Nullable;

public class PaperNetworkClient implements NetworkClient {

    private final NetworkManager networkManager;

    PaperNetworkClient(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) this.networkManager.getSocketAddress();
    }

    @Override
    public int getProtocolVersion() {
        return this.networkManager.protocolVersion;
    }

    @Nullable
    @Override
    public InetSocketAddress getVirtualHost() {
        return this.networkManager.virtualHost;
    }

    public static InetSocketAddress prepareVirtualHost(String host, int port) {
        int len = host.length();

        // FML appends a marker to the host to recognize FML clients (\0FML\0)
        int pos = host.indexOf('\0');
        if (pos >= 0) {
            len = pos;
        }

        // When clients connect with a SRV record, their host contains a trailing '.'
        if (len > 0 && host.charAt(len -  1) == '.') {
            len--;
        }

        return InetSocketAddress.createUnresolved(host.substring(0, len), port);
    }

}
