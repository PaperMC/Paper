package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Breeze;

public class CraftBreeze extends CraftMonster implements Breeze {

    public CraftBreeze(CraftServer server, net.minecraft.world.entity.monster.breeze.Breeze entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.breeze.Breeze getHandle() {
        return (net.minecraft.world.entity.monster.breeze.Breeze) this.entity;
    }

    @Override
    public int getInhaleTicks() {
        return this.getHandle().getInhaleTicks();
    }

    @Override
    public void setInhaleTicks(int ticks) {
        this.getHandle().setInhaleTicks(ticks);
    }

    @Override
    public int getJumpCooldown() {
        return this.getHandle().getJumpCooldownTicks();
    }

    @Override
    public void setJumpCooldown(int ticks) {
        this.getHandle().setJumpCooldownTicks(ticks);
    }

    @Override
    public boolean isJumping() {
        return this.getHandle().isJumping();
    }

    /* // TODO - snapshot - reimplement? but without reintroducing MC-199589
    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(target);
        net.minecraft.world.entity.LivingEntity attackTarget = (target instanceof CraftLivingEntity craftLivingEntity) ? craftLivingEntity.getHandle() : null;
        this.getHandle().getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget); // SPIGOT-7957: We need override memory for set target and trigger attack behaviours
    }
    */
}
