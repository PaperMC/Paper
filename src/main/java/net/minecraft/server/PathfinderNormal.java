package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.EnumSet;
import java.util.Iterator;
import javax.annotation.Nullable;

public class PathfinderNormal extends PathfinderAbstract {

    protected float j;
    private final Long2ObjectMap<PathType> k = new Long2ObjectOpenHashMap();
    private final Object2BooleanMap<AxisAlignedBB> l = new Object2BooleanOpenHashMap();

    public PathfinderNormal() {}

    @Override
    public void a(ChunkCache chunkcache, EntityInsentient entityinsentient) {
        super.a(chunkcache, entityinsentient);
        this.j = entityinsentient.a(PathType.WATER);
    }

    @Override
    public void a() {
        this.b.a(PathType.WATER, this.j);
        this.k.clear();
        this.l.clear();
        super.a();
    }

    @Override
    public PathPoint b() {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        int i = MathHelper.floor(this.b.locY());
        IBlockData iblockdata = this.a.getType(blockposition_mutableblockposition.c(this.b.locX(), (double) i, this.b.locZ()));
        BlockPosition blockposition;

        if (this.b.a(iblockdata.getFluid().getType())) {
            while (this.b.a(iblockdata.getFluid().getType())) {
                ++i;
                iblockdata = this.a.getType(blockposition_mutableblockposition.c(this.b.locX(), (double) i, this.b.locZ()));
            }

            --i;
        } else if (this.e() && this.b.isInWater()) {
            while (iblockdata.getBlock() == Blocks.WATER || iblockdata.getFluid() == FluidTypes.WATER.a(false)) {
                ++i;
                iblockdata = this.a.getType(blockposition_mutableblockposition.c(this.b.locX(), (double) i, this.b.locZ()));
            }

            --i;
        } else if (this.b.isOnGround()) {
            i = MathHelper.floor(this.b.locY() + 0.5D);
        } else {
            for (blockposition = this.b.getChunkCoordinates(); (this.a.getType(blockposition).isAir() || this.a.getType(blockposition).a((IBlockAccess) this.a, blockposition, PathMode.LAND)) && blockposition.getY() > 0; blockposition = blockposition.down()) {
                ;
            }

            i = blockposition.up().getY();
        }

        blockposition = this.b.getChunkCoordinates();
        PathType pathtype = this.a(this.b, blockposition.getX(), i, blockposition.getZ());

        if (this.b.a(pathtype) < 0.0F) {
            AxisAlignedBB axisalignedbb = this.b.getBoundingBox();

            if (this.b(blockposition_mutableblockposition.c(axisalignedbb.minX, (double) i, axisalignedbb.minZ)) || this.b(blockposition_mutableblockposition.c(axisalignedbb.minX, (double) i, axisalignedbb.maxZ)) || this.b(blockposition_mutableblockposition.c(axisalignedbb.maxX, (double) i, axisalignedbb.minZ)) || this.b(blockposition_mutableblockposition.c(axisalignedbb.maxX, (double) i, axisalignedbb.maxZ))) {
                PathPoint pathpoint = this.a((BlockPosition) blockposition_mutableblockposition);

                pathpoint.l = this.a(this.b, pathpoint.a());
                pathpoint.k = this.b.a(pathpoint.l);
                return pathpoint;
            }
        }

        PathPoint pathpoint1 = this.a(blockposition.getX(), i, blockposition.getZ());

        pathpoint1.l = this.a(this.b, pathpoint1.a());
        pathpoint1.k = this.b.a(pathpoint1.l);
        return pathpoint1;
    }

    private boolean b(BlockPosition blockposition) {
        PathType pathtype = this.a(this.b, blockposition);

        return this.b.a(pathtype) >= 0.0F;
    }

