package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.projectile.EntityEvokerFangs;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;

public class CraftEvokerFangs extends CraftEntity implements EvokerFangs {

    public CraftEvokerFangs(CraftServer server, EntityEvokerFangs entity) {
        super(server, entity);
    }

    @Override
    public EntityEvokerFangs getHandle() {
        return (EntityEvokerFangs) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvokerFangs";
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER_FANGS;
    }

    @Override
    public LivingEntity getOwner() {
        EntityLiving owner = getHandle().getOwner();

        return (owner == null) ? null : (LivingEntity) owner.getBukkitEntity();
    }

    @Override
    public void setOwner(LivingEntity owner) {
        getHandle().a(owner == null ? null : ((CraftLivingEntity) owner).getHandle());
    }
}
