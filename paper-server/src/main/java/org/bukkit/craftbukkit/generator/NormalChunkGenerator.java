package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.*;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final IChunkProvider provider;

    public NormalChunkGenerator(World world, long seed) {
        provider = world.worldProvider.getChunkProvider();
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
    public boolean isChunkLoaded(int i, int i1) {
        return provider.isChunkLoaded(i, i1);
    }

    @Override
    public Chunk getOrCreateChunk(int i, int i1) {
        return provider.getOrCreateChunk(i, i1);
    }

    @Override
    public Chunk getChunkAt(BlockPosition blockPosition) {
        return provider.getChunkAt(blockPosition);
    }

    @Override
    public void getChunkAt(IChunkProvider icp, int i, int i1) {
        provider.getChunkAt(icp, i, i1);
    }

    @Override
    public boolean a(IChunkProvider iChunkProvider, Chunk chunk, int i, int i1) {
        return provider.a(provider, chunk, i, i1);
    }

    @Override
    public boolean saveChunks(boolean bln, IProgressUpdate ipu) {
        return provider.saveChunks(bln, ipu);
    }

    @Override
    public boolean unloadChunks() {
        return provider.unloadChunks();
    }

    @Override
    public boolean canSave() {
        return provider.canSave();
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType ect, BlockPosition position) {
        return provider.getMobsFor(ect, position);
    }

    @Override
    public BlockPosition findNearestMapFeature(World world, String string, BlockPosition position) {
        return provider.findNearestMapFeature(world, string, position);
    }

    // n.m.s implementations always return 0. (The true implementation is in ChunkProviderServer)
    @Override
    public int getLoadedChunks() {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk chunk, int i, int i1) {
        provider.recreateStructures(chunk, i, i1);
    }

    @Override
    public String getName() {
        return "NormalWorldGenerator";
    }

    @Override
    public void c() {}
}
