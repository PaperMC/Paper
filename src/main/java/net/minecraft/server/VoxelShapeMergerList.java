package net.minecraft.server;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public final class VoxelShapeMergerList implements VoxelShapeMerger {

    private final DoubleArrayList a;
    private final IntArrayList b;
    private final IntArrayList c;

    protected VoxelShapeMergerList(DoubleList doublelist, DoubleList doublelist1, boolean flag, boolean flag1) {
        int i = 0;
        int j = 0;
        double d0 = Double.NaN;
        int k = doublelist.size();
        int l = doublelist1.size();
        int i1 = k + l;

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
                if (d0 < d1 - 1.0E-7D) {
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
