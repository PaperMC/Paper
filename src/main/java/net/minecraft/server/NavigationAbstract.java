package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public abstract class NavigationAbstract {

    protected final EntityInsentient a;
    protected final World b;
    @Nullable
    protected PathEntity c;
    protected double d;
    protected int e;
    protected int f;
    protected Vec3D g;
    protected BaseBlockPosition h;
    protected long i;
    protected long j;
    protected double k;
    protected float l;
    protected boolean m;
    protected long n;
    protected PathfinderAbstract o;
    private BlockPosition p;
    private int q;
    private float r;
    private final Pathfinder s;
    private boolean t;

    public NavigationAbstract(EntityInsentient entityinsentient, World world) {
        this.g = Vec3D.ORIGIN;
        this.h = BaseBlockPosition.ZERO;
        this.l = 0.5F;
        this.r = 1.0F;
        this.a = entityinsentient;
        this.b = world;
        int i = MathHelper.floor(entityinsentient.b(GenericAttributes.FOLLOW_RANGE) * 16.0D);

        this.s = this.a(i);
    }

    public void g() {
        this.r = 1.0F;
    }

    public void a(float f) {
        this.r = f;
    }

    public BlockPosition h() {
        return this.p;
    }

    protected abstract Pathfinder a(int i);

    public void a(double d0) {
        this.d = d0;
    }

    public boolean i() {
        return this.m;
    }

    public void j() {
        if (this.b.getTime() - this.n > 20L) {
            if (this.p != null) {
                this.c = null;
                this.c = this.a(this.p, this.q);
                this.n = this.b.getTime();
                this.m = false;
            }
        } else {
            this.m = true;
        }

    }

    @Nullable
    public final PathEntity a(double d0, double d1, double d2, int i) {
        return this.a(new BlockPosition(d0, d1, d2), i);
    }

    @Nullable
    public PathEntity a(Stream<BlockPosition> stream, int i) {
        return this.a((Set) stream.collect(Collectors.toSet()), 8, false, i);
    }

    @Nullable
    public PathEntity a(Set<BlockPosition> set, int i) {
        return this.a(set, 8, false, i);
    }

    @Nullable
    public PathEntity a(BlockPosition blockposition, int i) {
        return this.a(ImmutableSet.of(blockposition), 8, false, i);
    }

    @Nullable
    public PathEntity a(Entity entity, int i) {
        return this.a(ImmutableSet.of(entity.getChunkCoordinates()), 16, true, i);
    }

    @Nullable
    protected PathEntity a(Set<BlockPosition> set, int i, boolean flag, int j) {
        if (set.isEmpty()) {
            return null;
        } else if (this.a.locY() < 0.0D) {
            return null;
        } else if (!this.a()) {
            return null;
        } else if (this.c != null && !this.c.c() && set.contains(this.p)) {
            return this.c;
        } else {
            this.b.getMethodProfiler().enter("pathfind");
            float f = (float) this.a.b(GenericAttributes.FOLLOW_RANGE);
            BlockPosition blockposition = flag ? this.a.getChunkCoordinates().up() : this.a.getChunkCoordinates();
            int k = (int) (f + (float) i);
            ChunkCache chunkcache = new ChunkCache(this.b, blockposition.b(-k, -k, -k), blockposition.b(k, k, k));
            PathEntity pathentity = this.s.a(chunkcache, this.a, set, f, j, this.r);

            this.b.getMethodProfiler().exit();
            if (pathentity != null && pathentity.m() != null) {
                this.p = pathentity.m();
                this.q = j;
                this.f();
            }

            return pathentity;
        }
    }

    public boolean a(double d0, double d1, double d2, double d3) {
        return this.a(this.a(d0, d1, d2, 1), d3);
    }

    public boolean a(Entity entity, double d0) {
        PathEntity pathentity = this.a(entity, 1);

        return pathentity != null && this.a(pathentity, d0);
    }

    public boolean a(@Nullable PathEntity pathentity, double d0) {
        if (pathentity == null) {
            this.c = null;
            return false;
        } else {
            if (!pathentity.a(this.c)) {
                this.c = pathentity;
            }

            if (this.m()) {
                return false;
            } else {
                this.D_();
                if (this.c.e() <= 0) {
                    return false;
                } else {
                    this.d = d0;
                    Vec3D vec3d = this.b();

                    this.f = this.e;
                    this.g = vec3d;
                    return true;
                }
            }
        }
    }

    @Nullable
    public PathEntity k() {
        return this.c;
    }

    public void c() {
        ++this.e;
        if (this.m) {
            this.j();
        }

        if (!this.m()) {
            Vec3D vec3d;

            if (this.a()) {
                this.l();
            } else if (this.c != null && !this.c.c()) {
                vec3d = this.b();
                Vec3D vec3d1 = this.c.a((Entity) this.a);

                if (vec3d.y > vec3d1.y && !this.a.isOnGround() && MathHelper.floor(vec3d.x) == MathHelper.floor(vec3d1.x) && MathHelper.floor(vec3d.z) == MathHelper.floor(vec3d1.z)) {
                    this.c.a();
                }
            }

            PacketDebug.a(this.b, this.a, this.c, this.l);
            if (!this.m()) {
                vec3d = this.c.a((Entity) this.a);
                BlockPosition blockposition = new BlockPosition(vec3d);

                this.a.getControllerMove().a(vec3d.x, this.b.getType(blockposition.down()).isAir() ? vec3d.y : PathfinderNormal.a((IBlockAccess) this.b, blockposition), vec3d.z, this.d);
            }
        }
    }

    protected void l() {
        Vec3D vec3d = this.b();

        this.l = this.a.getWidth() > 0.75F ? this.a.getWidth() / 2.0F : 0.75F - this.a.getWidth() / 2.0F;
        BlockPosition blockposition = this.c.g();
        double d0 = Math.abs(this.a.locX() - ((double) blockposition.getX() + 0.5D));
        double d1 = Math.abs(this.a.locY() - (double) blockposition.getY());
        double d2 = Math.abs(this.a.locZ() - ((double) blockposition.getZ() + 0.5D));
        boolean flag = d0 < (double) this.l && d2 < (double) this.l && d1 < 1.0D;

        if (flag || this.a.b(this.c.h().l) && this.b(vec3d)) {
            this.c.a();
        }

        this.a(vec3d);
    }

    private boolean b(Vec3D vec3d) {
        if (this.c.f() + 1 >= this.c.e()) {
            return false;
        } else {
            Vec3D vec3d1 = Vec3D.c((BaseBlockPosition) this.c.g());

            if (!vec3d.a((IPosition) vec3d1, 2.0D)) {
                return false;
            } else {
                Vec3D vec3d2 = Vec3D.c((BaseBlockPosition) this.c.d(this.c.f() + 1));
                Vec3D vec3d3 = vec3d2.d(vec3d1);
                Vec3D vec3d4 = vec3d.d(vec3d1);

                return vec3d3.b(vec3d4) > 0.0D;
            }
        }
    }

    protected void a(Vec3D vec3d) {
        if (this.e - this.f > 100) {
            if (vec3d.distanceSquared(this.g) < 2.25D) {
                this.t = true;
                this.o();
            } else {
                this.t = false;
            }

            this.f = this.e;
            this.g = vec3d;
        }

        if (this.c != null && !this.c.c()) {
            BlockPosition blockposition = this.c.g();

            if (blockposition.equals(this.h)) {
                this.i += SystemUtils.getMonotonicMillis() - this.j;
            } else {
                this.h = blockposition;
                double d0 = vec3d.f(Vec3D.c(this.h));

                this.k = this.a.dM() > 0.0F ? d0 / (double) this.a.dM() * 1000.0D : 0.0D;
            }

            if (this.k > 0.0D && (double) this.i > this.k * 3.0D) {
                this.e();
            }

            this.j = SystemUtils.getMonotonicMillis();
        }

    }

    private void e() {
        this.f();
        this.o();
    }

    private void f() {
        this.h = BaseBlockPosition.ZERO;
        this.i = 0L;
        this.k = 0.0D;
        this.t = false;
    }

    public boolean m() {
        return this.c == null || this.c.c();
    }

    public boolean n() {
        return !this.m();
    }

    public void o() {
        this.c = null;
    }

    protected abstract Vec3D b();

    protected abstract boolean a();

    protected boolean p() {
        return this.a.aG() || this.a.aP();
    }

    protected void D_() {
        if (this.c != null) {
            for (int i = 0; i < this.c.e(); ++i) {
                PathPoint pathpoint = this.c.a(i);
                PathPoint pathpoint1 = i + 1 < this.c.e() ? this.c.a(i + 1) : null;
                IBlockData iblockdata = this.b.getType(new BlockPosition(pathpoint.a, pathpoint.b, pathpoint.c));

                if (iblockdata.a(Blocks.CAULDRON)) {
                    this.c.a(i, pathpoint.a(pathpoint.a, pathpoint.b + 1, pathpoint.c));
                    if (pathpoint1 != null && pathpoint.b >= pathpoint1.b) {
                        this.c.a(i + 1, pathpoint.a(pathpoint1.a, pathpoint.b + 1, pathpoint1.c));
                    }
                }
            }

        }
    }

    protected abstract boolean a(Vec3D vec3d, Vec3D vec3d1, int i, int j, int k);

    public boolean a(BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();

        return this.b.getType(blockposition1).i(this.b, blockposition1);
    }

    public PathfinderAbstract q() {
        return this.o;
    }

    public void d(boolean flag) {
        this.o.c(flag);
    }

    public boolean r() {
        return this.o.e();
    }

    public void b(BlockPosition blockposition) {
        if (this.c != null && !this.c.c() && this.c.e() != 0) {
            PathPoint pathpoint = this.c.d();
            Vec3D vec3d = new Vec3D(((double) pathpoint.a + this.a.locX()) / 2.0D, ((double) pathpoint.b + this.a.locY()) / 2.0D, ((double) pathpoint.c + this.a.locZ()) / 2.0D);

            if (blockposition.a((IPosition) vec3d, (double) (this.c.e() - this.c.f()))) {
                this.j();
            }

        }
    }

    public boolean t() {
        return this.t;
    }
}
