package net.minecraft.server;

import java.util.Iterator;

public class Navigation extends NavigationAbstract {

    private boolean p;

    public Navigation(EntityInsentient entityinsentient, World world) {
        super(entityinsentient, world);
    }

    @Override
    protected Pathfinder a(int i) {
        this.o = new PathfinderNormal();
        this.o.a(true);
        return new Pathfinder(this.o, i);
    }

    @Override
    protected boolean a() {
        return this.a.isOnGround() || this.p() || this.a.isPassenger();
    }

    @Override
    protected Vec3D b() {
        return new Vec3D(this.a.locX(), (double) this.u(), this.a.locZ());
    }

    @Override
    public PathEntity a(BlockPosition blockposition, int i) {
        BlockPosition blockposition1;

        if (this.b.getType(blockposition).isAir()) {
            for (blockposition1 = blockposition.down(); blockposition1.getY() > 0 && this.b.getType(blockposition1).isAir(); blockposition1 = blockposition1.down()) {
                ;
            }

            if (blockposition1.getY() > 0) {
                return super.a(blockposition1.up(), i);
            }

            while (blockposition1.getY() < this.b.getBuildHeight() && this.b.getType(blockposition1).isAir()) {
                blockposition1 = blockposition1.up();
            }

            blockposition = blockposition1;
        }

        if (!this.b.getType(blockposition).getMaterial().isBuildable()) {
            return super.a(blockposition, i);
        } else {
            for (blockposition1 = blockposition.up(); blockposition1.getY() < this.b.getBuildHeight() && this.b.getType(blockposition1).getMaterial().isBuildable(); blockposition1 = blockposition1.up()) {
                ;
            }

            return super.a(blockposition1, i);
        }
    }

    @Override
    public PathEntity a(Entity entity, int i) {
        return this.a(entity.getChunkCoordinates(), i);
    }

    private int u() {
        if (this.a.isInWater() && this.r()) {
            int i = MathHelper.floor(this.a.locY());
            Block block = this.b.getType(new BlockPosition(this.a.locX(), (double) i, this.a.locZ())).getBlock();
            int j = 0;

            do {
                if (block != Blocks.WATER) {
                    return i;
                }

                ++i;
                block = this.b.getType(new BlockPosition(this.a.locX(), (double) i, this.a.locZ())).getBlock();
                ++j;
            } while (j <= 16);

            return MathHelper.floor(this.a.locY());
        } else {
            return MathHelper.floor(this.a.locY() + 0.5D);
        }
    }

    @Override
    protected void D_() {
        super.D_();
        if (this.p) {
            if (this.b.e(new BlockPosition(this.a.locX(), this.a.locY() + 0.5D, this.a.locZ()))) {
                return;
            }

            for (int i = 0; i < this.c.e(); ++i) {
                PathPoint pathpoint = this.c.a(i);

                if (this.b.e(new BlockPosition(pathpoint.a, pathpoint.b, pathpoint.c))) {
                    this.c.b(i);
                    return;
                }
            }
        }

    }

    @Override
    protected boolean a(Vec3D vec3d, Vec3D vec3d1, int i, int j, int k) {
        int l = MathHelper.floor(vec3d.x);
        int i1 = MathHelper.floor(vec3d.z);
        double d0 = vec3d1.x - vec3d.x;
        double d1 = vec3d1.z - vec3d.z;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 < 1.0E-8D) {
            return false;
        } else {
            double d3 = 1.0D / Math.sqrt(d2);

            d0 *= d3;
            d1 *= d3;
            i += 2;
            k += 2;
            if (!this.a(l, MathHelper.floor(vec3d.y), i1, i, j, k, vec3d, d0, d1)) {
                return false;
            } else {
                i -= 2;
                k -= 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double) l - vec3d.x;
                double d7 = (double) i1 - vec3d.z;

                if (d0 >= 0.0D) {
                    ++d6;
                }

                if (d1 >= 0.0D) {
                    ++d7;
                }

                d6 /= d0;
                d7 /= d1;
                int j1 = d0 < 0.0D ? -1 : 1;
                int k1 = d1 < 0.0D ? -1 : 1;
                int l1 = MathHelper.floor(vec3d1.x);
                int i2 = MathHelper.floor(vec3d1.z);
                int j2 = l1 - l;
                int k2 = i2 - i1;

                do {
                    if (j2 * j1 <= 0 && k2 * k1 <= 0) {
                        return true;
                    }

                    if (d6 < d7) {
                        d6 += d4;
                        l += j1;
                        j2 = l1 - l;
                    } else {
                        d7 += d5;
                        i1 += k1;
                        k2 = i2 - i1;
                    }
                } while (this.a(l, MathHelper.floor(vec3d.y), i1, i, j, k, vec3d, d0, d1));

                return false;
            }
        }
    }

    private boolean a(int i, int j, int k, int l, int i1, int j1, Vec3D vec3d, double d0, double d1) {
        int k1 = i - l / 2;
        int l1 = k - j1 / 2;

        if (!this.b(k1, j, l1, l, i1, j1, vec3d, d0, d1)) {
            return false;
        } else {
            for (int i2 = k1; i2 < k1 + l; ++i2) {
                for (int j2 = l1; j2 < l1 + j1; ++j2) {
                    double d2 = (double) i2 + 0.5D - vec3d.x;
                    double d3 = (double) j2 + 0.5D - vec3d.z;

                    if (d2 * d0 + d3 * d1 >= 0.0D) {
                        PathType pathtype = this.o.a(this.b, i2, j - 1, j2, this.a, l, i1, j1, true, true);

                        if (!this.a(pathtype)) {
                            return false;
                        }

                        pathtype = this.o.a(this.b, i2, j, j2, this.a, l, i1, j1, true, true);
                        float f = this.a.a(pathtype);

                        if (f < 0.0F || f >= 8.0F) {
                            return false;
                        }

                        if (pathtype == PathType.DAMAGE_FIRE || pathtype == PathType.DANGER_FIRE || pathtype == PathType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    protected boolean a(PathType pathtype) {
        return pathtype == PathType.WATER ? false : (pathtype == PathType.LAVA ? false : pathtype != PathType.OPEN);
    }

    private boolean b(int i, int j, int k, int l, int i1, int j1, Vec3D vec3d, double d0, double d1) {
        Iterator iterator = BlockPosition.a(new BlockPosition(i, j, k), new BlockPosition(i + l - 1, j + i1 - 1, k + j1 - 1)).iterator();

        BlockPosition blockposition;
        double d2;
        double d3;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            blockposition = (BlockPosition) iterator.next();
            d2 = (double) blockposition.getX() + 0.5D - vec3d.x;
            d3 = (double) blockposition.getZ() + 0.5D - vec3d.z;
        } while (d2 * d0 + d3 * d1 < 0.0D || this.b.getType(blockposition).a((IBlockAccess) this.b, blockposition, PathMode.LAND));

        return false;
    }

    public void a(boolean flag) {
        this.o.b(flag);
    }

    public boolean f() {
        return this.o.c();
    }

    public void c(boolean flag) {
        this.p = flag;
    }
}
