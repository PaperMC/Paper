package net.minecraft.server;

public class OldChunkLoader {

    public OldChunkLoader() {}

    public static OldChunk a(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInt("xPos");
        int j = nbttagcompound.getInt("zPos");
        OldChunk oldchunk = new OldChunk(i, j);

        oldchunk.g = nbttagcompound.getByteArray("Blocks");
        oldchunk.f = new OldNibbleArray(nbttagcompound.getByteArray("Data"), 7);
        oldchunk.e = new OldNibbleArray(nbttagcompound.getByteArray("SkyLight"), 7);
        oldchunk.d = new OldNibbleArray(nbttagcompound.getByteArray("BlockLight"), 7);
        oldchunk.c = nbttagcompound.getByteArray("HeightMap");
        oldchunk.b = nbttagcompound.getBoolean("TerrainPopulated");
        oldchunk.h = nbttagcompound.getList("Entities");
        oldchunk.i = nbttagcompound.getList("TileEntities");
        oldchunk.j = nbttagcompound.getList("TileTicks");

        // CraftBukkit start
        try {
            oldchunk.a = nbttagcompound.getLong("LastUpdate");
        } catch (ClassCastException ex) {
            oldchunk.a = nbttagcompound.getInt("LastUpdate");
        }
        // CraftBukkit end

        return oldchunk;
    }

    public static void a(OldChunk oldchunk, NBTTagCompound nbttagcompound, WorldChunkManager worldchunkmanager) {
        nbttagcompound.setInt("xPos", oldchunk.k);
        nbttagcompound.setInt("zPos", oldchunk.l);
        nbttagcompound.setLong("LastUpdate", oldchunk.a);
        int[] aint = new int[oldchunk.c.length];

        for (int i = 0; i < oldchunk.c.length; ++i) {
            aint[i] = oldchunk.c[i];
        }

        nbttagcompound.setIntArray("HeightMap", aint);
        nbttagcompound.setBoolean("TerrainPopulated", oldchunk.b);
        NBTTagList nbttaglist = new NBTTagList("Sections");

        int j;

        for (int k = 0; k < 8; ++k) {
            boolean flag = true;

            for (j = 0; j < 16 && flag; ++j) {
                int l = 0;

                while (l < 16 && flag) {
                    int i1 = 0;

                    while (true) {
                        if (i1 < 16) {
                            int j1 = j << 11 | i1 << 7 | l + (k << 4);
                            byte b0 = oldchunk.g[j1];

                            if (b0 == 0) {
                                ++i1;
                                continue;
                            }

                            flag = false;
                        }

                        ++l;
                        break;
                    }
                }
            }

            if (!flag) {
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray(abyte.length, 4);
                NibbleArray nibblearray1 = new NibbleArray(abyte.length, 4);
                NibbleArray nibblearray2 = new NibbleArray(abyte.length, 4);

                for (int k1 = 0; k1 < 16; ++k1) {
                    for (int l1 = 0; l1 < 16; ++l1) {
                        for (int i2 = 0; i2 < 16; ++i2) {
                            int j2 = k1 << 11 | i2 << 7 | l1 + (k << 4);
                            byte b1 = oldchunk.g[j2];

                            abyte[l1 << 8 | i2 << 4 | k1] = (byte) (b1 & 255);
                            nibblearray.a(k1, l1, i2, oldchunk.f.a(k1, l1 + (k << 4), i2));
                            nibblearray1.a(k1, l1, i2, oldchunk.e.a(k1, l1 + (k << 4), i2));
                            nibblearray2.a(k1, l1, i2, oldchunk.d.a(k1, l1 + (k << 4), i2));
                        }
                    }
                }

                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Y", (byte) (k & 255));
                nbttagcompound1.setByteArray("Blocks", abyte);
                nbttagcompound1.setByteArray("Data", nibblearray.a);
                nbttagcompound1.setByteArray("SkyLight", nibblearray1.a);
                nbttagcompound1.setByteArray("BlockLight", nibblearray2.a);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.set("Sections", nbttaglist);
        byte[] abyte1 = new byte[256];

        for (int k2 = 0; k2 < 16; ++k2) {
            for (j = 0; j < 16; ++j) {
                abyte1[j << 4 | k2] = (byte) (worldchunkmanager.getBiome(oldchunk.k << 4 | k2, oldchunk.l << 4 | j).id & 255);
            }
        }

        nbttagcompound.setByteArray("Biomes", abyte1);
        nbttagcompound.set("Entities", oldchunk.h);
        nbttagcompound.set("TileEntities", oldchunk.i);
        if (oldchunk.j != null) {
            nbttagcompound.set("TileTicks", oldchunk.j);
        }
    }
}
