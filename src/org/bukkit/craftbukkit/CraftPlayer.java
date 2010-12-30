
package org.bukkit.craftbukkit;

import net.minecraft.server.EntityPlayerMP;
import org.bukkit.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {
    private final EntityPlayerMP entity;
    private final String name;

    public CraftPlayer(CraftServer server, EntityPlayerMP entity) {
        super(server, entity);
        this.entity = entity;
        name = entity.aw;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return server.getHandle().g(name);
    }

    @Override
    public EntityPlayerMP getHandle() {
        return entity;
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + name + '}';
    }
}
