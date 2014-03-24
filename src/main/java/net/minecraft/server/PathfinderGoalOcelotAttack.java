package net.minecraft.server;

public class PathfinderGoalOcelotAttack extends PathfinderGoal {

    World a;
    EntityInsentient b;
    EntityLiving c;
    int d;

    public PathfinderGoalOcelotAttack(EntityInsentient entityinsentient) {
        this.b = entityinsentient;
        this.a = entityinsentient.world;
        this.a(3);
    }

    public boolean a() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else {
            this.c = entityliving;
            return true;
        }
    }

    public boolean b() {
        return !this.c.isAlive() ? false : (this.b.f(this.c) > 225.0D ? false : !this.b.getNavigation().g() || this.a());
    }

    public void d() {
        this.c = null;
        this.b.getNavigation().h();
    }

    public void e() {
        this.b.getControllerLook().a(this.c, 30.0F, 30.0F);
        double d0 = (double) (this.b.width * 2.0F * this.b.width * 2.0F);
        double d1 = this.b.e(this.c.locX, this.c.boundingBox.b, this.c.locZ);
        double d2 = 0.8D;

        if (d1 > d0 && d1 < 16.0D) {
            d2 = 1.33D;
        } else if (d1 < 225.0D) {
            d2 = 0.6D;
        }

        this.b.getNavigation().a((Entity) this.c, d2);
        this.d = Math.max(this.d - 1, 0);
        if (d1 <= d0) {
            if (this.d <= 0) {
                this.d = 20;
                this.b.n(this.c);
            }
        }
    }
}
