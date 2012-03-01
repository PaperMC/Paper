package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityThrownExpBottle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftProjectile implements ThrownExpBottle {
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

    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
