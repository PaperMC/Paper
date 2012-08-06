package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class Vec3DPool {

    private final int a;
    private final int b;
    private final List c = new ArrayList();
    private int d = 0;
    private int e = 0;
    private int f = 0;

    public Vec3DPool(int i, int j) {
        this.a = i;
        this.b = j;
    }

    public Vec3D create(double d0, double d1, double d2) {
        if (this.f == 0) return new Vec3D(d0, d1, d2); // CraftBukkit - don't pool objects indefinitely if thread doesn't adhere to contract
        Vec3D vec3d;

        if (this.d >= this.c.size()) {
            vec3d = new Vec3D(d0, d1, d2);
            this.c.add(vec3d);
        } else {
            vec3d = (Vec3D) this.c.get(this.d);
            vec3d.b(d0, d1, d2);
        }

        ++this.d;
        return vec3d;
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
