package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jspecify.annotations.Nullable;

public class CustomWorldChunkManager extends BiomeSource {

    // Set true on the calling thread while inside findBiomeHorizontal so that
    // getNoiseBiome can use the fast path for structure placement searches.
    // findBiomeHorizontal is the only caller of getNoiseBiome for stronghold
    // ring position computation; normal chunk generation calls getNoiseBiome
    // directly without going through findBiomeHorizontal.
    private static final ThreadLocal<Boolean> IN_STRUCTURE_SEARCH =
        ThreadLocal.withInitial(() -> false);

    private final WorldInfo worldInfo;
    private final BiomeProvider biomeProvider;
    public final BiomeSource vanillaBiomeSource;

    public CustomWorldChunkManager(WorldInfo worldInfo, BiomeProvider biomeProvider, BiomeSource vanillaBiomeSource) {
        this.worldInfo = worldInfo;
        this.biomeProvider = biomeProvider;
        this.vanillaBiomeSource = vanillaBiomeSource;
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        throw new UnsupportedOperationException("Cannot serialize CustomWorldChunkManager");
    }

    // Paper - structure search fast path: intercept the horizontal biome search used
    // by ConcentricRingsStructurePlacement (strongholds). Sets a thread-local so
    // getNoiseBiome can call BiomeProvider.getStructurePlacementBiome() instead of
    // the full pipeline, avoiding 100-2000x amplification from pipeline chunk cache misses.
    @Override
    public @Nullable Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(
            int x, int y, int z, int searchRadius, int skipSteps,
            Predicate<Holder<Biome>> allowed, RandomSource random,
            boolean findClosest, Climate.Sampler sampler) {
        IN_STRUCTURE_SEARCH.set(true);
        try {
            return super.findBiomeHorizontal(x, y, z, searchRadius, skipSteps,
                                              allowed, random, findClosest, sampler);
        } finally {
            IN_STRUCTURE_SEARCH.set(false);
        }
    }

    @Override
    public Holder<net.minecraft.world.level.biome.Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler noise) {
        if (IN_STRUCTURE_SEARCH.get()) {
            Optional<org.bukkit.block.Biome> fast =
                this.biomeProvider.getStructurePlacementBiome(this.worldInfo, QuartPos.toBlock(x), QuartPos.toBlock(z));
            if (fast.isPresent()) {
                Holder<net.minecraft.world.level.biome.Biome> biome = CraftBiome.bukkitToMinecraftHolder(fast.get());
                if (biome != null) return biome;
            }
        }

        Holder<net.minecraft.world.level.biome.Biome> biome = CraftBiome.bukkitToMinecraftHolder(
            this.biomeProvider.getBiome(this.worldInfo, QuartPos.toBlock(x), QuartPos.toBlock(y), QuartPos.toBlock(z), CraftBiomeParameterPoint.createBiomeParameterPoint(noise, noise.sample(x, y, z)))
        );
        Preconditions.checkArgument(biome != null, "Cannot set the biome to %s", biome);

        return biome;
    }

    @Override
    protected Stream<Holder<net.minecraft.world.level.biome.Biome>> collectPossibleBiomes() {
        return this.biomeProvider.getBiomes(this.worldInfo).stream().map(biome -> {
            Holder<net.minecraft.world.level.biome.Biome> b = CraftBiome.bukkitToMinecraftHolder(biome);
            Preconditions.checkArgument(b != null, "Cannot use the biome %s", biome);
            return b;
        });
    }
}
