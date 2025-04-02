package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

/**
 * Represents a static, thread-safe snapshot of chunk of blocks
 * Purpose is to allow clean, efficient copy of a chunk data to be made, and then handed off for processing in another thread (e.g. map rendering)
 */
public class CraftChunkSnapshot implements ChunkSnapshot {
    private final int x, z;
    private final int minHeight, maxHeight, seaLevel;
    private final String worldName;
    private final PalettedContainer<BlockState>[] blockIds;
    private final byte[][] skylight;
    private final byte[][] emitLight;
    private final boolean[] empty;
    private final Heightmap heightmap; // Height map
    private final long captureFullTime;
    private final PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>>[] biome;

    CraftChunkSnapshot(int x, int z, int minHeight, int maxHeight, int seaLevel, String worldName, long fullTime, PalettedContainer<BlockState>[] sectionBlockIDs, byte[][] sectionSkyLights, byte[][] sectionEmitLights, boolean[] sectionEmpty, Heightmap heightmap, PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>>[] biome) {
        this.x = x;
        this.z = z;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.seaLevel = seaLevel;
        this.worldName = worldName;
        this.captureFullTime = fullTime;
        this.blockIds = sectionBlockIDs;
        this.skylight = sectionSkyLights;
        this.emitLight = sectionEmitLights;
        this.empty = sectionEmpty;
        this.heightmap = heightmap;
        this.biome = biome;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<BlockState> filter = Predicates.equalTo(((CraftBlockData) block).getState());
        for (PalettedContainer<BlockState> palette : this.blockIds) {
            if (palette.maybeHas(filter)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(Biome biome) {
        Preconditions.checkArgument(biome != null, "Biome cannot be null");

        Predicate<Holder<net.minecraft.world.level.biome.Biome>> filter = Predicates.equalTo(CraftBiome.bukkitToMinecraftHolder(biome));
        for (PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>> palette : this.biome) {
            if (palette.maybeHas(filter)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Material getBlockType(int x, int y, int z) {
        this.validateChunkCoordinates(x, y, z);

        return this.blockIds[this.getSectionIndex(y)].get(x, y & 0xF, z).getBukkitMaterial(); // Paper - optimise get calls
    }

    @Override
    public final BlockData getBlockData(int x, int y, int z) {
        this.validateChunkCoordinates(x, y, z);

        return CraftBlockData.fromData(this.blockIds[this.getSectionIndex(y)].get(x, y & 0xF, z));
    }

    @Override
    public final int getData(int x, int y, int z) {
        this.validateChunkCoordinates(x, y, z);

        return CraftMagicNumbers.toLegacyData(this.blockIds[this.getSectionIndex(y)].get(x, y & 0xF, z));
    }

    @Override
    public final int getBlockSkyLight(int x, int y, int z) {
        Preconditions.checkState(this.skylight != null, "ChunkSnapshot created without light data. Please call getSnapshot with includeLightData=true"); // Paper - Add getChunkSnapshot includeLightData parameter
        this.validateChunkCoordinates(x, y, z);

        int off = ((y & 0xF) << 7) | (z << 3) | (x >> 1);
        return (this.skylight[this.getSectionIndex(y)][off] >> ((x & 1) << 2)) & 0xF;
    }

    @Override
    public final int getBlockEmittedLight(int x, int y, int z) {
        Preconditions.checkState(this.emitLight != null, "ChunkSnapshot created without light data. Please call getSnapshot with includeLightData=true"); // Paper - Add getChunkSnapshot includeLightData parameter
        this.validateChunkCoordinates(x, y, z);

        int off = ((y & 0xF) << 7) | (z << 3) | (x >> 1);
        return (this.emitLight[this.getSectionIndex(y)][off] >> ((x & 1) << 2)) & 0xF;
    }

    @Override
    public final int getHighestBlockYAt(int x, int z) {
        Preconditions.checkState(this.heightmap != null, "ChunkSnapshot created without height map. Please call getSnapshot with includeMaxblocky=true");
        this.validateChunkCoordinates(x, 0, z);

        return this.heightmap.getHighestTaken(x, z);
    }

    @Override
    public final Biome getBiome(int x, int z) {
        return this.getBiome(x, 0, z);
    }

    @Override
    public final Biome getBiome(int x, int y, int z) {
        Preconditions.checkState(this.biome != null, "ChunkSnapshot created without biome. Please call getSnapshot with includeBiome=true");
        this.validateChunkCoordinates(x, y, z);

        PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>> biome = this.biome[this.getSectionIndex(y)]; // SPIGOT-7188: Don't need to convert y to biome coordinate scale since it is bound to the block chunk section
        return CraftBiome.minecraftHolderToBukkit(biome.get(x >> 2, (y & 0xF) >> 2, z >> 2));
    }

    @Override
    public final double getRawBiomeTemperature(int x, int z) {
        return this.getRawBiomeTemperature(x, 0, z);
    }

    @Override
    public final double getRawBiomeTemperature(int x, int y, int z) {
        Preconditions.checkState(this.biome != null, "ChunkSnapshot created without biome. Please call getSnapshot with includeBiome=true");
        this.validateChunkCoordinates(x, y, z);

        PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>> biome = this.biome[this.getSectionIndex(y)]; // SPIGOT-7188: Don't need to convert y to biome coordinate scale since it is bound to the block chunk section
        return biome.get(x >> 2, (y & 0xF) >> 2, z >> 2).value().getTemperature(new BlockPos((this.x << 4) | x, y, (this.z << 4) | z), this.seaLevel);
    }

    @Override
    public final long getCaptureFullTime() {
        return this.captureFullTime;
    }

    @Override
    public final boolean isSectionEmpty(int sy) {
        return this.empty[sy];
    }

    private int getSectionIndex(int y) {
        return (y - this.minHeight) >> 4;
    }

    private void validateChunkCoordinates(int x, int y, int z) {
        CraftChunk.validateChunkCoordinates(this.minHeight, this.maxHeight, x, y, z);
    }
}
