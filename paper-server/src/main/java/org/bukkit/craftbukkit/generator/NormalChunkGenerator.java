package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkPosition;
import net.minecraft.server.EnumCreatureType;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IProgressUpdate;
import net.minecraft.server.World;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final IChunkProvider provider;

    public NormalChunkGenerator(World world, long seed) {
        provider = world.worldProvider.getChunkProvider();
    }

    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().worldProvider.canSpawn(x, z);
    }

    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    public boolean isChunkLoaded(int i, int i1) {
        return provider.isChunkLoaded(i, i1);
    }

    public Chunk getOrCreateChunk(int i, int i1) {
        return provider.getOrCreateChunk(i, i1);
    }

    public Chunk getChunkAt(int i, int i1) {
        return provider.getChunkAt(i, i1);
    }

    public void getChunkAt(IChunkProvider icp, int i, int i1) {
        provider.getChunkAt(icp, i, i1);
    }

    public boolean saveChunks(boolean bln, IProgressUpdate ipu) {
        return provider.saveChunks(bln, ipu);
    }

    public boolean unloadChunks() {
        return provider.unloadChunks();
    }

    public boolean canSave() {
        return provider.canSave();
    }

    public List<?> getMobsFor(EnumCreatureType ect, int i, int i1, int i2) {
        return provider.getMobsFor(ect, i, i1, i2);
    }

    public ChunkPosition findNearestMapFeature(World world, String string, int i, int i1, int i2) {
        return provider.findNearestMapFeature(world, string, i, i1, i2);
    }

    public void recreateStructures(int i, int j) {
        provider.recreateStructures(i, j);
    }

    // n.m.s implementations always return 0. (The true implementation is in ChunkProviderServer)
    public int getLoadedChunks() {
        return 0;
    }

    public String getName() {
        return "NormalWorldGenerator";
    }

    public void b() {}
}
