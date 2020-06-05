package net.minecraft.server;

public abstract class LightEngineGraphSection extends LightEngineGraph {

    protected LightEngineGraphSection(int i, int j, int k) {
        super(i, j, k);
    }

    @Override
    protected boolean a(long i) {
        return i == Long.MAX_VALUE;
    }

    @Override
    protected void a(long i, int j, boolean flag) {
        // Paper start
        int x = (int) (i >> 42);
        int y = (int) (i << 44 >> 44);
        int z = (int) (i << 22 >> 42);
        // Paper end
        for (int k = -1; k <= 1; ++k) {
            for (int l = -1; l <= 1; ++l) {
                for (int i1 = -1; i1 <= 1; ++i1) {
                    if (k == 0 && l == 0 && i1 == 0) continue; // Paper
                    long j1 = (((long) (x + k) & 4194303L) << 42) | (((long) (y + l) & 1048575L)) | (((long) (z + i1) & 4194303L) << 20); // Paper

                    //if (j1 != i) { // Paper - checked above
                        this.b(i, j1, j, flag);
                    //} // Paper
                }
            }
        }

    }

    @Override
    protected int a(long i, long j, int k) {
        int l = k;

        // Paper start
        int x = (int) (i >> 42);
        int y = (int) (i << 44 >> 44);
        int z = (int) (i << 22 >> 42);
        // Paper end
        for (int i1 = -1; i1 <= 1; ++i1) {
            for (int j1 = -1; j1 <= 1; ++j1) {
                for (int k1 = -1; k1 <= 1; ++k1) {
                    long l1 = (((long) (x + i1) & 4194303L) << 42) | (((long) (y + j1) & 1048575L)) | (((long) (z + k1) & 4194303L) << 20); // Paper

                    if (l1 == i) {
                        l1 = Long.MAX_VALUE;
                    }

                    if (l1 != j) {
                        int i2 = this.b(l1, i, this.c(l1));

                        if (l > i2) {
                            l = i2;
                        }

                        if (l == 0) {
                            return l;
                        }
                    }
                }
            }
        }

        return l;
    }

    @Override
    protected int b(long i, long j, int k) {
        return i == Long.MAX_VALUE ? this.b(j) : k + 1;
    }

    protected abstract int b(long i);

    public void b(long i, int j, boolean flag) {
        this.a(Long.MAX_VALUE, i, j, flag);
    }
}
