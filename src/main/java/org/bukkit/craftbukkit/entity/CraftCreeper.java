package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityCreeper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creeper;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, EntityCreeper entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    public boolean isPowered() {
        return getHandle().W().a(17) == 1;
    }

    public void setPowered(boolean powered) {
        if (powered) {
            getHandle().W().b(17, 1);
        } else {
            getHandle().W().b(17, 0);
        }
    }

}
