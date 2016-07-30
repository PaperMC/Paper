package net.minecraft.server;

import javax.annotation.Nullable;

public class NibbleArray {

    @Nullable
    protected byte[] a;

    public NibbleArray() {}

    public NibbleArray(byte[] abyte) {
        this.a = abyte;
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
            this.a = new byte[2048];
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
        }

        return this.a;
    }

    public NibbleArray b() {
        return this.a == null ? new NibbleArray() : new NibbleArray((byte[]) this.a.clone());
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
