package net.minecraft.server;

import java.util.function.Predicate;
import javax.annotation.Nullable;

public class PathfinderTargetCondition {

    public static final PathfinderTargetCondition a = new PathfinderTargetCondition();
    private double b = -1.0D;
    private boolean c;
    private boolean d;
    private boolean e;
    private boolean f;
    private boolean g = true;
    private Predicate<EntityLiving> h;

    public PathfinderTargetCondition() {}

    public PathfinderTargetCondition a(double d0) {
        this.b = d0;
        return this;
    }

    public PathfinderTargetCondition a() {
        this.c = true;
        return this;
    }

    public PathfinderTargetCondition b() {
        this.d = true;
        return this;
    }

    public PathfinderTargetCondition c() {
        this.e = true;
        return this;
    }

    public PathfinderTargetCondition d() {
        this.f = true;
        return this;
    }

    public PathfinderTargetCondition e() {
        this.g = false;
        return this;
    }

    public PathfinderTargetCondition a(@Nullable Predicate<EntityLiving> predicate) {
        this.h = predicate;
        return this;
    }

    public boolean a(@Nullable EntityLiving entityliving, EntityLiving entityliving1) {
        if (entityliving == entityliving1) {
            return false;
        } else if (entityliving1.isSpectator()) {
            return false;
        } else if (!entityliving1.isAlive()) {
            return false;
        } else if (!this.c && entityliving1.isInvulnerable()) {
            return false;
        } else if (this.h != null && !this.h.test(entityliving1)) {
            return false;
        } else {
            if (entityliving != null) {
                if (!this.f) {
                    if (!entityliving.c(entityliving1)) {
                        return false;
                    }

                    if (!entityliving.a(entityliving1.getEntityType())) {
                        return false;
                    }
                }

                if (!this.d && entityliving.r(entityliving1)) {
                    return false;
                }

                if (this.b > 0.0D) {
                    double d0 = this.g ? entityliving1.A(entityliving) : 1.0D;
                    double d1 = Math.max(this.b * d0, 2.0D);
                    double d2 = entityliving.h(entityliving1.locX(), entityliving1.locY(), entityliving1.locZ());

                    if (d2 > d1 * d1) {
                        return false;
                    }
                }

                if (!this.e && entityliving instanceof EntityInsentient && !((EntityInsentient) entityliving).getEntitySenses().a(entityliving1)) {
                    return false;
                }
            }

            return true;
        }
    }
}
