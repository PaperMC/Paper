package com.destroystokyo.paper.entity;

import net.minecraft.server.IRangedEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public interface CraftRangedEntity<T extends IRangedEntity> extends RangedEntity {
    T getHandle();

    @Override
    default void rangedAttack(LivingEntity target, float charge) {
        getHandle().rangedAttack(((CraftLivingEntity) target).getHandle(), charge);
    }

    @Override
    default void setChargingAttack(boolean raiseHands) {
        getHandle().setChargingAttack(raiseHands);
    }
}
