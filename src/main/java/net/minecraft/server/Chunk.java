package net.minecraft.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;

public class Chunk {

    public static boolean a;
    public byte[] b;
    public int[] c;
    public boolean[] d;
    public boolean e;
    public World world;
    public NibbleArray g;
    public NibbleArray h;
    public NibbleArray i;
    public byte[] heightMap;
    public int k;
    public final int x;
    public final int z;
    public Map tileEntities;
    public List[] entitySlices;
    public boolean done;
    public boolean q;
    public boolean r;
    public boolean s;
    public long t;
    boolean u;

    public Chunk(World world, int i, int j) {
        this.c = new int[256];
        this.d = new boolean[256];
        this.tileEntities = new HashMap();
        this.done = false;
        this.q = false;
        this.s = false;
        this.t = 0L;
        this.u = false;
        world.getClass();
        this.entitySlices = new List[128 / 16];
        this.world = world;
        this.x = i;
        this.z = j;
        this.heightMap = new byte[256];

        for (int k = 0; k < this.entitySlices.length; ++k) {
            this.entitySlices[k] = new ArrayList();
        }

        Arrays.fill(this.c, -999);

        // CraftBukkit start
        if (!(this instanceof EmptyChunk)) {
            org.bukkit.craftbukkit.CraftWorld cworld = this.world.getWorld();
            this.bukkitChunk = (cworld == null) ? null : cworld.popPreservedChunk(i, j);
            if (this.bukkitChunk == null) {
                this.bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(this);
            }
        }
    }

    public org.bukkit.Chunk bukkitChunk;
    // CraftBukkit end

    public Chunk(World world, byte[] abyte, int i, int j) {
        this(world, i, j);
        this.b = abyte;

        int k = abyte.length;

        // Craftbukkit start - FIX THE DECOMPILER!
        this.g = new NibbleArray(k, 7);
        this.h = new NibbleArray(k, 7);
        this.i = new NibbleArray(k, 7);
        // Craftbukkit end
    }

    public boolean a(int i, int j) {
        return i == this.x && j == this.z;
    }

    public int b(int i, int j) {
        return this.heightMap[j << 4 | i] & 255;
    }

    public void a() {}

    public void initLighting() {

        int i = 128 - 1;

        int j;
        int k;

        for (j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {

                int l = 128 - 1;

                int i1 = j << 11;

                int j1;

                for (j1 = i1 | k << 7; l > 0 && Block.q[this.b[j1 + l - 1] & 255] == 0; --l) {
                    ;
                }

                this.heightMap[k << 4 | j] = (byte) l;
                if (l < i) {
                    i = l;
                }

                if (!this.world.worldProvider.e) {
                    int k1 = 15;

                    int l1 = 128 - 1;

                    do {
                        k1 -= Block.q[this.b[j1 + l1] & 255];
                        if (k1 > 0) {
                            this.h.a(j, l1, k, k1);
                        }

                        --l1;
                    } while (l1 > 0 && k1 > 0);
                }
            }
        }

        this.k = i;

        for (j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                this.d(j, k);
            }
        }

