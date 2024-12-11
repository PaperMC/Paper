package com.destroystokyo.paper.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public interface CraftRangedEntity<T extends Mob & RangedAttackMob> extends RangedEntity {
    T getHandle();

    @Override
    default void rangedAttack(LivingEntity target, float charge) {
        getHandle().performRangedAttack(((CraftLivingEntity) target).getHandle(), charge);
    }

    @Override
    default void setChargingAttack(boolean raiseHands) {
        getHandle().setAggressive(raiseHands);
    }
}
