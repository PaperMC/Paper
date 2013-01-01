package net.minecraft.server;

public class PathfinderGoalSit extends PathfinderGoal {

    private EntityTameableAnimal a;
    private boolean b = false;

    public PathfinderGoalSit(EntityTameableAnimal entitytameableanimal) {
        this.a = entitytameableanimal;
        this.a(5);
    }

    public boolean a() {
        if (!this.a.isTamed()) {
            return false;
        } else if (this.a.H()) {
            return false;
        } else if (!this.a.onGround) {
            return false;
        } else {
            EntityLiving entityliving = this.a.getOwner();

            return entityliving == null ? true : (this.a.e(entityliving) < 144.0D && entityliving.aC() != null ? false : this.b);
        }
    }

    public void c() {
        this.a.getNavigation().g();
        this.a.setSitting(true);
    }

    public void d() {
        this.a.setSitting(false);
    }

    public void a(boolean flag) {
        this.b = flag;
    }
}
