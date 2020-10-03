package net.minecraft.server;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface IBlockAccess {

    @Nullable
    TileEntity getTileEntity(BlockPosition blockposition);

    IBlockData getType(BlockPosition blockposition);

    Fluid getFluid(BlockPosition blockposition);

    default int g(BlockPosition blockposition) {
        return this.getType(blockposition).f();
    }

    default int J() {
        return 15;
    }

    default int getBuildHeight() {
        return 256;
    }

    default Stream<IBlockData> a(AxisAlignedBB axisalignedbb) {
        return BlockPosition.a(axisalignedbb).map(this::getType);
    }

    // CraftBukkit start - moved block handling into separate method for use by Block#rayTrace
    default MovingObjectPositionBlock rayTraceBlock(RayTrace raytrace1, BlockPosition blockposition) {
            IBlockData iblockdata = this.getType(blockposition);
            Fluid fluid = this.getFluid(blockposition);
            Vec3D vec3d = raytrace1.b();
            Vec3D vec3d1 = raytrace1.a();
            VoxelShape voxelshape = raytrace1.a(iblockdata, this, blockposition);
            MovingObjectPositionBlock movingobjectpositionblock = this.rayTrace(vec3d, vec3d1, blockposition, voxelshape, iblockdata);
            VoxelShape voxelshape1 = raytrace1.a(fluid, this, blockposition);
            MovingObjectPositionBlock movingobjectpositionblock1 = voxelshape1.rayTrace(vec3d, vec3d1, blockposition);
            double d0 = movingobjectpositionblock == null ? Double.MAX_VALUE : raytrace1.b().distanceSquared(movingobjectpositionblock.getPos());
            double d1 = movingobjectpositionblock1 == null ? Double.MAX_VALUE : raytrace1.b().distanceSquared(movingobjectpositionblock1.getPos());

            return d0 <= d1 ? movingobjectpositionblock : movingobjectpositionblock1;
    }
    // CraftBukkit end

    default MovingObjectPositionBlock rayTrace(RayTrace raytrace) {
        return (MovingObjectPositionBlock) a(raytrace, (raytrace1, blockposition) -> {
            return this.rayTraceBlock(raytrace1, blockposition); // CraftBukkit - moved into separate method
        }, (raytrace1) -> {
            Vec3D vec3d = raytrace1.b().d(raytrace1.a());

            return MovingObjectPositionBlock.a(raytrace1.a(), EnumDirection.a(vec3d.x, vec3d.y, vec3d.z), new BlockPosition(raytrace1.a()));
        });
    }

    @Nullable
    default MovingObjectPositionBlock rayTrace(Vec3D vec3d, Vec3D vec3d1, BlockPosition blockposition, VoxelShape voxelshape, IBlockData iblockdata) {
        MovingObjectPositionBlock movingobjectpositionblock = voxelshape.rayTrace(vec3d, vec3d1, blockposition);

        if (movingobjectpositionblock != null) {
            MovingObjectPositionBlock movingobjectpositionblock1 = iblockdata.m(this, blockposition).rayTrace(vec3d, vec3d1, blockposition);

            if (movingobjectpositionblock1 != null && movingobjectpositionblock1.getPos().d(vec3d).g() < movingobjectpositionblock.getPos().d(vec3d).g()) {
                return movingobjectpositionblock.a(movingobjectpositionblock1.getDirection());
            }
        }

        return movingobjectpositionblock;
    }

    default double a(VoxelShape voxelshape, Supplier<VoxelShape> supplier) {
        if (!voxelshape.isEmpty()) {
            return voxelshape.c(EnumDirection.EnumAxis.Y);
        } else {
            double d0 = ((VoxelShape) supplier.get()).c(EnumDirection.EnumAxis.Y);

            return d0 >= 1.0D ? d0 - 1.0D : Double.NEGATIVE_INFINITY;
        }
    }

    default double h(BlockPosition blockposition) {
        return this.a(this.getType(blockposition).getCollisionShape(this, blockposition), () -> {
            BlockPosition blockposition1 = blockposition.down();

            return this.getType(blockposition1).getCollisionShape(this, blockposition1);
        });
    }

    static <T> T a(RayTrace raytrace, BiFunction<RayTrace, BlockPosition, T> bifunction, Function<RayTrace, T> function) {
        Vec3D vec3d = raytrace.b();
        Vec3D vec3d1 = raytrace.a();

        if (vec3d.equals(vec3d1)) {
            return function.apply(raytrace);
        } else {
            double d0 = MathHelper.d(-1.0E-7D, vec3d1.x, vec3d.x);
            double d1 = MathHelper.d(-1.0E-7D, vec3d1.y, vec3d.y);
            double d2 = MathHelper.d(-1.0E-7D, vec3d1.z, vec3d.z);
            double d3 = MathHelper.d(-1.0E-7D, vec3d.x, vec3d1.x);
            double d4 = MathHelper.d(-1.0E-7D, vec3d.y, vec3d1.y);
            double d5 = MathHelper.d(-1.0E-7D, vec3d.z, vec3d1.z);
            int i = MathHelper.floor(d3);
            int j = MathHelper.floor(d4);
            int k = MathHelper.floor(d5);
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition(i, j, k);
            T t0 = bifunction.apply(raytrace, blockposition_mutableblockposition);

            if (t0 != null) {
                return t0;
            } else {
                double d6 = d0 - d3;
                double d7 = d1 - d4;
                double d8 = d2 - d5;
                int l = MathHelper.k(d6);
                int i1 = MathHelper.k(d7);
                int j1 = MathHelper.k(d8);
                double d9 = l == 0 ? Double.MAX_VALUE : (double) l / d6;
                double d10 = i1 == 0 ? Double.MAX_VALUE : (double) i1 / d7;
                double d11 = j1 == 0 ? Double.MAX_VALUE : (double) j1 / d8;
                double d12 = d9 * (l > 0 ? 1.0D - MathHelper.h(d3) : MathHelper.h(d3));
                double d13 = d10 * (i1 > 0 ? 1.0D - MathHelper.h(d4) : MathHelper.h(d4));
                double d14 = d11 * (j1 > 0 ? 1.0D - MathHelper.h(d5) : MathHelper.h(d5));

                T object; // CraftBukkit - decompile error

                do {
                    if (d12 > 1.0D && d13 > 1.0D && d14 > 1.0D) {
                        return function.apply(raytrace);
                    }

                    if (d12 < d13) {
                        if (d12 < d14) {
                            i += l;
                            d12 += d9;
                        } else {
                            k += j1;
                            d14 += d11;
                        }
                    } else if (d13 < d14) {
                        j += i1;
                        d13 += d10;
                    } else {
                        k += j1;
                        d14 += d11;
                    }

                    object = bifunction.apply(raytrace, blockposition_mutableblockposition.d(i, j, k));
                } while (object == null);

                return object;
            }
        }
    }
}
