package org.bukkit.craftbukkit.generator;

import io.papermc.paper.world.flag.PaperFeatureFlagProviderImpl;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.RandomState;
import org.bukkit.FeatureFlag;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.generator.BiomeProvider;
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
    public BiomeProvider vanillaBiomeProvider() {
        final RandomState randomState;
        if (this.vanillaChunkGenerator instanceof NoiseBasedChunkGenerator noiseBasedChunkGenerator) {
            randomState = RandomState.create(
                this.registryAccess.lookupOrThrow(Registries.NOISE),
                this.getSeed(),
                noiseBasedChunkGenerator.generatorSettings().value()
            );
        } else {
            // Values copied from net.minecraft.server.level.ChunkMap constructor
            randomState = RandomState.create(
                this.registryAccess.lookupOrThrow(Registries.NOISE),
                this.getSeed(),
                false,
                Blocks.STONE.defaultBlockState(),
                63,
                NoiseRouterData.none()
            );
        }

        final BiomeSource biomeSource = this.vanillaChunkGenerator.getBiomeSource();
        final BiomeResolver resolver = biomeSource.createUncachedResolver(randomState);

        final List<Biome> possibleBiomes = biomeSource.possibleBiomes().stream()
            .map(CraftBiome::minecraftHolderToBukkit)
            .toList();
        return new BiomeProvider() {
            @Override
            public Biome getBiome(final WorldInfo worldInfo, final int x, final int y, final int z) {
                return CraftBiome.minecraftHolderToBukkit(resolver.getNoiseBiome(x >> 2, y >> 2, z >> 2));
            }

            @Override
            public List<Biome> getBiomes(final WorldInfo worldInfo) {
                return possibleBiomes;
            }
        };
    }

    @Override
    public Set<FeatureFlag> getFeatureFlags() {
        return PaperFeatureFlagProviderImpl.fromNms(this.enabledFeatures);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.dimension;
    }
}
