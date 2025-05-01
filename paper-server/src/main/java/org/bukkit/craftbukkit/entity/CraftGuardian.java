package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;

public class CraftGuardian extends CraftMonster implements Guardian {

    private static final int MINIMUM_ATTACK_TICKS = -10;

    public CraftGuardian(CraftServer server, net.minecraft.world.entity.monster.Guardian entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Guardian getHandle() {
        return (net.minecraft.world.entity.monster.Guardian) this.entity;
    }

    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(target);

        // clean up laser target, when target is removed
        if (target == null) {
            this.getHandle().setActiveAttackTarget(0);
        }
    }

    @Override
    public boolean setLaser(boolean activated) {
        if (activated) {
            LivingEntity target = this.getTarget();
            if (target == null) {
                return false;
            }

            this.getHandle().setActiveAttackTarget(target.getEntityId());
        } else {
            this.getHandle().setActiveAttackTarget(0);
        }

        return true;
    }

    @Override
    public boolean hasLaser() {
        return this.getHandle().hasActiveAttackTarget();
    }

    @Override
    public int getLaserDuration() {
        return this.getHandle().getAttackDuration();
    }

    @Override
    public void setLaserTicks(int ticks) {
        Preconditions.checkArgument(ticks >= CraftGuardian.MINIMUM_ATTACK_TICKS, "ticks must be >= %s. Given %s", CraftGuardian.MINIMUM_ATTACK_TICKS, ticks);

        net.minecraft.world.entity.monster.Guardian.GuardianAttackGoal goal = this.getHandle().guardianAttackGoal;
        if (goal != null) {
            goal.attackTime = ticks;
        }
    }

    @Override
    public int getLaserTicks() {
        net.minecraft.world.entity.monster.Guardian.GuardianAttackGoal goal = this.getHandle().guardianAttackGoal;
        return (goal != null) ? goal.attackTime : CraftGuardian.MINIMUM_ATTACK_TICKS;
    }

    @Override
    public boolean isElder() {
        return false;
    }

    @Override
    public void setElder(boolean shouldBeElder) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isMoving() {
        return this.getHandle().isMoving();
    }
}
