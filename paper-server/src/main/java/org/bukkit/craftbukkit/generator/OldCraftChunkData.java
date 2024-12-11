package org.bukkit.craftbukkit.generator;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlockType;
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
    private final LevelChunkSection[] sections;
    private final Registry<net.minecraft.world.level.biome.Biome> biomes;
    private Set<BlockPos> tiles;
    private final Set<BlockPos> lights = new HashSet<>();

    public OldCraftChunkData(int minHeight, int maxHeight, Registry<net.minecraft.world.level.biome.Biome> biomes) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.biomes = biomes;
        this.sections = new LevelChunkSection[(((maxHeight - 1) >> 4) + 1) - (minHeight >> 4)];
    }

    @Override
    public int getMinHeight() {
        return this.minHeight;
    }

    @Override
    public int getMaxHeight() {
        return this.maxHeight;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        throw new UnsupportedOperationException("Unsupported, in older chunk generator api");
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
        return CraftBlockType.minecraftToBukkit(this.getTypeId(x, y, z).getBlock());
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
            LevelChunkSection section = this.getChunkSection(y, true);
            int offsetBase = y & 0xf;
            for (int x = xMin; x < xMax; x++) {
                for (int z = zMin; z < zMax; z++) {
                    section.setBlockState(x, offsetBase, z, type);
                }
            }
        }
    }

    public BlockState getTypeId(int x, int y, int z) {
        if (x != (x & 0xf) || y < this.minHeight || y >= this.maxHeight || z != (z & 0xf)) {
            return Blocks.AIR.defaultBlockState();
        }
        LevelChunkSection section = this.getChunkSection(y, false);
        if (section == null) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return section.getBlockState(x, y & 0xf, z);
        }
    }

    @Override
    public byte getData(int x, int y, int z) {
        return CraftMagicNumbers.toLegacyData(this.getTypeId(x, y, z));
    }

    private void setBlock(int x, int y, int z, BlockState type) {
        if (x != (x & 0xf) || y < this.minHeight || y >= this.maxHeight || z != (z & 0xf)) {
            return;
        }
        LevelChunkSection section = this.getChunkSection(y, true);
        section.setBlockState(x, y & 0xf, z, type);

        // SPIGOT-1753: Capture light blocks, for light updates
        if (type.getLightEmission() > 0) {
            this.lights.add(new BlockPos(x, y, z));
        } else {
            this.lights.remove(new BlockPos(x, y, z));
        }

        if (type.hasBlockEntity()) {
            if (this.tiles == null) {
                this.tiles = new HashSet<>();
            }

            this.tiles.add(new BlockPos(x, y, z));
        }
    }

    private LevelChunkSection getChunkSection(int y, boolean create) {
        int offset = (y - this.minHeight) >> 4;
        LevelChunkSection section = this.sections[offset];
        if (create && section == null) {
            this.sections[offset] = section = new LevelChunkSection(this.biomes);
        }
        return section;
    }

    LevelChunkSection[] getRawChunkData() {
        return this.sections;
    }

    Set<BlockPos> getTiles() {
        return this.tiles;
    }

    Set<BlockPos> getLights() {
        return this.lights;
    }
}
