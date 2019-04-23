package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.EnumCreatureType;
import net.minecraft.server.GeneratorAccess;
import net.minecraft.server.GeneratorSettingsDefault;
import net.minecraft.server.HeightMap;
import net.minecraft.server.IChunkAccess;
import net.minecraft.server.ITileEntity;
import net.minecraft.server.RegionLimitedWorldAccess;
import net.minecraft.server.StructureGenerator;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenFeatureConfiguration;
import net.minecraft.server.WorldGenStage;
import net.minecraft.server.WorldGenerator;
import net.minecraft.server.WorldServer;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator<GeneratorSettingsDefault> {
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final long seed;
    private final Random random;
    private final StructureGenerator strongholdGen = WorldGenerator.STRONGHOLD;

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
        super(world, world.worldProvider.getChunkGenerator().getWorldChunkManager(), new GeneratorSettingsDefault());
        this.world = (WorldServer) world;
        this.generator = generator;
        this.seed = seed;

        this.random = new Random(seed);
    }

    @Override
    public void buildBase(IChunkAccess ichunkaccess) {
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = this.getWorldChunkManager().getBiomeBlock(x << 4, z << 4, 16, 16);

        ChunkData data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
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
        ichunkaccess.a(biomegrid.biome);

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
    public void doCarving(IChunkAccess ichunkaccess, WorldGenStage.Features worldgenstage_features) {
    }

    @Override
    public void buildNoise(GeneratorAccess generatoraccess, IChunkAccess ichunkaccess) {
    }

    @Override
    public int getBaseHeight(int i, int j, HeightMap.Type heightmap_type) {
        return 0;
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType type, BlockPosition position) {
        BiomeBase biomebase = world.getBiome(position);

        return biomebase == null ? null : biomebase.getMobs(type);
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
    public void doMobSpawning(WorldServer worldserver, boolean flag, boolean flag1) {
    }

    @Override
    public boolean canSpawnStructure(BiomeBase biomebase, StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return biomebase.a(structuregenerator);
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
