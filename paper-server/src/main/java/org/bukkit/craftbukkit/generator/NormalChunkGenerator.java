package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.*;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final ChunkGenerator generator;

    public NormalChunkGenerator(World world, long seed) {
        generator = world.worldProvider.getChunkGenerator();
    }

    @Override
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().worldProvider.canSpawn(x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public Chunk getOrCreateChunk(int i, int i1) {
        return generator.getOrCreateChunk(i, i1);
    }

    @Override
    public void recreateStructures(int i, int i1) {
        generator.recreateStructures(i, i1);
    }

    @Override
    public boolean a(Chunk chunk, int i, int i1) {
        return generator.a(chunk, i, i1);
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumCreatureType, BlockPosition blockPosition) {
        return generator.getMobsFor(enumCreatureType, blockPosition);
    }

    @Override
    public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockPosition, boolean flag) {
        return generator.findNearestMapFeature(world, s, blockPosition, flag);
    }

    @Override
    public void recreateStructures(Chunk chunk, int i, int i1) {
        generator.recreateStructures(chunk, i, i1);
    }

    @Override
    public boolean a(World world, String string, BlockPosition bp) {
        return generator.a(world, string, bp);
    }
}
