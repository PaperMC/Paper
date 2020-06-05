package net.minecraft.server;

import com.destroystokyo.paper.util.pooled.PooledObjects; // Paper

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NibbleArray {

    // Paper start
    static final NibbleArray EMPTY_NIBBLE_ARRAY = new NibbleArray() {
        @Override
        public byte[] asBytes() {
            throw new IllegalStateException();
        }
    };
    long lightCacheKey = Long.MIN_VALUE;
    public static byte[] EMPTY_NIBBLE = new byte[2048];
    private static final int nibbleBucketSizeMultiplier = Integer.getInteger("Paper.nibbleBucketSize", 3072);
    private static final int maxPoolSize = Integer.getInteger("Paper.maxNibblePoolSize", (int) Math.min(6, Math.max(1, Runtime.getRuntime().maxMemory() / 1024 / 1024 / 1024)) * (nibbleBucketSizeMultiplier * 8));
    public static final PooledObjects<byte[]> BYTE_2048 = new PooledObjects<>(() -> new byte[2048], maxPoolSize);
    public static void releaseBytes(byte[] bytes) {
        if (bytes != null && bytes != EMPTY_NIBBLE && bytes.length == 2048) {
            System.arraycopy(EMPTY_NIBBLE, 0, bytes, 0, 2048);
            BYTE_2048.release(bytes);
        }
    }

    public NibbleArray markPoolSafe(byte[] bytes) {
        if (bytes != EMPTY_NIBBLE) this.a = bytes;
        return markPoolSafe();
    }
    public NibbleArray markPoolSafe() {
        poolSafe = true;
        return this;
    }
    public byte[] getIfSet() {
        return this.a != null ? this.a : EMPTY_NIBBLE;
    }
    public byte[] getCloneIfSet() {
        if (a == null) {
            return EMPTY_NIBBLE;
        }
        byte[] ret = BYTE_2048.acquire();
        System.arraycopy(getIfSet(), 0, ret, 0, 2048);
        return ret;
    }

    public NibbleArray cloneAndSet(byte[] bytes) {
        if (bytes != null && bytes != EMPTY_NIBBLE) {
            this.a = BYTE_2048.acquire();
            System.arraycopy(bytes, 0, this.a, 0, 2048);
        }
        return this;
    }
    boolean poolSafe = false;
    public java.lang.Runnable cleaner;
    private void registerCleaner() {
        if (!poolSafe) {
            cleaner = MCUtil.registerCleaner(this, this.a, NibbleArray::releaseBytes);
        } else {
            cleaner = MCUtil.once(() -> NibbleArray.releaseBytes(this.a));
        }
    }
    // Paper end
    @Nullable protected byte[] a;


    public NibbleArray() {}

    public NibbleArray(byte[] abyte) {
        // Paper start
        this(abyte, false);
    }
    public NibbleArray(byte[] abyte, boolean isSafe) {
        this.a = abyte;
        if (!isSafe) this.a = getCloneIfSet(); // Paper - clone for safety
        registerCleaner();
        // Paper end
        if (abyte.length != 2048) {
            throw (IllegalArgumentException) SystemUtils.c((Throwable) (new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + abyte.length)));
        }
    }

    protected NibbleArray(int i) {
        this.a = new byte[i];
    }

    public int a(int i, int j, int k) {
        return this.b(this.b(i, j, k));
    }

    public void a(int i, int j, int k, int l) {
        this.a(this.b(i, j, k), l);
    }

    protected int b(int i, int j, int k) {
        return j << 8 | k << 4 | i;
    }

    public int b(int i) { // PAIL: private -> public
        if (this.a == null) {
            return 0;
        } else {
            int j = this.d(i);

            return this.a[j] >> ((i & 1) << 2) & 15; // Spigot
        }
    }

    public void a(int i, int j) { // PAIL: private -> public
        if (this.a == null) {
            this.a = BYTE_2048.acquire(); // Paper
            registerCleaner();// Paper
        }

        int k = this.d(i);

        // Spigot start
        int shift = (i & 1) << 2;
        this.a[k] = (byte) (this.a[k] & ~(15 << shift) | (j & 15) << shift);
        // Spigot end
    }

    private boolean c(int i) {
        return (i & 1) == 0;
    }

    private int d(int i) {
        return i >> 1;
    }

    public byte[] asBytes() {
        if (this.a == null) {
            this.a = new byte[2048];
        } else { // Paper start
            // Accessor may need this object past garbage collection so need to clone it and return pooled value
            // If we know its safe for pre GC access, use asBytesPoolSafe(). If you just need read, use getIfSet()
            Runnable cleaner = this.cleaner;
            if (cleaner != null) {
                this.a = this.a.clone();
                cleaner.run(); // release the previously pooled value
                this.cleaner = null;
            }
        }
        // Paper end

        return this.a;
    }

    @Nonnull
    public byte[] asBytesPoolSafe() {
        if (this.a == null) {
            this.a = BYTE_2048.acquire(); // Paper
            registerCleaner(); // Paper
        }

        //noinspection ConstantConditions
        return this.a;
    }
    // Paper end

    public NibbleArray copy() { return this.b(); } // Paper - OBFHELPER
    public NibbleArray b() {
        return this.a == null ? new NibbleArray() : new NibbleArray(this.a); // Paper - clone in ctor
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 0; i < 4096; ++i) {
            stringbuilder.append(Integer.toHexString(this.b(i)));
            if ((i & 15) == 15) {
                stringbuilder.append("\n");
            }

            if ((i & 255) == 255) {
                stringbuilder.append("\n");
            }
        }

        return stringbuilder.toString();
    }

    public boolean c() {
        return this.a == null;
    }
}
