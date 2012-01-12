package org.bukkit.craftbukkit.generator;

import java.util.List;
import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkPosition;
import net.minecraft.server.EnumCreatureType;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IProgressUpdate;
import net.minecraft.server.World;
import net.minecraft.server.WorldChunkManager;
import net.minecraft.server.WorldGenStronghold;
import net.minecraft.server.WorldServer;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class CustomChunkGenerator extends InternalChunkGenerator {
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random;
    private final WorldGenStronghold strongholdGen = new WorldGenStronghold();

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        this.world = (WorldServer) world;
        this.generator = generator;

        this.random = new Random(seed);
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public Chunk getOrCreateChunk(int x, int z) {
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        byte[] types = generator.generate(world.getWorld(), random, x, z);

        Chunk chunk = new Chunk(world, types, x, z);

        chunk.initLighting();

        return chunk;
    }

    public void getChunkAt(IChunkProvider icp, int i, int i1) {
        // Nothing!
    }

    public boolean saveChunks(boolean bln, IProgressUpdate ipu) {
        return true;
    }

    public boolean unloadChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return generator.generate(world, random, x, z);
    }

    public Chunk getChunkAt(int x, int z) {
        return getOrCreateChunk(x, z);
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return generator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return generator.getDefaultPopulators(world);
    }

    public List getMobsFor(EnumCreatureType type, int x, int y, int z) {
        WorldChunkManager worldchunkmanager = world.getWorldChunkManager();

        if (worldchunkmanager == null) {
            return null;
        } else {
            BiomeBase biomebase = worldchunkmanager.getBiome(new ChunkCoordIntPair(x >> 4, z >> 4));

            return biomebase == null ? null : biomebase.getMobs(type);
        }
    }

    public ChunkPosition findNearestMapFeature(World world, String type, int x, int y, int z) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.getNearestGeneratedFeature(world, x, y, z) : null;
    }
}
