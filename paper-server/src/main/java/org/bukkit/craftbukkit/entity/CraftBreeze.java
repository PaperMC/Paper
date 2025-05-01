package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Breeze;
import org.bukkit.entity.LivingEntity;

public class CraftBreeze extends CraftMonster implements Breeze {

    public CraftBreeze(CraftServer server, net.minecraft.world.entity.monster.breeze.Breeze entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.breeze.Breeze getHandle() {
        return (net.minecraft.world.entity.monster.breeze.Breeze) this.entity;
    }

    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(target);
        net.minecraft.world.entity.LivingEntity entityLivingTarget = (target instanceof CraftLivingEntity craftLivingEntity) ? craftLivingEntity.getHandle() : null;
        this.getHandle().getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, entityLivingTarget); // SPIGOT-7957: We need override memory for set target and trigger attack behaviours
    }
}
