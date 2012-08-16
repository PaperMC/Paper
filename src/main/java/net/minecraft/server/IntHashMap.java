package net.minecraft.server;

import java.util.HashSet;
import java.util.Set;

public class IntHashMap {

    private transient IntHashMapEntry[] a = new IntHashMapEntry[16];
    private transient int b;
    private int c = 12;
    private final float d = 0.75F;
    private transient volatile int e;
    // private Set f = new HashSet(); // CraftBukkit - expensive and unused

    public IntHashMap() {}

    private static int g(int i) {
        i ^= i >>> 20 ^ i >>> 12;
        return i ^ i >>> 7 ^ i >>> 4;
    }

    private static int a(int i, int j) {
        return i & j - 1;
    }

    public Object get(int i) {
        int j = g(i);

        for (IntHashMapEntry inthashmapentry = this.a[a(j, this.a.length)]; inthashmapentry != null; inthashmapentry = inthashmapentry.c) {
            if (inthashmapentry.a == i) {
                return inthashmapentry.b;
            }
        }

        return null;
    }

    public boolean b(int i) {
        return this.c(i) != null;
    }

    final IntHashMapEntry c(int i) {
        int j = g(i);

        for (IntHashMapEntry inthashmapentry = this.a[a(j, this.a.length)]; inthashmapentry != null; inthashmapentry = inthashmapentry.c) {
            if (inthashmapentry.a == i) {
                return inthashmapentry;
            }
        }

        return null;
    }

    public void a(int i, Object object) {
        // this.f.add(Integer.valueOf(i)); // CraftBukkit
        int j = g(i);
        int k = a(j, this.a.length);

        for (IntHashMapEntry inthashmapentry = this.a[k]; inthashmapentry != null; inthashmapentry = inthashmapentry.c) {
            if (inthashmapentry.a == i) {
                inthashmapentry.b = object;
                return;
            }
        }

        ++this.e;
        this.a(j, i, object, k);
    }

    private void h(int i) {
        IntHashMapEntry[] ainthashmapentry = this.a;
        int j = ainthashmapentry.length;

        if (j == 1073741824) {
            this.c = Integer.MAX_VALUE;
        } else {
            IntHashMapEntry[] ainthashmapentry1 = new IntHashMapEntry[i];

            this.a(ainthashmapentry1);
            this.a = ainthashmapentry1;
            this.c = (int) ((float) i * this.d);
        }
    }

    private void a(IntHashMapEntry[] ainthashmapentry) {
        IntHashMapEntry[] ainthashmapentry1 = this.a;
        int i = ainthashmapentry.length;

        for (int j = 0; j < ainthashmapentry1.length; ++j) {
            IntHashMapEntry inthashmapentry = ainthashmapentry1[j];

            if (inthashmapentry != null) {
                ainthashmapentry1[j] = null;

                IntHashMapEntry inthashmapentry1;

                do {
                    inthashmapentry1 = inthashmapentry.c;
                    int k = a(inthashmapentry.d, i);

                    inthashmapentry.c = ainthashmapentry[k];
                    ainthashmapentry[k] = inthashmapentry;
                    inthashmapentry = inthashmapentry1;
                } while (inthashmapentry1 != null);
            }
        }
    }

    public Object d(int i) {
        // this.f.remove(Integer.valueOf(i)); // CraftBukkit
        IntHashMapEntry inthashmapentry = this.e(i);

        return inthashmapentry == null ? null : inthashmapentry.b;
    }

    final IntHashMapEntry e(int i) {
        int j = g(i);
        int k = a(j, this.a.length);
        IntHashMapEntry inthashmapentry = this.a[k];

        IntHashMapEntry inthashmapentry1;
        IntHashMapEntry inthashmapentry2;

        for (inthashmapentry1 = inthashmapentry; inthashmapentry1 != null; inthashmapentry1 = inthashmapentry2) {
            inthashmapentry2 = inthashmapentry1.c;
            if (inthashmapentry1.a == i) {
                ++this.e;
                --this.b;
                if (inthashmapentry == inthashmapentry1) {
                    this.a[k] = inthashmapentry2;
                } else {
                    inthashmapentry.c = inthashmapentry2;
                }

                return inthashmapentry1;
            }

            inthashmapentry = inthashmapentry1;
        }

        return inthashmapentry1;
    }

    public void c() {
        ++this.e;
        IntHashMapEntry[] ainthashmapentry = this.a;

        for (int i = 0; i < ainthashmapentry.length; ++i) {
            ainthashmapentry[i] = null;
        }

        this.b = 0;
    }

    private void a(int i, int j, Object object, int k) {
        IntHashMapEntry inthashmapentry = this.a[k];

        this.a[k] = new IntHashMapEntry(i, j, object, inthashmapentry);
        if (this.b++ >= this.c) {
            this.h(2 * this.a.length);
        }
    }

    static int f(int i) {
        return g(i);
    }
}
