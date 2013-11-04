package net.minecraft.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bukkit.Bukkit; // CraftBukkit

public class Chunk {

    private static final Logger t = LogManager.getLogger();
    public static boolean a;
    private ChunkSection[] sections;
    private byte[] v;
    public int[] b;
    public boolean[] c;
    public boolean d;
    public World world;
    public int[] heightMap;
    public final int locX;
    public final int locZ;
    private boolean w;
    public Map tileEntities;
    public List[] entitySlices;
    public boolean done;
    public boolean lit;
    public boolean m;
    public boolean n;
    public boolean o;
    public long p;
    public boolean q;
    public int r;
    public long s;
    private int x;

    public Chunk(World world, int i, int j) {
        this.sections = new ChunkSection[16];
        this.v = new byte[256];
        this.b = new int[256];
        this.c = new boolean[256];
        this.tileEntities = new HashMap();
        this.x = 4096;
        this.entitySlices = new List[16];
        this.world = world;
        this.locX = i;
        this.locZ = j;
        this.heightMap = new int[256];

        for (int k = 0; k < this.entitySlices.length; ++k) {
            this.entitySlices[k] = new org.bukkit.craftbukkit.util.UnsafeList(); // CraftBukkit - ArrayList -> UnsafeList
        }

        Arrays.fill(this.b, -999);
        Arrays.fill(this.v, (byte) -1);

        // CraftBukkit start
        if (!(this instanceof EmptyChunk)) {
            this.bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(this);
        }
    }

    public org.bukkit.Chunk bukkitChunk;
    public boolean mustSave;
    // CraftBukkit end

