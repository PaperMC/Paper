package net.minecraft.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit; // CraftBukkit

public class Chunk {

    public static boolean a;
    private ChunkSection[] sections;
    private byte[] s;
    public int[] b;
    public boolean[] c;
    public boolean d;
    public World world;
    public int[] heightMap;
    public final int x;
    public final int z;
    private boolean t;
    public Map tileEntities;
    public List[] entitySlices;
    public boolean done;
    public boolean l;
    public boolean m;
    public long n;
    public boolean seenByPlayer;
    public int p;
    public long q;
    private int u;

    public Chunk(World world, int i, int j) {
        this.sections = new ChunkSection[16];
        this.s = new byte[256];
        this.b = new int[256];
        this.c = new boolean[256];
        this.tileEntities = new HashMap();
        this.u = 4096;
        this.entitySlices = new List[16];
        this.world = world;
        this.x = i;
        this.z = j;
        this.heightMap = new int[256];

        for (int k = 0; k < this.entitySlices.length; ++k) {
            this.entitySlices[k] = new org.bukkit.craftbukkit.util.UnsafeList(); // CraftBukkit - ArrayList -> UnsafeList
        }

        Arrays.fill(this.b, -999);
        Arrays.fill(this.s, (byte) -1);

        // CraftBukkit start
        if (!(this instanceof EmptyChunk)) {
            this.bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(this);
        }
    }

    public org.bukkit.Chunk bukkitChunk;
    public boolean mustSave;
    // CraftBukkit end

