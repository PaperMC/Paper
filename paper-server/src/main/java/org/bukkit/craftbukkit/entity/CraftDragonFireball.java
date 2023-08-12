package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.EntityDragonFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.DragonFireball;

public class CraftDragonFireball extends CraftFireball implements DragonFireball {
    public CraftDragonFireball(CraftServer server, EntityDragonFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftDragonFireball";
    }
}
