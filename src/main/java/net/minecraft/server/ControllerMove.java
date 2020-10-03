package net.minecraft.server;

public class ControllerMove {

    protected final EntityInsentient a;
    protected double b;
    protected double c;
    protected double d;
    protected double e;
    protected float f;
    protected float g;
    protected ControllerMove.Operation h;

    public ControllerMove(EntityInsentient entityinsentient) {
        this.h = ControllerMove.Operation.WAIT;
        this.a = entityinsentient;
    }

    public boolean b() {
        return this.h == ControllerMove.Operation.MOVE_TO;
    }

    public double c() {
        return this.e;
    }

    public void a(double d0, double d1, double d2, double d3) {
        this.b = d0;
        this.c = d1;
        this.d = d2;
        this.e = d3;
        if (this.h != ControllerMove.Operation.JUMPING) {
            this.h = ControllerMove.Operation.MOVE_TO;
        }

    }

    public void a(float f, float f1) {
        this.h = ControllerMove.Operation.STRAFE;
        this.f = f;
        this.g = f1;
        this.e = 0.25D;
    }

    public void a() {
        float f;

        if (this.h == ControllerMove.Operation.STRAFE) {
            float f1 = (float) this.a.b(GenericAttributes.MOVEMENT_SPEED);
            float f2 = (float) this.e * f1;
            float f3 = this.f;
            float f4 = this.g;
            float f5 = MathHelper.c(f3 * f3 + f4 * f4);

            if (f5 < 1.0F) {
                f5 = 1.0F;
            }

            f5 = f2 / f5;
            f3 *= f5;
            f4 *= f5;
            float f6 = MathHelper.sin(this.a.yaw * 0.017453292F);
            float f7 = MathHelper.cos(this.a.yaw * 0.017453292F);
            float f8 = f3 * f7 - f4 * f6;

            f = f4 * f7 + f3 * f6;
            if (!this.b(f8, f)) {
                this.f = 1.0F;
                this.g = 0.0F;
            }

            this.a.q(f2);
            this.a.t(this.f);
            this.a.v(this.g);
            this.h = ControllerMove.Operation.WAIT;
        } else if (this.h == ControllerMove.Operation.MOVE_TO) {
            this.h = ControllerMove.Operation.WAIT;
            double d0 = this.b - this.a.locX();
            double d1 = this.d - this.a.locZ();
            double d2 = this.c - this.a.locY();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D) {
                this.a.t(0.0F);
                return;
            }

            f = (float) (MathHelper.d(d1, d0) * 57.2957763671875D) - 90.0F;
            this.a.yaw = this.a(this.a.yaw, f, 90.0F);
            this.a.q((float) (this.e * this.a.b(GenericAttributes.MOVEMENT_SPEED)));
            BlockPosition blockposition = this.a.getChunkCoordinates();
            IBlockData iblockdata = this.a.world.getType(blockposition);
            Block block = iblockdata.getBlock();
            VoxelShape voxelshape = iblockdata.getCollisionShape(this.a.world, blockposition);

            if (d2 > (double) this.a.G && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.a.getWidth()) || !voxelshape.isEmpty() && this.a.locY() < voxelshape.c(EnumDirection.EnumAxis.Y) + (double) blockposition.getY() && !block.a((Tag) TagsBlock.DOORS) && !block.a((Tag) TagsBlock.FENCES)) {
                this.a.getControllerJump().jump();
                this.h = ControllerMove.Operation.JUMPING;
            }
        } else if (this.h == ControllerMove.Operation.JUMPING) {
            this.a.q((float) (this.e * this.a.b(GenericAttributes.MOVEMENT_SPEED)));
            if (this.a.isOnGround()) {
                this.h = ControllerMove.Operation.WAIT;
            }
        } else {
            this.a.t(0.0F);
        }

    }

    private boolean b(float f, float f1) {
        NavigationAbstract navigationabstract = this.a.getNavigation();

        if (navigationabstract != null) {
            PathfinderAbstract pathfinderabstract = navigationabstract.q();

            if (pathfinderabstract != null && pathfinderabstract.a(this.a.world, MathHelper.floor(this.a.locX() + (double) f), MathHelper.floor(this.a.locY()), MathHelper.floor(this.a.locZ() + (double) f1)) != PathType.WALKABLE) {
                return false;
            }
        }

        return true;
    }

    protected float a(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        float f4 = f + f3;

        if (f4 < 0.0F) {
            f4 += 360.0F;
        } else if (f4 > 360.0F) {
            f4 -= 360.0F;
        }

        return f4;
    }

    public double d() {
        return this.b;
    }

    public double e() {
        return this.c;
    }

    public double f() {
        return this.d;
    }

    public static enum Operation {

        WAIT, MOVE_TO, STRAFE, JUMPING;

        private Operation() {}
    }
}
