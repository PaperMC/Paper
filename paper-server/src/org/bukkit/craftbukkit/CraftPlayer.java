
package org.bukkit.craftbukkit;

import net.minecraft.server.EntityPlayerMP;
import org.bukkit.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {
    private final EntityPlayerMP entity;

    public CraftPlayer(CraftServer server, EntityPlayerMP entity) {
        super(server, entity);
        this.entity = entity;
    }

    public boolean isOnline() {
        return server.getHandle().g(getName());
    }

    @Override
    public EntityPlayerMP getHandle() {
        return entity;
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }
}
