package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class Vec3DPool {

    private final int a;
    private final int b;
    // CraftBukkit start
    // private final List pool = new ArrayList();
    private Vec3D freelist = null;
    private Vec3D alloclist = null;
    private Vec3D freelisthead = null;
    private Vec3D alloclisthead = null;
    private int total_size = 0;
    // CraftBukkit end
    private int position;
    private int largestSize;
    private int resizeTime;

    public Vec3DPool(int i, int j) {
        this.a = i;
        this.b = j;
    }

    public final Vec3D create(double d0, double d1, double d2) { // CraftBukkit - Add final
        if (this.resizeTime == 0) return Vec3D.a(d0, d1, d2); // CraftBukkit - Don't pool objects indefinitely if thread doesn't adhere to contract
        Vec3D vec3d;

        if (this.freelist == null) { // CraftBukkit
            vec3d = new Vec3D(this, d0, d1, d2);
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
        vec3d.next = this.alloclist; // Add to allocated list
        this.alloclist = vec3d;
        // CraftBukkit end
        ++this.position;
        return vec3d;
    }

    // CraftBukkit start - Offer back vector (can save LOTS of unneeded bloat) - works about 90% of the time
    public void release(Vec3D v) {
        if (this.alloclist == v) {
            this.alloclist = v.next; // Pop off alloc list
            // Push on to free list
            if (this.freelist == null) this.freelisthead = v;
            v.next = this.freelist;
            this.freelist = v;
            this.position--;
        }
    }
    // CraftBukkit end

    public void a() {
        if (this.position > this.largestSize) {
            this.largestSize = this.position;
        }

        // CraftBukkit start - Intelligent cache
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
        if ((this.resizeTime++ & 0xff) == 0) {
            int newSize = total_size - (total_size >> 3);
            if (newSize > this.largestSize) { // newSize will be 87.5%, but if we were not in that range, we clear some of the cache
                for (int i = total_size; i > newSize; i--) {
                    freelist = freelist.next;
                }
                total_size = newSize;
            }
            this.largestSize = 0;
            // this.f = 0; // We do not reset to zero; it doubles for a flag
        }

        this.position = 0;
        // CraftBukkit end
    }

    public int c() {
        return this.total_size; // CraftBukkit
    }

    public int d() {
        return this.position;
    }

    private boolean e() {
        return this.b < 0 || this.a < 0;
    }
}
