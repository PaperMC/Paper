package org.bukkit.craftbukkit.generator;

import java.util.List;
import java.util.Random;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkPosition;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.EnumCreatureType;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IProgressUpdate;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenStronghold;
import net.minecraft.server.WorldServer;

import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.block.CraftBlock;

public class CustomChunkGenerator extends InternalChunkGenerator {
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random;
    private final WorldGenStronghold strongholdGen = new WorldGenStronghold();

    private static class CustomBiomeGrid implements BiomeGrid {
        BiomeBase[] biome;

        public Biome getBiome(int x, int z) {
            return CraftBlock.biomeBaseToBiome(biome[(z << 4) | x]);
        }

        public void setBiome(int x, int z, Biome bio) {
           biome[(z << 4) | x] = CraftBlock.biomeToBiomeBase(bio);
        }
    }

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

        Chunk chunk;

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = new BiomeBase[256];
        world.getWorldChunkManager().getBiomeBlock(biomegrid.biome, x << 4, z << 4, 16, 16);

        // Try extended block method (1.2+)
        short[][] xbtypes = generator.generateExtBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
        if (xbtypes != null) {
            chunk = new Chunk(this.world, x, z);

            ChunkSection[] csect = chunk.i();
            int scnt = Math.min(csect.length, xbtypes.length);

            // Loop through returned sections
            for (int sec = 0; sec < scnt; sec++) {
                if (xbtypes[sec] == null) {
                    continue;
                }
                byte[] secBlkID = new byte[4096]; // Allocate blk ID bytes
                byte[] secExtBlkID = null; // Delay getting extended ID nibbles
                short[] bdata = xbtypes[sec];
                // Loop through data, 2 blocks at a time
                for (int i = 0, j = 0; i < bdata.length; i += 2, j++) {
                    short b1 = bdata[i];
                    short b2 = bdata[i + 1];
                    byte extb = (byte) ((b1 >> 8) | ((b2 >> 4) & 0xF0));

                    secBlkID[i] = (byte) b1;
                    secBlkID[(i + 1)] = (byte) b2;

                    if (extb != 0) { // If extended block ID data
                        if (secExtBlkID == null) { // Allocate if needed
                            secExtBlkID = new byte[2048];
                        }
                        secExtBlkID[j] = extb;
                    }
                }
                // Build chunk section
                csect[sec] = new ChunkSection(sec << 4, true, secBlkID, secExtBlkID);
            }
        }
        else { // Else check for byte-per-block section data
            byte[][] btypes = generator.generateBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);

            if (btypes != null) {
                chunk = new Chunk(this.world, x, z);

                ChunkSection[] csect = chunk.i();
                int scnt = Math.min(csect.length, btypes.length);

                for (int sec = 0; sec < scnt; sec++) {
                    if (btypes[sec] == null) {
                        continue;
                    }
                    csect[sec] = new ChunkSection(sec << 4, true, btypes[sec], null);
                }
            }
            else { // Else, fall back to pre 1.2 method
                @SuppressWarnings("deprecation")
                byte[] types = generator.generate(this.world.getWorld(), this.random, x, z);
                int ydim = types.length / 256;
                int scnt = ydim / 16;

                chunk = new Chunk(this.world, x, z); // Create empty chunk

                ChunkSection[] csect = chunk.i();

                scnt = Math.min(scnt, csect.length);
                // Loop through sections
                for (int sec = 0; sec < scnt; sec++) {
                    ChunkSection cs = null; // Add sections when needed
                    byte[] csbytes = null;

                    for (int cy = 0; cy < 16; cy++) {
                        int cyoff = cy | (sec << 4);

                        for (int cx = 0; cx < 16; cx++) {
                            int cxyoff = (cx * ydim * 16) + cyoff;

                            for (int cz = 0; cz < 16; cz++) {
                                byte blk = types[cxyoff + (cz * ydim)];

                                if (blk != 0) { // If non-empty
                                    if (cs == null) { // If no section yet, get one
                                        cs = csect[sec] = new ChunkSection(sec << 4, true);
                                        csbytes = cs.getIdArray();
                                    }
                                    csbytes[(cy << 8) | (cz << 4) | cx] = blk;
                                }
                            }
                        }
                    }
                    // If section built, finish prepping its state
                    if (cs != null) {
                        cs.recalcBlockCounts();
                    }
                }
            }
        }
        // Set biome grid
        byte[] biomeIndex = chunk.m();
        for (int i = 0; i < biomeIndex.length; i++) {
            biomeIndex[i] = (byte) (biomegrid.biome[i].id & 0xFF);
        }
        // Initialize lighting
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

    @SuppressWarnings("deprecation")
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return generator.generate(world, random, x, z);
    }

    public byte[][] generateBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateBlockSections(world, random, x, z, biomes);
    }

    public short[][] generateExtBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateExtBlockSections(world, random, x, z, biomes);
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

    public List<?> getMobsFor(EnumCreatureType type, int x, int y, int z) {
        BiomeBase biomebase = world.getBiome(x, z);

        return biomebase == null ? null : biomebase.getMobs(type);
    }

    public ChunkPosition findNearestMapFeature(World world, String type, int x, int y, int z) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.getNearestGeneratedFeature(world, x, y, z) : null;
    }

    public void recreateStructures(int i, int j) {}

    public int getLoadedChunks() {
        return 0;
    }

    public String getName() {
        return "CustomChunkGenerator";
    }

    public void b() {}
}
