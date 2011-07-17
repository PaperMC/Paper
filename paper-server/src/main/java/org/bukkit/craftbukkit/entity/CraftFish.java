package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFish;
import net.minecraft.server.EntityHuman;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;

public class CraftFish extends CraftEntity implements Fish {
    public CraftFish(CraftServer server, EntityFish entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftFish";
    }

    public LivingEntity getShooter() {
        if (((EntityFish) getHandle()).owner != null) {
            return (LivingEntity) ((EntityFish) getHandle()).owner.getBukkitEntity();
        }

        return null;

    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftHumanEntity) {
            ((EntityFish) getHandle()).owner = (EntityHuman) ((CraftHumanEntity) shooter).entity;
        }
    }

}
