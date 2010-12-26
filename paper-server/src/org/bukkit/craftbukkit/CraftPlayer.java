
package org.bukkit.craftbukkit;

import net.minecraft.server.EntityPlayerMP;
import org.bukkit.Player;

public class CraftPlayer implements Player {
    private EntityPlayerMP player;
    private final String name;
    private final CraftServer server;

    public CraftPlayer(CraftServer serv, EntityPlayerMP handle) {
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
