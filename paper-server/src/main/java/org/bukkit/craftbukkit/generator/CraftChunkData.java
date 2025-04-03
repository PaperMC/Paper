package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.HeightMap;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftHeightMap;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

/**
 * Data to be used for the block types and data in a newly generated chunk.
 */
public final class CraftChunkData implements ChunkGenerator.ChunkData {
    private final int maxHeight;
    private final int minHeight;
    private final WeakReference<ChunkAccess> weakChunk;

    public CraftChunkData(World world, ChunkAccess chunkAccess) {
        this(world.getMaxHeight(), world.getMinHeight(), chunkAccess);
    }

    CraftChunkData(int maxHeight, int minHeight, ChunkAccess chunkAccess) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.weakChunk = new WeakReference<>(chunkAccess);
    }

    public ChunkAccess getHandle() {
        ChunkAccess access = this.weakChunk.get();

        Preconditions.checkState(access != null, "IChunkAccess no longer present, are you using it in a different tick?");

        return access;
    }

    public void breakLink() {
        this.weakChunk.clear();
    }

    @Override
    public int getMaxHeight() {
        return this.maxHeight;
    }

    @Override
    public int getMinHeight() {
        return this.minHeight;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return CraftBiome.minecraftHolderToBukkit(this.getHandle().getNoiseBiome(x >> 2, y >> 2, z >> 2));
    }

    @Override
    public void setBlock(int x, int y, int z, Material material) {
        this.setBlock(x, y, z, material.createBlockData());
    }

    @Override
    public void setBlock(int x, int y, int z, MaterialData material) {
        this.setBlock(x, y, z, CraftMagicNumbers.getBlock(material));
    }

    @Override
    public void setBlock(int x, int y, int z, BlockData blockData) {
        this.setBlock(x, y, z, ((CraftBlockData) blockData).getState());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.createBlockData());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, CraftMagicNumbers.getBlock(material));
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockData blockData) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, ((CraftBlockData) blockData).getState());
    }

    @Override
    public Material getType(int x, int y, int z) {
        return this.getTypeId(x, y, z).getBukkitMaterial(); // Paper - optimise getType calls
    }

    @Override
    public MaterialData getTypeAndData(int x, int y, int z) {
        return CraftMagicNumbers.getMaterial(this.getTypeId(x, y, z));
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        return CraftBlockData.fromData(this.getTypeId(x, y, z));
    }

    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState type) {
        // Clamp to sane values.
        if (xMin > 0xf || yMin >= this.maxHeight || zMin > 0xf) {
            return;
        }
        if (xMin < 0) {
            xMin = 0;
        }
        if (yMin < this.minHeight) {
            yMin = this.minHeight;
        }
        if (zMin < 0) {
            zMin = 0;
        }
        if (xMax > 0x10) {
            xMax = 0x10;
        }
        if (yMax > this.maxHeight) {
            yMax = this.maxHeight;
        }
        if (zMax > 0x10) {
            zMax = 0x10;
        }
        if (xMin >= xMax || yMin >= yMax || zMin >= zMax) {
            return;
        }
        for (int y = yMin; y < yMax; y++) {
            for (int x = xMin; x < xMax; x++) {
                for (int z = zMin; z < zMax; z++) {
                    this.setBlock(x, y, z, type);
                }
            }
        }
    }

    public BlockState getTypeId(int x, int y, int z) {
        if (x != (x & 0xf) || y < this.minHeight || y >= this.maxHeight || z != (z & 0xf)) {
            return Blocks.AIR.defaultBlockState();
        }

        ChunkAccess access = this.getHandle();
        return access.getBlockState(new BlockPos(access.getPos().getMinBlockX() + x, y, access.getPos().getMinBlockZ() + z));
    }

    @Override
    public byte getData(int x, int y, int z) {
        return CraftMagicNumbers.toLegacyData(this.getTypeId(x, y, z));
    }

    private void setBlock(int x, int y, int z, BlockState type) {
        if (x != (x & 0xf) || y < this.minHeight || y >= this.maxHeight || z != (z & 0xf)) {
            return;
        }

        ChunkAccess access = this.getHandle();
        BlockPos pos = new BlockPos(access.getPos().getMinBlockX() + x, y, access.getPos().getMinBlockZ() + z);
        BlockState oldBlockState = access.setBlockState(pos, type);

        if (type.hasBlockEntity()) {
            BlockEntity blockEntity = ((EntityBlock) type.getBlock()).newBlockEntity(pos, type);

            // newBlockEntity can return null, currently only the case with material MOVING_PISTON
            if (blockEntity == null) {
                access.removeBlockEntity(pos);
            } else {
                access.setBlockEntity(blockEntity);
            }
        } else if (oldBlockState != null && oldBlockState.hasBlockEntity()) {
            access.removeBlockEntity(pos);
        }
    }

    @Override
    public int getHeight(final HeightMap heightMap, final int x, final int z) {
        Preconditions.checkArgument(heightMap != null, "HeightMap cannot be null");
        Preconditions.checkArgument(x >= 0 && x <= 15 && z >= 0 && z <= 15, "Cannot get height outside of a chunks bounds, must be between 0 and 15, got x: %s, z: %s", x, z);

        return getHandle().getHeight(CraftHeightMap.toNMS(heightMap), x, z);
    }
}
