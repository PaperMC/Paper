package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.Mob;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Flying;

// TODO: REMOVE?
public class CraftFlying extends CraftMob implements Flying {

    public CraftFlying(CraftServer server, Mob entity) {
        super(server, entity);
    }

}
