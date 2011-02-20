package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.craftbukkit.CraftWorld;

public class Chunk {

    public static boolean a;
    public byte[] b;
    public boolean c;
    public World d;
    public NibbleArray e;
    public NibbleArray f;
    public NibbleArray g;
    public byte[] h;
    public int i;
    public final int j;
    public final int k;
    public Map l;
    public List[] m;
    public boolean n;
    public boolean o;
    public boolean p;
    public boolean q;
    public long r;

    public Chunk(World world, int i, int j) {
        this.l = new HashMap();
        this.m = new List[8];
        this.n = false;
        this.o = false;
        this.q = false;
        this.r = 0L;
        this.d = world;
        this.j = i;
        this.k = j;
        this.h = new byte[256];

        for (int k = 0; k < this.m.length; ++k) {
            this.m[k] = new ArrayList();
        }

        // CraftBukkit start
        CraftWorld cw = ((WorldServer) world).getWorld();
        bukkitChunk = (cw == null) ? null:cw.popPreservedChunk( i, j );
        if (bukkitChunk == null) {
            bukkitChunk = new org.bukkit.craftbukkit.CraftChunk( this );
        }
    }

    public org.bukkit.Chunk bukkitChunk;
    // CraftBukkit end

    public Chunk(World world, byte[] abyte, int i, int j) {
        this(world, i, j);
        this.b = abyte;
        this.e = new NibbleArray(abyte.length);
        this.f = new NibbleArray(abyte.length);
        this.g = new NibbleArray(abyte.length);
    }

    public boolean a(int i, int j) {
        return i == this.j && j == this.k;
    }

    public int b(int i, int j) {
        return this.h[j << 4 | i] & 255;
    }

    public void a() {}

    public void b() {
        int i = 127;

        int j;
        int k;

        for (j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                int l = 127;

                int i1;

                for (i1 = j << 11 | k << 7; l > 0 && Block.q[this.b[i1 + l - 1]] == 0; --l) {
                    ;
                }

                this.h[k << 4 | j] = (byte) l;
                if (l < i) {
                    i = l;
                }

                if (!this.d.q.e) {
                    int j1 = 15;
                    int k1 = 127;

                    do {
                        j1 -= Block.q[this.b[i1 + k1]];
                        if (j1 > 0) {
                            this.f.a(j, k1, k, j1);
                        }

                        --k1;
                    } while (k1 > 0 && j1 > 0);
                }
            }
        }

        this.i = i;

