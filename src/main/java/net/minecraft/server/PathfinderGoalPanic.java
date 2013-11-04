package net.minecraft.server;

public class PathfinderGoalPanic extends PathfinderGoal {

    private EntityCreature a;
    private double b;
    private double c;
    private double d;
    private double e;

    public PathfinderGoalPanic(EntityCreature entitycreature, double d0) {
        this.a = entitycreature;
        this.b = d0;
        this.a(1);
    }

    public boolean a() {
        if (this.a.getLastDamager() == null && !this.a.isBurning()) {
            return false;
        } else {
            Vec3D vec3d = RandomPositionGenerator.a(this.a, 5, 4);

            if (vec3d == null) {
                return false;
            } else {
                this.c = vec3d.c;
                this.d = vec3d.d;
                this.e = vec3d.e;
                return true;
            }
        }
    }

    public void c() {
        this.a.getNavigation().a(this.c, this.d, this.e, this.b);
    }

    public boolean b() {
        // CraftBukkit start - introduce a temporary timeout hack until this is fixed properly
        if ((this.a.ticksLived - this.a.aK()) > 100) {
            this.a.b((EntityLiving) null);
            return false;
        }
        // CraftBukkit end
        return !this.a.getNavigation().g();
    }
}
