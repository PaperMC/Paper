package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class ChunkLoader implements IChunkLoader {

    private File a;
    private boolean b;

    public ChunkLoader(File file1, boolean flag) {
        this.a = file1;
        this.b = flag;
    }

    private File a(int i, int j) {
        String s = "c." + Integer.toString(i, 36) + "." + Integer.toString(j, 36) + ".dat";
        String s1 = Integer.toString(i & 63, 36);
        String s2 = Integer.toString(j & 63, 36);
        File file1 = new File(this.a, s1);

        if (!file1.exists()) {
            if (!this.b) {
                return null;
            }

            file1.mkdir();
        }

        file1 = new File(file1, s2);
        if (!file1.exists()) {
            if (!this.b) {
                return null;
            }

            file1.mkdir();
        }

        file1 = new File(file1, s);
        return !file1.exists() && !this.b ? null : file1;
    }

    public Chunk a(World world, int i, int j) {
        File file1 = this.a(i, j);

        if (file1 != null && file1.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file1);
                NBTTagCompound nbttagcompound = NBTCompressedStreamTools.a((InputStream) fileinputstream);

                if (!nbttagcompound.hasKey("Level")) {
                    System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
                    return null;
                }

                if (!nbttagcompound.getCompound("Level").hasKey("Blocks")) {
                    System.out.println("Chunk file at " + i + "," + j + " is missing block data, skipping");
                    return null;
                }

                Chunk chunk = a(world, nbttagcompound.getCompound("Level"));

                if (!chunk.a(i, j)) {
                    System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.x + ", " + chunk.z + ")");
                    nbttagcompound.setInt("xPos", i);
                    nbttagcompound.setInt("zPos", j);
                    chunk = a(world, nbttagcompound.getCompound("Level"));
                }

                chunk.h();
                return chunk;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }

    public void a(World world, Chunk chunk) {
        world.l();
        File file1 = this.a(chunk.x, chunk.z);

        if (file1.exists()) {
            WorldData worlddata = world.getWorldData();

            worlddata.b(worlddata.g() - file1.length());
        }

        try {
            File file2 = new File(this.a, "tmp_chunk.dat");
            FileOutputStream fileoutputstream = new FileOutputStream(file2);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound.set("Level", nbttagcompound1);
            a(chunk, world, nbttagcompound1);
            NBTCompressedStreamTools.a(nbttagcompound, (OutputStream) fileoutputstream);
            fileoutputstream.close();
            if (file1.exists()) {
                file1.delete();
            }

            file2.renameTo(file1);
            WorldData worlddata1 = world.getWorldData();

            worlddata1.b(worlddata1.g() + file1.length());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void a(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
        world.l();
        nbttagcompound.setInt("xPos", chunk.x);
        nbttagcompound.setInt("zPos", chunk.z);
        nbttagcompound.setLong("LastUpdate", world.getTime());
        nbttagcompound.setByteArray("Blocks", chunk.b);
        nbttagcompound.setByteArray("Data", chunk.g.a);
        nbttagcompound.setByteArray("SkyLight", chunk.h.a);
        nbttagcompound.setByteArray("BlockLight", chunk.i.a);
        nbttagcompound.setByteArray("HeightMap", chunk.heightMap);
        nbttagcompound.setBoolean("TerrainPopulated", chunk.done);
        chunk.s = false;
        NBTTagList nbttaglist = new NBTTagList();

        Iterator iterator;
        NBTTagCompound nbttagcompound1;

        for (int i = 0; i < chunk.entitySlices.length; ++i) {
            iterator = chunk.entitySlices[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                chunk.s = true;
                nbttagcompound1 = new NBTTagCompound();
                if (entity.c(nbttagcompound1)) {
                    nbttaglist.add(nbttagcompound1);
                }
            }
        }

        nbttagcompound.set("Entities", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        iterator = chunk.tileEntities.values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            tileentity.b(nbttagcompound1);
            nbttaglist1.add(nbttagcompound1);
        }

        nbttagcompound.set("TileEntities", nbttaglist1);
        List list = world.a(chunk, false);

        if (list != null) {
            long j = world.getTime();
            NBTTagList nbttaglist2 = new NBTTagList();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator1.next();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();

                nbttagcompound2.setInt("i", nextticklistentry.d);
                nbttagcompound2.setInt("x", nextticklistentry.a);
                nbttagcompound2.setInt("y", nextticklistentry.b);
                nbttagcompound2.setInt("z", nextticklistentry.c);
                nbttagcompound2.setInt("t", (int) (nextticklistentry.e - j));
                nbttaglist2.add(nbttagcompound2);
            }

            nbttagcompound.set("TileTicks", nbttaglist2);
        }
    }

    public static Chunk a(World world, NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInt("xPos");
        int j = nbttagcompound.getInt("zPos");
        Chunk chunk = new Chunk(world, i, j);

        chunk.b = nbttagcompound.getByteArray("Blocks");
        chunk.g = new NibbleArray(nbttagcompound.getByteArray("Data"), world.heightBits);
        chunk.h = new NibbleArray(nbttagcompound.getByteArray("SkyLight"), world.heightBits);
        chunk.i = new NibbleArray(nbttagcompound.getByteArray("BlockLight"), world.heightBits);
        chunk.heightMap = nbttagcompound.getByteArray("HeightMap");
        chunk.done = nbttagcompound.getBoolean("TerrainPopulated");
        if (!chunk.g.a()) {
            chunk.g = new NibbleArray(chunk.b.length, world.heightBits);
        }

        if (chunk.heightMap == null || !chunk.h.a()) {
            chunk.heightMap = new byte[256];
            chunk.h = new NibbleArray(chunk.b.length, world.heightBits);
            chunk.initLighting();
        }

        if (!chunk.i.a()) {
            chunk.i = new NibbleArray(chunk.b.length, world.heightBits);
            chunk.a();
        }

        NBTTagList nbttaglist = nbttagcompound.getList("Entities");

        if (nbttaglist != null) {
            for (int k = 0; k < nbttaglist.size(); ++k) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(k);
                Entity entity = EntityTypes.a(nbttagcompound1, world);

                chunk.s = true;
                if (entity != null) {
                    chunk.a(entity);
                }
            }
        }

        NBTTagList nbttaglist1 = nbttagcompound.getList("TileEntities");

        if (nbttaglist1 != null) {
            for (int l = 0; l < nbttaglist1.size(); ++l) {
                NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist1.get(l);
                TileEntity tileentity = TileEntity.c(nbttagcompound2);

                if (tileentity != null) {
                    chunk.a(tileentity);
                }
            }
        }

        if (nbttagcompound.hasKey("TileTicks")) {
            NBTTagList nbttaglist2 = nbttagcompound.getList("TileTicks");

            if (nbttaglist2 != null) {
                for (int i1 = 0; i1 < nbttaglist2.size(); ++i1) {
                    NBTTagCompound nbttagcompound3 = (NBTTagCompound) nbttaglist2.get(i1);

                    world.d(nbttagcompound3.getInt("x"), nbttagcompound3.getInt("y"), nbttagcompound3.getInt("z"), nbttagcompound3.getInt("i"), nbttagcompound3.getInt("t"));
                }
            }
        }

        return chunk;
    }

    public void a() {}

    public void b() {}

    public void b(World world, Chunk chunk) {}
}
