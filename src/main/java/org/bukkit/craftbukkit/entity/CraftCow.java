package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityCow;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cow;

public class CraftCow extends CraftAnimals implements Cow {

    public CraftCow(CraftServer server, EntityCow entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCow";
    }

}
