package org.bukkit.craftbukkit.generator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class CraftWorldInfo implements WorldInfo {

    private final String name;
    private final NamespacedKey dimension;
    private final UUID uuid;
    private final World.Environment environment;
    private final long seed;
    private final int minHeight;
    private final int maxHeight;
    private final FeatureFlagSet enabledFeatures;
    private final ChunkGenerator vanillaChunkGenerator;
    private final RegistryAccess registryAccess;

    public CraftWorldInfo(
        String name,
        NamespacedKey dimension,
        long seed,
        FeatureFlagSet enabledFeatures,
        World.Environment environment,
        DimensionType dimensionType,
        ChunkGenerator vanillaChunkGenerator,
        RegistryAccess registryAccess,
        UUID uuid
    ) {
        this.name = name;
        this.dimension = dimension;
        this.seed = seed;
        this.enabledFeatures = enabledFeatures;
        this.environment = environment;
        this.minHeight = dimensionType.minY();
        this.maxHeight = dimensionType.minY() + dimensionType.height();
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
        if (this.vanillaChunkGenerator instanceof net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator noiseBasedChunkGenerator) {
            randomState = net.minecraft.world.level.levelgen.RandomState.create(
                this.registryAccess.lookupOrThrow(net.minecraft.core.registries.Registries.NOISE),
                this.getSeed(),
                noiseBasedChunkGenerator.generatorSettings().value()
            );
        } else {
            // Values copied from net.minecraft.server.level.ChunkMap constructor
            randomState = net.minecraft.world.level.levelgen.RandomState.create(
                this.registryAccess.lookupOrThrow(net.minecraft.core.registries.Registries.NOISE),
                this.getSeed(),
                false,
                net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),
                63,
                net.minecraft.world.level.levelgen.NoiseRouterData.none()
            );
        }

        final net.minecraft.world.level.biome.BiomeSource biomeSource = this.vanillaChunkGenerator.getBiomeSource();
        // TODO - snapshot - not sure about createCachingResolver or createUncachedResolver
        final net.minecraft.world.level.biome.BiomeResolver resolver = biomeSource.createCachingResolver(randomState);

        final java.util.List<org.bukkit.block.Biome> possibleBiomes = biomeSource.possibleBiomes().stream()
            .map(CraftBiome::minecraftHolderToBukkit)
            .toList();
        return new org.bukkit.generator.BiomeProvider() {
            @Override
            public org.bukkit.block.Biome getBiome(final WorldInfo worldInfo, final int x, final int y, final int z) {
                return org.bukkit.craftbukkit.block.CraftBiome.minecraftHolderToBukkit(
                    resolver.getNoiseBiome(x >> 2, y >> 2, z >> 2));
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

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.dimension;
    }
}
