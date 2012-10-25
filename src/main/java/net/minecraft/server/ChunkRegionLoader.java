package net.minecraft.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChunkRegionLoader implements IAsyncChunkSaver, IChunkLoader {

    private List a = new ArrayList();
    private Set b = new HashSet();
    private Object c = new Object();
    private final File d;

    public ChunkRegionLoader(File file1) {
        this.d = file1;
    }

    public Chunk a(World world, int i, int j) {
        NBTTagCompound nbttagcompound = null;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
        Object object = this.c;

        synchronized (this.c) {
            if (this.b.contains(chunkcoordintpair)) {
                Iterator iterator = this.a.iterator();

                while (iterator.hasNext()) {
                    PendingChunkToSave pendingchunktosave = (PendingChunkToSave) iterator.next();

                    if (pendingchunktosave.a.equals(chunkcoordintpair)) {
                        nbttagcompound = pendingchunktosave.b;
                        break;
                    }
                }
            }
        }

        if (nbttagcompound == null) {
            DataInputStream datainputstream = RegionFileCache.c(this.d, i, j);

            if (datainputstream == null) {
                return null;
            }

            nbttagcompound = NBTCompressedStreamTools.a((DataInput) datainputstream);
        }

        return this.a(world, i, j, nbttagcompound);
    }

    protected Chunk a(World world, int i, int j, NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("Level")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
            return null;
        } else if (!nbttagcompound.getCompound("Level").hasKey("Sections")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing block data, skipping");
            return null;
        } else {
            Chunk chunk = this.a(world, nbttagcompound.getCompound("Level"));

            if (!chunk.a(i, j)) {
                System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.x + ", " + chunk.z + ")");
                nbttagcompound.getCompound("Level").setInt("xPos", i); // CraftBukkit - .getCompound("Level")
                nbttagcompound.getCompound("Level").setInt("zPos", j); // CraftBukkit - .getCompound("Level")
                chunk = this.a(world, nbttagcompound.getCompound("Level"));
            }

            return chunk;
        }
    }

    public void a(World world, Chunk chunk) {
        // CraftBukkit start - "handle" exception
        try {
            world.C();
        } catch (ExceptionWorldConflict ex) {
            ex.printStackTrace();
        }
        // CraftBukkit end

        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound.set("Level", nbttagcompound1);
            this.a(chunk, world, nbttagcompound1);
            this.a(chunk.l(), nbttagcompound);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void a(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) {
        Object object = this.c;

        synchronized (this.c) {
            if (this.b.contains(chunkcoordintpair)) {
                for (int i = 0; i < this.a.size(); ++i) {
                    if (((PendingChunkToSave) this.a.get(i)).a.equals(chunkcoordintpair)) {
                        this.a.set(i, new PendingChunkToSave(chunkcoordintpair, nbttagcompound));
                        return;
                    }
                }
            }

            this.a.add(new PendingChunkToSave(chunkcoordintpair, nbttagcompound));
            this.b.add(chunkcoordintpair);
            FileIOThread.a.a(this);
        }
    }

    public boolean c() {
        PendingChunkToSave pendingchunktosave = null;
        Object object = this.c;

        synchronized (this.c) {
            if (this.a.isEmpty()) {
                return false;
            }

            pendingchunktosave = (PendingChunkToSave) this.a.remove(0);
            this.b.remove(pendingchunktosave.a);
        }

        if (pendingchunktosave != null) {
            try {
                this.a(pendingchunktosave);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void a(PendingChunkToSave pendingchunktosave) throws java.io.IOException { // CraftBukkit - public -> private, added throws
        DataOutputStream dataoutputstream = RegionFileCache.d(this.d, pendingchunktosave.a.x, pendingchunktosave.a.z);

        NBTCompressedStreamTools.a(pendingchunktosave.b, (DataOutput) dataoutputstream);
        dataoutputstream.close();
    }

    public void b(World world, Chunk chunk) {}

    public void a() {}

    public void b() {}

    private void a(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("xPos", chunk.x);
        nbttagcompound.setInt("zPos", chunk.z);
        nbttagcompound.setLong("LastUpdate", world.getTime());
        nbttagcompound.setIntArray("HeightMap", chunk.heightMap);
        nbttagcompound.setBoolean("TerrainPopulated", chunk.done);
        ChunkSection[] achunksection = chunk.i();
        NBTTagList nbttaglist = new NBTTagList("Sections");
        ChunkSection[] achunksection1 = achunksection;
        int i = achunksection.length;

        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j) {
            ChunkSection chunksection = achunksection1[j];

            if (chunksection != null) {
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte) (chunksection.d() >> 4 & 255));
                nbttagcompound1.setByteArray("Blocks", chunksection.g());
                if (chunksection.i() != null) {
                    nbttagcompound1.setByteArray("Add", chunksection.i().a);
                }

                nbttagcompound1.setByteArray("Data", chunksection.j().a);
                nbttagcompound1.setByteArray("SkyLight", chunksection.l().a);
                nbttagcompound1.setByteArray("BlockLight", chunksection.k().a);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.set("Sections", nbttaglist);
        nbttagcompound.setByteArray("Biomes", chunk.m());
        chunk.m = false;
        NBTTagList nbttaglist1 = new NBTTagList();

        Iterator iterator;

        for (i = 0; i < chunk.entitySlices.length; ++i) {
            iterator = chunk.entitySlices[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                chunk.m = true;
                nbttagcompound1 = new NBTTagCompound();
                if (entity.c(nbttagcompound1)) {
                    nbttaglist1.add(nbttagcompound1);
                }
            }
        }

        nbttagcompound.set("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        iterator = chunk.tileEntities.values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            tileentity.b(nbttagcompound1);
            nbttaglist2.add(nbttagcompound1);
        }

        nbttagcompound.set("TileEntities", nbttaglist2);
        List list = world.a(chunk, false);

        if (list != null) {
            long k = world.getTime();
            NBTTagList nbttaglist3 = new NBTTagList();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator1.next();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();

                nbttagcompound2.setInt("i", nextticklistentry.d);
                nbttagcompound2.setInt("x", nextticklistentry.a);
                nbttagcompound2.setInt("y", nextticklistentry.b);
                nbttagcompound2.setInt("z", nextticklistentry.c);
                nbttagcompound2.setInt("t", (int) (nextticklistentry.e - k));
                nbttaglist3.add(nbttagcompound2);
            }

            nbttagcompound.set("TileTicks", nbttaglist3);
        }
    }

    private Chunk a(World world, NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInt("xPos");
        int j = nbttagcompound.getInt("zPos");
        Chunk chunk = new Chunk(world, i, j);

        chunk.heightMap = nbttagcompound.getIntArray("HeightMap");
        chunk.done = nbttagcompound.getBoolean("TerrainPopulated");
        NBTTagList nbttaglist = nbttagcompound.getList("Sections");
        byte b0 = 16;
        ChunkSection[] achunksection = new ChunkSection[b0];

        for (int k = 0; k < nbttaglist.size(); ++k) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(k);
            byte b1 = nbttagcompound1.getByte("Y");
            ChunkSection chunksection = new ChunkSection(b1 << 4);

            chunksection.a(nbttagcompound1.getByteArray("Blocks"));
            if (nbttagcompound1.hasKey("Add")) {
                chunksection.a(new NibbleArray(nbttagcompound1.getByteArray("Add"), 4));
            }

            chunksection.b(new NibbleArray(nbttagcompound1.getByteArray("Data"), 4));
            chunksection.d(new NibbleArray(nbttagcompound1.getByteArray("SkyLight"), 4));
            chunksection.c(new NibbleArray(nbttagcompound1.getByteArray("BlockLight"), 4));
            chunksection.recalcBlockCounts();
            achunksection[b1] = chunksection;
        }

        chunk.a(achunksection);
        if (nbttagcompound.hasKey("Biomes")) {
            chunk.a(nbttagcompound.getByteArray("Biomes"));
        }

        NBTTagList nbttaglist1 = nbttagcompound.getList("Entities");

        if (nbttaglist1 != null) {
            for (int l = 0; l < nbttaglist1.size(); ++l) {
                NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist1.get(l);
                Entity entity = EntityTypes.a(nbttagcompound2, world);

                chunk.m = true;
                if (entity != null) {
                    chunk.a(entity);
                }
            }
        }

        NBTTagList nbttaglist2 = nbttagcompound.getList("TileEntities");

        if (nbttaglist2 != null) {
            for (int i1 = 0; i1 < nbttaglist2.size(); ++i1) {
                NBTTagCompound nbttagcompound3 = (NBTTagCompound) nbttaglist2.get(i1);
                TileEntity tileentity = TileEntity.c(nbttagcompound3);

                if (tileentity != null) {
                    chunk.a(tileentity);
                }
            }
        }

        if (nbttagcompound.hasKey("TileTicks")) {
            NBTTagList nbttaglist3 = nbttagcompound.getList("TileTicks");

            if (nbttaglist3 != null) {
                for (int j1 = 0; j1 < nbttaglist3.size(); ++j1) {
                    NBTTagCompound nbttagcompound4 = (NBTTagCompound) nbttaglist3.get(j1);

                    world.b(nbttagcompound4.getInt("x"), nbttagcompound4.getInt("y"), nbttagcompound4.getInt("z"), nbttagcompound4.getInt("i"), nbttagcompound4.getInt("t"));
                }
            }
        }

        return chunk;
    }
}
