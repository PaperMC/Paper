package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityCow;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.MushroomCow;

public class CraftMushroomCow extends CraftCow implements MushroomCow {
    public CraftMushroomCow(CraftServer server, EntityCow entity) {
        super(server, entity);
    }
}
