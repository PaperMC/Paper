package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class AABBPool {

    private final int a;
    private final int b;
    private final List pool = new ArrayList();
    private int d;
    private int largestSize;
    private int resizeTime;

    public AABBPool(int i, int j) {
        this.a = i;
        this.b = j;
    }

    public AxisAlignedBB a(double d0, double d1, double d2, double d3, double d4, double d5) {
        // CraftBukkit - don't pool objects indefinitely if thread doesn't adhere to contract
        if (this.resizeTime == 0) return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
        AxisAlignedBB axisalignedbb;

        if (this.d >= this.pool.size()) {
            axisalignedbb = new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
            this.pool.add(axisalignedbb);
        } else {
            axisalignedbb = (AxisAlignedBB) this.pool.get(this.d);
            axisalignedbb.b(d0, d1, d2, d3, d4, d5);
        }

        ++this.d;
        return axisalignedbb;
    }

    public void a() {
        if (this.d > this.largestSize) {
            this.largestSize = this.d;
        }

        // CraftBukkit start - Intelligent cache
        if ((this.resizeTime++ & 0xff) == 0) {
            int newSize = this.pool.size() - (this.pool.size() >> 3);
            // newSize will be 87.5%, but if we were not in that range, we clear some of the cache
            if (newSize > this.largestSize) {
                // Work down from size() to prevent insane array copies
                for (int i = this.pool.size() - 1; i > newSize; i--) {
                    this.pool.remove(i);
                }
            }

            this.largestSize = 0;
            // this.resizeTime = 0; // We do not reset to zero; it doubles for a flag
        }
        // CraftBukkit end

        this.d = 0;
    }

    public int c() {
        return this.pool.size();
    }

    public int d() {
        return this.d;
    }
}
