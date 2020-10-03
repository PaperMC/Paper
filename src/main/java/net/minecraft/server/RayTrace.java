package net.minecraft.server;

import java.util.function.Predicate;

public class RayTrace {

    private final Vec3D a;
    private final Vec3D b;
    private final RayTrace.BlockCollisionOption c;
    private final RayTrace.FluidCollisionOption d;
    private final VoxelShapeCollision e;

    public RayTrace(Vec3D vec3d, Vec3D vec3d1, RayTrace.BlockCollisionOption raytrace_blockcollisionoption, RayTrace.FluidCollisionOption raytrace_fluidcollisionoption, Entity entity) {
        this.a = vec3d;
        this.b = vec3d1;
        this.c = raytrace_blockcollisionoption;
        this.d = raytrace_fluidcollisionoption;
        this.e = (entity == null) ? VoxelShapeCollision.a() : VoxelShapeCollision.a(entity); // CraftBukkit
    }

    public Vec3D a() {
        return this.b;
    }

    public Vec3D b() {
        return this.a;
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return this.c.get(iblockdata, iblockaccess, blockposition, this.e);
    }

    public VoxelShape a(Fluid fluid, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return this.d.a(fluid) ? fluid.d(iblockaccess, blockposition) : VoxelShapes.a();
    }

    public static enum FluidCollisionOption {

        NONE((fluid) -> {
            return false;
        }), SOURCE_ONLY(Fluid::isSource), ANY((fluid) -> {
            return !fluid.isEmpty();
        });

        private final Predicate<Fluid> predicate;

        private FluidCollisionOption(Predicate<Fluid> predicate) { // CraftBukkit - decompile error
            this.predicate = predicate;
        }

        public boolean a(Fluid fluid) {
            return this.predicate.test(fluid);
        }
    }

    public interface c {

        VoxelShape get(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision);
    }

    public static enum BlockCollisionOption implements RayTrace.c {

        COLLIDER(BlockBase.BlockData::b), OUTLINE(BlockBase.BlockData::a), VISUAL(BlockBase.BlockData::c);

        private final RayTrace.c d;

        private BlockCollisionOption(RayTrace.c raytrace_c) {
            this.d = raytrace_c;
        }

        @Override
        public VoxelShape get(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
            return this.d.get(iblockdata, iblockaccess, blockposition, voxelshapecollision);
        }
    }
}
