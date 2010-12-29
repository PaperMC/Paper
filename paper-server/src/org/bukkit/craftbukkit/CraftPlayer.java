
package org.bukkit.craftbukkit;

import net.minecraft.server.EntityPlayerMP;
import org.bukkit.Location;
import org.bukkit.Player;
import org.bukkit.World;

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

    public Location getLocation() {
        return new Location(getWorld(), player.p, player.q, player.r, player.v, player.w);
    }

    public World getWorld() {
        return server.getWorld(player.b.e);
    }

    public EntityPlayerMP getEntity() {
        return player;
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + name + '}';
    }
}