        this.q = true;
    }

    public void loadNOP() {}

    private void d(int i, int j) {
        this.d[i + j * 16] = true;
    }

    private void i() {
        World j0000 = this.world;
        int j0001 = this.x * 16 + 8;

        if (j0000.areChunksLoaded(j0001, 128 / 2, this.z * 16 + 8, 16)) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    if (this.d[j + k * 16]) {
                        this.d[j + k * 16] = false;
                        int l = this.b(j, k);
                        int i1 = this.x * 16 + j;
                        int j1 = this.z * 16 + k;
                        int k1 = this.world.getHighestBlockYAt(i1 - 1, j1);
                        int l1 = this.world.getHighestBlockYAt(i1 + 1, j1);
                        int i2 = this.world.getHighestBlockYAt(i1, j1 - 1);
                        int j2 = this.world.getHighestBlockYAt(i1, j1 + 1);

                        if (l1 < k1) {
                            k1 = l1;
                        }

                        if (i2 < k1) {
                            k1 = i2;
                        }

                        if (j2 < k1) {
                            k1 = j2;
                        }

                        this.u = true;
                        this.f(i1, j1, k1);
                        this.u = true;
                        this.f(i1 - 1, j1, l);
                        this.f(i1 + 1, j1, l);
                        this.f(i1, j1 - 1, l);
                        this.f(i1, j1 + 1, l);
                    }
                }
            }
        }
    }

    private void f(int i, int j, int k) {
        int l = this.world.getHighestBlockYAt(i, j);

        if (l > k) {
            this.d(i, j, k, l + 1);
        } else if (l < k) {
            this.d(i, j, l, k + 1);
        }
    }

    private void d(int i, int j, int k, int l) {
        if (l > k) {
            World world = this.world;

            if (world.areChunksLoaded(i, 128 / 2, j, 16)) {
                for (int i1 = k; i1 < l; ++i1) {
                    this.world.b(EnumSkyBlock.SKY, i, i1, j);
                }

                this.q = true;
            }
        }
    }

    private void g(int i, int j, int k) {
        int l = this.heightMap[k << 4 | i] & 255;
        int i1 = l;

        if (j > l) {
            i1 = j;
        }
        //
        this.world.getClass();
        int j1 = i << 11;

        for (int k1 = j1 | k << 7; i1 > 0 && Block.q[this.b[k1 + i1 - 1] & 255] == 0; --i1) {
            ;
        }

        if (i1 != l) {
            this.world.g(i, k, i1, l);
            this.heightMap[k << 4 | i] = (byte) i1;
            int l1;
            int i2;
            int j2;

            if (i1 < this.k) {
                this.k = i1;
            } else {

                l1 = 128 - 1;

                for (i2 = 0; i2 < 16; ++i2) {
                    for (j2 = 0; j2 < 16; ++j2) {
                        if ((this.heightMap[j2 << 4 | i2] & 255) < l1) {
                            l1 = this.heightMap[j2 << 4 | i2] & 255;
                        }
                    }
                }

                this.k = l1;
            }

            l1 = this.x * 16 + i;
            i2 = this.z * 16 + k;
            if (i1 < l) {
                for (j2 = i1; j2 < l; ++j2) {
                    this.h.a(i, j2, k, 15);
                }
            } else {
                for (j2 = l; j2 < i1; ++j2) {
                    this.h.a(i, j2, k, 0);
                }
            }

            for (j2 = 15; i1 > 0 && j2 > 0; this.h.a(i, i1, k, j2)) {
                --i1;
                int k2 = Block.q[this.getTypeId(i, i1, k)];

                if (k2 == 0) {
                    k2 = 1;
                }

                j2 -= k2;
                if (j2 < 0) {
                    j2 = 0;
                }
            }

            byte b0 = this.heightMap[k << 4 | i];
            int l2 = l;
            int i3 = b0;

            if (b0 < l) {
                l2 = b0;
                i3 = l;
            }

            this.d(l1 - 1, i2, l2, i3);
            this.d(l1 + 1, i2, l2, i3);
            this.d(l1, i2 - 1, l2, i3);
            this.d(l1, i2 + 1, l2, i3);
            this.d(l1, i2, l2, i3);
            this.q = true;
        }
    }

    public int getTypeId(int i, int j, int k) {
        byte[] abyte = this.b;

        int l = i << 11;

        return abyte[l | k << 7 | j] & 255;
    }

    public boolean a(int i, int j, int k, int l, int i1) {
        byte b0 = (byte) l;
        int j1 = k << 4 | i;

        if (j >= this.c[j1] - 1) {
            this.c[j1] = -999;
        }

        int k1 = this.heightMap[k << 4 | i] & 255;
        byte[] j2000 = this.b;

        int j2001 = i << 11;

        int i2 = j2000[j2001 | k << 7 | j] & 255;

        if (i2 == l && this.g.a(i, j, k) == i1) {
            return false;
        } else {
            int j2 = this.x * 16 + i;
            int k2 = this.z * 16 + k;

            j2000 = this.b;

            j2001 = i << 11;

            j2000[j2001 | k << 7 | j] = (byte) (b0 & 255);
            if (i2 != 0 && !this.world.isStatic) {
                Block.byId[i2].remove(this.world, j2, j, k2);
            }

            this.g.a(i, j, k, i1);
            if (!this.world.worldProvider.e) {
                if (Block.q[b0 & 255] != 0) {
                    if (j >= k1) {
                        this.g(i, j + 1, k);
                    }
                } else if (j == k1 - 1) {
                    this.g(i, j, k);
                }

                this.world.a(EnumSkyBlock.SKY, j2, j, k2, j2, j, k2);
            }

            this.world.a(EnumSkyBlock.BLOCK, j2, j, k2, j2, j, k2);
            this.d(i, k);
            this.g.a(i, j, k, i1);
            TileEntity tileentity;

            if (l != 0) {
                if (!this.world.isStatic) {
                    Block.byId[l].a(this.world, j2, j, k2);
                }

                if (Block.byId[l] instanceof BlockContainer) {
                    tileentity = this.d(i, j, k);
                    if (tileentity == null) {
                        tileentity = ((BlockContainer) Block.byId[l]).a_();
                        this.world.setTileEntity(i, j, k, tileentity);
                    }

                    if (tileentity != null) {
                        tileentity.g();
                    }
                }
            } else if (i2 > 0 && Block.byId[i2] instanceof BlockContainer) {
                tileentity = this.d(i, j, k);
                if (tileentity != null) {
                    tileentity.g();
                }
            }

            this.q = true;
            return true;
        }
    }

    public boolean a(int i, int j, int k, int l) {
        byte b0 = (byte) l;
        int i1 = k << 4 | i;

        if (j >= this.c[i1] - 1) {
            this.c[i1] = -999;
        }

        int j1 = this.heightMap[i1] & 255;
        byte[] j2000 = this.b;

        int j2001 = i << 11;

        int l1 = j2000[j2001 | k << 7 | j] & 255;

        if (l1 == l) {
            return false;
        } else {
            int i2 = this.x * 16 + i;
            int j2 = this.z * 16 + k;

            j2000 = this.b;

            j2001 = i << 11;

            j2000[j2001 | k << 7 | j] = (byte) (b0 & 255);
            if (l1 != 0) {
                Block.byId[l1].remove(this.world, i2, j, j2);
            }

            this.g.a(i, j, k, 0);
            if (Block.q[b0 & 255] != 0) {
                if (j >= j1) {
                    this.g(i, j + 1, k);
                }
            } else if (j == j1 - 1) {
                this.g(i, j, k);
            }

            this.world.a(EnumSkyBlock.SKY, i2, j, j2, i2, j, j2);
            this.world.a(EnumSkyBlock.BLOCK, i2, j, j2, i2, j, j2);
            this.d(i, k);
            TileEntity tileentity;

            if (l != 0) {
                if (!this.world.isStatic) {
                    Block.byId[l].a(this.world, i2, j, j2);
                }

                if (l > 0 && Block.byId[l] instanceof BlockContainer) {
                    tileentity = this.d(i, j, k);
                    if (tileentity == null) {
                        tileentity = ((BlockContainer) Block.byId[l]).a_();
                        this.world.setTileEntity(i, j, k, tileentity);
                    }

                    if (tileentity != null) {
                        tileentity.g();
                    }
                }
            } else if (l1 > 0 && Block.byId[l1] instanceof BlockContainer) {
                tileentity = this.d(i, j, k);
                if (tileentity != null) {
                    tileentity.g();
                }
            }

            this.q = true;
            return true;
        }
    }

    public int getData(int i, int j, int k) {
        return this.g.a(i, j, k);
    }

    public void b(int i, int j, int k, int l) {
        this.q = true;
        this.g.a(i, j, k, l);
        int i1 = this.getTypeId(i, j, k);

        if (i1 > 0 && Block.byId[i1] instanceof BlockContainer) {
            TileEntity tileentity = this.d(i, j, k);

            if (tileentity != null) {
                tileentity.g();
                tileentity.n = l;
            }
        }
    }

    public int a(EnumSkyBlock enumskyblock, int i, int j, int k) {
        return enumskyblock == EnumSkyBlock.SKY ? this.h.a(i, j, k) : (enumskyblock == EnumSkyBlock.BLOCK ? this.i.a(i, j, k) : 0);
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        this.q = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            this.h.a(i, j, k, l);
        } else {
            if (enumskyblock != EnumSkyBlock.BLOCK) {
                return;
            }

            this.i.a(i, j, k, l);
        }
    }

    public int c(int i, int j, int k, int l) {
        int i1 = this.h.a(i, j, k);

        if (i1 > 0) {
            a = true;
        }

        i1 -= l;
        int j1 = this.i.a(i, j, k);

        if (j1 > i1) {
            i1 = j1;
        }

        return i1;
    }

    public void a(Entity entity) {
        this.s = true;
        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);

        if (i != this.x || j != this.z) {
            // CraftBukkit start
            Bukkit.getLogger().warning("Wrong location for " + entity + " in world '" + world.getWorld().getName() + "'!");
            // Thread.dumpStack();
            Bukkit.getLogger().warning("Entity is at " + entity.locX + "," + entity.locZ + " (chunk " + i + "," + j + ") but was stored in chunk " + this.x + "," + this.z);
            // CraftBukkit end
        }

        int k = MathHelper.floor(entity.locY / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.entitySlices.length) {
            k = this.entitySlices.length - 1;
        }

        entity.bV = true;
        entity.bW = this.x;
        entity.bX = k;
        entity.bY = this.z;
        this.entitySlices[k].add(entity);
    }

    public void b(Entity entity) {
        this.a(entity, entity.bX);
    }

    public void a(Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }

        if (i >= this.entitySlices.length) {
            i = this.entitySlices.length - 1;
        }

        this.entitySlices[i].remove(entity);
    }

    public boolean c(int i, int j, int k) {
        return j >= (this.heightMap[k << 4 | i] & 255);
    }

    public TileEntity d(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        TileEntity tileentity = (TileEntity) this.tileEntities.get(chunkposition);

        if (tileentity == null) {
            int l = this.getTypeId(i, j, k);

            if (!Block.isTileEntity[l]) {
                return null;
            }

            if (tileentity == null) {
                tileentity = ((BlockContainer) Block.byId[l]).a_();
                this.world.setTileEntity(this.x * 16 + i, j, this.z * 16 + k, tileentity);
            }

            tileentity = (TileEntity) this.tileEntities.get(chunkposition);
        }

        if (tileentity != null && tileentity.m()) {
            this.tileEntities.remove(chunkposition);
            return null;
        } else {
            return tileentity;
        }
    }

    public void a(TileEntity tileentity) {
        int i = tileentity.x - this.x * 16;
        int j = tileentity.y;
        int k = tileentity.z - this.z * 16;

        this.a(i, j, k, tileentity);
        if (this.e) {
            this.world.h.add(tileentity);
        }
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        tileentity.world = this.world;
        tileentity.x = this.x * 16 + i;
        tileentity.y = j;
        tileentity.z = this.z * 16 + k;
        if (this.getTypeId(i, j, k) != 0 && Block.byId[this.getTypeId(i, j, k)] instanceof BlockContainer) {
            tileentity.n();
            this.tileEntities.put(chunkposition, tileentity);
        } else {
            // CraftBukkit start
            System.out.println("Attempted to place a tile entity (" + tileentity + ") at " + tileentity.x + "," + tileentity.y + "," + tileentity.z
                    + " (" + org.bukkit.Material.getMaterial(getTypeId(i, j, k)) + ") where there was no entity tile!");
            // CraftBukkit end
        }
    }

    public void e(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        if (this.e) {
            TileEntity tileentity = (TileEntity) this.tileEntities.remove(chunkposition);

            if (tileentity != null) {
                tileentity.i();
            }
        }
    }

    public void addEntities() {
        this.e = true;
        this.world.a(this.tileEntities.values());

        for (int i = 0; i < this.entitySlices.length; ++i) {
            this.world.a(this.entitySlices[i]);
        }
    }

    public void removeEntities() {
        this.e = false;
        Iterator iterator = this.tileEntities.values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            this.world.a(tileentity);
        }

        for (int i = 0; i < this.entitySlices.length; ++i) {
            // CraftBukkit start
            java.util.Iterator<Object> iter = this.entitySlices[i].iterator();
            while (iter.hasNext()) {
                Entity entity = (Entity) iter.next();
                int cx = org.bukkit.Location.locToBlock(entity.locX) >> 4;
                int cz = org.bukkit.Location.locToBlock(entity.locZ) >> 4;

                // Do not pass along players, as doing so can get them stuck outside of time.
                // (which for example disables inventory icon updates and prevents block breaking)
                if (entity instanceof EntityPlayer && (cx != this.x || cz != this.z)) {
                    iter.remove();
                }
            }
            // CraftBukkit end

            this.world.b(this.entitySlices[i]);
        }
    }

    public void f() {
        this.q = true;
    }

    public void a(Entity entity, AxisAlignedBB axisalignedbb, List list) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.e + 2.0D) / 16.0D);

        if (i < 0) {
            i = 0;
        }

        if (j >= this.entitySlices.length) {
            j = this.entitySlices.length - 1;
        }

        for (int k = i; k <= j; ++k) {
            List list1 = this.entitySlices[k];

            for (int l = 0; l < list1.size(); ++l) {
                Entity entity1 = (Entity) list1.get(l);

                if (entity1 != entity && entity1.boundingBox.a(axisalignedbb)) {
                    list.add(entity1);
                }
            }
        }
    }

    public void a(Class oclass, AxisAlignedBB axisalignedbb, List list) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.e + 2.0D) / 16.0D);

        if (i < 0) {
            i = 0;
        }

        if (j >= this.entitySlices.length) {
            j = this.entitySlices.length - 1;
        }

        for (int k = i; k <= j; ++k) {
            List list1 = this.entitySlices[k];

            for (int l = 0; l < list1.size(); ++l) {
                Entity entity = (Entity) list1.get(l);

                if (oclass.isAssignableFrom(entity.getClass()) && entity.boundingBox.a(axisalignedbb)) {
                    list.add(entity);
                }
            }
        }
    }

    public boolean a(boolean flag) {
        if (this.r) {
            return false;
        } else {
            if (flag) {
                if (this.s && this.world.getTime() != this.t) {
                    return true;
                }
            } else if (this.s && this.world.getTime() >= this.t + 600L) {
                return true;
            }

            return this.q;
        }
    }

    public int getData(byte[] abyte, int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1 = l - i;
        int i2 = i1 - j;
        int j2 = j1 - k;

        if (l1 * i2 * j2 == this.b.length) {
            System.arraycopy(this.b, 0, abyte, k1, this.b.length);
            k1 += this.b.length;
            System.arraycopy(this.g.a, 0, abyte, k1, this.g.a.length);
            k1 += this.g.a.length;
            System.arraycopy(this.i.a, 0, abyte, k1, this.i.a.length);
            k1 += this.i.a.length;
            System.arraycopy(this.h.a, 0, abyte, k1, this.h.a.length);
            k1 += this.h.a.length;
            return k1;
        } else {
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;

            for (k2 = i; k2 < l; ++k2) {
                for (i3 = k; i3 < j1; ++i3) {

                    l2 = k2 << 11;

                    j3 = l2 | i3 << 7 | j;
                    k3 = i1 - j;
                    System.arraycopy(this.b, j3, abyte, k1, k3);
                    k1 += k3;
                }
            }

            for (k2 = i; k2 < l; ++k2) {
                for (i3 = k; i3 < j1; ++i3) {

                    l2 = k2 << 11;

                    j3 = (l2 | i3 << 7 | j) >> 1;
                    k3 = (i1 - j) / 2;
                    System.arraycopy(this.g.a, j3, abyte, k1, k3);
                    k1 += k3;
                }
            }

            for (k2 = i; k2 < l; ++k2) {
                for (i3 = k; i3 < j1; ++i3) {

                    l2 = k2 << 11;

                    j3 = (l2 | i3 << 7 | j) >> 1;
                    k3 = (i1 - j) / 2;
                    System.arraycopy(this.i.a, j3, abyte, k1, k3);
                    k1 += k3;
                }
            }

            for (k2 = i; k2 < l; ++k2) {
                for (i3 = k; i3 < j1; ++i3) {

                    l2 = k2 << 11;

                    j3 = (l2 | i3 << 7 | j) >> 1;
                    k3 = (i1 - j) / 2;
                    System.arraycopy(this.h.a, j3, abyte, k1, k3);
                    k1 += k3;
                }
            }

            return k1;
        }
    }

    public Random a(long i) {
        return new Random(this.world.getSeed() + (long) (this.x * this.x * 4987142) + (long) (this.x * 5947611) + (long) (this.z * this.z) * 4392871L + (long) (this.z * 389711) ^ i);
    }

    public void g() {
        BlockRegister.a(this.b);
    }

    public void a(IChunkProvider ichunkprovider, IChunkProvider ichunkprovider1, int i, int j) {
        if (!this.done && ichunkprovider.isChunkLoaded(i + 1, j + 1) && ichunkprovider.isChunkLoaded(i, j + 1) && ichunkprovider.isChunkLoaded(i + 1, j)) {
            ichunkprovider.getChunkAt(ichunkprovider1, i, j);
        }

        if (ichunkprovider.isChunkLoaded(i - 1, j) && !ichunkprovider.getOrCreateChunk(i - 1, j).done && ichunkprovider.isChunkLoaded(i - 1, j + 1) && ichunkprovider.isChunkLoaded(i, j + 1) && ichunkprovider.isChunkLoaded(i - 1, j + 1)) {
            ichunkprovider.getChunkAt(ichunkprovider1, i - 1, j);
        }

        if (ichunkprovider.isChunkLoaded(i, j - 1) && !ichunkprovider.getOrCreateChunk(i, j - 1).done && ichunkprovider.isChunkLoaded(i + 1, j - 1) && ichunkprovider.isChunkLoaded(i + 1, j - 1) && ichunkprovider.isChunkLoaded(i + 1, j)) {
            ichunkprovider.getChunkAt(ichunkprovider1, i, j - 1);
        }

        if (ichunkprovider.isChunkLoaded(i - 1, j - 1) && !ichunkprovider.getOrCreateChunk(i - 1, j - 1).done && ichunkprovider.isChunkLoaded(i, j - 1) && ichunkprovider.isChunkLoaded(i - 1, j)) {
            ichunkprovider.getChunkAt(ichunkprovider1, i - 1, j - 1);
        }
    }

    public int c(int i, int j) {
        int k = i | j << 4;
        int l = this.c[k];

        if (l == -999) {

            int i1 = 128 - 1;

            l = -1;

            while (i1 > 0 && l == -1) {
                int j1 = this.getTypeId(i, i1, j);
                Material material = j1 == 0 ? Material.AIR : Block.byId[j1].material;

                if (!material.isSolid() && !material.isLiquid()) {
                    --i1;
                } else {
                    l = i1 + 1;
                }
            }

            this.c[k] = l;
        }

        return l;
    }

    public void h() {
        this.i();
    }
}
