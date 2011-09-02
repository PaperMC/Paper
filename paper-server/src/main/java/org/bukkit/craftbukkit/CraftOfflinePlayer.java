package org.bukkit.craftbukkit;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class CraftOfflinePlayer implements OfflinePlayer {
    private final String name;
    private final CraftServer server;

    protected CraftOfflinePlayer(CraftServer server, String name) {
        this.server = server;
        this.name = name;
    }

    public boolean isOnline() {
        return false;
    }

    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

    public boolean isOp() {
        return server.getHandle().isOp(getName());
    }

    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().e(getName());
        } else {
            server.getHandle().f(getName());
        }
    }
}
