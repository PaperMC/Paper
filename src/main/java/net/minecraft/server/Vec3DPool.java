package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class Vec3DPool {

    private final int a;
    private final int b;
    // CraftBukkit start
    // private final List c = new ArrayList();
    private Vec3D freelist = null;
    private Vec3D alloclist = null;
    private Vec3D freelisthead = null;
    private Vec3D alloclisthead = null;
    private int total_size = 0;
    private int hit;
    private int miss;
    // CraftBukkit end
    private int d = 0;
    private int e = 0;
    private int f = 0;

    public Vec3DPool(int i, int j) {
        this.a = i;
        this.b = j;
    }

    public final Vec3D create(double d0, double d1, double d2) { // CraftBukkit - add final
        if (this.f == 0) return new Vec3D(d0, d1, d2); // CraftBukkit - don't pool objects indefinitely if thread doesn't adhere to contract
        Vec3D vec3d;

        if (this.freelist == null) { // CraftBukkit
            vec3d = new Vec3D(d0, d1, d2);
            this.total_size++; // CraftBukkit
        } else {
            // CraftBukkit start
            vec3d = this.freelist;
            this.freelist = vec3d.next;
            // CraftBukkit end
            vec3d.b(d0, d1, d2);
        }

        // CraftBukkit start
        if (this.alloclist == null) {
            this.alloclisthead = vec3d;
        }
        vec3d.next = this.alloclist; // add to allocated list
        this.alloclist = vec3d;
        // CraftBukkit end
        ++this.d;
        return vec3d;
    }

    // CraftBukkit start - offer back vector (can save LOTS of unneeded bloat) - works about 90% of the time
    public void release(Vec3D v) {
        if (this.alloclist == v) {
            this.alloclist = v.next; // Pop off alloc list
            // Push on to free list
            if (this.freelist == null) this.freelisthead = v;
            v.next = this.freelist;
            this.freelist = v;
            this.d--;
        }
    }
    // CraftBukkit end

    public void a() {
        if (this.d > this.e) {
            this.e = this.d;
        }

        // CraftBukkit start - intelligent cache
        // Take any allocated blocks and put them on free list
        if (this.alloclist != null) {
            if (this.freelist == null) {
                this.freelist = this.alloclist;
                this.freelisthead = this.alloclisthead;
            }
            else {
                this.alloclisthead.next = this.freelist;
                this.freelist = this.alloclist;
                this.freelisthead = this.alloclisthead;
            }
            this.alloclist = null;
        }
        if ((this.f++ & 0xff) == 0) {
            int newSize = total_size - (total_size >> 3);
            if (newSize > this.e) { // newSize will be 87.5%, but if we were not in that range, we clear some of the cache
                for (int i = total_size; i > newSize; i--) {
                    freelist = freelist.next;
                }
                total_size = newSize;
            }
            this.e = 0;
            // this.f = 0; // We do not reset to zero; it doubles for a flag
        }
        // CraftBukkit end

        this.d = 0;
    }
}