    public Chunk(World world, byte[] abyte, int i, int j) {
        this(world, i, j);
        int k = abyte.length / 256;

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j1 = 0; j1 < k; ++j1) {
                    byte b0 = abyte[l << 11 | i1 << 7 | j1];

                    if (b0 != 0) {
                        int k1 = j1 >> 4;

                        if (this.sections[k1] == null) {
                            this.sections[k1] = new ChunkSection(k1 << 4, !world.worldProvider.g);
                        }

                        this.sections[k1].setTypeId(l, j1 & 15, i1, b0);
                    }
                }
            }
        }
    }

    public boolean a(int i, int j) {
        return i == this.x && j == this.z;
    }

    public int b(int i, int j) {
        return this.heightMap[j << 4 | i];
    }

    public int h() {
        for (int i = this.sections.length - 1; i >= 0; --i) {
            if (this.sections[i] != null) {
                return this.sections[i].getYPosition();
            }
        }

        return 0;
    }

    public ChunkSection[] i() {
        return this.sections;
    }

    public void initLighting() {
        int i = this.h();

        this.p = Integer.MAX_VALUE;

        int j;
        int k;

        for (j = 0; j < 16; ++j) {
            k = 0;

            while (k < 16) {
                this.b[j + (k << 4)] = -999;
                int l = i + 16 - 1;

                while (true) {
                    if (l > 0) {
                        if (this.b(j, l - 1, k) == 0) {
                            --l;
                            continue;
                        }

                        this.heightMap[k << 4 | j] = l;
                        if (l < this.p) {
                            this.p = l;
                        }
                    }

                    if (!this.world.worldProvider.g) {
                        l = 15;
                        int i1 = i + 16 - 1;

                        do {
                            l -= this.b(j, i1, k);
                            if (l > 0) {
                                ChunkSection chunksection = this.sections[i1 >> 4];

                                if (chunksection != null) {
                                    chunksection.setSkyLight(j, i1 & 15, k, l);
                                    this.world.p((this.x << 4) + j, i1, (this.z << 4) + k);
                                }
                            }

                            --i1;
                        } while (i1 > 0 && l > 0);
                    }

                    ++k;
                    break;
                }
            }
        }

        this.l = true;

        for (j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                this.e(j, k);
            }
        }
    }

    private void e(int i, int j) {
        this.c[i + j * 16] = true;
        this.t = true;
    }

    private void q() {
        this.world.methodProfiler.a("recheckGaps");
        if (this.world.areChunksLoaded(this.x * 16 + 8, 0, this.z * 16 + 8, 16)) {
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (this.c[i + j * 16]) {
                        this.c[i + j * 16] = false;
                        int k = this.b(i, j);
                        int l = this.x * 16 + i;
                        int i1 = this.z * 16 + j;
                        int j1 = this.world.g(l - 1, i1);
                        int k1 = this.world.g(l + 1, i1);
                        int l1 = this.world.g(l, i1 - 1);
                        int i2 = this.world.g(l, i1 + 1);

                        if (k1 < j1) {
                            j1 = k1;
                        }

                        if (l1 < j1) {
                            j1 = l1;
                        }

                        if (i2 < j1) {
                            j1 = i2;
                        }

                        this.g(l, i1, j1);
                        this.g(l - 1, i1, k);
                        this.g(l + 1, i1, k);
                        this.g(l, i1 - 1, k);
                        this.g(l, i1 + 1, k);
                    }
                }
            }

            this.t = false;
        }

        this.world.methodProfiler.b();
    }

    private void g(int i, int j, int k) {
        int l = this.world.getHighestBlockYAt(i, j);

        if (l > k) {
            this.d(i, j, k, l + 1);
        } else if (l < k) {
            this.d(i, j, l, k + 1);
        }
    }

    private void d(int i, int j, int k, int l) {
        if (l > k && this.world.areChunksLoaded(i, 0, j, 16)) {
            for (int i1 = k; i1 < l; ++i1) {
                this.world.c(EnumSkyBlock.SKY, i, i1, j);
            }

            this.l = true;
        }
    }

    private void h(int i, int j, int k) {
        int l = this.heightMap[k << 4 | i] & 255;
        int i1 = l;

        if (j > l) {
            i1 = j;
        }

        while (i1 > 0 && this.b(i, i1 - 1, k) == 0) {
            --i1;
        }

        if (i1 != l) {
            this.world.e(i + this.x * 16, k + this.z * 16, i1, l);
            this.heightMap[k << 4 | i] = i1;
            int j1 = this.x * 16 + i;
            int k1 = this.z * 16 + k;
            int l1;
            int i2;

            if (!this.world.worldProvider.g) {
                ChunkSection chunksection;

                if (i1 < l) {
                    for (l1 = i1; l1 < l; ++l1) {
                        chunksection = this.sections[l1 >> 4];
                        if (chunksection != null) {
                            chunksection.setSkyLight(i, l1 & 15, k, 15);
                            this.world.p((this.x << 4) + i, l1, (this.z << 4) + k);
                        }
                    }
                } else {
                    for (l1 = l; l1 < i1; ++l1) {
                        chunksection = this.sections[l1 >> 4];
                        if (chunksection != null) {
                            chunksection.setSkyLight(i, l1 & 15, k, 0);
                            this.world.p((this.x << 4) + i, l1, (this.z << 4) + k);
                        }
                    }
                }

                l1 = 15;

                while (i1 > 0 && l1 > 0) {
                    --i1;
                    i2 = this.b(i, i1, k);
                    if (i2 == 0) {
                        i2 = 1;
                    }

                    l1 -= i2;
                    if (l1 < 0) {
                        l1 = 0;
                    }

                    ChunkSection chunksection1 = this.sections[i1 >> 4];

                    if (chunksection1 != null) {
                        chunksection1.setSkyLight(i, i1 & 15, k, l1);
                    }
                }
            }

            l1 = this.heightMap[k << 4 | i];
            i2 = l;
            int j2 = l1;

            if (l1 < l) {
                i2 = l1;
                j2 = l;
            }

            if (l1 < this.p) {
                this.p = l1;
            }

            if (!this.world.worldProvider.g) {
                this.d(j1 - 1, k1, i2, j2);
                this.d(j1 + 1, k1, i2, j2);
                this.d(j1, k1 - 1, i2, j2);
                this.d(j1, k1 + 1, i2, j2);
                this.d(j1, k1, i2, j2);
            }

            this.l = true;
        }
    }

    public int b(int i, int j, int k) {
        return Block.lightBlock[this.getTypeId(i, j, k)];
    }

    public int getTypeId(int i, int j, int k) {
        if (j >> 4 >= this.sections.length) {
            return 0;
        } else {
            ChunkSection chunksection = this.sections[j >> 4];

            return chunksection != null ? chunksection.getTypeId(i, j & 15, k) : 0;
        }
    }

    public int getData(int i, int j, int k) {
        if (j >> 4 >= this.sections.length) {
            return 0;
        } else {
            ChunkSection chunksection = this.sections[j >> 4];

            return chunksection != null ? chunksection.getData(i, j & 15, k) : 0;
        }
    }

    public boolean a(int i, int j, int k, int l, int i1) {
        int j1 = k << 4 | i;

        if (j >= this.b[j1] - 1) {
            this.b[j1] = -999;
        }

        int k1 = this.heightMap[j1];
        int l1 = this.getTypeId(i, j, k);
        int i2 = this.getData(i, j, k);

        if (l1 == l && i2 == i1) {
            return false;
        } else {
            ChunkSection chunksection = this.sections[j >> 4];
            boolean flag = false;

            if (chunksection == null) {
                if (l == 0) {
                    return false;
                }

                chunksection = this.sections[j >> 4] = new ChunkSection(j >> 4 << 4, !this.world.worldProvider.g);
                flag = j >= k1;
            }

            int j2 = this.x * 16 + i;
            int k2 = this.z * 16 + k;

            if (l1 != 0 && !this.world.isStatic) {
                Block.byId[l1].l(this.world, j2, j, k2, i2);
            }

            chunksection.setTypeId(i, j & 15, k, l);
            if (l1 != 0) {
                if (!this.world.isStatic) {
                    Block.byId[l1].remove(this.world, j2, j, k2, l1, i2);
                } else if (Block.byId[l1] instanceof IContainer && l1 != l) {
                    this.world.s(j2, j, k2);
                }
            }

            if (chunksection.getTypeId(i, j & 15, k) != l) {
                return false;
            } else {
                chunksection.setData(i, j & 15, k, i1);
                if (flag) {
                    this.initLighting();
                } else {
                    if (Block.lightBlock[l & 4095] > 0) {
                        if (j >= k1) {
                            this.h(i, j + 1, k);
                        }
                    } else if (j == k1 - 1) {
                        this.h(i, j, k);
                    }

                    this.e(i, k);
                }

                TileEntity tileentity;

                if (l != 0) {
                    // CraftBukkit - Don't place while processing the BlockPlaceEvent, unless it's a BlockContainer
                    if (!this.world.isStatic && (!this.world.callingPlaceEvent || (Block.byId[l] instanceof BlockContainer))) {
                        Block.byId[l].onPlace(this.world, j2, j, k2);
                    }

                    if (Block.byId[l] instanceof IContainer) {
                        // CraftBukkit start - Don't create tile entity if placement failed
                        if (this.getTypeId(i, j, k) != l) {
                            return false;
                        }
                        // CraftBukkit end

                        tileentity = this.e(i, j, k);
                        if (tileentity == null) {
                            tileentity = ((IContainer) Block.byId[l]).b(this.world);
                            this.world.setTileEntity(j2, j, k2, tileentity);
                        }

                        if (tileentity != null) {
                            tileentity.i();
                        }
                    }
                } else if (l1 > 0 && Block.byId[l1] instanceof IContainer) {
                    tileentity = this.e(i, j, k);
                    if (tileentity != null) {
                        tileentity.i();
                    }
                }

                this.l = true;
                return true;
            }
        }
    }

    public boolean b(int i, int j, int k, int l) {
        ChunkSection chunksection = this.sections[j >> 4];

        if (chunksection == null) {
            return false;
        } else {
            int i1 = chunksection.getData(i, j & 15, k);

            if (i1 == l) {
                return false;
            } else {
                this.l = true;
                chunksection.setData(i, j & 15, k, l);
                int j1 = chunksection.getTypeId(i, j & 15, k);

                if (j1 > 0 && Block.byId[j1] instanceof IContainer) {
                    TileEntity tileentity = this.e(i, j, k);

                    if (tileentity != null) {
                        tileentity.i();
                        tileentity.p = l;
                    }
                }

                return true;
            }
        }
    }

    public int getBrightness(EnumSkyBlock enumskyblock, int i, int j, int k) {
        ChunkSection chunksection = this.sections[j >> 4];

        return chunksection == null ? (this.d(i, j, k) ? enumskyblock.c : 0) : (enumskyblock == EnumSkyBlock.SKY ? (this.world.worldProvider.g ? 0 : chunksection.getSkyLight(i, j & 15, k)) : (enumskyblock == EnumSkyBlock.BLOCK ? chunksection.getEmittedLight(i, j & 15, k) : enumskyblock.c));
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        ChunkSection chunksection = this.sections[j >> 4];

        if (chunksection == null) {
            chunksection = this.sections[j >> 4] = new ChunkSection(j >> 4 << 4, !this.world.worldProvider.g);
            this.initLighting();
        }

        this.l = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            if (!this.world.worldProvider.g) {
                chunksection.setSkyLight(i, j & 15, k, l);
            }
        } else if (enumskyblock == EnumSkyBlock.BLOCK) {
            chunksection.setEmittedLight(i, j & 15, k, l);
        }
    }

    public int c(int i, int j, int k, int l) {
        ChunkSection chunksection = this.sections[j >> 4];

        if (chunksection == null) {
            return !this.world.worldProvider.g && l < EnumSkyBlock.SKY.c ? EnumSkyBlock.SKY.c - l : 0;
        } else {
            int i1 = this.world.worldProvider.g ? 0 : chunksection.getSkyLight(i, j & 15, k);

            if (i1 > 0) {
                a = true;
            }

            i1 -= l;
            int j1 = chunksection.getEmittedLight(i, j & 15, k);

            if (j1 > i1) {
                i1 = j1;
            }

            return i1;
        }
    }

    public void a(Entity entity) {
        this.m = true;
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

        entity.ai = true;
        entity.aj = this.x;
        entity.ak = k;
        entity.al = this.z;
        this.entitySlices[k].add(entity);
    }

    public void b(Entity entity) {
        this.a(entity, entity.ak);
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

    public boolean d(int i, int j, int k) {
        return j >= this.heightMap[k << 4 | i];
    }

    public TileEntity e(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        TileEntity tileentity = (TileEntity) this.tileEntities.get(chunkposition);

        if (tileentity == null) {
            int l = this.getTypeId(i, j, k);

            if (l <= 0 || !Block.byId[l].t()) {
                return null;
            }

            if (tileentity == null) {
                tileentity = ((IContainer) Block.byId[l]).b(this.world);
                this.world.setTileEntity(this.x * 16 + i, j, this.z * 16 + k, tileentity);
            }

            tileentity = (TileEntity) this.tileEntities.get(chunkposition);
        }

        if (tileentity != null && tileentity.r()) {
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
        if (this.d) {
            this.world.tileEntityList.add(tileentity);
        }
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        tileentity.b(this.world);
        tileentity.x = this.x * 16 + i;
        tileentity.y = j;
        tileentity.z = this.z * 16 + k;
        if (this.getTypeId(i, j, k) != 0 && Block.byId[this.getTypeId(i, j, k)] instanceof IContainer) {
            if (this.tileEntities.containsKey(chunkposition)) {
                ((TileEntity) this.tileEntities.get(chunkposition)).w_();
            }

            tileentity.s();
            this.tileEntities.put(chunkposition, tileentity);
            // CraftBukkit start
        } else {
            System.out.println("Attempted to place a tile entity (" + tileentity + ") at " + tileentity.x + "," + tileentity.y + "," + tileentity.z
                    + " (" + org.bukkit.Material.getMaterial(getTypeId(i, j, k)) + ") where there was no entity tile!");
            System.out.println("Chunk coordinates: " + (this.x * 16) + "," + (this.z * 16));
            new Exception().printStackTrace();
            // CraftBukkit end
        }
    }

    public void f(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        if (this.d) {
            TileEntity tileentity = (TileEntity) this.tileEntities.remove(chunkposition);

            if (tileentity != null) {
                tileentity.w_();
            }
        }
    }

    public void addEntities() {
        this.d = true;
        this.world.a(this.tileEntities.values());

        for (int i = 0; i < this.entitySlices.length; ++i) {
            Iterator iterator = this.entitySlices[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                entity.R();
            }

            this.world.a(this.entitySlices[i]);
        }
    }

    public void removeEntities() {
        this.d = false;
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

                // Do not pass along players, as doing so can get them stuck outside of time.
                // (which for example disables inventory icon updates and prevents block breaking)
                if (entity instanceof EntityPlayer) {
                    iter.remove();
                }
            }
            // CraftBukkit end

            this.world.b(this.entitySlices[i]);
        }
    }

    public void e() {
        this.l = true;
    }

    public void a(Entity entity, AxisAlignedBB axisalignedbb, List list, IEntitySelector ientityselector) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.e + 2.0D) / 16.0D);

        if (i < 0) {
            i = 0;
            j = Math.max(i, j);
        }

        if (j >= this.entitySlices.length) {
            j = this.entitySlices.length - 1;
            i = Math.min(i, j);
        }

        for (int k = i; k <= j; ++k) {
            List list1 = this.entitySlices[k];

            for (int l = 0; l < list1.size(); ++l) {
                Entity entity1 = (Entity) list1.get(l);

                if (entity1 != entity && entity1.boundingBox.b(axisalignedbb) && (ientityselector == null || ientityselector.a(entity1))) {
                    list.add(entity1);
                    Entity[] aentity = entity1.ao();

                    if (aentity != null) {
                        for (int i1 = 0; i1 < aentity.length; ++i1) {
                            entity1 = aentity[i1];
                            if (entity1 != entity && entity1.boundingBox.b(axisalignedbb) && (ientityselector == null || ientityselector.a(entity1))) {
                                list.add(entity1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void a(Class oclass, AxisAlignedBB axisalignedbb, List list, IEntitySelector ientityselector) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.e + 2.0D) / 16.0D);

        if (i < 0) {
            i = 0;
        } else if (i >= this.entitySlices.length) {
            i = this.entitySlices.length - 1;
        }

        if (j >= this.entitySlices.length) {
            j = this.entitySlices.length - 1;
        } else if (j < 0) {
            j = 0;
        }

        for (int k = i; k <= j; ++k) {
            List list1 = this.entitySlices[k];

            for (int l = 0; l < list1.size(); ++l) {
                Entity entity = (Entity) list1.get(l);

                if (oclass.isAssignableFrom(entity.getClass()) && entity.boundingBox.b(axisalignedbb) && (ientityselector == null || ientityselector.a(entity))) {
                    list.add(entity);
                }
            }
        }
    }

    public boolean a(boolean flag) {
        if (flag) {
            if (this.m && this.world.getTime() != this.n || this.l) {
                return true;
            }
        } else if (this.m && this.world.getTime() >= this.n + 600L) {
            return true;
        }

        return this.l;
    }

    public Random a(long i) {
        return new Random(this.world.getSeed() + (long) (this.x * this.x * 4987142) + (long) (this.x * 5947611) + (long) (this.z * this.z) * 4392871L + (long) (this.z * 389711) ^ i);
    }

    public boolean isEmpty() {
        return false;
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

    public int d(int i, int j) {
        int k = i | j << 4;
        int l = this.b[k];

        if (l == -999) {
            int i1 = this.h() + 15;

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

            this.b[k] = l;
        }

        return l;
    }

    public void k() {
        if (this.t && !this.world.worldProvider.g) {
            this.q();
        }
    }

    public ChunkCoordIntPair l() {
        return new ChunkCoordIntPair(this.x, this.z);
    }

    public boolean c(int i, int j) {
        if (i < 0) {
            i = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        for (int k = i; k <= j; k += 16) {
            ChunkSection chunksection = this.sections[k >> 4];

            if (chunksection != null && !chunksection.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void a(ChunkSection[] achunksection) {
        this.sections = achunksection;
    }

    public BiomeBase a(int i, int j, WorldChunkManager worldchunkmanager) {
        int k = this.s[j << 4 | i] & 255;

        if (k == 255) {
            BiomeBase biomebase = worldchunkmanager.getBiome((this.x << 4) + i, (this.z << 4) + j);

            k = biomebase.id;
            this.s[j << 4 | i] = (byte) (k & 255);
        }

        return BiomeBase.biomes[k] == null ? BiomeBase.PLAINS : BiomeBase.biomes[k];
    }

    public byte[] m() {
        return this.s;
    }

    public void a(byte[] abyte) {
        this.s = abyte;
    }

    public void n() {
        this.u = 0;
    }

    public void o() {
        for (int i = 0; i < 8; ++i) {
            if (this.u >= 4096) {
                return;
            }

            int j = this.u % 16;
            int k = this.u / 16 % 16;
            int l = this.u / 256;

            ++this.u;
            int i1 = (this.x << 4) + k;
            int j1 = (this.z << 4) + l;

            for (int k1 = 0; k1 < 16; ++k1) {
                int l1 = (j << 4) + k1;

                if (this.sections[j] == null && (k1 == 0 || k1 == 15 || k == 0 || k == 15 || l == 0 || l == 15) || this.sections[j] != null && this.sections[j].getTypeId(k, k1, l) == 0) {
                    if (Block.lightEmission[this.world.getTypeId(i1, l1 - 1, j1)] > 0) {
                        this.world.A(i1, l1 - 1, j1);
                    }

                    if (Block.lightEmission[this.world.getTypeId(i1, l1 + 1, j1)] > 0) {
                        this.world.A(i1, l1 + 1, j1);
                    }

                    if (Block.lightEmission[this.world.getTypeId(i1 - 1, l1, j1)] > 0) {
                        this.world.A(i1 - 1, l1, j1);
                    }

                    if (Block.lightEmission[this.world.getTypeId(i1 + 1, l1, j1)] > 0) {
                        this.world.A(i1 + 1, l1, j1);
                    }

                    if (Block.lightEmission[this.world.getTypeId(i1, l1, j1 - 1)] > 0) {
                        this.world.A(i1, l1, j1 - 1);
                    }

                    if (Block.lightEmission[this.world.getTypeId(i1, l1, j1 + 1)] > 0) {
                        this.world.A(i1, l1, j1 + 1);
                    }

                    this.world.A(i1, l1, j1);
                }
            }
        }
    }
}
