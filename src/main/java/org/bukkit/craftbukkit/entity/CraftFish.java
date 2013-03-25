package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFishingHook;
import net.minecraft.server.EntityHuman;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;

public class CraftFish extends AbstractProjectile implements Fish {
    public CraftFish(CraftServer server, EntityFishingHook entity) {
        super(server, entity);
    }

    public LivingEntity getShooter() {
        if (getHandle().owner != null) {
            return getHandle().owner.getBukkitEntity();
        }

        return null;
    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftHumanEntity) {
            getHandle().owner = (EntityHuman) ((CraftHumanEntity) shooter).entity;
        }
    }

    @Override
    public EntityFishingHook getHandle() {
        return (EntityFishingHook) entity;
    }

    @Override
    public String toString() {
        return "CraftFish";
    }

    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }
}
