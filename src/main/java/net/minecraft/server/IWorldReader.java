package net.minecraft.server;

import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface IWorldReader extends IBlockLightAccess, ICollisionAccess, BiomeManager.Provider {

    @Nullable
    IChunkAccess getChunkAt(int i, int j, ChunkStatus chunkstatus, boolean flag);

    @Deprecated
    boolean isChunkLoaded(int i, int j);

    int a(HeightMap.Type heightmap_type, int i, int j);

    int c();

    BiomeManager d();

    default BiomeBase getBiome(BlockPosition blockposition) {
        return this.d().a(blockposition);
    }

    default Stream<IBlockData> c(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.floor(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.floor(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.floor(axisalignedbb.maxZ);

        return this.isAreaLoaded(i, k, i1, j, l, j1) ? this.a(axisalignedbb) : Stream.empty();
    }

    @Override
    default BiomeBase getBiome(int i, int j, int k) {
        IChunkAccess ichunkaccess = this.getChunkAt(i >> 2, k >> 2, ChunkStatus.BIOMES, false);

        return ichunkaccess != null && ichunkaccess.getBiomeIndex() != null ? ichunkaccess.getBiomeIndex().getBiome(i, j, k) : this.a(i, j, k);
    }

    BiomeBase a(int i, int j, int k);

    boolean s_();

    @Deprecated
    int getSeaLevel();

    DimensionManager getDimensionManager();

    default BlockPosition getHighestBlockYAt(HeightMap.Type heightmap_type, BlockPosition blockposition) {
        return new BlockPosition(blockposition.getX(), this.a(heightmap_type, blockposition.getX(), blockposition.getZ()), blockposition.getZ());
    }

    default boolean isEmpty(BlockPosition blockposition) {
        return this.getType(blockposition).isAir();
    }

    default boolean x(BlockPosition blockposition) {
        if (blockposition.getY() >= this.getSeaLevel()) {
            return this.e(blockposition);
        } else {
            BlockPosition blockposition1 = new BlockPosition(blockposition.getX(), this.getSeaLevel(), blockposition.getZ());

            if (!this.e(blockposition1)) {
                return false;
            } else {
                for (blockposition1 = blockposition1.down(); blockposition1.getY() > blockposition.getY(); blockposition1 = blockposition1.down()) {
                    IBlockData iblockdata = this.getType(blockposition1);

                    if (iblockdata.b((IBlockAccess) this, blockposition1) > 0 && !iblockdata.getMaterial().isLiquid()) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    @Deprecated
    default float y(BlockPosition blockposition) {
        return this.getDimensionManager().a(this.getLightLevel(blockposition));
    }

    default int c(BlockPosition blockposition, EnumDirection enumdirection) {
        return this.getType(blockposition).c(this, blockposition, enumdirection);
    }

    default IChunkAccess z(BlockPosition blockposition) {
        return this.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    default IChunkAccess getChunkAt(int i, int j) {
        return this.getChunkAt(i, j, ChunkStatus.FULL, true);
    }

    default IChunkAccess getChunkAt(int i, int j, ChunkStatus chunkstatus) {
        return this.getChunkAt(i, j, chunkstatus, true);
    }

    @Nullable
    @Override
    default IBlockAccess c(int i, int j) {
        return this.getChunkAt(i, j, ChunkStatus.EMPTY, false);
    }

    default boolean A(BlockPosition blockposition) {
        return this.getFluid(blockposition).a((Tag) TagsFluid.WATER);
    }

    default boolean containsLiquid(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.f(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.f(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.f(axisalignedbb.maxZ);
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    IBlockData iblockdata = this.getType(blockposition_mutableblockposition.d(k1, l1, i2));

                    if (!iblockdata.getFluid().isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    default int getLightLevel(BlockPosition blockposition) {
        return this.c(blockposition, this.c());
    }

    default int c(BlockPosition blockposition, int i) {
        return blockposition.getX() >= -30000000 && blockposition.getZ() >= -30000000 && blockposition.getX() < 30000000 && blockposition.getZ() < 30000000 ? this.getLightLevel(blockposition, i) : 15;
    }

    @Deprecated
    default boolean isLoaded(BlockPosition blockposition) {
        return this.isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    @Deprecated
    default boolean areChunksLoadedBetween(BlockPosition blockposition, BlockPosition blockposition1) {
        return this.isAreaLoaded(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
    }

    @Deprecated
    default boolean isAreaLoaded(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    if (!this.isChunkLoaded(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
