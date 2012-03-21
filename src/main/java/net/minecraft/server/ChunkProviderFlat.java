package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class ChunkProviderFlat implements IChunkProvider {

    private World a;
    private Random b;
    private final boolean c;
    private WorldGenVillage d = new WorldGenVillage(1);

    public ChunkProviderFlat(World world, long i, boolean flag) {
        this.a = world;
        this.c = flag;
        this.b = new Random(i);
    }

    private void a(byte[] abyte) {
        int i = abyte.length / 256;

        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < i; ++l) {
                    int i1 = 0;

                    if (l == 0) {
                        i1 = Block.BEDROCK.id;
                    } else if (l <= 2) {
                        i1 = Block.DIRT.id;
                    } else if (l == 3) {
                        i1 = Block.GRASS.id;
                    }

                    abyte[j << 11 | k << 7 | l] = (byte) i1;
                }
            }
        }
    }

    public Chunk getChunkAt(int i, int j) {
        return this.getOrCreateChunk(i, j);
    }

    public Chunk getOrCreateChunk(int i, int j) {
        byte[] abyte = new byte['\u8000'];

        this.a(abyte);
        Chunk chunk = new Chunk(this.a, abyte, i, j);

        if (this.c) {
            this.d.a(this, this.a, i, j, abyte);
        }
        // CraftBukkit start - prime biome data to prevent uninitialized values racing to client
        BiomeBase[] bb = this.a.getWorldChunkManager().getBiomeBlock(null, i * 16, j * 16, 16, 16);
        byte[] biomes = chunk.l();
        for(int idx = 0; idx < biomes.length; idx++) {
            biomes[idx] = (byte) bb[idx].id;
        }
        // CraftBukkit end

        chunk.initLighting();
        return chunk;
    }

    public boolean isChunkLoaded(int i, int j) {
        return true;
    }

    public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {
        this.b.setSeed(this.a.getSeed());
        long k = this.b.nextLong() / 2L * 2L + 1L;
        long l = this.b.nextLong() / 2L * 2L + 1L;

        this.b.setSeed((long) i * k + (long) j * l ^ this.a.getSeed());
        if (this.c) {
            this.d.a(this.a, this.b, i, j);
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        return true;
    }

    public boolean unloadChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public List getMobsFor(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        BiomeBase biomebase = this.a.getBiome(i, k);

        return biomebase == null ? null : biomebase.getMobs(enumcreaturetype);
    }

    public ChunkPosition findNearestMapFeature(World world, String s, int i, int j, int k) {
        return null;
    }
}
