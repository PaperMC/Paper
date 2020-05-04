package net.minecraft.server;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public final class VoxelShapeMergerList implements VoxelShapeMerger {

    private final DoubleList a; // Paper
    private final IntArrayList b;
    private final IntArrayList c;

    // Paper start
    private static final IntArrayList INFINITE_B_1 = new IntArrayList(new int[]{1, 1});
    private static final IntArrayList INFINITE_B_0 = new IntArrayList(new int[]{0, 0});
    private static final IntArrayList INFINITE_C = new IntArrayList(new int[]{0, 1});
    // Paper end

    protected VoxelShapeMergerList(DoubleList doublelist, DoubleList doublelist1, boolean flag, boolean flag1) {
        int i = 0;
        int j = 0;
        double d0 = Double.NaN;
        int k = doublelist.size();
        int l = doublelist1.size();
        int i1 = k + l;

        // Paper start - optimize common path of infinity doublelist
        int size = doublelist.size();
        double tail = doublelist.getDouble(size - 1);
        double head = doublelist.getDouble(0);
        if (head == Double.NEGATIVE_INFINITY && tail == Double.POSITIVE_INFINITY && !flag && !flag1 && (size == 2 || size == 4)) {
            this.a = doublelist1;
            if (size == 2) {
                this.b = INFINITE_B_0;
            } else {
                this.b = INFINITE_B_1;
            }
            this.c = INFINITE_C;
            return;
        }
        // Paper end

        this.a = new DoubleArrayList(i1);
        this.b = new IntArrayList(i1);
        this.c = new IntArrayList(i1);

        while (true) {
            boolean flag2 = i < k;
            boolean flag3 = j < l;

            if (!flag2 && !flag3) {
                if (this.a.isEmpty()) {
                    this.a.add(Math.min(doublelist.getDouble(k - 1), doublelist1.getDouble(l - 1)));
                }

                return;
            }

            boolean flag4 = flag2 && (!flag3 || doublelist.getDouble(i) < doublelist1.getDouble(j) + 1.0E-7D);
            double d1 = flag4 ? doublelist.getDouble(i++) : doublelist1.getDouble(j++);

            if ((i != 0 && flag2 || flag4 || flag1) && (j != 0 && flag3 || !flag4 || flag)) {
                if (!(d0 >= d1 - 1.0E-7D)) { // Paper - decompile error - welcome to hell
                    this.b.add(i - 1);
                    this.c.add(j - 1);
                    this.a.add(d1);
                    d0 = d1;
                } else if (!this.a.isEmpty()) {
                    this.b.set(this.b.size() - 1, i - 1);
                    this.c.set(this.c.size() - 1, j - 1);
                }
            }
        }
    }

    @Override
    public boolean a(VoxelShapeMerger.a voxelshapemerger_a) {
        for (int i = 0; i < this.a.size() - 1; ++i) {
            if (!voxelshapemerger_a.merge(this.b.getInt(i), this.c.getInt(i), i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public DoubleList a() {
        return this.a;
    }
}
