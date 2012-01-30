package net.minecraft.server;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkRegionLoader implements IChunkLoader, IAsyncChunkSaver {

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
                for (int k = 0; k < this.a.size(); ++k) {
                    if (((PendingChunkToSave) this.a.get(k)).a.equals(chunkcoordintpair)) {
                        nbttagcompound = ((PendingChunkToSave) this.a.get(k)).b;
                        break;
                    }
                }
            }
        }

        if (nbttagcompound == null) {
            DataInputStream datainputstream = RegionFileCache.b(this.d, i, j);

            if (datainputstream == null) {
                return null;
            }

            nbttagcompound = NBTCompressedStreamTools.a((DataInput) datainputstream);
        }

        if (!nbttagcompound.hasKey("Level")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
            return null;
        } else if (!nbttagcompound.getCompound("Level").hasKey("Blocks")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing block data, skipping");
            return null;
        } else {
            Chunk chunk = ChunkLoader.a(world, nbttagcompound.getCompound("Level"));

            if (!chunk.a(i, j)) {
                System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.x + ", " + chunk.z + ")");
                nbttagcompound.getCompound("Level").setInt("xPos", i); // CraftBukkit - .getCompound("Level")
                nbttagcompound.getCompound("Level").setInt("zPos", j); // CraftBukkit - .getCompound("Level")
                chunk = ChunkLoader.a(world, nbttagcompound.getCompound("Level"));
            }

            chunk.h();
            return chunk;
        }
    }

    public void a(World world, Chunk chunk) {
        world.l();

        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound.set("Level", nbttagcompound1);
            ChunkLoader.a(chunk, world, nbttagcompound1);
            this.a(chunk.j(), nbttagcompound);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void a(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) {
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
            if (this.a.size() <= 0) {
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

    public void a(PendingChunkToSave pendingchunktosave) throws IOException { // CraftBukkit - Added throws
        DataOutputStream dataoutputstream = RegionFileCache.c(this.d, pendingchunktosave.a.x, pendingchunktosave.a.z);

        NBTCompressedStreamTools.a(pendingchunktosave.b, (DataOutput) dataoutputstream);
        dataoutputstream.close();
    }

    public void b(World world, Chunk chunk) {}

    public void a() {}

    public void b() {}
}
