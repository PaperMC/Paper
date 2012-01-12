package net.minecraft.server;

public class LongHashMap {

    private transient LongHashMapEntry[] entries = new LongHashMapEntry[16];
    private transient int count;
    private int c = 12;
    private final float d = 0.75F;
    private transient volatile int e;

    public LongHashMap() {}

    private static int g(long i) {
        return a((int) (i ^ i >>> 32));
    }

    private static int a(int i) {
        i ^= i >>> 20 ^ i >>> 12;
        return i ^ i >>> 7 ^ i >>> 4;
    }

    private static int a(int i, int j) {
        return i & j - 1;
    }

    public int count() {
        return this.count;
    }

    public Object getEntry(long i) {
        int j = g(i);

        for (LongHashMapEntry longhashmapentry = this.entries[a(j, this.entries.length)]; longhashmapentry != null; longhashmapentry = longhashmapentry.c) {
            if (longhashmapentry.a == i) {
                return longhashmapentry.b;
            }
        }

        return null;
    }

    public boolean contains(long i) {
        return this.c(i) != null;
    }

    final LongHashMapEntry c(long i) {
        int j = g(i);

        for (LongHashMapEntry longhashmapentry = this.entries[a(j, this.entries.length)]; longhashmapentry != null; longhashmapentry = longhashmapentry.c) {
            if (longhashmapentry.a == i) {
                return longhashmapentry;
            }
        }

        return null;
    }

    public void put(long i, Object object) {
        int j = g(i);
        int k = a(j, this.entries.length);

        for (LongHashMapEntry longhashmapentry = this.entries[k]; longhashmapentry != null; longhashmapentry = longhashmapentry.c) {
            if (longhashmapentry.a == i) {
                longhashmapentry.b = object;
            }
        }

        ++this.e;
        this.a(j, i, object, k);
    }

    private void b(int i) {
        LongHashMapEntry[] alonghashmapentry = this.entries;
        int j = alonghashmapentry.length;

        if (j == 1073741824) {
            this.c = Integer.MAX_VALUE;
        } else {
            LongHashMapEntry[] alonghashmapentry1 = new LongHashMapEntry[i];

            this.a(alonghashmapentry1);
            this.entries = alonghashmapentry1;
            this.c = (int) ((float) i * this.d);
        }
    }

    private void a(LongHashMapEntry[] alonghashmapentry) {
        LongHashMapEntry[] alonghashmapentry1 = this.entries;
        int i = alonghashmapentry.length;

        for (int j = 0; j < alonghashmapentry1.length; ++j) {
            LongHashMapEntry longhashmapentry = alonghashmapentry1[j];

            if (longhashmapentry != null) {
                alonghashmapentry1[j] = null;

                LongHashMapEntry longhashmapentry1;

                do {
                    longhashmapentry1 = longhashmapentry.c;
                    int k = a(longhashmapentry.d, i);

                    longhashmapentry.c = alonghashmapentry[k];
                    alonghashmapentry[k] = longhashmapentry;
                    longhashmapentry = longhashmapentry1;
                } while (longhashmapentry1 != null);
            }
        }
    }

    public Object remove(long i) {
        LongHashMapEntry longhashmapentry = this.e(i);

        return longhashmapentry == null ? null : longhashmapentry.b;
    }

    final LongHashMapEntry e(long i) {
        int j = g(i);
        int k = a(j, this.entries.length);
        LongHashMapEntry longhashmapentry = this.entries[k];

        LongHashMapEntry longhashmapentry1;
        LongHashMapEntry longhashmapentry2;

        for (longhashmapentry1 = longhashmapentry; longhashmapentry1 != null; longhashmapentry1 = longhashmapentry2) {
            longhashmapentry2 = longhashmapentry1.c;
            if (longhashmapentry1.a == i) {
                ++this.e;
                --this.count;
                if (longhashmapentry == longhashmapentry1) {
                    this.entries[k] = longhashmapentry2;
                } else {
                    longhashmapentry.c = longhashmapentry2;
                }

                return longhashmapentry1;
            }

            longhashmapentry = longhashmapentry1;
        }

        return longhashmapentry1;
    }

    private void a(int i, long j, Object object, int k) {
        LongHashMapEntry longhashmapentry = this.entries[k];

        this.entries[k] = new LongHashMapEntry(i, j, object, longhashmapentry);
        if (this.count++ >= this.c) {
            this.b(2 * this.entries.length);
        }
    }

    static int f(long i) {
        return g(i);
    }
}
