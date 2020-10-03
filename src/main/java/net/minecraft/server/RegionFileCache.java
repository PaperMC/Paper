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
            if (this.cache.size() >= 256) {
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

    @Nullable
    public NBTTagCompound read(ChunkCoordIntPair chunkcoordintpair) throws IOException {
        // CraftBukkit start - SPIGOT-5680: There's no good reason to preemptively create files on read, save that for writing
        RegionFile regionfile = this.getFile(chunkcoordintpair, true);
        if (regionfile == null) {
            return null;
        }
        // CraftBukkit end
        DataInputStream datainputstream = regionfile.a(chunkcoordintpair);
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
        DataOutputStream dataoutputstream = regionfile.c(chunkcoordintpair);
        Throwable throwable = null;

        try {
            NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) dataoutputstream);
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
