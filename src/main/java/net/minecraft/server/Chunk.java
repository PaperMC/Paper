package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// CraftBukkit start
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
// CraftBukkit end

public class Chunk {

    public static boolean a;
    public byte[] b;
    public boolean c;
    public World world;
    public NibbleArray e;
    public NibbleArray f;
    public NibbleArray g;
    public byte[] h;
    public int i;
    public final int x;
    public final int z;
    public Map tileEntities;
    public List[] entitySlices;
    public boolean done;
    public boolean o;
    public boolean p;
    public boolean q;
    public long r;

    public Chunk(World world, int i, int j) {
        this.tileEntities = new HashMap();
        this.entitySlices = new List[8];
        this.done = false;
        this.o = false;
        this.q = false;
        this.r = 0L;
        this.world = world;
        this.x = i;
        this.z = j;
        this.h = new byte[256];

        for (int k = 0; k < this.entitySlices.length; ++k) {
            this.entitySlices[k] = new ArrayList();
        }

        // CraftBukkit start
        CraftWorld cw = ((WorldServer) world).getWorld();
        bukkitChunk = (cw == null) ? null : cw.popPreservedChunk(i, j);
        if (bukkitChunk == null) {
            bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(this);
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
        return i == this.x && j == this.z;
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

                for (i1 = j << 11 | k << 7; l > 0 && Block.q[this.b[i1 + l - 1] & 255] == 0; --l) {
                    ;
                }

                this.h[k << 4 | j] = (byte) l;
                if (l < i) {
                    i = l;
                }

                if (!this.world.worldProvider.e) {
                    int j1 = 15;
                    int k1 = 127;

                    do {
                        j1 -= Block.q[this.b[i1 + k1] & 255];
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

    public void loadNOP() {}

    private void c(int i, int j) {
        int k = this.b(i, j);
        int l = this.x * 16 + i;
        int i1 = this.z * 16 + j;

        this.f(l - 1, i1, k);
        this.f(l + 1, i1, k);
        this.f(l, i1 - 1, k);
        this.f(l, i1 + 1, k);
    }

    private void f(int i, int j, int k) {
        int l = this.world.getHighestBlockYAt(i, j);

        if (l > k) {
            this.world.a(EnumSkyBlock.SKY, i, k, j, i, l, j);
            this.o = true;
        } else if (l < k) {
            this.world.a(EnumSkyBlock.SKY, i, l, j, i, k, j);
            this.o = true;
        }
    }

    private void g(int i, int j, int k) {
        int l = this.h[k << 4 | i] & 255;
        int i1 = l;

        if (j > l) {
            i1 = j;
        }

        for (int j1 = i << 11 | k << 7; i1 > 0 && Block.q[this.b[j1 + i1 - 1] & 255] == 0; --i1) {
            ;
        }

        if (i1 != l) {
            this.world.g(i, k, i1, l);
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

            k1 = this.x * 16 + i;
            l1 = this.z * 16 + k;
            if (i1 < l) {
                for (i2 = i1; i2 < l; ++i2) {
                    this.f.a(i, i2, k, 15);
                }
            } else {
                this.world.a(EnumSkyBlock.SKY, k1, l, l1, k1, i1, l1);

                for (i2 = l; i2 < i1; ++i2) {
                    this.f.a(i, i2, k, 0);
                }
            }

            i2 = 15;

            int j2;

            for (j2 = i1; i1 > 0 && i2 > 0; this.f.a(i, i1, k, i2)) {
                --i1;
                int k2 = Block.q[this.getTypeId(i, i1, k)];

                if (k2 == 0) {
                    k2 = 1;
                }

                i2 -= k2;
                if (i2 < 0) {
                    i2 = 0;
                }
            }

            while (i1 > 0 && Block.q[this.getTypeId(i, i1 - 1, k)] == 0) {
                --i1;
            }

            if (i1 != j2) {
                this.world.a(EnumSkyBlock.SKY, k1 - 1, i1, l1 - 1, k1 + 1, j2, l1 + 1);
            }

            this.o = true;
        }
    }

    public int getTypeId(int i, int j, int k) {
        return this.b[i << 11 | k << 7 | j] & 255;
    }

    public boolean a(int i, int j, int k, int l, int i1) {
        byte b0 = (byte) l;
        int j1 = this.h[k << 4 | i] & 255;
        int k1 = this.b[i << 11 | k << 7 | j] & 255;

        if (k1 == l && this.e.a(i, j, k) == i1) {
            return false;
        } else {
            int l1 = this.x * 16 + i;
            int i2 = this.z * 16 + k;

            this.b[i << 11 | k << 7 | j] = (byte) (b0 & 255);
            if (k1 != 0 && !this.world.isStatic) {
                Block.byId[k1].remove(this.world, l1, j, i2);
            }

            this.e.a(i, j, k, i1);
            if (!this.world.worldProvider.e) {
                if (Block.q[b0 & 255] != 0) {
                    if (j >= j1) {
                        this.g(i, j + 1, k);
                    }
                } else if (j == j1 - 1) {
                    this.g(i, j, k);
                }

                this.world.a(EnumSkyBlock.SKY, l1, j, i2, l1, j, i2);
            }

            this.world.a(EnumSkyBlock.BLOCK, l1, j, i2, l1, j, i2);
            this.c(i, k);
            this.e.a(i, j, k, i1);
            if (l != 0) {
                Block.byId[l].e(this.world, l1, j, i2);
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
            int k1 = this.x * 16 + i;
            int l1 = this.z * 16 + k;

            this.b[i << 11 | k << 7 | j] = (byte) (b0 & 255);
            if (j1 != 0) {
                Block.byId[j1].remove(this.world, k1, j, l1);
            }

            this.e.a(i, j, k, 0);
            if (Block.q[b0 & 255] != 0) {
                if (j >= i1) {
                    this.g(i, j + 1, k);
                }
            } else if (j == i1 - 1) {
                this.g(i, j, k);
            }

            this.world.a(EnumSkyBlock.SKY, k1, j, l1, k1, j, l1);
            this.world.a(EnumSkyBlock.BLOCK, k1, j, l1, k1, j, l1);
            this.c(i, k);
            if (l != 0 && !this.world.isStatic) {
                Block.byId[l].e(this.world, k1, j, l1);
            }

            this.o = true;
            return true;
        }
    }

    public int getData(int i, int j, int k) {
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
        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);

        if (i != this.x || j != this.z) {
            System.out.println("Wrong location! " + entity);
            // CraftBukkit
            System.out.println("" + entity.locX + "," + entity.locZ + "(" + i + "," + j + ") vs " + this.x + "," + this.z);
            Thread.dumpStack();
        }

        int k = MathHelper.floor(entity.locY / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.entitySlices.length) {
            k = this.entitySlices.length - 1;
        }

        entity.bE = true;
        entity.bF = this.x;
        entity.bG = k;
        entity.bH = this.z;
        this.entitySlices[k].add(entity);
    }

    public void b(Entity entity) {
        this.a(entity, entity.bG);
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
        return j >= (this.h[k << 4 | i] & 255);
    }

    public TileEntity d(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        TileEntity tileentity = (TileEntity) this.tileEntities.get(chunkposition);

        if (tileentity == null) {
            int l = this.getTypeId(i, j, k);

            if (!Block.isTileEntity[l]) {
                return null;
            }

            BlockContainer blockcontainer = (BlockContainer) Block.byId[l];

            blockcontainer.e(this.world, this.x * 16 + i, j, this.z * 16 + k);
            tileentity = (TileEntity) this.tileEntities.get(chunkposition);
        }

        return tileentity;
    }

    public void a(TileEntity tileentity) {
        int i = tileentity.e - this.x * 16;
        int j = tileentity.f;
        int k = tileentity.g - this.z * 16;

        this.a(i, j, k, tileentity);
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        tileentity.world = this.world;
        tileentity.e = this.x * 16 + i;
        tileentity.f = j;
        tileentity.g = this.z * 16 + k;
        if (this.getTypeId(i, j, k) != 0 && Block.byId[this.getTypeId(i, j, k)] instanceof BlockContainer) {
            if (this.c) {
                if (this.tileEntities.get(chunkposition) != null) {
                    this.world.c.remove(this.tileEntities.get(chunkposition));
                }

                this.world.c.add(tileentity);
            }

            this.tileEntities.put(chunkposition, tileentity);
        } else {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
        }
    }

    public void e(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        if (this.c) {
            this.world.c.remove(this.tileEntities.remove(chunkposition));
        }
    }

    public void addEntities() {
        this.c = true;
        this.world.c.addAll(this.tileEntities.values());

        for (int i = 0; i < this.entitySlices.length; ++i) {
            this.world.a(this.entitySlices[i]);
        }
    }

    public void removeEntities() {
        this.c = false;
        this.world.c.removeAll(this.tileEntities.values());

        for (int i = 0; i < this.entitySlices.length; ++i) {
            // CraftBukkit start
            Iterator<Object> iter = this.entitySlices[i].iterator();
            while (iter.hasNext()) {
                Entity entity = (Entity) iter.next();
                int cx = Location.locToBlock(entity.locX) >> 4;
                int cz = Location.locToBlock(entity.locZ) >> 4;

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
        this.o = true;
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
        if (this.p) {
            return false;
        } else {
            if (flag) {
                if (this.q && this.world.getTime() != this.r) {
                    return true;
                }
            } else if (this.q && this.world.getTime() >= this.r + 600L) {
                return true;
            }

            return this.o;
        }
    }

    public int a(byte[] abyte, int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1 = l - i;
        int i2 = i1 - j;
        int j2 = j1 - k;

        if (l1 * i2 * j2 == this.b.length) {
            System.arraycopy(this.b, 0, abyte, k1, this.b.length);
            k1 += this.b.length;
            System.arraycopy(this.e.a, 0, abyte, k1, this.e.a.length);
            k1 += this.e.a.length;
            System.arraycopy(this.g.a, 0, abyte, k1, this.g.a.length);
            k1 += this.g.a.length;
            System.arraycopy(this.f.a, 0, abyte, k1, this.f.a.length);
            k1 += this.f.a.length;
            return k1;
        } else {
            int k2;
            int l2;
            int i3;
            int j3;

            for (k2 = i; k2 < l; ++k2) {
                for (l2 = k; l2 < j1; ++l2) {
                    i3 = k2 << 11 | l2 << 7 | j;
                    j3 = i1 - j;
                    System.arraycopy(this.b, i3, abyte, k1, j3);
                    k1 += j3;
                }
            }

            for (k2 = i; k2 < l; ++k2) {
                for (l2 = k; l2 < j1; ++l2) {
                    i3 = (k2 << 11 | l2 << 7 | j) >> 1;
                    j3 = (i1 - j) / 2;
                    System.arraycopy(this.e.a, i3, abyte, k1, j3);
                    k1 += j3;
                }
            }

            for (k2 = i; k2 < l; ++k2) {
                for (l2 = k; l2 < j1; ++l2) {
                    i3 = (k2 << 11 | l2 << 7 | j) >> 1;
                    j3 = (i1 - j) / 2;
                    System.arraycopy(this.g.a, i3, abyte, k1, j3);
                    k1 += j3;
                }
            }

            for (k2 = i; k2 < l; ++k2) {
                for (l2 = k; l2 < j1; ++l2) {
                    i3 = (k2 << 11 | l2 << 7 | j) >> 1;
                    j3 = (i1 - j) / 2;
                    System.arraycopy(this.f.a, i3, abyte, k1, j3);
                    k1 += j3;
                }
            }

            return k1;
        }
    }

    public Random a(long i) {
        return new Random(this.world.getSeed() + (long) (this.x * this.x * 4987142) + (long) (this.x * 5947611) + (long) (this.z * this.z) * 4392871L + (long) (this.z * 389711) ^ i);
    }

    public boolean g() {
        return false;
    }

    public void h() {
        BlockRegister.a(this.b);
    }
}
