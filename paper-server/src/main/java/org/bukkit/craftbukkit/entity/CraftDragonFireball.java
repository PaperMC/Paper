package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.DragonFireball;

public class CraftDragonFireball extends CraftFireball implements DragonFireball {
    public CraftDragonFireball(CraftServer server, net.minecraft.world.entity.projectile.DragonFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftDragonFireball";
    }
}
