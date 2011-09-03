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
        return server.getHandle().isOp(getName().toLowerCase());
    }

    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().e(getName().toLowerCase());
        } else {
            server.getHandle().f(getName().toLowerCase());
        }
    }

    public boolean isBanned() {
        return server.getHandle().banByName.contains(name.toLowerCase());
    }

    public void setBanned(boolean value) {
        if (value) {
            server.getHandle().a(name.toLowerCase());
        } else {
            server.getHandle().b(name.toLowerCase());
        }
    }

    public boolean isWhitelisted() {
        return server.getHandle().e().contains(name.toLowerCase());
    }

    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().k(name.toLowerCase());
        } else {
            server.getHandle().l(name.toLowerCase());
        }
    }
}
