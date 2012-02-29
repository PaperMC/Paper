package org.bukkit.craftbukkit.entity;

import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityComplex;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {
    public CraftComplexLivingEntity(CraftServer server, EntityComplex entity) {
        super(server, entity);
    }

    @Override
    public EntityComplex getHandle() {
        return (EntityComplex) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }

    @Override
    public void damage(int amount, org.bukkit.entity.Entity source) {
        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.playerAttack(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.mobAttack(((CraftLivingEntity) source).getHandle());
        }

        if (entity instanceof EntityComplex) {
            ((EntityComplex) entity).dealDamage(reason, amount);
        } else {
            entity.damageEntity(reason, amount);
        }
    }
}
