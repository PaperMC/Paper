
package org.bukkit.craftbukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.*;
import net.minecraft.server.*;

public class CraftServer implements Server {
    private final String serverName = "CraftBucket";
    private final String serverVersion;
    private final HashMap<String, Player> playerCache = new HashMap<String, Player>();

    protected final MinecraftServer console;
    protected final hl server;

    public CraftServer(MinecraftServer instance, String ver) {
        serverVersion = ver;

        console = instance;
        server = console.f;
    }

    public String getName() {
        return serverName;
    }

    public String getVersion() {
        return serverVersion;
    }

    public Player[] getOnlinePlayers() {
        List<fi> online = server.b;
        Player[] players = new Player[online.size()];

        for (int i = 0; i < players.length; i++) {
            String name = online.get(i).aw;
            
            if (playerCache.containsKey(name)) {
                players[i] = playerCache.get(name);
            } else {
                players[i] = new CraftPlayer(this, online.get(i));
                playerCache.put(name, players[i]);
            }
        }

        return players;
    }

}
