package org.bukkit.craftbukkit.generator;

import java.util.UUID;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.WorldUUID;
import org.bukkit.generator.WorldInfo;

public class CraftWorldInfo implements WorldInfo {

    private final String name;
    private final UUID uuid;
    private final World.Environment environment;
    private final long seed;
    private final int minHeight;
    private final int maxHeight;
    private final net.minecraft.world.flag.FeatureFlagSet enabledFeatures; // Paper - feature flag API
    // Paper start
    private final net.minecraft.world.level.chunk.ChunkGenerator vanillaChunkGenerator;
    private final net.minecraft.core.RegistryAccess.Frozen registryAccess;

    public CraftWorldInfo(PrimaryLevelData worldDataServer, LevelStorageSource.LevelStorageAccess session, World.Environment environment, DimensionType dimensionManager, net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator, net.minecraft.core.RegistryAccess.Frozen registryAccess) {
        this.registryAccess = registryAccess;
        this.vanillaChunkGenerator = chunkGenerator;
        // Paper end
        this.name = worldDataServer.getLevelName();
        this.uuid = WorldUUID.getOrCreate(session.levelDirectory.path().toFile());
        this.environment = environment;
        this.seed = worldDataServer.worldGenOptions().seed();
        this.minHeight = dimensionManager.minY();
        this.maxHeight = dimensionManager.minY() + dimensionManager.height();
        this.enabledFeatures = worldDataServer.enabledFeatures(); // Paper - feature flag API
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

    // Paper start
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
            .map(biome -> org.bukkit.craftbukkit.block.CraftBiome.minecraftHolderToBukkit(biome))
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
    // Paper end

    // Paper start - feature flag API
    @Override
    public java.util.Set<org.bukkit.FeatureFlag> getFeatureFlags() {
        return io.papermc.paper.world.flag.PaperFeatureFlagProviderImpl.fromNms(this.enabledFeatures);
    }
    // Paper end - feature flag API
}
