package net.minecraft.server;

import java.util.EnumSet;

public abstract class PathfinderGoalGotoTarget extends PathfinderGoal {

    protected final EntityCreature a;
    public final double b;
    protected int c;
    protected int d;
    private int g;
    protected BlockPosition e;
    private boolean h;
    private final int i;
    private final int j;
    protected int f;

    public PathfinderGoalGotoTarget(EntityCreature entitycreature, double d0, int i) {
        this(entitycreature, d0, i, 1);
    }

    public PathfinderGoalGotoTarget(EntityCreature entitycreature, double d0, int i, int j) {
        this.e = BlockPosition.ZERO;
        this.a = entitycreature;
        this.b = d0;
        this.i = i;
        this.f = 0;
        this.j = j;
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.JUMP));
    }

    @Override
    public boolean a() {
        if (this.c > 0) {
            --this.c;
            return false;
        } else {
            this.c = this.a(this.a);
            return this.m();
        }
    }

    protected int a(EntityCreature entitycreature) {
        return 200 + entitycreature.getRandom().nextInt(200);
    }

    @Override
    public boolean b() {
        return this.d >= -this.g && this.d <= 1200 && this.a(this.a.world, this.e);
    }

    @Override
    public void c() {
        this.g();
        this.d = 0;
        this.g = this.a.getRandom().nextInt(this.a.getRandom().nextInt(1200) + 1200) + 1200;
    }

    protected void g() {
        this.a.getNavigation().a((double) ((float) this.e.getX()) + 0.5D, (double) (this.e.getY() + 1), (double) ((float) this.e.getZ()) + 0.5D, this.b);
    }

    public double h() {
        return 1.0D;
    }

    protected BlockPosition j() {
        return this.e.up();
    }

    @Override
    public void e() {
        BlockPosition blockposition = this.j();

        if (!blockposition.a((IPosition) this.a.getPositionVector(), this.h())) {
            this.h = false;
            ++this.d;
            if (this.k()) {
                this.a.getNavigation().a((double) ((float) blockposition.getX()) + 0.5D, (double) blockposition.getY(), (double) ((float) blockposition.getZ()) + 0.5D, this.b);
            }
        } else {
            this.h = true;
            --this.d;
        }

    }

    public boolean k() {
        return this.d % 40 == 0;
    }

    protected boolean l() {
        return this.h;
    }

    protected boolean m() {
        int i = this.i;
        int j = this.j;
        BlockPosition blockposition = this.a.getChunkCoordinates();
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        for (int k = this.f; k <= j; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < i; ++l) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, i1, k - 1, j1);
                        if (this.a.a((BlockPosition) blockposition_mutableblockposition) && this.a(this.a.world, blockposition_mutableblockposition)) {
                            this.e = blockposition_mutableblockposition;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    protected abstract boolean a(IWorldReader iworldreader, BlockPosition blockposition);
}
