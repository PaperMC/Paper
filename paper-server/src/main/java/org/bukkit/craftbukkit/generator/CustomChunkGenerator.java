package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftHeightMap;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator {

    private static final long INITIAL_SEED = 0;

    private final net.minecraft.world.level.chunk.ChunkGenerator delegate;
    private final ChunkGenerator generator;
    private final ServerLevel world;
    private final Random random = new Random();
    private boolean newApi;
    private boolean implementBaseHeight = true;

    @Deprecated
    private class CustomBiomeGrid implements BiomeGrid {

        private final ChunkAccess chunk;

        public CustomBiomeGrid(ChunkAccess chunk) {
            this.chunk = chunk;
        }

        @Override
        public Biome getBiome(int x, int z) {
            return this.getBiome(x, 0, z);
        }

        @Override
        public void setBiome(int x, int z, Biome biome) {
            for (int y = CustomChunkGenerator.this.world.getWorld().getMinHeight(); y < CustomChunkGenerator.this.world.getWorld().getMaxHeight(); y += 4) {
                this.setBiome(x, y, z, biome);
            }
        }

        @Override
        public Biome getBiome(int x, int y, int z) {
            return CraftBiome.minecraftHolderToBukkit(this.chunk.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z)));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome biome) {
            Holder<net.minecraft.world.level.biome.Biome> b = CraftBiome.bukkitToMinecraftHolder(biome);
            Preconditions.checkArgument(b != null, "Cannot set the biome to %s", biome);
            this.chunk.setNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), b);
        }
    }

    public CustomChunkGenerator(ServerLevel world, net.minecraft.world.level.chunk.ChunkGenerator delegate, ChunkGenerator generator) {
        super(delegate.getBiomeSource(), delegate.generationSettingsGetter);

        this.world = world;
        this.delegate = delegate;
        this.generator = generator;
    }

    public net.minecraft.world.level.chunk.ChunkGenerator getDelegate() {
        return this.delegate;
    }

    private static WorldgenRandom getSeededRandom() {
        return new WorldgenRandom(new LegacyRandomSource(INITIAL_SEED));
    }

    @Override
    public BiomeSource getBiomeSource() {
        return this.delegate.getBiomeSource();
    }

    @Override
    public int getMinY() {
        return this.delegate.getMinY();
    }

    @Override
    public int getSeaLevel() {
        return this.delegate.getSeaLevel();
    }

    @Override
    public void createStructures(RegistryAccess registryAccess, ChunkGeneratorStructureState state, StructureManager structureManager, ChunkAccess centerChunk, StructureTemplateManager structureTemplateManager, ResourceKey<Level> level) {
        WorldgenRandom random = CustomChunkGenerator.getSeededRandom();
        int x = centerChunk.getPos().x();
        int z = centerChunk.getPos().z();

        random.setSeed(Mth.getSeed(x, "should-structures".hashCode(), z) ^ this.world.getSeed());
        if (this.generator.shouldGenerateStructures(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z)) {
            super.createStructures(registryAccess, state, structureManager, centerChunk, structureTemplateManager, level);
        }
    }

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {
        WorldgenRandom random = getSeededRandom();
        int x = protoChunk.getPos().x();
        int z = protoChunk.getPos().z();

        random.setSeed(Mth.getSeed(x, "should-surface".hashCode(), z) ^ level.getSeed());
        if (this.generator.shouldGenerateSurface(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z)) {
            this.delegate.buildSurface(level, structureManager, randomState, protoChunk);
        }

        CraftChunkData chunkData = new CraftChunkData(this.world.getWorld(), protoChunk);

        random.setLargeFeatureWithSalt(INITIAL_SEED, x, z, 0);
        this.generator.generateSurface(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z, chunkData);

        if (false && this.generator.shouldGenerateBedrock()) {
            random = getSeededRandom();
            random.setLargeFeatureWithSalt(INITIAL_SEED, x, z, 0);
            // this.delegate.buildBedrock(protoChunk, random);
        }

        random = getSeededRandom();
        random.setLargeFeatureWithSalt(INITIAL_SEED, x, z, 0);
        this.generator.generateBedrock(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z, chunkData);
        chunkData.breakLink();

        // return if new api is used
        if (this.newApi) {
            return;
        }

        // old ChunkGenerator logic, for backwards compatibility
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        this.random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomeGrid = new CustomBiomeGrid(protoChunk);

        ChunkData data;
        try {
            if (this.generator.isParallelCapable()) {
                data = this.generator.generateChunkData(this.world.getWorld(), this.random, x, z, biomeGrid);
            } else {
                synchronized (this) {
                    data = this.generator.generateChunkData(this.world.getWorld(), this.random, x, z, biomeGrid);
                }
            }
        } catch (UnsupportedOperationException exception) {
            this.newApi = true;
            return;
        }

        Preconditions.checkArgument(data instanceof OldCraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        OldCraftChunkData craftData = (OldCraftChunkData) data;
        LevelChunkSection[] sections = craftData.getRawChunkData();

        LevelChunkSection[] protoSections = protoChunk.getSections();
        int sectionCount = Math.min(protoSections.length, sections.length);

        // Loop through returned sections
        for (int sectionIndex = 0; sectionIndex < sectionCount; sectionIndex++) {
            if (sections[sectionIndex] == null) {
                continue;
            }
            LevelChunkSection section = sections[sectionIndex];

            // SPIGOT-6843: Copy biomes over to new section.
            // Not the most performant way, but has a small footprint and developer should move to the new api anyway
            LevelChunkSection oldSection = protoSections[sectionIndex];
            for (int biomeX = 0; biomeX < 4; biomeX++) {
                for (int biomeY = 0; biomeY < 4; biomeY++) {
                    for (int biomeZ = 0; biomeZ < 4; biomeZ++) {
                        section.setNoiseBiome(biomeX, biomeY, biomeZ, oldSection.getNoiseBiome(biomeX, biomeY, biomeZ));
                    }
                }
            }

            protoSections[sectionIndex] = section;
        }

        if (craftData.getTiles() != null) {
            for (BlockPos pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();

                BlockState state = craftData.getBlockState(tx, ty, tz);
                if (state.hasBlockEntity()) {
                    BlockEntity blockEntity = ((EntityBlock) state.getBlock()).newBlockEntity(new BlockPos((x << 4) + tx, ty, (z << 4) + tz), state);
                    if (blockEntity != null) {
                        protoChunk.setBlockEntity(blockEntity);
                    }
                }
            }
        }
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {
        WorldgenRandom random = getSeededRandom();
        int x = chunk.getPos().x();
        int z = chunk.getPos().z();

        random.setSeed(Mth.getSeed(x, "should-caves".hashCode(), z) ^ region.getSeed());
        if (this.generator.shouldGenerateCaves(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z)) {
            this.delegate.applyCarvers(region, seed, randomState, biomeManager, structureManager, chunk);
        }

        // Minecraft removed the LIQUID_CARVERS stage from world generation, without removing the LIQUID Carving enum.
        // Meaning this method is only called once for each chunk, so no check is required.
        CraftChunkData chunkData = new CraftChunkData(this.world.getWorld(), chunk);
        random.setDecorationSeed(seed, 0, 0);

        this.generator.generateCaves(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z, chunkData);
        chunkData.breakLink();
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess centerChunk) {
        CompletableFuture<ChunkAccess> future = null;
        WorldgenRandom random = getSeededRandom();
        int x = centerChunk.getPos().x();
        int z = centerChunk.getPos().z();

        random.setSeed(Mth.getSeed(x, "should-noise".hashCode(), z) ^ this.world.getSeed());
        if (this.generator.shouldGenerateNoise(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z)) {
            future = this.delegate.fillFromNoise(blender, randomState, structureManager, centerChunk);
        }

        Function<ChunkAccess, ChunkAccess> work = chunk -> {
            CraftChunkData chunkData = new CraftChunkData(this.world.getWorld(), chunk);
            random.setLargeFeatureWithSalt(INITIAL_SEED, x, z, 0);

            this.generator.generateNoise(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z, chunkData);
            chunkData.breakLink();
            return chunk;
        };

        return future == null ? CompletableFuture.supplyAsync(() -> work.apply(centerChunk), io.papermc.paper.FeatureHooks.getWorldgenExecutor()) : future.thenApply(work); // Paper - chunk system
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
        if (this.implementBaseHeight) {
            try {
                WorldgenRandom random = getSeededRandom();
                random.setLargeFeatureWithSalt(INITIAL_SEED, SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z), 0);

                return this.generator.getBaseHeight(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z, CraftHeightMap.fromNMS(type));
            } catch (UnsupportedOperationException exception) {
                this.implementBaseHeight = false;
            }
        }

        return this.delegate.getBaseHeight(x, z, type, heightAccessor, randomState);
    }

    @Override
    public WeightedList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<net.minecraft.world.level.biome.Biome> biome, StructureManager structureManager, MobCategory mobCategory, BlockPos pos) {
        return this.delegate.getMobsAt(biome, structureManager, mobCategory, pos);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        WorldgenRandom random = getSeededRandom();
        int x = chunk.getPos().x();
        int z = chunk.getPos().z();

        random.setSeed(Mth.getSeed(x, "should-decoration".hashCode(), z) ^ level.getSeed());
        super.applyBiomeDecoration(level, chunk, structureManager, this.generator.shouldGenerateDecorations(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z));
    }

    @Override
    public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {
        this.delegate.addDebugScreenInfo(result, randomState, feetPos);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
        WorldgenRandom random = getSeededRandom();
        int x = worldGenRegion.getCenter().x();
        int z = worldGenRegion.getCenter().z();

        random.setSeed(Mth.getSeed(x, "should-mobs".hashCode(), z) ^ worldGenRegion.getSeed());
        if (this.generator.shouldGenerateMobs(this.world.getWorld(), new RandomSourceWrapper.RandomWrapper(random), x, z)) {
            this.delegate.spawnOriginalMobs(worldGenRegion);
        }
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor heightAccessor) {
        return this.delegate.getSpawnHeight(heightAccessor);
    }

    @Override
    public int getGenDepth() {
        return this.delegate.getGenDepth();
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) {
        return this.delegate.getBaseColumn(x, z, heightAccessor, randomState);
    }

    @Override
    protected MapCodec<? extends net.minecraft.world.level.chunk.ChunkGenerator> codec() {
        return MapCodec.unit(null);
    }
}
