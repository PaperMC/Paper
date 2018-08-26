package org.bukkit.craftbukkit.generator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.*;

import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator<C extends GeneratorSettings> extends InternalChunkGenerator<C> {
    private final ChunkGenerator<?> generator;

    public NormalChunkGenerator(World world, long seed) {
        generator = world.worldProvider.getChunkGenerator();
    }

    @Override
    public ChunkData generateChunkData(org.bukkit.World world, Random random, int x, int z, BiomeGrid biome) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return true; // PAIL
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumCreatureType, BlockPosition blockPosition) {
        return generator.getMobsFor(enumCreatureType, blockPosition);
    }

    @Override
    public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockPosition, int i, boolean flag) {
        return generator.findNearestMapFeature(world, s, blockPosition, i, flag);
    }

    @Override
    public void createChunk(IChunkAccess ichunkaccess) {
        generator.createChunk(ichunkaccess);
    }

    @Override
    public void addFeatures(RegionLimitedWorldAccess regionlimitedworldaccess, WorldGenStage.Features worldgenstage_features) {
        generator.addFeatures(regionlimitedworldaccess, worldgenstage_features);
    }

    @Override
    public void addDecorations(RegionLimitedWorldAccess regionlimitedworldaccess) {
        generator.addDecorations(regionlimitedworldaccess);
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionlimitedworldaccess) {
        generator.addMobs(regionlimitedworldaccess);
    }

    @Override
    public C getSettings() {
        return (C) generator.getSettings();
    }

    @Override
    public int a(World world, boolean flag, boolean flag1) {
        return generator.a(world, flag, flag1);
    }

    @Override
    public boolean canSpawnStructure(BiomeBase biomebase, StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return generator.canSpawnStructure(biomebase, structuregenerator);
    }

    @Override
    public WorldGenFeatureConfiguration getFeatureConfiguration(BiomeBase biomebase, StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return generator.getFeatureConfiguration(biomebase, structuregenerator);
    }

    @Override
    public Long2ObjectMap<StructureStart> getStructureStartCache(StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return generator.getStructureStartCache(structuregenerator);
    }

    @Override
    public Long2ObjectMap<LongSet> getStructureCache(StructureGenerator<? extends WorldGenFeatureConfiguration> structuregenerator) {
        return generator.getStructureCache(structuregenerator);
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return generator.getWorldChunkManager();
    }

    @Override
    public long getSeed() {
        return generator.getSeed();
    }

    @Override
    public int getSpawnHeight() {
        return generator.getSpawnHeight();
    }

    @Override
    public int e() {
        return generator.e(); // PAIL: Gen depth
    }
}
