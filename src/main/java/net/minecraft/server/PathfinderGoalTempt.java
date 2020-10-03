package net.minecraft.server;

import java.util.EnumSet;

public class PathfinderGoalTempt extends PathfinderGoal {

    private static final PathfinderTargetCondition c = (new PathfinderTargetCondition()).a(10.0D).a().b().d().c();
    protected final EntityCreature a;
    private final double d;
    private double e;
    private double f;
    private double g;
    private double h;
    private double i;
    protected EntityHuman target;
    private int j;
    private boolean k;
    private final RecipeItemStack l;
    private final boolean m;

    public PathfinderGoalTempt(EntityCreature entitycreature, double d0, RecipeItemStack recipeitemstack, boolean flag) {
        this(entitycreature, d0, flag, recipeitemstack);
    }

    public PathfinderGoalTempt(EntityCreature entitycreature, double d0, boolean flag, RecipeItemStack recipeitemstack) {
        this.a = entitycreature;
        this.d = d0;
        this.l = recipeitemstack;
        this.m = flag;
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        if (!(entitycreature.getNavigation() instanceof Navigation) && !(entitycreature.getNavigation() instanceof NavigationFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }

    @Override
    public boolean a() {
        if (this.j > 0) {
            --this.j;
            return false;
        } else {
            this.target = this.a.world.a(PathfinderGoalTempt.c, (EntityLiving) this.a);
            return this.target == null ? false : this.a(this.target.getItemInMainHand()) || this.a(this.target.getItemInOffHand());
        }
    }

    protected boolean a(ItemStack itemstack) {
        return this.l.test(itemstack);
    }

    @Override
    public boolean b() {
        if (this.g()) {
            if (this.a.h((Entity) this.target) < 36.0D) {
                if (this.target.h(this.e, this.f, this.g) > 0.010000000000000002D) {
                    return false;
                }

                if (Math.abs((double) this.target.pitch - this.h) > 5.0D || Math.abs((double) this.target.yaw - this.i) > 5.0D) {
                    return false;
                }
            } else {
                this.e = this.target.locX();
                this.f = this.target.locY();
                this.g = this.target.locZ();
            }

            this.h = (double) this.target.pitch;
            this.i = (double) this.target.yaw;
        }

        return this.a();
    }

    protected boolean g() {
        return this.m;
    }

    @Override
    public void c() {
        this.e = this.target.locX();
        this.f = this.target.locY();
        this.g = this.target.locZ();
        this.k = true;
    }

    @Override
    public void d() {
        this.target = null;
        this.a.getNavigation().o();
        this.j = 100;
        this.k = false;
    }

    @Override
    public void e() {
        this.a.getControllerLook().a(this.target, (float) (this.a.eo() + 20), (float) this.a.O());
        if (this.a.h((Entity) this.target) < 6.25D) {
            this.a.getNavigation().o();
        } else {
            this.a.getNavigation().a((Entity) this.target, this.d);
        }

    }

    public boolean h() {
        return this.k;
    }
}
