package net.minecraft.server;

import java.util.EnumSet;

public class PathfinderGoalSit extends PathfinderGoal {

    private final EntityTameableAnimal entity;

    public PathfinderGoalSit(EntityTameableAnimal entitytameableanimal) {
        this.entity = entitytameableanimal;
        this.a(EnumSet.of(PathfinderGoal.Type.JUMP, PathfinderGoal.Type.MOVE));
    }

    @Override
    public boolean b() {
        return this.entity.isWillSit();
    }

    @Override
    public boolean a() {
        if (!this.entity.isTamed()) {
            return false;
        } else if (this.entity.aG()) {
            return false;
        } else if (!this.entity.isOnGround()) {
            return false;
        } else {
            EntityLiving entityliving = this.entity.getOwner();

            return entityliving == null ? true : (this.entity.h((Entity) entityliving) < 144.0D && entityliving.getLastDamager() != null ? false : this.entity.isWillSit());
        }
    }

    @Override
    public void c() {
        this.entity.getNavigation().o();
        this.entity.setSitting(true);
    }

    @Override
    public void d() {
        this.entity.setSitting(false);
    }
}
