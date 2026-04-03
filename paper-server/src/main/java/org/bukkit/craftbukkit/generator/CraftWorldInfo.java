package org.bukkit.craftbukkit.generator;

import java.util.UUID;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import org.bukkit.World;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.generator.WorldInfo;

public class CraftWorldInfo implements WorldInfo {

    private final String name;
    private final UUID uuid;
    private final World.Environment environment;
    private final long seed;
    private final int minHeight;
    private final int maxHeight;
    private final FeatureFlagSet enabledFeatures;
    private final ChunkGenerator vanillaChunkGenerator;
    private final RegistryAccess.Frozen registryAccess;

    public CraftWorldInfo(
        String name,
        long seed,
        FeatureFlagSet enabledFeatures,
        World.Environment environment,
        DimensionType dimensionManager,
        ChunkGenerator vanillaChunkGenerator,
        RegistryAccess.Frozen registryAccess,
        UUID uuid
    ) {
        this.name = name;
        this.seed = seed;
        this.enabledFeatures = enabledFeatures;
        this.environment = environment;
        this.minHeight = dimensionManager.minY();
        this.maxHeight = dimensionManager.minY() + dimensionManager.height();
        this.vanillaChunkGenerator = vanillaChunkGenerator;
        this.registryAccess = registryAccess;
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUID() {
        return this.uuid;
    }

    @Override
    public World.Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public long getSeed() {
        return this.seed;
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
    public org.bukkit.generator.BiomeProvider vanillaBiomeProvider() {
        final net.minecraft.world.level.levelgen.RandomState randomState;
        if (vanillaChunkGenerator instanceof net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator noiseBasedChunkGenerator) {
            randomState = net.minecraft.world.level.levelgen.RandomState.create(noiseBasedChunkGenerator.generatorSettings().value(),
                registryAccess.lookupOrThrow(net.minecraft.core.registries.Registries.NOISE), getSeed());
        } else {
            randomState = net.minecraft.world.level.levelgen.RandomState.create(net.minecraft.world.level.levelgen.NoiseGeneratorSettings.dummy(),
                registryAccess.lookupOrThrow(net.minecraft.core.registries.Registries.NOISE), getSeed());
        }

        final java.util.List<org.bukkit.block.Biome> possibleBiomes = CraftWorldInfo.this.vanillaChunkGenerator.getBiomeSource().possibleBiomes().stream()
            .map(CraftBiome::minecraftHolderToBukkit)
            .toList();
        return new org.bukkit.generator.BiomeProvider() {
            @Override
            public org.bukkit.block.Biome getBiome(final WorldInfo worldInfo, final int x, final int y, final int z) {
                return org.bukkit.craftbukkit.block.CraftBiome.minecraftHolderToBukkit(
                    CraftWorldInfo.this.vanillaChunkGenerator.getBiomeSource().getNoiseBiome(x >> 2, y >> 2, z >> 2, randomState.sampler()));
            }

            @Override
            public java.util.List<org.bukkit.block.Biome> getBiomes(final org.bukkit.generator.WorldInfo worldInfo) {
                return possibleBiomes;
            }
        };
    }

    @Override
    public java.util.Set<org.bukkit.FeatureFlag> getFeatureFlags() {
        return io.papermc.paper.world.flag.PaperFeatureFlagProviderImpl.fromNms(this.enabledFeatures);
    }
}
