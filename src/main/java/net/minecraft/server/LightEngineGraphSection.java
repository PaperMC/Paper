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
        for (int k = -1; k <= 1; ++k) {
            for (int l = -1; l <= 1; ++l) {
                for (int i1 = -1; i1 <= 1; ++i1) {
                    long j1 = SectionPosition.a(i, k, l, i1);

                    if (j1 != i) {
                        this.b(i, j1, j, flag);
                    }
                }
            }
        }

    }

    @Override
    protected int a(long i, long j, int k) {
        int l = k;

        for (int i1 = -1; i1 <= 1; ++i1) {
            for (int j1 = -1; j1 <= 1; ++j1) {
                for (int k1 = -1; k1 <= 1; ++k1) {
                    long l1 = SectionPosition.a(i, i1, j1, k1);

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