    @Override
    public PathDestination a(double d0, double d1, double d2) {
        return new PathDestination(this.a(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2)));
    }

    @Override
    public int a(PathPoint[] apathpoint, PathPoint pathpoint) {
        int i = 0;
        int j = 0;
        PathType pathtype = this.a(this.b, pathpoint.a, pathpoint.b + 1, pathpoint.c);
        PathType pathtype1 = this.a(this.b, pathpoint.a, pathpoint.b, pathpoint.c);

        if (this.b.a(pathtype) >= 0.0F && pathtype1 != PathType.STICKY_HONEY) {
            j = MathHelper.d(Math.max(1.0F, this.b.G));
        }

        double d0 = a((IBlockAccess) this.a, new BlockPosition(pathpoint.a, pathpoint.b, pathpoint.c));
        PathPoint pathpoint1 = this.a(pathpoint.a, pathpoint.b, pathpoint.c + 1, j, d0, EnumDirection.SOUTH, pathtype1);

        if (this.a(pathpoint1, pathpoint)) {
            apathpoint[i++] = pathpoint1;
        }

        PathPoint pathpoint2 = this.a(pathpoint.a - 1, pathpoint.b, pathpoint.c, j, d0, EnumDirection.WEST, pathtype1);

        if (this.a(pathpoint2, pathpoint)) {
            apathpoint[i++] = pathpoint2;
        }

        PathPoint pathpoint3 = this.a(pathpoint.a + 1, pathpoint.b, pathpoint.c, j, d0, EnumDirection.EAST, pathtype1);

        if (this.a(pathpoint3, pathpoint)) {
            apathpoint[i++] = pathpoint3;
        }

        PathPoint pathpoint4 = this.a(pathpoint.a, pathpoint.b, pathpoint.c - 1, j, d0, EnumDirection.NORTH, pathtype1);

        if (this.a(pathpoint4, pathpoint)) {
            apathpoint[i++] = pathpoint4;
        }

        PathPoint pathpoint5 = this.a(pathpoint.a - 1, pathpoint.b, pathpoint.c - 1, j, d0, EnumDirection.NORTH, pathtype1);

        if (this.a(pathpoint, pathpoint2, pathpoint4, pathpoint5)) {
            apathpoint[i++] = pathpoint5;
        }

        PathPoint pathpoint6 = this.a(pathpoint.a + 1, pathpoint.b, pathpoint.c - 1, j, d0, EnumDirection.NORTH, pathtype1);

        if (this.a(pathpoint, pathpoint3, pathpoint4, pathpoint6)) {
            apathpoint[i++] = pathpoint6;
        }

        PathPoint pathpoint7 = this.a(pathpoint.a - 1, pathpoint.b, pathpoint.c + 1, j, d0, EnumDirection.SOUTH, pathtype1);

        if (this.a(pathpoint, pathpoint2, pathpoint1, pathpoint7)) {
            apathpoint[i++] = pathpoint7;
        }

        PathPoint pathpoint8 = this.a(pathpoint.a + 1, pathpoint.b, pathpoint.c + 1, j, d0, EnumDirection.SOUTH, pathtype1);

        if (this.a(pathpoint, pathpoint3, pathpoint1, pathpoint8)) {
            apathpoint[i++] = pathpoint8;
        }

        return i;
    }

    private boolean a(PathPoint pathpoint, PathPoint pathpoint1) {
        return pathpoint != null && !pathpoint.i && (pathpoint.k >= 0.0F || pathpoint1.k < 0.0F);
    }

    private boolean a(PathPoint pathpoint, @Nullable PathPoint pathpoint1, @Nullable PathPoint pathpoint2, @Nullable PathPoint pathpoint3) {
        if (pathpoint3 != null && pathpoint2 != null && pathpoint1 != null) {
            if (pathpoint3.i) {
                return false;
            } else if (pathpoint2.b <= pathpoint.b && pathpoint1.b <= pathpoint.b) {
                if (pathpoint1.l != PathType.WALKABLE_DOOR && pathpoint2.l != PathType.WALKABLE_DOOR && pathpoint3.l != PathType.WALKABLE_DOOR) {
                    boolean flag = pathpoint2.l == PathType.FENCE && pathpoint1.l == PathType.FENCE && (double) this.b.getWidth() < 0.5D;

                    return pathpoint3.k >= 0.0F && (pathpoint2.b < pathpoint.b || pathpoint2.k >= 0.0F || flag) && (pathpoint1.b < pathpoint.b || pathpoint1.k >= 0.0F || flag);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean a(PathPoint pathpoint) {
        Vec3D vec3d = new Vec3D((double) pathpoint.a - this.b.locX(), (double) pathpoint.b - this.b.locY(), (double) pathpoint.c - this.b.locZ());
        AxisAlignedBB axisalignedbb = this.b.getBoundingBox();
        int i = MathHelper.f(vec3d.f() / axisalignedbb.a());

        vec3d = vec3d.a((double) (1.0F / (float) i));

        for (int j = 1; j <= i; ++j) {
            axisalignedbb = axisalignedbb.c(vec3d);
            if (this.a(axisalignedbb)) {
                return false;
            }
        }

        return true;
    }

    public static double a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();
        VoxelShape voxelshape = iblockaccess.getType(blockposition1).getCollisionShape(iblockaccess, blockposition1);

        return (double) blockposition1.getY() + (voxelshape.isEmpty() ? 0.0D : voxelshape.c(EnumDirection.EnumAxis.Y));
    }

    @Nullable
    private PathPoint a(int i, int j, int k, int l, double d0, EnumDirection enumdirection, PathType pathtype) {
        PathPoint pathpoint = null;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        double d1 = a((IBlockAccess) this.a, (BlockPosition) blockposition_mutableblockposition.d(i, j, k));

        if (d1 - d0 > 1.125D) {
            return null;
        } else {
            PathType pathtype1 = this.a(this.b, i, j, k);
            float f = this.b.a(pathtype1);
            double d2 = (double) this.b.getWidth() / 2.0D;

            if (f >= 0.0F) {
                pathpoint = this.a(i, j, k);
                pathpoint.l = pathtype1;
                pathpoint.k = Math.max(pathpoint.k, f);
            }

            if (pathtype == PathType.FENCE && pathpoint != null && pathpoint.k >= 0.0F && !this.a(pathpoint)) {
                pathpoint = null;
            }

            if (pathtype1 == PathType.WALKABLE) {
                return pathpoint;
            } else {
                if ((pathpoint == null || pathpoint.k < 0.0F) && l > 0 && pathtype1 != PathType.FENCE && pathtype1 != PathType.UNPASSABLE_RAIL && pathtype1 != PathType.TRAPDOOR) {
                    pathpoint = this.a(i, j + 1, k, l - 1, d0, enumdirection, pathtype);
                    if (pathpoint != null && (pathpoint.l == PathType.OPEN || pathpoint.l == PathType.WALKABLE) && this.b.getWidth() < 1.0F) {
                        double d3 = (double) (i - enumdirection.getAdjacentX()) + 0.5D;
                        double d4 = (double) (k - enumdirection.getAdjacentZ()) + 0.5D;
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(d3 - d2, a((IBlockAccess) this.a, (BlockPosition) blockposition_mutableblockposition.c(d3, (double) (j + 1), d4)) + 0.001D, d4 - d2, d3 + d2, (double) this.b.getHeight() + a((IBlockAccess) this.a, (BlockPosition) blockposition_mutableblockposition.c((double) pathpoint.a, (double) pathpoint.b, (double) pathpoint.c)) - 0.002D, d4 + d2);

                        if (this.a(axisalignedbb)) {
                            pathpoint = null;
                        }
                    }
                }

                if (pathtype1 == PathType.WATER && !this.e()) {
                    if (this.a(this.b, i, j - 1, k) != PathType.WATER) {
                        return pathpoint;
                    }

                    while (j > 0) {
                        --j;
                        pathtype1 = this.a(this.b, i, j, k);
                        if (pathtype1 != PathType.WATER) {
                            return pathpoint;
                        }

                        pathpoint = this.a(i, j, k);
                        pathpoint.l = pathtype1;
                        pathpoint.k = Math.max(pathpoint.k, this.b.a(pathtype1));
                    }
                }

                if (pathtype1 == PathType.OPEN) {
                    int i1 = 0;
                    int j1 = j;

                    while (pathtype1 == PathType.OPEN) {
                        --j;
                        PathPoint pathpoint1;

                        if (j < 0) {
                            pathpoint1 = this.a(i, j1, k);
                            pathpoint1.l = PathType.BLOCKED;
                            pathpoint1.k = -1.0F;
                            return pathpoint1;
                        }

                        if (i1++ >= this.b.bO()) {
                            pathpoint1 = this.a(i, j, k);
                            pathpoint1.l = PathType.BLOCKED;
                            pathpoint1.k = -1.0F;
                            return pathpoint1;
                        }

                        pathtype1 = this.a(this.b, i, j, k);
                        f = this.b.a(pathtype1);
                        if (pathtype1 != PathType.OPEN && f >= 0.0F) {
                            pathpoint = this.a(i, j, k);
                            pathpoint.l = pathtype1;
                            pathpoint.k = Math.max(pathpoint.k, f);
                            break;
                        }

                        if (f < 0.0F) {
                            pathpoint1 = this.a(i, j, k);
                            pathpoint1.l = PathType.BLOCKED;
                            pathpoint1.k = -1.0F;
                            return pathpoint1;
                        }
                    }
                }

                if (pathtype1 == PathType.FENCE) {
                    pathpoint = this.a(i, j, k);
                    pathpoint.i = true;
                    pathpoint.l = pathtype1;
                    pathpoint.k = pathtype1.a();
                }

                return pathpoint;
            }
        }
    }

    private boolean a(AxisAlignedBB axisalignedbb) {
        return (Boolean) this.l.computeIfAbsent(axisalignedbb, (axisalignedbb1) -> {
            return !this.a.getCubes(this.b, axisalignedbb);
        });
    }

    @Override
    public PathType a(IBlockAccess iblockaccess, int i, int j, int k, EntityInsentient entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1) {
        EnumSet<PathType> enumset = EnumSet.noneOf(PathType.class);
        PathType pathtype = PathType.BLOCKED;
        BlockPosition blockposition = entityinsentient.getChunkCoordinates();

        pathtype = this.a(iblockaccess, i, j, k, l, i1, j1, flag, flag1, enumset, pathtype, blockposition);
        if (enumset.contains(PathType.FENCE)) {
            return PathType.FENCE;
        } else if (enumset.contains(PathType.UNPASSABLE_RAIL)) {
            return PathType.UNPASSABLE_RAIL;
        } else {
            PathType pathtype1 = PathType.BLOCKED;
            Iterator iterator = enumset.iterator();

            while (iterator.hasNext()) {
                PathType pathtype2 = (PathType) iterator.next();

                if (entityinsentient.a(pathtype2) < 0.0F) {
                    return pathtype2;
                }

                if (entityinsentient.a(pathtype2) >= entityinsentient.a(pathtype1)) {
                    pathtype1 = pathtype2;
                }
            }

            if (pathtype == PathType.OPEN && entityinsentient.a(pathtype1) == 0.0F && l <= 1) {
                return PathType.OPEN;
            } else {
                return pathtype1;
            }
        }
    }

    public PathType a(IBlockAccess iblockaccess, int i, int j, int k, int l, int i1, int j1, boolean flag, boolean flag1, EnumSet<PathType> enumset, PathType pathtype, BlockPosition blockposition) {
        for (int k1 = 0; k1 < l; ++k1) {
            for (int l1 = 0; l1 < i1; ++l1) {
                for (int i2 = 0; i2 < j1; ++i2) {
                    int j2 = k1 + i;
                    int k2 = l1 + j;
                    int l2 = i2 + k;
                    PathType pathtype1 = this.a(iblockaccess, j2, k2, l2);

                    pathtype1 = this.a(iblockaccess, flag, flag1, blockposition, pathtype1);
                    if (k1 == 0 && l1 == 0 && i2 == 0) {
                        pathtype = pathtype1;
                    }

                    enumset.add(pathtype1);
                }
            }
        }

        return pathtype;
    }

    protected PathType a(IBlockAccess iblockaccess, boolean flag, boolean flag1, BlockPosition blockposition, PathType pathtype) {
        if (pathtype == PathType.DOOR_WOOD_CLOSED && flag && flag1) {
            pathtype = PathType.WALKABLE_DOOR;
        }

        if (pathtype == PathType.DOOR_OPEN && !flag1) {
            pathtype = PathType.BLOCKED;
        }

        if (pathtype == PathType.RAIL && !(iblockaccess.getType(blockposition).getBlock() instanceof BlockMinecartTrackAbstract) && !(iblockaccess.getType(blockposition.down()).getBlock() instanceof BlockMinecartTrackAbstract)) {
            pathtype = PathType.UNPASSABLE_RAIL;
        }

        if (pathtype == PathType.LEAVES) {
            pathtype = PathType.BLOCKED;
        }

        return pathtype;
    }

    private PathType a(EntityInsentient entityinsentient, BlockPosition blockposition) {
        return this.a(entityinsentient, blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    private PathType a(EntityInsentient entityinsentient, int i, int j, int k) {
        return (PathType) this.k.computeIfAbsent(BlockPosition.a(i, j, k), (l) -> {
            return this.a(this.a, i, j, k, entityinsentient, this.d, this.e, this.f, this.d(), this.c());
        });
    }

    @Override
    public PathType a(IBlockAccess iblockaccess, int i, int j, int k) {
        return a(iblockaccess, new BlockPosition.MutableBlockPosition(i, j, k));
    }

    public static PathType a(IBlockAccess iblockaccess, BlockPosition.MutableBlockPosition blockposition_mutableblockposition) {
        int i = blockposition_mutableblockposition.getX();
        int j = blockposition_mutableblockposition.getY();
        int k = blockposition_mutableblockposition.getZ();
        PathType pathtype = b(iblockaccess, blockposition_mutableblockposition);

        if (pathtype == PathType.OPEN && j >= 1) {
            PathType pathtype1 = b(iblockaccess, blockposition_mutableblockposition.d(i, j - 1, k));

            pathtype = pathtype1 != PathType.WALKABLE && pathtype1 != PathType.OPEN && pathtype1 != PathType.WATER && pathtype1 != PathType.LAVA ? PathType.WALKABLE : PathType.OPEN;
            if (pathtype1 == PathType.DAMAGE_FIRE) {
                pathtype = PathType.DAMAGE_FIRE;
            }

            if (pathtype1 == PathType.DAMAGE_CACTUS) {
                pathtype = PathType.DAMAGE_CACTUS;
            }

            if (pathtype1 == PathType.DAMAGE_OTHER) {
                pathtype = PathType.DAMAGE_OTHER;
            }

            if (pathtype1 == PathType.STICKY_HONEY) {
                pathtype = PathType.STICKY_HONEY;
            }
        }

        if (pathtype == PathType.WALKABLE) {
            pathtype = a(iblockaccess, blockposition_mutableblockposition.d(i, j, k), pathtype);
        }

        return pathtype;
    }

    public static PathType a(IBlockAccess iblockaccess, BlockPosition.MutableBlockPosition blockposition_mutableblockposition, PathType pathtype) {
        int i = blockposition_mutableblockposition.getX();
        int j = blockposition_mutableblockposition.getY();
        int k = blockposition_mutableblockposition.getZ();

        for (int l = -1; l <= 1; ++l) {
            for (int i1 = -1; i1 <= 1; ++i1) {
                for (int j1 = -1; j1 <= 1; ++j1) {
                    if (l != 0 || j1 != 0) {
                        blockposition_mutableblockposition.d(i + l, j + i1, k + j1);
                        IBlockData iblockdata = iblockaccess.getType(blockposition_mutableblockposition);

                        if (iblockdata.a(Blocks.CACTUS)) {
                            return PathType.DANGER_CACTUS;
                        }

                        if (iblockdata.a(Blocks.SWEET_BERRY_BUSH)) {
                            return PathType.DANGER_OTHER;
                        }

                        if (a(iblockdata)) {
                            return PathType.DANGER_FIRE;
                        }

                        if (iblockaccess.getFluid(blockposition_mutableblockposition).a((Tag) TagsFluid.WATER)) {
                            return PathType.WATER_BORDER;
                        }
                    }
                }
            }
        }

        return pathtype;
    }

    protected static PathType b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        IBlockData iblockdata = iblockaccess.getType(blockposition);
        Block block = iblockdata.getBlock();
        Material material = iblockdata.getMaterial();

        if (iblockdata.isAir()) {
            return PathType.OPEN;
        } else if (!iblockdata.a((Tag) TagsBlock.TRAPDOORS) && !iblockdata.a(Blocks.LILY_PAD)) {
            if (iblockdata.a(Blocks.CACTUS)) {
                return PathType.DAMAGE_CACTUS;
            } else if (iblockdata.a(Blocks.SWEET_BERRY_BUSH)) {
                return PathType.DAMAGE_OTHER;
            } else if (iblockdata.a(Blocks.HONEY_BLOCK)) {
                return PathType.STICKY_HONEY;
            } else if (iblockdata.a(Blocks.COCOA)) {
                return PathType.COCOA;
            } else {
                Fluid fluid = iblockaccess.getFluid(blockposition);

                return fluid.a((Tag) TagsFluid.WATER) ? PathType.WATER : (fluid.a((Tag) TagsFluid.LAVA) ? PathType.LAVA : (a(iblockdata) ? PathType.DAMAGE_FIRE : (BlockDoor.l(iblockdata) && !(Boolean) iblockdata.get(BlockDoor.OPEN) ? PathType.DOOR_WOOD_CLOSED : (block instanceof BlockDoor && material == Material.ORE && !(Boolean) iblockdata.get(BlockDoor.OPEN) ? PathType.DOOR_IRON_CLOSED : (block instanceof BlockDoor && (Boolean) iblockdata.get(BlockDoor.OPEN) ? PathType.DOOR_OPEN : (block instanceof BlockMinecartTrackAbstract ? PathType.RAIL : (block instanceof BlockLeaves ? PathType.LEAVES : (!block.a((Tag) TagsBlock.FENCES) && !block.a((Tag) TagsBlock.WALLS) && (!(block instanceof BlockFenceGate) || (Boolean) iblockdata.get(BlockFenceGate.OPEN)) ? (!iblockdata.a(iblockaccess, blockposition, PathMode.LAND) ? PathType.BLOCKED : PathType.OPEN) : PathType.FENCE))))))));
            }
        } else {
            return PathType.TRAPDOOR;
        }
    }

    private static boolean a(IBlockData iblockdata) {
        return iblockdata.a((Tag) TagsBlock.FIRE) || iblockdata.a(Blocks.LAVA) || iblockdata.a(Blocks.MAGMA_BLOCK) || BlockCampfire.g(iblockdata);
    }
}