    public Chunk(World world, Block[] ablock, int i, int j) {
        this(world, i, j);
        int k = ablock.length / 256;
        boolean flag = !world.worldProvider.g;

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j1 = 0; j1 < k; ++j1) {
                    Block block = ablock[l << 11 | i1 << 7 | j1];

                    if (block != null && block.getMaterial() != Material.AIR) {
                        int k1 = j1 >> 4;

                        if (this.sections[k1] == null) {
                            this.sections[k1] = new ChunkSection(k1 << 4, flag);
                        }

                        this.sections[k1].setTypeId(l, j1 & 15, i1, block);
                    }
                }
            }
        }
    }

    public Chunk(World world, Block[] ablock, byte[] abyte, int i, int j) {
        this(world, i, j);
        int k = ablock.length / 256;
        boolean flag = !world.worldProvider.g;

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j1 = 0; j1 < k; ++j1) {
                    int k1 = l * k * 16 | i1 * k | j1;
                    Block block = ablock[k1];

                    if (block != null && block != Blocks.AIR) {
                        int l1 = j1 >> 4;

                        if (this.sections[l1] == null) {
                            this.sections[l1] = new ChunkSection(l1 << 4, flag);
                        }

                        this.sections[l1].setTypeId(l, j1 & 15, i1, block);
                        this.sections[l1].setData(l, j1 & 15, i1, abyte[k1]);
                    }
                }
            }
        }
    }

    public boolean a(int i, int j) {
        return i == this.locX && j == this.locZ;
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

        this.r = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j) {
            int k = 0;

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
                        if (l < this.r) {
                            this.r = l;
                        }
                    }

                    if (!this.world.worldProvider.g) {
                        l = 15;
                        int i1 = i + 16 - 1;

                        do {
                            int j1 = this.b(j, i1, k);

                            if (j1 == 0 && l != 15) {
                                j1 = 1;
                            }

                            l -= j1;
                            if (l > 0) {
                                ChunkSection chunksection = this.sections[i1 >> 4];

                                if (chunksection != null) {
                                    chunksection.setSkyLight(j, i1 & 15, k, l);
                                    this.world.m((this.locX << 4) + j, i1, (this.locZ << 4) + k);
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

        this.n = true;
    }

    private void e(int i, int j) {
        this.c[i + j * 16] = true;
        this.w = true;
    }

    private void c(boolean flag) {
        this.world.methodProfiler.a("recheckGaps");
        if (this.world.areChunksLoaded(this.locX * 16 + 8, 0, this.locZ * 16 + 8, 16)) {
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (this.c[i + j * 16]) {
                        this.c[i + j * 16] = false;
                        int k = this.b(i, j);
                        int l = this.locX * 16 + i;
                        int i1 = this.locZ * 16 + j;
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
                        if (flag) {
                            this.world.methodProfiler.b();
                            return;
                        }
                    }
                }
            }

            this.w = false;
        }

        this.world.methodProfiler.b();
    }

    private void g(int i, int j, int k) {
        int l = this.world.getHighestBlockYAt(i, j);

        if (l > k) {
            this.c(i, j, k, l + 1);
        } else if (l < k) {
            this.c(i, j, l, k + 1);
        }
    }

    private void c(int i, int j, int k, int l) {
        if (l > k && this.world.areChunksLoaded(i, 0, j, 16)) {
            for (int i1 = k; i1 < l; ++i1) {
                this.world.c(EnumSkyBlock.SKY, i, i1, j);
            }

            this.n = true;
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
            this.world.b(i + this.locX * 16, k + this.locZ * 16, i1, l);
            this.heightMap[k << 4 | i] = i1;
            int j1 = this.locX * 16 + i;
            int k1 = this.locZ * 16 + k;
            int l1;
            int i2;

            if (!this.world.worldProvider.g) {
                ChunkSection chunksection;

                if (i1 < l) {
                    for (l1 = i1; l1 < l; ++l1) {
                        chunksection = this.sections[l1 >> 4];
                        if (chunksection != null) {
                            chunksection.setSkyLight(i, l1 & 15, k, 15);
                            this.world.m((this.locX << 4) + i, l1, (this.locZ << 4) + k);
                        }
                    }
                } else {
                    for (l1 = l; l1 < i1; ++l1) {
                        chunksection = this.sections[l1 >> 4];
                        if (chunksection != null) {
                            chunksection.setSkyLight(i, l1 & 15, k, 0);
                            this.world.m((this.locX << 4) + i, l1, (this.locZ << 4) + k);
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

            if (l1 < this.r) {
                this.r = l1;
            }

            if (!this.world.worldProvider.g) {
                this.c(j1 - 1, k1, i2, j2);
                this.c(j1 + 1, k1, i2, j2);
                this.c(j1, k1 - 1, i2, j2);
                this.c(j1, k1 + 1, i2, j2);
                this.c(j1, k1, i2, j2);
            }

            this.n = true;
        }
    }

    public int b(int i, int j, int k) {
        return this.getType(i, j, k).k();
    }

    public Block getType(int i, int j, int k) {
        Block block = Blocks.AIR;

        if (j >> 4 < this.sections.length) {
            ChunkSection chunksection = this.sections[j >> 4];

            if (chunksection != null) {
                try {
                    block = chunksection.getTypeId(i, j & 15, k);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.a(throwable, "Getting block");
                    CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being got");

                    crashreportsystemdetails.a("Location", (Callable) (new CrashReportLocation(this, i, j, k)));
                    throw new ReportedException(crashreport);
                }
            }
        }

        return block;
    }

    public int getData(int i, int j, int k) {
        if (j >> 4 >= this.sections.length) {
            return 0;
        } else {
            ChunkSection chunksection = this.sections[j >> 4];

            return chunksection != null ? chunksection.getData(i, j & 15, k) : 0;
        }
    }

    public boolean a(int i, int j, int k, Block block, int l) {
        int i1 = k << 4 | i;

        if (j >= this.b[i1] - 1) {
            this.b[i1] = -999;
        }

        int j1 = this.heightMap[i1];
        Block block1 = this.getType(i, j, k);
        int k1 = this.getData(i, j, k);

        if (block1 == block && k1 == l) {
            return false;
        } else {
            ChunkSection chunksection = this.sections[j >> 4];
            boolean flag = false;

            if (chunksection == null) {
                if (block == Blocks.AIR) {
                    return false;
                }

                chunksection = this.sections[j >> 4] = new ChunkSection(j >> 4 << 4, !this.world.worldProvider.g);
                flag = j >= j1;
            }

            int l1 = this.locX * 16 + i;
            int i2 = this.locZ * 16 + k;

            if (!this.world.isStatic) {
                block1.f(this.world, l1, j, i2, k1);
            }

            chunksection.setTypeId(i, j & 15, k, block);
            if (!this.world.isStatic) {
                block1.remove(this.world, l1, j, i2, block1, k1);
            } else if (block1 instanceof IContainer && block1 != block) {
                this.world.p(l1, j, i2);
            }

            if (chunksection.getTypeId(i, j & 15, k) != block) {
                return false;
            } else {
                chunksection.setData(i, j & 15, k, l);
                if (flag) {
                    this.initLighting();
                } else {
                    int j2 = block.k();
                    int k2 = block1.k();

                    if (j2 > 0) {
                        if (j >= j1) {
                            this.h(i, j + 1, k);
                        }
                    } else if (j == j1 - 1) {
                        this.h(i, j, k);
                    }

                    if (j2 != k2 && (j2 < k2 || this.getBrightness(EnumSkyBlock.SKY, i, j, k) > 0 || this.getBrightness(EnumSkyBlock.BLOCK, i, j, k) > 0)) {
                        this.e(i, k);
                    }
                }

                TileEntity tileentity;

                if (block1 instanceof IContainer) {
                    tileentity = this.e(i, j, k);
                    if (tileentity != null) {
                        tileentity.u();
                    }
                }

                // CraftBukkit - Don't place while processing the BlockPlaceEvent, unless it's a BlockContainer
                if (!this.world.isStatic && (!this.world.callingPlaceEvent || (block instanceof BlockContainer))) {
                    block.onPlace(this.world, l1, j, i2);
                }

                if (block instanceof IContainer) {
                    // CraftBukkit start - Don't create tile entity if placement failed
                    if (this.getType(i, j, k) != block) {
                        return false;
                    }
                    // CraftBukkit end

                    tileentity = this.e(i, j, k);
                    if (tileentity == null) {
                        tileentity = ((IContainer) block).a(this.world, l);
                        this.world.setTileEntity(l1, j, i2, tileentity);
                    }

                    if (tileentity != null) {
                        tileentity.u();
                    }
                }

                this.n = true;
                return true;
            }
        }
    }

    public boolean a(int i, int j, int k, int l) {
        ChunkSection chunksection = this.sections[j >> 4];

        if (chunksection == null) {
            return false;
        } else {
            int i1 = chunksection.getData(i, j & 15, k);

            if (i1 == l) {
                return false;
            } else {
                this.n = true;
                chunksection.setData(i, j & 15, k, l);
                if (chunksection.getTypeId(i, j & 15, k) instanceof IContainer) {
                    TileEntity tileentity = this.e(i, j, k);

                    if (tileentity != null) {
                        tileentity.u();
                        tileentity.g = l;
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

        this.n = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            if (!this.world.worldProvider.g) {
                chunksection.setSkyLight(i, j & 15, k, l);
            }
        } else if (enumskyblock == EnumSkyBlock.BLOCK) {
            chunksection.setEmittedLight(i, j & 15, k, l);
        }
    }

    public int b(int i, int j, int k, int l) {
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
        this.o = true;
        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);

        if (i != this.locX || j != this.locZ) {
            // CraftBukkit start
            Bukkit.getLogger().warning("Wrong location for " + entity + " in world '" + world.getWorld().getName() + "'!");
            // t.error("Wrong location! " + entity);
            // Thread.dumpStack();
            Bukkit.getLogger().warning("Entity is at " + entity.locX + "," + entity.locZ + " (chunk " + i + "," + j + ") but was stored in chunk " + this.locX + "," + this.locZ);
            // CraftBukkit end
        }

        int k = MathHelper.floor(entity.locY / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.entitySlices.length) {
            k = this.entitySlices.length - 1;
        }

        entity.ah = true;
        entity.ai = this.locX;
        entity.aj = k;
        entity.ak = this.locZ;
        this.entitySlices[k].add(entity);
    }

    public void b(Entity entity) {
        this.a(entity, entity.aj);
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
            Block block = this.getType(i, j, k);

            if (!block.isTileEntity()) {
                return null;
            }

            tileentity = ((IContainer) block).a(this.world, this.getData(i, j, k));
            this.world.setTileEntity(this.locX * 16 + i, j, this.locZ * 16 + k, tileentity);
        }

        if (tileentity != null && tileentity.r()) {
            this.tileEntities.remove(chunkposition);
            return null;
        } else {
            return tileentity;
        }
    }

    public void a(TileEntity tileentity) {
        int i = tileentity.x - this.locX * 16;
        int j = tileentity.y;
        int k = tileentity.z - this.locZ * 16;

        this.a(i, j, k, tileentity);
        if (this.d) {
            this.world.tileEntityList.add(tileentity);
        }
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        tileentity.a(this.world);
        tileentity.x = this.locX * 16 + i;
        tileentity.y = j;
        tileentity.z = this.locZ * 16 + k;
        if (this.getType(i, j, k) instanceof IContainer) {
            if (this.tileEntities.containsKey(chunkposition)) {
                ((TileEntity) this.tileEntities.get(chunkposition)).s();
            }

            tileentity.t();
            this.tileEntities.put(chunkposition, tileentity);
            // CraftBukkit start
        } else {
            System.out.println("Attempted to place a tile entity (" + tileentity + ") at " + tileentity.x + "," + tileentity.y + "," + tileentity.z
                    + " (" + org.bukkit.Material.getMaterial(Block.b(getType(i, j, k))) + ") where there was no entity tile!");
            System.out.println("Chunk coordinates: " + (this.locX * 16) + "," + (this.locZ * 16));
            new Exception().printStackTrace();
            // CraftBukkit end
        }
    }

    public void f(int i, int j, int k) {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);

        if (this.d) {
            TileEntity tileentity = (TileEntity) this.tileEntities.remove(chunkposition);

            if (tileentity != null) {
                tileentity.s();
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

                entity.X();
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
        this.n = true;
    }

    public void a(Entity entity, AxisAlignedBB axisalignedbb, List list, IEntitySelector ientityselector) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.e + 2.0D) / 16.0D);

        i = MathHelper.a(i, 0, this.entitySlices.length - 1);
        j = MathHelper.a(j, 0, this.entitySlices.length - 1);

        for (int k = i; k <= j; ++k) {
            List list1 = this.entitySlices[k];

            for (int l = 0; l < list1.size(); ++l) {
                Entity entity1 = (Entity) list1.get(l);

                if (entity1 != entity && entity1.boundingBox.b(axisalignedbb) && (ientityselector == null || ientityselector.a(entity1))) {
                    list.add(entity1);
                    Entity[] aentity = entity1.at();

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

        i = MathHelper.a(i, 0, this.entitySlices.length - 1);
        j = MathHelper.a(j, 0, this.entitySlices.length - 1);

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
            if (this.o && this.world.getTime() != this.p || this.n) {
                return true;
            }
        } else if (this.o && this.world.getTime() >= this.p + 600L) {
            return true;
        }

        return this.n;
    }

    public Random a(long i) {
        return new Random(this.world.getSeed() + (long) (this.locX * this.locX * 4987142) + (long) (this.locX * 5947611) + (long) (this.locZ * this.locZ) * 4392871L + (long) (this.locZ * 389711) ^ i);
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
                Block block = this.getType(i, i1, j);
                Material material = block.getMaterial();

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

    public void b(boolean flag) {
        if (this.w && !this.world.worldProvider.g && !flag) {
            this.c(this.world.isStatic);
        }

        this.m = true;
        if (!this.lit && this.done) {
            this.p();
        }
    }

    public boolean k() {
        return this.m && this.done && this.lit;
    }

    public ChunkCoordIntPair l() {
        return new ChunkCoordIntPair(this.locX, this.locZ);
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
        int k = this.v[j << 4 | i] & 255;

        if (k == 255) {
            BiomeBase biomebase = worldchunkmanager.getBiome((this.locX << 4) + i, (this.locZ << 4) + j);

            k = biomebase.id;
            this.v[j << 4 | i] = (byte) (k & 255);
        }

        return BiomeBase.getBiome(k) == null ? BiomeBase.PLAINS : BiomeBase.getBiome(k);
    }

    public byte[] m() {
        return this.v;
    }

    public void a(byte[] abyte) {
        this.v = abyte;
    }

    public void n() {
        this.x = 0;
    }

    public void o() {
        for (int i = 0; i < 8; ++i) {
            if (this.x >= 4096) {
                return;
            }

            int j = this.x % 16;
            int k = this.x / 16 % 16;
            int l = this.x / 256;

            ++this.x;
            int i1 = (this.locX << 4) + k;
            int j1 = (this.locZ << 4) + l;

            for (int k1 = 0; k1 < 16; ++k1) {
                int l1 = (j << 4) + k1;

                if (this.sections[j] == null && (k1 == 0 || k1 == 15 || k == 0 || k == 15 || l == 0 || l == 15) || this.sections[j] != null && this.sections[j].getTypeId(k, k1, l).getMaterial() == Material.AIR) {
                    if (this.world.getType(i1, l1 - 1, j1).m() > 0) {
                        this.world.t(i1, l1 - 1, j1);
                    }

                    if (this.world.getType(i1, l1 + 1, j1).m() > 0) {
                        this.world.t(i1, l1 + 1, j1);
                    }

                    if (this.world.getType(i1 - 1, l1, j1).m() > 0) {
                        this.world.t(i1 - 1, l1, j1);
                    }

                    if (this.world.getType(i1 + 1, l1, j1).m() > 0) {
                        this.world.t(i1 + 1, l1, j1);
                    }

                    if (this.world.getType(i1, l1, j1 - 1).m() > 0) {
                        this.world.t(i1, l1, j1 - 1);
                    }

                    if (this.world.getType(i1, l1, j1 + 1).m() > 0) {
                        this.world.t(i1, l1, j1 + 1);
                    }

                    this.world.t(i1, l1, j1);
                }
            }
        }
    }

    public void p() {
        this.done = true;
        this.lit = true;
        if (!this.world.worldProvider.g) {
            if (this.world.b(this.locX * 16 - 1, 0, this.locZ * 16 - 1, this.locX * 16 + 1, 63, this.locZ * 16 + 1)) {
                for (int i = 0; i < 16; ++i) {
                    for (int j = 0; j < 16; ++j) {
                        if (!this.f(i, j)) {
                            this.lit = false;
                            break;
                        }
                    }
                }

                if (this.lit) {
                    Chunk chunk = this.world.getChunkAtWorldCoords(this.locX * 16 - 1, this.locZ * 16);

                    chunk.a(3);
                    chunk = this.world.getChunkAtWorldCoords(this.locX * 16 + 16, this.locZ * 16);
                    chunk.a(1);
                    chunk = this.world.getChunkAtWorldCoords(this.locX * 16, this.locZ * 16 - 1);
                    chunk.a(0);
                    chunk = this.world.getChunkAtWorldCoords(this.locX * 16, this.locZ * 16 + 16);
                    chunk.a(2);
                }
            } else {
                this.lit = false;
            }
        }
    }

    private void a(int i) {
        if (this.done) {
            int j;

            if (i == 3) {
                for (j = 0; j < 16; ++j) {
                    this.f(15, j);
                }
            } else if (i == 1) {
                for (j = 0; j < 16; ++j) {
                    this.f(0, j);
                }
            } else if (i == 0) {
                for (j = 0; j < 16; ++j) {
                    this.f(j, 15);
                }
            } else if (i == 2) {
                for (j = 0; j < 16; ++j) {
                    this.f(j, 0);
                }
            }
        }
    }

    private boolean f(int i, int j) {
        int k = this.h();
        boolean flag = false;
        boolean flag1 = false;

        int l;

        for (l = k + 16 - 1; l > 63 || l > 0 && !flag1; --l) {
            int i1 = this.b(i, l, j);

            if (i1 == 255 && l < 63) {
                flag1 = true;
            }

            if (!flag && i1 > 0) {
                flag = true;
            } else if (flag && i1 == 0 && !this.world.t(this.locX * 16 + i, l, this.locZ * 16 + j)) {
                return false;
            }
        }

        for (; l > 0; --l) {
            if (this.getType(i, l, j).m() > 0) {
                this.world.t(this.locX * 16 + i, l, this.locZ * 16 + j);
            }
        }

        return true;
    }
}
