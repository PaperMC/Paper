package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.*;

import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.block.CraftBlock;

public class CustomChunkGenerator extends InternalChunkGenerator<GeneratorSettingsDefault> {
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final long seed;
    private final Random random;
    private final WorldChunkManager chunkManager;
    private final WorldGenStronghold strongholdGen = new WorldGenStronghold();
    private final GeneratorSettingsDefault settings = new GeneratorSettingsDefault();

    private static class CustomBiomeGrid implements BiomeGrid {
        BiomeBase[] biome;

        @Override
        public Biome getBiome(int x, int z) {
            return CraftBlock.biomeBaseToBiome(biome[(z << 4) | x]);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
            biome[(z << 4) | x] = CraftBlock.biomeToBiomeBase(bio);
        }
    }

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        this.world = (WorldServer) world;
        this.generator = generator;
        this.seed = seed;

        this.random = new Random(seed);
        this.chunkManager = world.worldProvider.getChunkGenerator().getWorldChunkManager();
    }

    @Override
    public void createChunk(IChunkAccess ichunkaccess) {
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = chunkManager.getBiomeBlock(x << 4, z << 4, 16, 16);

        ChunkData data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
        Preconditions.checkArgument(data instanceof CraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        ChunkSection[] sections = ((CraftChunkData) data).getRawChunkData();

        ChunkSection[] csect = ichunkaccess.getSections();
        int scnt = Math.min(csect.length, sections.length);

        // Loop through returned sections
        for (int sec = 0; sec < scnt; sec++) {
            if (sections[sec] == null) {
                continue;
            }
            ChunkSection section = sections[sec];

            csect[sec] = section;
        }

        // Set biome grid
        ichunkaccess.a(biomegrid.biome);
    }

    @Override
    public ChunkData generateChunkData(org.bukkit.World world, Random random, int x, int z, BiomeGrid biome) {
        return generator.generateChunkData(world, random, x, z, biome);
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return generator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return generator.getDefaultPopulators(world);
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType type, BlockPosition position) {
        BiomeBase biomebase = world.getBiome(position);

        return biomebase == null ? null : biomebase.getMobs(type);
    }

    @Override
    public void addFeatures(RegionLimitedWorldAccess regionlimitedworldaccess, WorldGenStage.Features worldgenstage_features) {
    }

    @Override
    public void addDecorations(RegionLimitedWorldAccess regionlimitedworldaccess) {
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionlimitedworldaccess) {
    }

    @Override
    public BlockPosition findNearestMapFeature(World world, String type, BlockPosition position, int i, boolean flag) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.getNearestGeneratedFeature(world, this, position, i, flag) : null;
    }

    @Override
    public GeneratorSettingsDefault getSettings() {
        return settings;
    }

    @Override
    public int a(World world, boolean flag, boolean flag1) {
        return 0;
    }

    @Override
    public boolean canSpawnStructure(BiomeBase biomebase, StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return biomebase.a(structuregenerator);
    }

    @Override
    public WorldGenFeatureConfiguration getFeatureConfiguration(BiomeBase biomebase, StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return biomebase.b(structuregenerator);
    }

    // Taken from ChunkGeneratorAbstract
    private final Map<StructureGenerator<? extends WorldGenFeatureConfiguration>, Long2ObjectMap<StructureStart>> structureStartCache = Maps.newHashMap();

    @Override
    public Long2ObjectMap<StructureStart> getStructureStartCache(StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return (Long2ObjectMap) this.structureStartCache.computeIfAbsent(structuregenerator, (s) -> {
            return Long2ObjectMaps.synchronize(new ExpiringMap(8192, 10000));
        });
    }

    // Taken from ChunkGeneratorAbstract
    private final Map<StructureGenerator<? extends WorldGenFeatureConfiguration>, Long2ObjectMap<LongSet>> structureCache = Maps.newHashMap();

    @Override
    public Long2ObjectMap<LongSet> getStructureCache(StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return (Long2ObjectMap) this.structureCache.computeIfAbsent(structuregenerator, (s) -> {
            return Long2ObjectMaps.synchronize(new ExpiringMap(8192, 10000));
        });
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return chunkManager;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getSpawnHeight() {
        return world.getSeaLevel() + 1;
    }

    @Override
    public int getGenerationDepth() {
        return world.getHeight();
    }
}
