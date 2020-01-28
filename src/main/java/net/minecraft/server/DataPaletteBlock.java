package net.minecraft.server;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import com.destroystokyo.paper.antixray.ChunkPacketInfo; // Paper - Anti-Xray - Add chunk packet info
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataPaletteBlock<T> implements DataPaletteExpandable<T> {

    private final DataPalette<T> b; private final DataPalette<T> getDataPaletteGlobal() { return this.b; } // Paper - OBFHELPER
    private final DataPaletteExpandable<T> c = (i, object) -> {
        return 0;
    };
    private final RegistryBlockID<T> d;
    private final Function<NBTTagCompound, T> e;
    private final Function<T, NBTTagCompound> f;
    private final T g;
    private final T[] predefinedObjects; // Paper - Anti-Xray - Add predefined objects
    protected DataBits a; public final DataBits getDataBits() { return this.a; } // Paper - OBFHELPER
    private DataPalette<T> h; private DataPalette<T> getDataPalette() { return this.h; } // Paper - OBFHELPER
    private int i; private int getBitsPerObject() { return this.i; } // Paper - OBFHELPER
    private final ReentrantLock j = new ReentrantLock();

    public void a() { /* // Paper start - disable this - use proper synchronization
        if (this.j.isLocked() && !this.j.isHeldByCurrentThread()) {
            String s = (String) Thread.getAllStackTraces().keySet().stream().filter(Objects::nonNull).map((thread) -> {
                return thread.getName() + ": \n\tat " + (String) Arrays.stream(thread.getStackTrace()).map(Object::toString).collect(Collectors.joining("\n\tat "));
            }).collect(Collectors.joining("\n"));
            CrashReport crashreport = new CrashReport("Writing into PalettedContainer from multiple threads", new IllegalStateException());
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Thread dumps");

            crashreportsystemdetails.a("Thread dumps", (Object) s);
            throw new ReportedException(crashreport);
        } else {
            this.j.lock();
        } */ // Paper end
    }

    public void b() {
        //this.j.unlock(); // Paper - disable this
    }

    // Paper start - Anti-Xray - Add predefined objects
    @Deprecated public DataPaletteBlock(DataPalette<T> datapalette, RegistryBlockID<T> registryblockid, Function<NBTTagCompound, T> function, Function<T, NBTTagCompound> function1, T t0) { this(datapalette, registryblockid, function, function1, t0, null, true); } // Notice for updates: Please make sure this constructor isn't used anywhere
    public DataPaletteBlock(DataPalette<T> datapalette, RegistryBlockID<T> registryblockid, Function<NBTTagCompound, T> function, Function<T, NBTTagCompound> function1, T t0, T[] predefinedObjects, boolean initialize) {
        // Paper end
        this.b = datapalette;
        this.d = registryblockid;
        this.e = function;
        this.f = function1;
        this.g = t0;
        // Paper start - Anti-Xray - Add predefined objects
        this.predefinedObjects = predefinedObjects;

        if (initialize) {
            if (predefinedObjects == null) {
                // Default
                this.initialize(4);
            } else {
                // MathHelper.d() is trailingBits(roundCeilPow2(n)), alternatively; (int)ceil(log2(n)); however it's trash, use numberOfLeadingZeros instead
                // Count the bits of the maximum array index to initialize a data palette with enough space from the beginning
                // The length of the array is used because air is also added to the data palette from the beginning
                // Start with at least 4
                int maxIndex = predefinedObjects.length >> 4;
                int bitCount = (32 - Integer.numberOfLeadingZeros(Math.max(16, maxIndex) - 1));

                // Initialize with at least 15 free indixes
                this.initialize((1 << bitCount) - predefinedObjects.length < 16 ? bitCount + 1 : bitCount);
                this.addPredefinedObjects();
            }
        }
        // Paper end
    }

    // Paper start - Anti-Xray - Add predefined objects
    private void addPredefinedObjects() {
        if (this.predefinedObjects != null && this.getDataPalette() != this.getDataPaletteGlobal()) {
            for (int i = 0; i < this.predefinedObjects.length; i++) {
                this.getDataPalette().getOrCreateIdFor(this.predefinedObjects[i]);
            }
        }
    }
    // Paper end

    private static int b(int i, int j, int k) {
        return j << 8 | k << 4 | i;
    }

    private void initialize(int bitsPerObject) { this.b(bitsPerObject); } // Paper - OBFHELPER
    private void b(int i) {
        if (i != this.i) {
            this.i = i;
            if (this.i <= 4) {
                this.i = 4;
                this.h = new DataPaletteLinear<>(this.d, this.i, this, this.e);
            } else if (this.i < 9) {
                this.h = new DataPaletteHash<>(this.d, this.i, this, this.e, this.f);
            } else {
                this.h = this.b;
                this.i = MathHelper.e(this.d.a());
            }

            this.h.a(this.g);
            this.a = new DataBits(this.i, 4096);
        }
    }

    @Override
    public synchronized int onResize(int i, T t0) { // Paper - synchronize
        this.a();
        DataBits databits = this.a;
        DataPalette<T> datapalette = this.h;

        this.b(i);

        int j;

        this.addPredefinedObjects(); // Paper - Anti-Xray - Add predefined objects
        for (j = 0; j < databits.b(); ++j) {
            T t1 = datapalette.a(databits.a(j));

            if (t1 != null) {
                this.setBlockIndex(j, t1);
            }
        }

        j = this.h.a(t0);
        this.b();
        return j;
    }

    public T setBlock(int i, int j, int k, T t0) {
        //this.a(); // Paper - remove to reduce ops - synchronize handled below
        return this.a(b(i, j, k), t0); // Paper

        //this.b(); // Paper
        //return t1; // PAper
    }

    public T b(int i, int j, int k, T t0) {
        return this.a(b(i, j, k), t0);
    }

    protected synchronized T a(int i, T t0) { // Paper - synchronize - writes
        int j = this.h.a(t0);
        int k = this.a.a(i, j);
        T t1 = this.h.a(k);

        return t1 == null ? this.g : t1;
    }

    protected void setBlockIndex(int i, T t0) {
        int j = this.h.a(t0);

        this.a.b(i, j);
    }

    public T a(int i, int j, int k) {
        return this.a(j << 8 | k << 4 | i); // Paper - inline
    }

    protected T a(int i) {
        T t0 = this.h.a(this.a.a(i));

        return t0 == null ? this.g : t0;
    }

    // Paper start - Anti-Xray - Add chunk packet info
    @Deprecated public void writeDataPaletteBlock(PacketDataSerializer packetDataSerializer) { this.b(packetDataSerializer); } // OBFHELPER // Notice for updates: Please make sure this method isn't used anywhere
    @Deprecated public void b(PacketDataSerializer packetdataserializer) { this.writeDataPaletteBlock(packetdataserializer, null, 0); } // Notice for updates: Please make sure this method isn't used anywhere
    public void writeDataPaletteBlock(PacketDataSerializer packetDataSerializer, ChunkPacketInfo<T> chunkPacketInfo, int chunkSectionIndex) { this.b(packetDataSerializer, chunkPacketInfo, chunkSectionIndex); } // OBFHELPER
    public synchronized void b(PacketDataSerializer packetdataserializer, ChunkPacketInfo<T> chunkPacketInfo, int chunkSectionIndex) { // Paper - synchronize
        // Paper end
        this.a();
        packetdataserializer.writeByte(this.i);
        this.h.b(packetdataserializer);
        // Paper start - Anti-Xray - Add chunk packet info
        if (chunkPacketInfo != null) {
            chunkPacketInfo.setBitsPerObject(chunkSectionIndex, this.getBitsPerObject());
            chunkPacketInfo.setDataPalette(chunkSectionIndex, this.getDataPalette());
            chunkPacketInfo.setDataBitsIndex(chunkSectionIndex, packetdataserializer.writerIndex() + PacketDataSerializer.countBytes(this.getDataBits().getDataBits().length));
            chunkPacketInfo.setPredefinedObjects(chunkSectionIndex, this.predefinedObjects);
        }
        // Paper end
        packetdataserializer.a(this.a.a());
        this.b();
    }

    public synchronized void a(NBTTagList nbttaglist, long[] along) { // Paper - synchronize
        this.a();
        // Paper - Anti-Xray - TODO: Should this.predefinedObjects.length just be added here (faster) or should the contents be compared to calculate the size (less RAM)?
        int i = Math.max(4, MathHelper.e(nbttaglist.size() + (this.predefinedObjects == null ? 0 : this.predefinedObjects.length))); // Paper - Anti-Xray - Calculate the size with predefined objects

        if (true || i != this.i) { // Paper - Anti-Xray - Not initialized yet
            this.b(i);
        }

        this.h.a(nbttaglist);
        this.addPredefinedObjects(); // Paper - Anti-Xray - Add predefined objects
        int j = along.length * 64 / 4096;

        if (this.h == this.b) {
            DataPalette<T> datapalette = new DataPaletteHash<>(this.d, i, this.c, this.e, this.f);

            datapalette.a(nbttaglist);
            DataBits databits = new DataBits(i, 4096, along);

            for (int k = 0; k < 4096; ++k) {
                this.a.b(k, this.b.a(datapalette.a(databits.a(k))));
            }
        } else if (j == this.i) {
            System.arraycopy(along, 0, this.a.a(), 0, along.length);
        } else {
            DataBits databits1 = new DataBits(j, 4096, along);

            for (int l = 0; l < 4096; ++l) {
                this.a.b(l, databits1.a(l));
            }
        }

        this.b();
    }

    public synchronized void a(NBTTagCompound nbttagcompound, String s, String s1) { // Paper - synchronize
        this.a();
        DataPaletteHash<T> datapalettehash = new DataPaletteHash<>(this.d, this.i, this.c, this.e, this.f);
        T t0 = this.g;
        int i = datapalettehash.a(this.g);
        int[] aint = new int[4096];

        for (int j = 0; j < 4096; ++j) {
            T t1 = this.a(j);

            if (t1 != t0) {
                t0 = t1;
                i = datapalettehash.a(t1);
            }

            aint[j] = i;
        }

        NBTTagList nbttaglist = new NBTTagList();

        datapalettehash.b(nbttaglist);
        nbttagcompound.set(s, nbttaglist);
        int k = Math.max(4, MathHelper.e(nbttaglist.size()));
        DataBits databits = new DataBits(k, 4096);

        for (int l = 0; l < aint.length; ++l) {
            databits.b(l, aint[l]);
        }

        nbttagcompound.a(s1, databits.a());
        this.b();
    }

    public int c() {
        return 1 + this.h.a() + PacketDataSerializer.a(this.a.b()) + this.a.a().length * 8;
    }

    public boolean contains(Predicate<T> predicate) {
        return this.h.a(predicate);
    }

    public void a(DataPaletteBlock.a<T> datapaletteblock_a) {
        Int2IntOpenHashMap int2intopenhashmap = new Int2IntOpenHashMap();

        this.a.a((i) -> {
            int2intopenhashmap.put(i, int2intopenhashmap.get(i) + 1);
        });
        int2intopenhashmap.int2IntEntrySet().forEach((entry) -> {
            datapaletteblock_a.accept(this.h.a(entry.getIntKey()), entry.getIntValue());
        });
    }

    // Paper start
    public void forEachLocation(DataPaletteBlock.a<T> datapaletteblock_a) {
        this.getDataBits().forEach((int location, int data) -> {
            datapaletteblock_a.accept(this.getDataPalette().getObject(data), location);
        });
    }
    // Paper end

    @FunctionalInterface
    public interface a<T> {

        void accept(T t0, int i);
    }
}
