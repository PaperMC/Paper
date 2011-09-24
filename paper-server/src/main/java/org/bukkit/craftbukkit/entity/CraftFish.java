package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFishingHook;
import net.minecraft.server.EntityHuman;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;

public class CraftFish extends AbstractProjectile implements Fish {
    public CraftFish(CraftServer server, EntityFishingHook entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftFish";
    }

    public LivingEntity getShooter() {
        if (((EntityFishingHook) getHandle()).owner != null) {
            return (LivingEntity) ((EntityFishingHook) getHandle()).owner.getBukkitEntity();
        }

        return null;

    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftHumanEntity) {
            ((EntityFishingHook) getHandle()).owner = (EntityHuman) ((CraftHumanEntity) shooter).entity;
        }
    }

}
