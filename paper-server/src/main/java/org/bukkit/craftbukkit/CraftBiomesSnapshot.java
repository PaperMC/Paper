package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.bukkit.BiomesSnapshot;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftBiomesSnapshot implements BiomesSnapshot {

    private final int x, z;
    private final String worldname;
    private final int minHeight, maxHeight, seaLevel;
    private final Registry<net.minecraft.world.level.biome.Biome> biomeRegistry;
    private final PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] biome;

    public CraftBiomesSnapshot(final int x, final int z, final String worldname, final int minHeight, final int maxHeight, int seaLevel, final Registry<net.minecraft.world.level.biome.Biome> registry, final PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] biome) {
        this.x = x;
        this.z = z;
        this.worldname = worldname;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.biomeRegistry = registry;
        this.biome = biome;
        this.seaLevel = seaLevel;
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
        return this.worldname;
    }

    @Override
    public void setBiome(final int x, final int y, final int z, final Biome biome) {
        Preconditions.checkState(this.biome != null, "ChunkSnapshot created without biome. Please call getSnapshot with includeBiome=true");
        Objects.requireNonNull(biome, "biome cannot be null");
        this.validateChunkCoordinates(x, y, z);
        PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>> biomeLocal = this.biome[this.getSectionIndex(y)];
        biomeLocal.set(x >> 2, (y & 0xF) >> 2, z >> 2, CraftBiome.bukkitToMinecraftHolder(biome));
    }

    @Override
    public final double getRawBiomeTemperature(int x, int y, int z) {
        Preconditions.checkState(this.biome != null, "ChunkSnapshot created without biome. Please call getSnapshot with includeBiome=true");
        this.validateChunkCoordinates(x, y, z);

        PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>> biome = this.biome[this.getSectionIndex(y)]; // SPIGOT-7188: Don't need to convert y to biome coordinate scale since it is bound to the block chunk section
        return biome.get(x >> 2, (y & 0xF) >> 2, z >> 2).value().getTemperature(new BlockPos((this.x << 4) | x, y, (this.z << 4) | z), seaLevel);
    }

    @Override
    public final Biome getBiome(int x, int y, int z) {
        Preconditions.checkState(this.biome != null, "ChunkSnapshot created without biome. Please call getSnapshot with includeBiome=true");
        this.validateChunkCoordinates(x, y, z);

        PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>> biome = this.biome[this.getSectionIndex(y)]; // SPIGOT-7188: Don't need to convert y to biome coordinate scale since it is bound to the block chunk section
        return CraftBiome.minecraftHolderToBukkit(biome.get(x >> 2, (y & 0xF) >> 2, z >> 2));
    }


    @Override
    public boolean contains(Biome biome) {
        Preconditions.checkArgument(biome != null, "Biome cannot be null");

        Predicate<Holder<net.minecraft.world.level.biome.Biome>> nms = Predicates.equalTo(CraftBiome.bukkitToMinecraftHolder(biome));
        for (PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>> palette : this.biome) {
            if (palette.maybeHas(nms)) {
                return true;
            }
        }

        return false;
    }

    public PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] getBiome(){
        return biome;
    }

    protected void validateChunkCoordinates(int x, int y, int z) {
        CraftChunk.validateChunkCoordinates(this.minHeight, this.maxHeight, x, y, z);
    }

    protected int getSectionIndex(int y) {
        return (y - this.minHeight) >> 4;
    }
}
