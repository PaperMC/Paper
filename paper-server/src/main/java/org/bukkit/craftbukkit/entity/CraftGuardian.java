package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;

public class CraftGuardian extends CraftMonster implements Guardian {

    private static final int MINIMUM_ATTACK_TICKS = -10;

    public CraftGuardian(CraftServer server, EntityGuardian entity) {
        super(server, entity);
    }

    @Override
    public EntityGuardian getHandle() {
        return (EntityGuardian) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(target);

        // clean up laser target, when target is removed
        if (target == null) {
            getHandle().setActiveAttackTarget(0);
        }
    }

    @Override
    public boolean setLaser(boolean activated) {
        if (activated) {
            LivingEntity target = getTarget();
            if (target == null) {
                return false;
            }

            getHandle().setActiveAttackTarget(target.getEntityId());
        } else {
            getHandle().setActiveAttackTarget(0);
        }

        return true;
    }

    @Override
    public boolean hasLaser() {
        return getHandle().hasActiveAttackTarget();
    }

    @Override
    public int getLaserDuration() {
        return getHandle().getAttackDuration();
    }

    @Override
    public void setLaserTicks(int ticks) {
        Preconditions.checkArgument(ticks >= MINIMUM_ATTACK_TICKS, "ticks must be >= %s. Given %s", MINIMUM_ATTACK_TICKS, ticks);

        EntityGuardian.PathfinderGoalGuardianAttack goal = getHandle().guardianAttackGoal;
        if (goal != null) {
            goal.attackTime = ticks;
        }
    }

    @Override
    public int getLaserTicks() {
        EntityGuardian.PathfinderGoalGuardianAttack goal = getHandle().guardianAttackGoal;
        return (goal != null) ? goal.attackTime : MINIMUM_ATTACK_TICKS;
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
        return getHandle().isMoving();
    }
}
