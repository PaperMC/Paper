
package org.bukkit.craftbukkit;

import org.bukkit.Server;
import net.minecraft.server.MinecraftServer;

public class CraftServer implements Server {
    private final String name = "CraftBucket";
    private String version;
    MinecraftServer server;

    protected CraftServer(MinecraftServer instance, String ver) {
        server = instance;
        version = ver;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

}
