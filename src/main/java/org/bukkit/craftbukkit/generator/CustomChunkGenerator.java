package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BiomeManager;
import net.minecraft.server.BiomeStorage;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChunkProviderGenerate;
import net.minecraft.server.ChunkProviderHell;
import net.minecraft.server.ChunkProviderTheEnd;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.DefinedStructureManager;
import net.minecraft.server.EnumCreatureType;
import net.minecraft.server.GeneratorAccess;
import net.minecraft.server.GeneratorSettingsDefault;
import net.minecraft.server.GeneratorSettingsEnd;
import net.minecraft.server.GeneratorSettingsNether;
import net.minecraft.server.GeneratorSettingsOverworld;
import net.minecraft.server.HeightMap;
import net.minecraft.server.IChunkAccess;
import net.minecraft.server.ITileEntity;
import net.minecraft.server.ProtoChunk;
import net.minecraft.server.RegionLimitedWorldAccess;
import net.minecraft.server.StructureGenerator;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.WorldChunkManager;
import net.minecraft.server.WorldGenFeatureConfiguration;
import net.minecraft.server.WorldGenStage;
import net.minecraft.server.WorldServer;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator<GeneratorSettingsDefault> {
    private final net.minecraft.server.ChunkGenerator delegate;
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random = new Random();

    private class CustomBiomeGrid implements BiomeGrid {

        private final BiomeStorage biome; // SPIGOT-5529: stored in 4x4 grid

        public CustomBiomeGrid(BiomeStorage biome) {
            this.biome = biome;
        }

        @Override
        public Biome getBiome(int x, int z) {
            return getBiome(x, 0, z);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
            for (int y = 0; y < world.getWorld().getMaxHeight(); y += 4) {
                setBiome(x, y, z, bio);
            }
        }

        @Override
        public Biome getBiome(int x, int y, int z) {
            return CraftBlock.biomeBaseToBiome(biome.getBiome(x >> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase(bio));
        }
    }

    public CustomChunkGenerator(World world, ChunkGenerator generator) {
        super(world, world.worldProvider.getChunkGenerator().getWorldChunkManager(), new GeneratorSettingsDefault());
        switch (world.getWorld().getEnvironment()) {
            case NORMAL:
                this.delegate = new ChunkProviderGenerate(world, world.worldProvider.getChunkGenerator().getWorldChunkManager(), new GeneratorSettingsOverworld());
                break;
            case NETHER:
                this.delegate = new ChunkProviderHell(world, world.worldProvider.getChunkGenerator().getWorldChunkManager(), new GeneratorSettingsNether());
                break;
            case THE_END:
                this.delegate = new ChunkProviderTheEnd(world, world.worldProvider.getChunkGenerator().getWorldChunkManager(), new GeneratorSettingsEnd());
                break;
            default:
                throw new AssertionError("Unknown delegate for environment " + world.getWorld().getEnvironment());
        }

        this.world = (WorldServer) world;
        this.generator = generator;
    }

    @Override
    public void createBiomes(IChunkAccess ichunkaccess) {
        // Don't allow the server to override any custom biomes that have been set
    }

    @Override
    public <C extends WorldGenFeatureConfiguration> C getFeatureConfiguration(BiomeBase biomebase, StructureGenerator<C> structuregenerator) {
        return (C) delegate.getFeatureConfiguration(biomebase, structuregenerator);
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return delegate.getWorldChunkManager();
    }

    @Override
    public void storeStructures(GeneratorAccess generatoraccess, IChunkAccess ichunkaccess) {
        delegate.storeStructures(generatoraccess, ichunkaccess);
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public void buildBase(RegionLimitedWorldAccess regionlimitedworldaccess, IChunkAccess ichunkaccess) {
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new BiomeStorage(ichunkaccess.getPos(), this.getWorldChunkManager()));

        ChunkData data;
        if (generator.isParallelCapable()) {
            data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
        } else {
            synchronized (this) {
                data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
            }
        }

        Preconditions.checkArgument(data instanceof CraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        CraftChunkData craftData = (CraftChunkData) data;
        ChunkSection[] sections = craftData.getRawChunkData();

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
        ((ProtoChunk) ichunkaccess).a(biomegrid.biome);

        if (craftData.getTiles() != null) {
            for (BlockPosition pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();
                Block block = craftData.getTypeId(tx, ty, tz).getBlock();

                if (block.isTileEntity()) {
                    TileEntity tile = ((ITileEntity) block).createTile(world);
                    ichunkaccess.setTileEntity(new BlockPosition((x << 4) + tx, ty, (z << 4) + tz), tile);
                }
            }
        }
    }

    @Override
    public void createStructures(BiomeManager biomemanager, IChunkAccess ichunkaccess, net.minecraft.server.ChunkGenerator<?> chunkgenerator, DefinedStructureManager definedstructuremanager) {
        if (generator.shouldGenerateStructures()) {
            // Still need a way of getting the biome of this chunk to pass to createStructures
            // Using default biomes for now.
            delegate.createStructures(biomemanager, ichunkaccess, chunkgenerator, definedstructuremanager);
        }
    }

    @Override
    public void doCarving(BiomeManager biomemanager, IChunkAccess ichunkaccess, WorldGenStage.Features worldgenstage_features) {
        if (generator.shouldGenerateCaves()) {
            delegate.doCarving(biomemanager, ichunkaccess, worldgenstage_features);
        }
    }

    @Override
    public void buildNoise(GeneratorAccess generatoraccess, IChunkAccess ichunkaccess) {
        // Disable vanilla generation
    }

    @Override
    public int getBaseHeight(int i, int j, HeightMap.Type heightmap_type) {
        return delegate.getBaseHeight(i, j, heightmap_type);
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        return delegate.getMobsFor(enumcreaturetype, blockposition);
    }

    @Override
    public void addDecorations(RegionLimitedWorldAccess regionlimitedworldaccess) {
        if (generator.shouldGenerateDecorations()) {
            delegate.addDecorations(regionlimitedworldaccess);
        }
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionlimitedworldaccess) {
        if (generator.shouldGenerateMobs()) {
            delegate.addMobs(regionlimitedworldaccess);
        }
    }

    @Override
    public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockposition, int i, boolean flag) {
        return delegate.findNearestMapFeature(world, s, blockposition, i, flag);
    }

    @Override
    public GeneratorSettingsDefault getSettings() {
        return delegate.getSettings();
    }

    @Override
    public void doMobSpawning(WorldServer worldserver, boolean flag, boolean flag1) {
        delegate.doMobSpawning(worldserver, flag, flag1);
    }

    @Override
    public boolean canSpawnStructure(BiomeBase biomebase, StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return delegate.canSpawnStructure(biomebase, structuregenerator);
    }

    @Override
    public long getSeed() {
        return delegate.getSeed();
    }

    @Override
    public int getSpawnHeight() {
        return delegate.getSpawnHeight();
    }

    @Override
    public int getGenerationDepth() {
        return delegate.getGenerationDepth();
    }
}
