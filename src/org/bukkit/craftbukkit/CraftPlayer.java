
package org.bukkit.craftbukkit;

import net.minecraft.server.fi;
import org.bukkit.Player;

public class CraftPlayer implements Player {
    private fi player;
    private final String name;
    private final CraftServer server;

    public CraftPlayer(CraftServer serv, fi handle) {
        player = handle;
        name = player.aw;
        server = serv;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return server.server.g(name);
    }
}
