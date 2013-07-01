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

    // CraftBukkit start
    public boolean chunkExists(World world, int i, int j) {
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);

        synchronized (this.c) {
            if (this.b.contains(chunkcoordintpair)) {
                for (int k = 0; k < this.a.size(); ++k) {
                    if (((PendingChunkToSave) this.a.get(k)).a.equals(chunkcoordintpair)) {
                        return true;
                    }
                }
            }
        }

        return RegionFileCache.a(this.d, i, j).chunkExists(i & 31, j & 31);
    }
    // CraftBukkit end

    // CraftBukkit start - Add async variant, provide compatibility
    public Chunk a(World world, int i, int j) {
        Object[] data = this.loadChunk(world, i, j);
        if (data != null) {
            Chunk chunk = (Chunk) data[0];
            NBTTagCompound nbttagcompound = (NBTTagCompound) data[1];
            this.loadEntities(chunk, nbttagcompound.getCompound("Level"), world);
            return chunk;
        }

        return null;
    }

    public Object[] loadChunk(World world, int i, int j) {
        // CraftBukkit end
        NBTTagCompound nbttagcompound = null;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
        Object object = this.c;

        synchronized (this.c) {
            if (this.b.contains(chunkcoordintpair)) {
                for (int k = 0; k < this.a.size(); ++k) {
                    if (((PendingChunkToSave) this.a.get(k)).a.equals(chunkcoordintpair)) {
                        nbttagcompound = ((PendingChunkToSave) this.a.get(k)).b;
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

    protected Object[] a(World world, int i, int j, NBTTagCompound nbttagcompound) { // CraftBukkit - return Chunk -> Object[]
        if (!nbttagcompound.hasKey("Level")) {
            world.getLogger().severe("Chunk file at " + i + "," + j + " is missing level data, skipping");
            return null;
        } else if (!nbttagcompound.getCompound("Level").hasKey("Sections")) {
            world.getLogger().severe("Chunk file at " + i + "," + j + " is missing block data, skipping");
            return null;
        } else {
            Chunk chunk = this.a(world, nbttagcompound.getCompound("Level"));

            if (!chunk.a(i, j)) {
                world.getLogger().severe("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.x + ", " + chunk.z + ")");
                nbttagcompound.getCompound("Level").setInt("xPos", i); // CraftBukkit - .getCompound("Level")
                nbttagcompound.getCompound("Level").setInt("zPos", j); // CraftBukkit - .getCompound("Level")

                // CraftBukkit start - Have to move tile entities since we don't load them at this stage
                NBTTagList tileEntities = nbttagcompound.getCompound("Level").getList("TileEntities");
                if (tileEntities != null) {
                    for (int te = 0; te < tileEntities.size(); te++) {
                        NBTTagCompound tileEntity = (NBTTagCompound) tileEntities.get(te);
                        int x = tileEntity.getInt("x") - chunk.x * 16;
                        int z = tileEntity.getInt("z") - chunk.z * 16;
                        tileEntity.setInt("x", i * 16 + x);
                        tileEntity.setInt("z", j * 16 + z);
                    }
                }
                // CraftBukkit end

                chunk = this.a(world, nbttagcompound.getCompound("Level"));
            }

            // CraftBukkit start
            Object[] data = new Object[2];
            data[0] = chunk;
            data[1] = nbttagcompound;
            return data;
            // CraftBukkit end
        }
    }

    public void a(World world, Chunk chunk) {
        // CraftBukkit start - "handle" exception
        try {
            world.G();
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

    public void b() {
        while (this.c()) {
            ;
        }
    }

    private void a(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("xPos", chunk.x);
        nbttagcompound.setInt("zPos", chunk.z);
        nbttagcompound.setLong("LastUpdate", world.getTime());
        nbttagcompound.setIntArray("HeightMap", chunk.heightMap);
        nbttagcompound.setBoolean("TerrainPopulated", chunk.done);
        nbttagcompound.setLong("InhabitedTime", chunk.q);
        ChunkSection[] achunksection = chunk.i();
        NBTTagList nbttaglist = new NBTTagList("Sections");
        boolean flag = !world.worldProvider.g;
        ChunkSection[] achunksection1 = achunksection;
        int i = achunksection.length;

        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j) {
            ChunkSection chunksection = achunksection1[j];

            if (chunksection != null) {
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte) (chunksection.getYPosition() >> 4 & 255));
                nbttagcompound1.setByteArray("Blocks", chunksection.getIdArray());
                if (chunksection.getExtendedIdArray() != null) {
                    nbttagcompound1.setByteArray("Add", chunksection.getExtendedIdArray().a);
                }

                nbttagcompound1.setByteArray("Data", chunksection.getDataArray().a);
                nbttagcompound1.setByteArray("BlockLight", chunksection.getEmittedLightArray().a);
                if (flag) {
                    nbttagcompound1.setByteArray("SkyLight", chunksection.getSkyLightArray().a);
                } else {
                    nbttagcompound1.setByteArray("SkyLight", new byte[chunksection.getEmittedLightArray().a.length]);
                }

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

                nbttagcompound1 = new NBTTagCompound();
                if (entity.d(nbttagcompound1)) {
                    chunk.m = true;
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
                nbttagcompound2.setInt("p", nextticklistentry.f);
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
        chunk.q = nbttagcompound.getLong("InhabitedTime");
        NBTTagList nbttaglist = nbttagcompound.getList("Sections");
        byte b0 = 16;
        ChunkSection[] achunksection = new ChunkSection[b0];
        boolean flag = !world.worldProvider.g;

        for (int k = 0; k < nbttaglist.size(); ++k) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(k);
            byte b1 = nbttagcompound1.getByte("Y");
            ChunkSection chunksection = new ChunkSection(b1 << 4, flag);

            chunksection.setIdArray(nbttagcompound1.getByteArray("Blocks"));
            if (nbttagcompound1.hasKey("Add")) {
                chunksection.setExtendedIdArray(new NibbleArray(nbttagcompound1.getByteArray("Add"), 4));
            }

            chunksection.setDataArray(new NibbleArray(nbttagcompound1.getByteArray("Data"), 4));
            chunksection.setEmittedLightArray(new NibbleArray(nbttagcompound1.getByteArray("BlockLight"), 4));
            if (flag) {
                chunksection.setSkyLightArray(new NibbleArray(nbttagcompound1.getByteArray("SkyLight"), 4));
            }

            chunksection.recalcBlockCounts();
            achunksection[b1] = chunksection;
        }

        chunk.a(achunksection);
        if (nbttagcompound.hasKey("Biomes")) {
            chunk.a(nbttagcompound.getByteArray("Biomes"));
        }

        // CraftBukkit start - End this method here and split off entity loading to another method
        return chunk;
    }

    public void loadEntities(Chunk chunk, NBTTagCompound nbttagcompound, World world) {
        // CraftBukkit end

        NBTTagList nbttaglist1 = nbttagcompound.getList("Entities");

        if (nbttaglist1 != null) {
            for (int l = 0; l < nbttaglist1.size(); ++l) {
                NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist1.get(l);
                Entity entity = EntityTypes.a(nbttagcompound2, world);

                chunk.m = true;
                if (entity != null) {
                    chunk.a(entity);
                    Entity entity1 = entity;

                    for (NBTTagCompound nbttagcompound3 = nbttagcompound2; nbttagcompound3.hasKey("Riding"); nbttagcompound3 = nbttagcompound3.getCompound("Riding")) {
                        Entity entity2 = EntityTypes.a(nbttagcompound3.getCompound("Riding"), world);

                        if (entity2 != null) {
                            chunk.a(entity2);
                            entity1.mount(entity2);
                        }

                        entity1 = entity2;
                    }
                }
            }
        }

        NBTTagList nbttaglist2 = nbttagcompound.getList("TileEntities");

        if (nbttaglist2 != null) {
            for (int i1 = 0; i1 < nbttaglist2.size(); ++i1) {
                NBTTagCompound nbttagcompound4 = (NBTTagCompound) nbttaglist2.get(i1);
                TileEntity tileentity = TileEntity.c(nbttagcompound4);

                if (tileentity != null) {
                    chunk.a(tileentity);
                }
            }
        }

        if (nbttagcompound.hasKey("TileTicks")) {
            NBTTagList nbttaglist3 = nbttagcompound.getList("TileTicks");

            if (nbttaglist3 != null) {
                for (int j1 = 0; j1 < nbttaglist3.size(); ++j1) {
                    NBTTagCompound nbttagcompound5 = (NBTTagCompound) nbttaglist3.get(j1);

                    world.b(nbttagcompound5.getInt("x"), nbttagcompound5.getInt("y"), nbttagcompound5.getInt("z"), nbttagcompound5.getInt("i"), nbttagcompound5.getInt("t"), nbttagcompound5.getInt("p"));
                }
            }
        }

        // return chunk; // CraftBukkit
    }
}
