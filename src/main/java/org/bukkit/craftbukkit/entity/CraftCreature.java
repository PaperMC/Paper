package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class CraftCreature extends CraftLivingEntity implements Creature{
    private EntityCreature entity;

    public CraftCreature(CraftServer server, EntityCreature entity) {
        super(server, entity);
        this.entity = entity;
    }

    public void setTarget(LivingEntity target) {
        if (target == null) {
            entity.d = null;
        } else if (target instanceof CraftLivingEntity) {
            EntityLiving victim = ((CraftLivingEntity)target).getHandle();
            entity.d = victim;
            entity.a = entity.world.a(entity, entity.d, 16.0F);
        }
    }

    public CraftLivingEntity getTarget() {
        if (entity.d == null) {
            return null;
        } else {
            return (CraftLivingEntity)entity.d.getBukkitEntity();
        }
    }

    @Override
    public EntityCreature getHandle() {
        return entity;
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }
}
