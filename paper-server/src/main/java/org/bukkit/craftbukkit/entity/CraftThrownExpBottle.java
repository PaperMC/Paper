package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.EntityThrownExpBottle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftThrowableProjectile implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, EntityThrownExpBottle entity) {
        super(server, entity);
    }

    @Override
    public EntityThrownExpBottle getHandle() {
        return (EntityThrownExpBottle) entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }
}
