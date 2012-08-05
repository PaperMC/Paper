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

        if (this.f++ == this.a) {
            int i = Math.max(this.e, this.c.size() - this.b);

            while (this.c.size() > i) {
                this.c.remove(i);
            }

            this.e = 0;
            this.f = 0;
        }

        this.d = 0;
    }
}
