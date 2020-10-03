package net.minecraft.server;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

public interface ICollisionAccess extends IBlockAccess {

    WorldBorder getWorldBorder();

    @Nullable
    IBlockAccess c(int i, int j);

    default boolean a(@Nullable Entity entity, VoxelShape voxelshape) {
        return true;
    }

    default boolean a(IBlockData iblockdata, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        VoxelShape voxelshape = iblockdata.b((IBlockAccess) this, blockposition, voxelshapecollision);

        return voxelshape.isEmpty() || this.a((Entity) null, voxelshape.a((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()));
    }

    default boolean j(Entity entity) {
        return this.a(entity, VoxelShapes.a(entity.getBoundingBox()));
    }

    default boolean b(AxisAlignedBB axisalignedbb) {
        return this.b((Entity) null, axisalignedbb, (entity) -> {
            return true;
        });
    }

    default boolean getCubes(Entity entity) {
        return this.b(entity, entity.getBoundingBox(), (entity1) -> {
            return true;
        });
    }

    default boolean getCubes(Entity entity, AxisAlignedBB axisalignedbb) {
        return this.b(entity, axisalignedbb, (entity1) -> {
            return true;
        });
    }

    default boolean b(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate) {
        return this.d(entity, axisalignedbb, predicate).allMatch(VoxelShape::isEmpty);
    }

    Stream<VoxelShape> c(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate);

    default Stream<VoxelShape> d(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate) {
        return Stream.concat(this.b(entity, axisalignedbb), this.c(entity, axisalignedbb, predicate));
    }

    default Stream<VoxelShape> b(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
        return StreamSupport.stream(new VoxelShapeSpliterator(this, entity, axisalignedbb), false);
    }

    default Stream<VoxelShape> b(@Nullable Entity entity, AxisAlignedBB axisalignedbb, BiPredicate<IBlockData, BlockPosition> bipredicate) {
        return StreamSupport.stream(new VoxelShapeSpliterator(this, entity, axisalignedbb, bipredicate), false);
    }
}