        for (j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                this.c(j, k);
            }
        }

        this.o = true;
    }

    public void c() {
        byte b0 = 32;

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int k = i << 11 | j << 7;

                int l;
                int i1;

                for (l = 0; l < 128; ++l) {
                    i1 = Block.s[this.b[k + l]];
                    if (i1 > 0) {
                        this.g.a(i, l, j, i1);
                    }
                }

                l = 15;

                for (i1 = b0 - 2; i1 < 128 && l > 0; this.g.a(i, i1, j, l)) {
                    ++i1;
                    byte b1 = this.b[k + i1];
                    int j1 = Block.q[b1];
                    int k1 = Block.s[b1];

                    if (j1 == 0) {
                        j1 = 1;
                    }

                    l -= j1;
                    if (k1 > l) {
                        l = k1;
                    }

                    if (l < 0) {
                        l = 0;
                    }
                }
            }
        }

        this.d.a(EnumSkyBlock.BLOCK, this.j * 16, b0 - 1, this.k * 16, this.j * 16 + 16, b0 + 1, this.k * 16 + 16);
        this.o = true;
    }

    private void c(int i, int j) {
        int k = this.b(i, j);
        int l = this.j * 16 + i;
        int i1 = this.k * 16 + j;

        this.f(l - 1, i1, k);
        this.f(l + 1, i1, k);
        this.f(l, i1 - 1, k);
        this.f(l, i1 + 1, k);
    }

    private void f(int i, int j, int k) {
        int l = this.d.d(i, j);

        if (l > k) {
            this.d.a(EnumSkyBlock.SKY, i, k, j, i, l, j);
            this.o = true;
        } else if (l < k) {
            this.d.a(EnumSkyBlock.SKY, i, l, j, i, k, j);
            this.o = true;
        }
    }

    private void g(int i, int j, int k) {
        int l = this.h[k << 4 | i] & 255;
        int i1 = l;

        if (j > l) {
            i1 = j;
        }

        for (int j1 = i << 11 | k << 7; i1 > 0 && Block.q[this.b[j1 + i1 - 1]] == 0; --i1) {
            ;
        }

        if (i1 != l) {
            this.d.g(i, k, i1, l);
            this.h[k << 4 | i] = (byte) i1;
            int k1;
            int l1;
            int i2;

            if (i1 < this.i) {
                this.i = i1;
            } else {
                k1 = 127;

                for (l1 = 0; l1 < 16; ++l1) {
                    for (i2 = 0; i2 < 16; ++i2) {
                        if ((this.h[i2 << 4 | l1] & 255) < k1) {
                            k1 = this.h[i2 << 4 | l1] & 255;
                        }
                    }
                }

                this.i = k1;
            }

            k1 = this.j * 16 + i;
            l1 = this.k * 16 + k;
            if (i1 < l) {
                for (i2 = i1; i2 < l; ++i2) {
                    this.f.a(i, i2, k, 15);
                }
            } else {
                this.d.a(EnumSkyBlock.SKY, k1, l, l1, k1, i1, l1);

                for (i2 = l; i2 < i1; ++i2) {
                    this.f.a(i, i2, k, 0);
                }
            }

            i2 = 15;

            int j2;

            for (j2 = i1; i1 > 0 && i2 > 0; this.f.a(i, i1, k, i2)) {
                --i1;
                int k2 = Block.q[this.a(i, i1, k)];

                if (k2 == 0) {
                    k2 = 1;
                }

                i2 -= k2;
                if (i2 < 0) {
                    i2 = 0;
                }
            }

            while (i1 > 0 && Block.q[this.a(i, i1 - 1, k)] == 0) {
                --i1;
            }

            if (i1 != j2) {
                this.d.a(EnumSkyBlock.SKY, k1 - 1, i1, l1 - 1, k1 + 1, j2, l1 + 1);
            }

            this.o = true;
        }
    }

    public int a(int i, int j, int k) {
        return this.b[i << 11 | k << 7 | j];
    }

    public boolean a(int i, int j, int k, int l, int i1) {
        byte b0 = (byte) l;
        int j1 = this.h[k << 4 | i] & 255;
        int k1 = this.b[i << 11 | k << 7 | j] & 255;

        if (k1 == l && this.e.a(i, j, k) == i1) {
            return false;
        } else {
            int l1 = this.j * 16 + i;
            int i2 = this.k * 16 + k;

            this.b[i << 11 | k << 7 | j] = b0;
            if (k1 != 0 && !this.d.isStatic) {
                Block.byId[k1].b(this.d, l1, j, i2);
            }

            this.e.a(i, j, k, i1);
            if (!this.d.q.e) {
                if (Block.q[b0] != 0) {
                    if (j >= j1) {
                        this.g(i, j + 1, k);
                    }
                } else if (j == j1 - 1) {
                    this.g(i, j, k);
                }

                this.d.a(EnumSkyBlock.SKY, l1, j, i2, l1, j, i2);
            }

            this.d.a(EnumSkyBlock.BLOCK, l1, j, i2, l1, j, i2);
            this.c(i, k);
            this.e.a(i, j, k, i1);
            if (l != 0) {
                Block.byId[l].e(this.d, l1, j, i2);
            }

            this.o = true;
            return true;
        }
    }

    public boolean a(int i, int j, int k, int l) {
        byte b0 = (byte) l;
        int i1 = this.h[k << 4 | i] & 255;
        int j1 = this.b[i << 11 | k << 7 | j] & 255;

        if (j1 == l) {
            return false;
        } else {
            int k1 = this.j * 16 + i;
            int l1 = this.k * 16 + k;

            this.b[i << 11 | k << 7 | j] = b0;
            if (j1 != 0) {
                Block.byId[j1].b(this.d, k1, j, l1);
            }

            this.e.a(i, j, k, 0);
            if (Block.q[b0] != 0) {
                if (j >= i1) {
                    this.g(i, j + 1, k);
                }
            } else if (j == i1 - 1) {
                this.g(i, j, k);
            }

            this.d.a(EnumSkyBlock.SKY, k1, j, l1, k1, j, l1);
            this.d.a(EnumSkyBlock.BLOCK, k1, j, l1, k1, j, l1);
            this.c(i, k);
            if (l != 0 && !this.d.isStatic) {
                Block.byId[l].e(this.d, k1, j, l1);
            }

            this.o = true;
            return true;
        }
    }

    public int b(int i, int j, int k) {
        return this.e.a(i, j, k);
    }

    public void b(int i, int j, int k, int l) {
        this.o = true;
        this.e.a(i, j, k, l);
    }

    public int a(EnumSkyBlock enumskyblock, int i, int j, int k) {
        return enumskyblock == EnumSkyBlock.SKY ? this.f.a(i, j, k) : (enumskyblock == EnumSkyBlock.BLOCK ? this.g.a(i, j, k) : 0);
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        this.o = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            this.f.a(i, j, k, l);
        } else {
            if (enumskyblock != EnumSkyBlock.BLOCK) {
                return;
            }

            this.g.a(i, j, k, l);
        }
    }

    public int c(int i, int j, int k, int l) {
        int i1 = this.f.a(i, j, k);

        if (i1 > 0) {
            a = true;
        }

        i1 -= l;
        int j1 = this.g.a(i, j, k);

        if (j1 > i1) {
            i1 = j1;
        }

        return i1;
    }

    public void a(Entity entity) {
        this.q = true;
        int i = MathHelper.b(entity.locX / 16.0D);
        int j = MathHelper.b(entity.locZ / 16.0D);

        if (i != this.j || j != this.k) {
            System.out.println("Wrong location! " + entity);
            System.out.println("" + entity.locX + "," + entity.locZ + "(" + i + "," + j + ") vs " + this.j + "," + this.k); // CraftBukkit
            Thread.dumpStack();
        }

        int k = MathHelper.b(entity.locY / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.m.length) {
            k = this.m.length - 1;
        }

        entity.ag = true;
        entity.chunkX = this.j;
        entity.ai = k;
        entity.chunkZ = this.k;
        this.m[k].add(entity);
    }

    public void b(Entity entity) {
        this.a(entity, entity.ai);
    }

    public void a(Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }

        if (i >= this.m.length) {
            i = this.m.length - 1;
        }

        this.m[i].remove(entity);
    }

    public boolean c(int i, int j, int k) {
        return j >= (this.h[k << 4 | i] & 255);
    }

    public TileEntity d(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        TileEntity tileentity = (TileEntity) this.l.get(chunkposition);

        if (tileentity == null) {
            int l = this.a(i, j, k);

            if (!Block.p[l]) {
                return null;
            }

            BlockContainer blockcontainer = (BlockContainer) Block.byId[l];

            blockcontainer.e(this.d, this.j * 16 + i, j, this.k * 16 + k);
            tileentity = (TileEntity) this.l.get(chunkposition);
        }

        return tileentity;
    }

    public void a(TileEntity tileentity) {
        int i = tileentity.b - this.j * 16;
        int j = tileentity.c;
        int k = tileentity.d - this.k * 16;

        this.a(i, j, k, tileentity);
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        tileentity.a = this.d;
        tileentity.b = this.j * 16 + i;
        tileentity.c = j;
        tileentity.d = this.k * 16 + k;
        if (this.a(i, j, k) != 0 && Block.byId[this.a(i, j, k)] instanceof BlockContainer) {
            if (this.c) {
                if (this.l.get(chunkposition) != null) {
                    this.d.c.remove(this.l.get(chunkposition));
                }

                this.d.c.add(tileentity);
            }

            this.l.put(chunkposition, tileentity);
        } else {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
        }
    }

    public void e(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        if (this.c) {
            this.d.c.remove(this.l.remove(chunkposition));
        }
    }

    public void d() {
        this.c = true;
        this.d.c.addAll(this.l.values());

        for (int i = 0; i < this.m.length; ++i) {
            this.d.a(this.m[i]);
        }
    }

    public void e() {
        this.c = false;
        this.d.c.removeAll(this.l.values());

        for (int i = 0; i < this.m.length; ++i) {
            this.d.b(this.m[i]);
        }
    }

    public void f() {
        this.o = true;
    }

    public void a(Entity entity, AxisAlignedBB axisalignedbb, List list) {
        int i = MathHelper.b((axisalignedbb.b - 2.0D) / 16.0D);
        int j = MathHelper.b((axisalignedbb.e + 2.0D) / 16.0D);

        if (i < 0) {
            i = 0;
        }

        if (j >= this.m.length) {
            j = this.m.length - 1;
        }

        for (int k = i; k <= j; ++k) {
            List list1 = this.m[k];

            for (int l = 0; l < list1.size(); ++l) {
                Entity entity1 = (Entity) list1.get(l);

                if (entity1 != entity && entity1.boundingBox.a(axisalignedbb)) {
                    list.add(entity1);
                }
            }
        }
    }

    public void a(Class oclass, AxisAlignedBB axisalignedbb, List list) {
        int i = MathHelper.b((axisalignedbb.b - 2.0D) / 16.0D);
        int j = MathHelper.b((axisalignedbb.e + 2.0D) / 16.0D);

        if (i < 0) {
            i = 0;
        }

        if (j >= this.m.length) {
            j = this.m.length - 1;
        }

        for (int k = i; k <= j; ++k) {
            List list1 = this.m[k];

            for (int l = 0; l < list1.size(); ++l) {
                Entity entity = (Entity) list1.get(l);

                if (oclass.isAssignableFrom(entity.getClass()) && entity.boundingBox.a(axisalignedbb)) {
                    list.add(entity);
                }
            }
        }
    }

    public boolean a(boolean flag) {
        if (this.p) {
            return false;
        } else {
            if (flag) {
                if (this.q && this.d.e != this.r) {
                    return true;
                }
            } else if (this.q && this.d.e >= this.r + 600L) {
                return true;
            }

            return this.o;
        }
    }

    public int a(byte[] abyte, int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1;
        int i2;
        int j2;
        int k2;

        for (l1 = i; l1 < l; ++l1) {
            for (i2 = k; i2 < j1; ++i2) {
                j2 = l1 << 11 | i2 << 7 | j;
                k2 = i1 - j;
                System.arraycopy(this.b, j2, abyte, k1, k2);
                k1 += k2;
            }
        }

        for (l1 = i; l1 < l; ++l1) {
            for (i2 = k; i2 < j1; ++i2) {
                j2 = (l1 << 11 | i2 << 7 | j) >> 1;
                k2 = (i1 - j) / 2;
                System.arraycopy(this.e.a, j2, abyte, k1, k2);
                k1 += k2;
            }
        }

        for (l1 = i; l1 < l; ++l1) {
            for (i2 = k; i2 < j1; ++i2) {
                j2 = (l1 << 11 | i2 << 7 | j) >> 1;
                k2 = (i1 - j) / 2;
                System.arraycopy(this.g.a, j2, abyte, k1, k2);
                k1 += k2;
            }
        }

        for (l1 = i; l1 < l; ++l1) {
            for (i2 = k; i2 < j1; ++i2) {
                j2 = (l1 << 11 | i2 << 7 | j) >> 1;
                k2 = (i1 - j) / 2;
                System.arraycopy(this.f.a, j2, abyte, k1, k2);
                k1 += k2;
            }
        }

        return k1;
    }

    public Random a(long i) {
        return new Random(this.d.u + (long) (this.j * this.j * 4987142) + (long) (this.j * 5947611) + (long) (this.k * this.k) * 4392871L + (long) (this.k * 389711) ^ i);
    }

    public boolean g() {
        return false;
    }
}
