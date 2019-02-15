package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nullable;

public final class RegionFileCache implements AutoCloseable {

    public final Long2ObjectLinkedOpenHashMap<RegionFile> cache = new Long2ObjectLinkedOpenHashMap();
    private final File b;
    private final boolean c;

    RegionFileCache(File file, boolean flag) {
        this.b = file;
        this.c = flag;
    }

    private RegionFile getFile(ChunkCoordIntPair chunkcoordintpair, boolean existingOnly) throws IOException { // CraftBukkit
        long i = ChunkCoordIntPair.pair(chunkcoordintpair.getRegionX(), chunkcoordintpair.getRegionZ());
        RegionFile regionfile = (RegionFile) this.cache.getAndMoveToFirst(i);

        if (regionfile != null) {
            return regionfile;
        } else {
            if (this.cache.size() >= com.destroystokyo.paper.PaperConfig.regionFileCacheSize) { // Paper - configurable
                ((RegionFile) this.cache.removeLast()).close();
            }

            if (!this.b.exists()) {
                this.b.mkdirs();
            }

            File file = new File(this.b, "r." + chunkcoordintpair.getRegionX() + "." + chunkcoordintpair.getRegionZ() + ".mca");
            if (existingOnly && !file.exists()) return null; // CraftBukkit
            RegionFile regionfile1 = new RegionFile(file, this.b, this.c);

            this.cache.putAndMoveToFirst(i, regionfile1);
            return regionfile1;
        }
    }

    // Paper start
    private static void printOversizedLog(String msg, File file, int x, int z) {
        org.apache.logging.log4j.LogManager.getLogger().fatal(msg + " (" + file.toString().replaceAll(".+[\\\\/]", "") + " - " + x + "," + z + ") Go clean it up to remove this message. /minecraft:tp " + (x<<4)+" 128 "+(z<<4) + " - DO NOT REPORT THIS TO PAPER - You may ask for help on Discord, but do not file an issue. These error messages can not be removed.");
    }

    private static final int DEFAULT_SIZE_THRESHOLD = 1024 * 8;
    private static final int OVERZEALOUS_TOTAL_THRESHOLD = 1024 * 64;
    private static final int OVERZEALOUS_THRESHOLD = 1024;
    private static int SIZE_THRESHOLD = DEFAULT_SIZE_THRESHOLD;
    private static void resetFilterThresholds() {
        SIZE_THRESHOLD = Math.max(1024 * 4, Integer.getInteger("Paper.FilterThreshhold", DEFAULT_SIZE_THRESHOLD));
    }
    static {
        resetFilterThresholds();
    }

    static boolean isOverzealous() {
        return SIZE_THRESHOLD == OVERZEALOUS_THRESHOLD;
    }


    private static NBTTagCompound readOversizedChunk(RegionFile regionfile, ChunkCoordIntPair chunkCoordinate) throws IOException {
        synchronized (regionfile) {
            try (DataInputStream datainputstream = regionfile.getReadStream(chunkCoordinate)) {
                NBTTagCompound oversizedData = regionfile.getOversizedData(chunkCoordinate.x, chunkCoordinate.z);
                NBTTagCompound chunk = NBTCompressedStreamTools.readNBT((DataInput) datainputstream);
                if (oversizedData == null) {
                    return chunk;
                }
                NBTTagCompound oversizedLevel = oversizedData.getCompound("Level");
                NBTTagCompound level = chunk.getCompound("Level");

                mergeChunkList(level, oversizedLevel, "Entities");
                mergeChunkList(level, oversizedLevel, "TileEntities");

                chunk.set("Level", level);

                return chunk;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw throwable;
            }
        }
    }

    private static void mergeChunkList(NBTTagCompound level, NBTTagCompound oversizedLevel, String key) {
        NBTTagList levelList = level.getList(key, 10);
        NBTTagList oversizedList = oversizedLevel.getList(key, 10);

        if (!oversizedList.isEmpty()) {
            levelList.addAll(oversizedList);
            level.set(key, levelList);
        }
    }

    private static int getNBTSize(NBTBase nbtBase) {
        DataOutputStream test = new DataOutputStream(new org.apache.commons.io.output.NullOutputStream());
        try {
            nbtBase.write(test);
            return test.size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Paper End

    @Nullable
    public NBTTagCompound read(ChunkCoordIntPair chunkcoordintpair) throws IOException {
        // CraftBukkit start - SPIGOT-5680: There's no good reason to preemptively create files on read, save that for writing
        RegionFile regionfile = this.getFile(chunkcoordintpair, true);
        if (regionfile == null) {
            return null;
        }
        // CraftBukkit end
        DataInputStream datainputstream = regionfile.a(chunkcoordintpair);
        // Paper start
        if (regionfile.isOversized(chunkcoordintpair.x, chunkcoordintpair.z)) {
            printOversizedLog("Loading Oversized Chunk!", regionfile.file, chunkcoordintpair.x, chunkcoordintpair.z);
            return readOversizedChunk(regionfile, chunkcoordintpair);
        }
        // Paper end
        Throwable throwable = null;

        NBTTagCompound nbttagcompound;

        try {
            if (datainputstream != null) {
                nbttagcompound = NBTCompressedStreamTools.a((DataInput) datainputstream);
                return nbttagcompound;
            }

            nbttagcompound = null;
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (datainputstream != null) {
                if (throwable != null) {
                    try {
                        datainputstream.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                } else {
                    datainputstream.close();
                }
            }

        }

        return nbttagcompound;
    }

    protected void write(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) throws IOException {
        RegionFile regionfile = this.getFile(chunkcoordintpair, false); // CraftBukkit
        int attempts = 0; Exception laste = null; while (attempts++ < 5) { try { // Paper
        DataOutputStream dataoutputstream = regionfile.c(chunkcoordintpair);
        Throwable throwable = null;

        try {
            NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) dataoutputstream);
            regionfile.setOversized(chunkcoordintpair.x, chunkcoordintpair.z, false); // Paper - We don't do this anymore, mojang stores differently, but clear old meta flag if it exists to get rid of our own meta file once last oversized is gone
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (dataoutputstream != null) {
                if (throwable != null) {
                    try {
                        dataoutputstream.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                } else {
                    dataoutputstream.close();
                }
            }

        }

            // Paper start
            return;
        } catch (Exception ex)  {
            laste = ex;
        }
        }

        if (laste != null) {
            com.destroystokyo.paper.exception.ServerInternalException.reportInternalException(laste);
            MinecraftServer.LOGGER.error("Failed to save chunk", laste);
        }
        // Paper end
    }

    public void close() throws IOException {
        ExceptionSuppressor<IOException> exceptionsuppressor = new ExceptionSuppressor<>();
        ObjectIterator objectiterator = this.cache.values().iterator();

        while (objectiterator.hasNext()) {
            RegionFile regionfile = (RegionFile) objectiterator.next();

            try {
                regionfile.close();
            } catch (IOException ioexception) {
                exceptionsuppressor.a(ioexception);
            }
        }

        exceptionsuppressor.a();
    }

    public void a() throws IOException {
        ObjectIterator objectiterator = this.cache.values().iterator();

        while (objectiterator.hasNext()) {
            RegionFile regionfile = (RegionFile) objectiterator.next();

            regionfile.a();
        }

    }
}
