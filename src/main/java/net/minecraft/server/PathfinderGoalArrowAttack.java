package net.minecraft.server;

import org.bukkit.event.entity.EntityTargetEvent; // CraftBukkit

public class PathfinderGoalArrowAttack extends PathfinderGoal {

    private final EntityLiving a;
    private final IRangedEntity b;
    private EntityLiving c;
    private int d = 0;
    private float e;
    private int f = 0;
    private int g;
    private float h;

    public PathfinderGoalArrowAttack(IRangedEntity irangedentity, float f, int i, float f1) {
        if (!(irangedentity instanceof EntityLiving)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.b = irangedentity;
            this.a = (EntityLiving) irangedentity;
            this.e = f;
            this.g = i;
            this.h = f1 * f1;
            this.a(3);
        }
    }

    public boolean a() {
        EntityLiving entityliving = this.a.aF();

        if (entityliving == null) {
            return false;
        } else {
            this.c = entityliving;
            return true;
        }
    }

    public boolean b() {
        return this.a() || !this.a.getNavigation().f();
    }

    public void d() {
        // CraftBukkit start
        EntityTargetEvent.TargetReason reason = this.c.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTargetEvent((Entity) b, null, reason);
        // CraftBukkit end
        this.c = null;
        this.f = 0;
    }

    public void e() {
        double d0 = this.a.e(this.c.locX, this.c.boundingBox.b, this.c.locZ);
        boolean flag = this.a.az().canSee(this.c);

        if (flag) {
            ++this.f;
        } else {
            this.f = 0;
        }

        if (d0 <= (double) this.h && this.f >= 20) {
            this.a.getNavigation().g();
        } else {
            this.a.getNavigation().a(this.c, this.e);
        }

        this.a.getControllerLook().a(this.c, 30.0F, 30.0F);
        this.d = Math.max(this.d - 1, 0);
        if (this.d <= 0) {
            if (d0 <= (double) this.h && flag) {
                this.b.d(this.c);
                this.d = this.g;
            }
        }
    }
}
