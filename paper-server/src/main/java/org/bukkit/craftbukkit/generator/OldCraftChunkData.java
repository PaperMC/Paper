package org.bukkit.craftbukkit.generator;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

/**
 * Data to be used for the block types and data in a newly generated chunk.
 */
@Deprecated
public final class OldCraftChunkData implements ChunkGenerator.ChunkData {
    private final int minHeight;
    private final int maxHeight;
    private final ChunkSection[] sections;
    private final IRegistry<BiomeBase> biomes;
    private Set<BlockPosition> tiles;
    private final Set<BlockPosition> lights = new HashSet<>();

    public OldCraftChunkData(int minHeight, int maxHeight, IRegistry<BiomeBase> biomes) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.biomes = biomes;
        this.sections = new ChunkSection[(((maxHeight - 1) >> 4) + 1) - (minHeight >> 4)];
    }

    @Override
    public int getMinHeight() {
        return minHeight;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        throw new UnsupportedOperationException("Unsupported, in older chunk generator api");
    }

    @Override
    public void setBlock(int x, int y, int z, Material material) {
        setBlock(x, y, z, material.createBlockData());
    }

    @Override
    public void setBlock(int x, int y, int z, MaterialData material) {
        setBlock(x, y, z, CraftMagicNumbers.getBlock(material));
    }

    @Override
    public void setBlock(int x, int y, int z, BlockData blockData) {
        setBlock(x, y, z, ((CraftBlockData) blockData).getState());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
        setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.createBlockData());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material) {
        setRegion(xMin, yMin, zMin, xMax, yMax, zMax, CraftMagicNumbers.getBlock(material));
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockData blockData) {
        setRegion(xMin, yMin, zMin, xMax, yMax, zMax, ((CraftBlockData) blockData).getState());
    }

    @Override
    public Material getType(int x, int y, int z) {
        return CraftMagicNumbers.getMaterial(getTypeId(x, y, z).getBlock());
    }

    @Override
    public MaterialData getTypeAndData(int x, int y, int z) {
        return CraftMagicNumbers.getMaterial(getTypeId(x, y, z));
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        return CraftBlockData.fromData(getTypeId(x, y, z));
    }

    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockData type) {
        // Clamp to sane values.
        if (xMin > 0xf || yMin >= maxHeight || zMin > 0xf) {
            return;
        }
        if (xMin < 0) {
            xMin = 0;
        }
        if (yMin < minHeight) {
            yMin = minHeight;
        }
        if (zMin < 0) {
            zMin = 0;
        }
        if (xMax > 0x10) {
            xMax = 0x10;
        }
        if (yMax > maxHeight) {
            yMax = maxHeight;
        }
        if (zMax > 0x10) {
            zMax = 0x10;
        }
        if (xMin >= xMax || yMin >= yMax || zMin >= zMax) {
            return;
        }
        for (int y = yMin; y < yMax; y++) {
            ChunkSection section = getChunkSection(y, true);
            int offsetBase = y & 0xf;
            for (int x = xMin; x < xMax; x++) {
                for (int z = zMin; z < zMax; z++) {
                    section.setBlockState(x, offsetBase, z, type);
                }
            }
        }
    }

    public IBlockData getTypeId(int x, int y, int z) {
        if (x != (x & 0xf) || y < minHeight || y >= maxHeight || z != (z & 0xf)) {
            return Blocks.AIR.defaultBlockState();
        }
        ChunkSection section = getChunkSection(y, false);
        if (section == null) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return section.getBlockState(x, y & 0xf, z);
        }
    }

    @Override
    public byte getData(int x, int y, int z) {
        return CraftMagicNumbers.toLegacyData(getTypeId(x, y, z));
    }

    private void setBlock(int x, int y, int z, IBlockData type) {
        if (x != (x & 0xf) || y < minHeight || y >= maxHeight || z != (z & 0xf)) {
            return;
        }
        ChunkSection section = getChunkSection(y, true);
        section.setBlockState(x, y & 0xf, z, type);

        // SPIGOT-1753: Capture light blocks, for light updates
        if (type.getLightEmission() > 0) {
            lights.add(new BlockPosition(x, y, z));
        } else {
            lights.remove(new BlockPosition(x, y, z));
        }

        if (type.hasBlockEntity()) {
            if (tiles == null) {
                tiles = new HashSet<>();
            }

            tiles.add(new BlockPosition(x, y, z));
        }
    }

    private ChunkSection getChunkSection(int y, boolean create) {
        int offset = (y - minHeight) >> 4;
        ChunkSection section = sections[offset];
        if (create && section == null) {
            sections[offset] = section = new ChunkSection(offset + (minHeight >> 4), biomes);
        }
        return section;
    }

    ChunkSection[] getRawChunkData() {
        return sections;
    }

    Set<BlockPosition> getTiles() {
        return tiles;
    }

    Set<BlockPosition> getLights() {
        return lights;
    }
}
