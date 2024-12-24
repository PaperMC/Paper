package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

/**
 * Represents a static, thread-safe snapshot of chunk of blocks
 * Purpose is to allow clean, efficient copy of a chunk data to be made, and then handed off for processing in another thread (e.g. map rendering)
 */
public class CraftChunkSnapshot extends CraftBiomesSnapshot implements ChunkSnapshot {
    private final PalettedContainer<BlockState>[] blockids;
    private final byte[][] skylight;
    private final byte[][] emitlight;
    private final boolean[] empty;
    private final Heightmap hmap; // Height map
    private final long captureFulltime;

    CraftChunkSnapshot(int x, int z, int minHeight, int maxHeight, int seaLevel, String wname, long wtime, PalettedContainer<BlockState>[] sectionBlockIDs, byte[][] sectionSkyLights, byte[][] sectionEmitLights, boolean[] sectionEmpty, Heightmap hmap, Registry<net.minecraft.world.level.biome.Biome> biomeRegistry, PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] biome) {
        super(x, z, wname, minHeight, maxHeight, seaLevel, biomeRegistry, biome);
        this.captureFulltime = wtime;
        this.blockids = sectionBlockIDs;
        this.skylight = sectionSkyLights;
        this.emitlight = sectionEmitLights;
        this.empty = sectionEmpty;
        this.hmap = hmap;
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<BlockState> nms = Predicates.equalTo(((CraftBlockData) block).getState());
        for (PalettedContainer<BlockState> palette : this.blockids) {
            if (palette.maybeHas(nms)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Material getBlockType(int x, int y, int z) {
        this.validateChunkCoordinates(x, y, z);

        return this.blockids[this.getSectionIndex(y)].get(x, y & 0xF, z).getBukkitMaterial(); // Paper - optimise getType calls
    }

    @Override
    public final BlockData getBlockData(int x, int y, int z) {
        this.validateChunkCoordinates(x, y, z);

        return CraftBlockData.fromData(this.blockids[this.getSectionIndex(y)].get(x, y & 0xF, z));
    }

    @Override
    public final int getData(int x, int y, int z) {
        this.validateChunkCoordinates(x, y, z);

        return CraftMagicNumbers.toLegacyData(this.blockids[this.getSectionIndex(y)].get(x, y & 0xF, z));
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
        Preconditions.checkState(this.emitlight != null, "ChunkSnapshot created without light data. Please call getSnapshot with includeLightData=true"); // Paper - Add getChunkSnapshot includeLightData parameter
        this.validateChunkCoordinates(x, y, z);

        int off = ((y & 0xF) << 7) | (z << 3) | (x >> 1);
        return (this.emitlight[this.getSectionIndex(y)][off] >> ((x & 1) << 2)) & 0xF;
    }

    @Override
    public final int getHighestBlockYAt(int x, int z) {
        Preconditions.checkState(this.hmap != null, "ChunkSnapshot created without height map. Please call getSnapshot with includeMaxblocky=true");
        this.validateChunkCoordinates(x, 0, z);

        return this.hmap.getHighestTaken(x, z);
    }

    @Override
    public final Biome getBiome(int x, int z) {
        return this.getBiome(x, 0, z);
    }

    @Override
    public final double getRawBiomeTemperature(int x, int z) {
        return this.getRawBiomeTemperature(x, 0, z);
    }

    @Override
    public final long getCaptureFullTime() {
        return this.captureFulltime;
    }

    @Override
    public final boolean isSectionEmpty(int sy) {
        return this.empty[sy];
    }
}
