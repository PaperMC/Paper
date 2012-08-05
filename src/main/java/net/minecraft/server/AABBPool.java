package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class AABBPool {

    private final int a;
    private final int b;
    private final List c = new ArrayList();
    private int d = 0;
    private int e = 0;
    private int f = 0;

    public AABBPool(int i, int j) {
        this.a = i;
        this.b = j;
    }

    public AxisAlignedBB a(double d0, double d1, double d2, double d3, double d4, double d5) {
        if (this.f == 0) return new AxisAlignedBB(d0, d1, d2, d3, d4, d5); // CraftBukkit - don't pool objects indefinitely if thread doesn't adhere to contract

        AxisAlignedBB axisalignedbb;

        if (this.d >= this.c.size()) {
            axisalignedbb = new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
            this.c.add(axisalignedbb);
        } else {
            axisalignedbb = (AxisAlignedBB) this.c.get(this.d);
            axisalignedbb.b(d0, d1, d2, d3, d4, d5);
        }

        ++this.d;
        return axisalignedbb;
    }

    public void a() {
        if (this.d > this.e) {
            this.e = this.d;
        }

        // CraftBukkit start - intelligent cache
        if ((this.f++ & 0xff) == 0) {
            int newSize = this.c.size() - (this.c.size() >> 3);
            if (newSize > this.e) { // newSize will be 87.5%, but if we were not in that range, we clear some of the cache
                for (int i = this.c.size() - 1; i > newSize; i--) { // Work down from size() to prevent insane array copies
                    this.c.remove(i);
                }
            }

            this.e = 0;
            // this.f = 0; // We do not reset to zero; it doubles for a flag
        }
        // CraftBukkit end

        this.d = 0;
    }
}
