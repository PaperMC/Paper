package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
// CraftBukkit end

public class World implements IBlockAccess {

    public boolean a = false;
    private List u = new ArrayList();
    public List b = new ArrayList();
    private List v = new ArrayList();
    private TreeSet w = new TreeSet();
    private Set x = new HashSet();
    public List c = new ArrayList();
    public List d = new ArrayList();
    private long y = 16777215L;
    public int e = 0;
    protected int f = (new Random()).nextInt();
    protected int g = 1013904223;
    public boolean h = false;
    private long z = System.currentTimeMillis();
    protected int i = 40;
    public int j;
    public Random k = new Random();
    public boolean l = false;
    public final WorldProvider m;
    protected List n = new ArrayList();
    public IChunkProvider o; // CraftBukkit protected->public
    protected final IDataManager p;
    public WorldData q; // CraftBukkit protected->public
    public boolean r;
    private boolean A;
    private ArrayList B = new ArrayList();
    private int C = 0;
    public boolean D = true; // CraftBukkit private->public
    public boolean E = true; // CraftBukkit private->public
    static int s = 0;
    private Set F = new HashSet();
    private int G;
    private List H;
    public boolean isStatic;

    public WorldChunkManager a() {
        return this.m.b;
    }

    public World(IDataManager idatamanager, String s, long i, WorldProvider worldprovider) {
        this.G = this.k.nextInt(12000);
        this.H = new ArrayList();
        this.isStatic = false;
        this.p = idatamanager;
        this.q = idatamanager.c();
        this.l = this.q == null;
        if (worldprovider != null) {
            this.m = worldprovider;
        } else if (this.q != null && this.q.h() == -1) {
            this.m = new WorldProviderHell();
        } else {
            this.m = new WorldProvider();
        }

        boolean flag = false;

        if (this.q == null) {
            this.q = new WorldData(i, s);
            flag = true;
        } else {
            this.q.a(s);
        }

        this.m.a(this);
        this.o = this.b();
        if (flag) {
            this.c();
        }

        this.g();
    }

    protected IChunkProvider b() {
        IChunkLoader ichunkloader = this.p.a(this.m);

        return new ChunkProviderLoadOrGenerate(this, ichunkloader, this.m.c());
    }

    protected void c() {
        this.r = true;
        int i = 0;
        byte b0 = 64;

        int j;

        for (j = 0; !this.m.a(i, j); j += this.k.nextInt(64) - this.k.nextInt(64)) {
            i += this.k.nextInt(64) - this.k.nextInt(64);
        }

        this.q.a(i, b0, j);
        this.r = false;
    }

    public int a(int i, int j) {
        int k;

        for (k = 63; !this.isEmpty(i, k + 1, j); ++k) {
            ;
        }

        return this.getTypeId(i, k, j);
    }

    public void a(boolean flag, IProgressUpdate iprogressupdate) {
        if (this.o.b()) {
            if (iprogressupdate != null) {
                iprogressupdate.a("Saving level");
            }

            this.t();
            if (iprogressupdate != null) {
                iprogressupdate.b("Saving chunks");
            }

            this.o.a(flag, iprogressupdate);
        }
    }

    private void t() {
        this.j();
        this.p.a(this.q, this.d);
    }

    public int getTypeId(int i, int j, int k) {
        return i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000 ? (j < 0 ? 0 : (j >= 128 ? 0 : this.c(i >> 4, k >> 4).a(i & 15, j, k & 15))) : 0;
    }

    public boolean isEmpty(int i, int j, int k) {
        return this.getTypeId(i, j, k) == 0;
    }

    public boolean f(int i, int j, int k) {
        return j >= 0 && j < 128 ? this.f(i >> 4, k >> 4) : false;
    }

    public boolean a(int i, int j, int k, int l) {
        return this.a(i - l, j - l, k - l, i + l, j + l, k + l);
    }

    public boolean a(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < 128) {
            i >>= 4;
            j >>= 4;
            k >>= 4;
            l >>= 4;
            i1 >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    if (!this.f(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean f(int i, int j) {
        return this.o.a(i, j);
    }

    public Chunk b(int i, int j) {
        return this.c(i >> 4, j >> 4);
    }

    // CraftBukkit start
    Chunk lastChunkAccessed;
    int lastXAccessed = Integer.MIN_VALUE;
    int lastZAccessed = Integer.MIN_VALUE;
    final Object chunkLock = new Object();
    public Chunk c(int i, int j) {
        Chunk result = null;
        synchronized (chunkLock) {
            if (lastChunkAccessed == null || lastXAccessed != i || lastZAccessed != j) {
                lastXAccessed = i;
                lastZAccessed = j;
                lastChunkAccessed = this.o.b(i, j);
            }
            result = lastChunkAccessed;
        }
        return result;
    }
    // CraftBukkit end

    public boolean setTypeIdAndData(int i, int j, int k, int l, int i1) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            if (j < 0) {
                return false;
            } else if (j >= 128) {
                return false;
            } else {
                Chunk chunk = this.c(i >> 4, k >> 4);

                return chunk.a(i & 15, j, k & 15, l, i1);
            }
        } else {
            return false;
        }
    }

    public boolean setTypeId(int i, int j, int k, int l) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            if (j < 0) {
                return false;
            } else if (j >= 128) {
                return false;
            } else {
                Chunk chunk = this.c(i >> 4, k >> 4);

                return chunk.a(i & 15, j, k & 15, l);
            }
        } else {
            return false;
        }
    }

    public Material getMaterial(int i, int j, int k) {
        int l = this.getTypeId(i, j, k);

        return l == 0 ? Material.AIR : Block.byId[l].material;
    }

    public int getData(int i, int j, int k) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            if (j < 0) {
                return 0;
            } else if (j >= 128) {
                return 0;
            } else {
                Chunk chunk = this.c(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.b(i, j, k);
            }
        } else {
            return 0;
        }
    }

    public void c(int i, int j, int k, int l) {
        if (this.d(i, j, k, l)) {
            this.f(i, j, k, this.getTypeId(i, j, k));
        }
    }

    public boolean d(int i, int j, int k, int l) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            if (j < 0) {
                return false;
            } else if (j >= 128) {
                return false;
            } else {
                Chunk chunk = this.c(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                chunk.b(i, j, k, l);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean e(int i, int j, int k, int l) {
        // Craftbukkit start
        int old = this.getTypeId(i, j, k);
        if (this.setTypeId(i, j, k, l)) {
            this.f(i, j, k, l == 0 ? old : l);
            return true;
        } else {
            return false;
        }
        // Craftbukkit end
    }

    public boolean b(int i, int j, int k, int l, int i1) {
        // Craftbukkit start
        int old = this.getTypeId(i, j, k);
        if (this.setTypeIdAndData(i, j, k, l, i1)) {
            this.f(i, j, k, l == 0 ? old : l);
            return true;
        } else {
            return false;
        }
        // Craftbukkit end
    }

    public void g(int i, int j, int k) {
        for (int l = 0; l < this.n.size(); ++l) {
            ((IWorldAccess) this.n.get(l)).a(i, j, k);
        }
    }

    protected void f(int i, int j, int k, int l) {
        this.g(i, j, k);
        this.h(i, j, k, l);
    }

    public void g(int i, int j, int k, int l) {
        if (k > l) {
            int i1 = l;

            l = k;
            k = i1;
        }

        this.b(i, k, j, i, l, j);
    }

    public void h(int i, int j, int k) {
        for (int l = 0; l < this.n.size(); ++l) {
            ((IWorldAccess) this.n.get(l)).a(i, j, k, i, j, k);
        }
    }

    public void b(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < this.n.size(); ++k1) {
            ((IWorldAccess) this.n.get(k1)).a(i, j, k, l, i1, j1);
        }
    }

    public void h(int i, int j, int k, int l) {
        this.k(i - 1, j, k, l);
        this.k(i + 1, j, k, l);
        this.k(i, j - 1, k, l);
        this.k(i, j + 1, k, l);
        this.k(i, j, k - 1, l);
        this.k(i, j, k + 1, l);
    }

    private void k(int i, int j, int k, int l) {
        if (!this.h && !this.isStatic) {
            Block block = Block.byId[this.getTypeId(i, j, k)];

            if (block != null) {
                // CraftBukkit start
                CraftWorld world = ((WorldServer) this).getWorld();
                if (world != null) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(i, j, k), l);
                    ((WorldServer) this).getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit stop

                block.a(this, i, j, k, l);
            }
        }
    }

    public boolean i(int i, int j, int k) {
        return this.c(i >> 4, k >> 4).c(i & 15, j, k & 15);
    }

    public int j(int i, int j, int k) {
        return this.a(i, j, k, true);
    }

    public int a(int i, int j, int k, boolean flag) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            int l;

            if (flag) {
                l = this.getTypeId(i, j, k);
                if (l == Block.STEP.id || l == Block.SOIL.id) {
                    int i1 = this.a(i, j + 1, k, false);
                    int j1 = this.a(i + 1, j, k, false);
                    int k1 = this.a(i - 1, j, k, false);
                    int l1 = this.a(i, j, k + 1, false);
                    int i2 = this.a(i, j, k - 1, false);

                    if (j1 > i1) {
                        i1 = j1;
                    }

                    if (k1 > i1) {
                        i1 = k1;
                    }

                    if (l1 > i1) {
                        i1 = l1;
                    }

                    if (i2 > i1) {
                        i1 = i2;
                    }

                    return i1;
                }
            }

            if (j < 0) {
                return 0;
            } else if (j >= 128) {
                l = 15 - this.e;
                if (l < 0) {
                    l = 0;
                }

                return l;
            } else {
                Chunk chunk = this.c(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.c(i, j, k, this.e);
            }
        } else {
            return 15;
        }
    }

    public boolean k(int i, int j, int k) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            if (j < 0) {
                return false;
            } else if (j >= 128) {
                return true;
            } else if (!this.f(i >> 4, k >> 4)) {
                return false;
            } else {
                Chunk chunk = this.c(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.c(i, j, k);
            }
        } else {
            return false;
        }
    }

    public int d(int i, int j) {
        if (i >= -32000000 && j >= -32000000 && i < 32000000 && j <= 32000000) {
            if (!this.f(i >> 4, j >> 4)) {
                return 0;
            } else {
                Chunk chunk = this.c(i >> 4, j >> 4);

                return chunk.b(i & 15, j & 15);
            }
        } else {
            return 0;
        }
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        if (!this.m.e || enumskyblock != EnumSkyBlock.SKY) {
            if (this.f(i, j, k)) {
                if (enumskyblock == EnumSkyBlock.SKY) {
                    if (this.k(i, j, k)) {
                        l = 15;
                    }
                } else if (enumskyblock == EnumSkyBlock.BLOCK) {
                    int i1 = this.getTypeId(i, j, k);

                    if (Block.s[i1] > l) {
                        l = Block.s[i1];
                    }
                }

                if (this.a(enumskyblock, i, j, k) != l) {
                    this.a(enumskyblock, i, j, k, i, j, k);
                }
            }
        }
    }

    public int a(EnumSkyBlock enumskyblock, int i, int j, int k) {
        if (j >= 0 && j < 128 && i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            int l = i >> 4;
            int i1 = k >> 4;

            if (!this.f(l, i1)) {
                return 0;
            } else {
                Chunk chunk = this.c(l, i1);

                return chunk.a(enumskyblock, i & 15, j, k & 15);
            }
        } else {
            return enumskyblock.c;
        }
    }

    public void b(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) {
            if (j >= 0) {
                if (j < 128) {
                    if (this.f(i >> 4, k >> 4)) {
                        Chunk chunk = this.c(i >> 4, k >> 4);

                        chunk.a(enumskyblock, i & 15, j, k & 15, l);

                        for (int i1 = 0; i1 < this.n.size(); ++i1) {
                            ((IWorldAccess) this.n.get(i1)).a(i, j, k);
                        }
                    }
                }
            }
        }
    }

    public float l(int i, int j, int k) {
        return this.m.f[this.j(i, j, k)];
    }

    public boolean d() {
        return this.e < 4;
    }

    public MovingObjectPosition a(Vec3D vec3d, Vec3D vec3d1) {
        return this.a(vec3d, vec3d1, false);
    }

    public MovingObjectPosition a(Vec3D vec3d, Vec3D vec3d1, boolean flag) {
        if (!Double.isNaN(vec3d.a) && !Double.isNaN(vec3d.b) && !Double.isNaN(vec3d.c)) {
            if (!Double.isNaN(vec3d1.a) && !Double.isNaN(vec3d1.b) && !Double.isNaN(vec3d1.c)) {
                int i = MathHelper.b(vec3d1.a);
                int j = MathHelper.b(vec3d1.b);
                int k = MathHelper.b(vec3d1.c);
                int l = MathHelper.b(vec3d.a);
                int i1 = MathHelper.b(vec3d.b);
                int j1 = MathHelper.b(vec3d.c);
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec3d.a) || Double.isNaN(vec3d.b) || Double.isNaN(vec3d.c)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return null;
                    }

                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    }

                    if (i < l) {
                        d0 = (double) l + 0.0D;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    }

                    if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    }

                    if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec3d1.a - vec3d.a;
                    double d7 = vec3d1.b - vec3d.b;
                    double d8 = vec3d1.c - vec3d.c;

                    if (d0 != 999.0D) {
                        d3 = (d0 - vec3d.a) / d6;
                    }

                    if (d1 != 999.0D) {
                        d4 = (d1 - vec3d.b) / d7;
                    }

                    if (d2 != 999.0D) {
                        d5 = (d2 - vec3d.c) / d8;
                    }

                    boolean flag1 = false;
                    byte b0;

                    if (d3 < d4 && d3 < d5) {
                        if (i > l) {
                            b0 = 4;
                        } else {
                            b0 = 5;
                        }

                        vec3d.a = d0;
                        vec3d.b += d7 * d3;
                        vec3d.c += d8 * d3;
                    } else if (d4 < d5) {
                        if (j > i1) {
                            b0 = 0;
                        } else {
                            b0 = 1;
                        }

                        vec3d.a += d6 * d4;
                        vec3d.b = d1;
                        vec3d.c += d8 * d4;
                    } else {
                        if (k > j1) {
                            b0 = 2;
                        } else {
                            b0 = 3;
                        }

                        vec3d.a += d6 * d5;
                        vec3d.b += d7 * d5;
                        vec3d.c = d2;
                    }

                    Vec3D vec3d2 = Vec3D.b(vec3d.a, vec3d.b, vec3d.c);

                    l = (int) (vec3d2.a = (double) MathHelper.b(vec3d.a));
                    if (b0 == 5) {
                        --l;
                        ++vec3d2.a;
                    }

                    i1 = (int) (vec3d2.b = (double) MathHelper.b(vec3d.b));
                    if (b0 == 1) {
                        --i1;
                        ++vec3d2.b;
                    }

                    j1 = (int) (vec3d2.c = (double) MathHelper.b(vec3d.c));
                    if (b0 == 3) {
                        --j1;
                        ++vec3d2.c;
                    }

                    int l1 = this.getTypeId(l, i1, j1);
                    int i2 = this.getData(l, i1, j1);
                    Block block = Block.byId[l1];

                    if (l1 > 0 && block.a(i2, flag)) {
                        MovingObjectPosition movingobjectposition = block.a(this, l, i1, j1, vec3d, vec3d1);

                        if (movingobjectposition != null) {
                            return movingobjectposition;
                        }
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void a(Entity entity, String s, float f, float f1) {
        for (int i = 0; i < this.n.size(); ++i) {
            ((IWorldAccess) this.n.get(i)).a(s, entity.locX, entity.locY - (double) entity.height, entity.locZ, f, f1);
        }
    }

    public void a(double d0, double d1, double d2, String s, float f, float f1) {
        for (int i = 0; i < this.n.size(); ++i) {
            ((IWorldAccess) this.n.get(i)).a(s, d0, d1, d2, f, f1);
        }
    }

    public void a(String s, int i, int j, int k) {
        for (int l = 0; l < this.n.size(); ++l) {
            ((IWorldAccess) this.n.get(l)).a(s, i, j, k);
        }
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {
        for (int i = 0; i < this.n.size(); ++i) {
            ((IWorldAccess) this.n.get(i)).a(s, d0, d1, d2, d3, d4, d5);
        }
    }

    public boolean a(Entity entity) {
        int i = MathHelper.b(entity.locX / 16.0D);
        int j = MathHelper.b(entity.locZ / 16.0D);
        boolean flag = false;

        if (entity instanceof EntityHuman) {
            flag = true;
        }

        // CraftBukkit start
        if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
            CreatureSpawnEvent event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving) entity);
            if (event.isCancelled()) {
                return false;
            }
        }
        // CraftBukkit end

        if (!flag && !this.f(i, j)) {
            return false;
        } else {
            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;

                this.d.add(entityhuman);
                this.q();
            }

            this.c(i, j).a(entity);
            this.b.add(entity);
            this.b(entity);
            return true;
        }
    }

    protected void b(Entity entity) {
        for (int i = 0; i < this.n.size(); ++i) {
            ((IWorldAccess) this.n.get(i)).a(entity);
        }
    }

    protected void c(Entity entity) {
        for (int i = 0; i < this.n.size(); ++i) {
            ((IWorldAccess) this.n.get(i)).b(entity);
        }
    }

    public void d(Entity entity) {
        if (entity.passenger != null) {
            entity.passenger.b((Entity) null);
        }

        if (entity.vehicle != null) {
            entity.b((Entity) null);
        }

        entity.D();
        if (entity instanceof EntityHuman) {
            this.d.remove((EntityHuman) entity);
            this.q();
        }
    }

    public void e(Entity entity) {
        entity.D();
        if (entity instanceof EntityHuman) {
            this.d.remove((EntityHuman) entity);
            this.q();
        }

        int i = entity.chunkX;
        int j = entity.chunkZ;

        if (entity.bA && this.f(i, j)) {
            this.c(i, j).b(entity);
        }

        this.b.remove(entity);
        this.c(entity);
    }

    public void a(IWorldAccess iworldaccess) {
        this.n.add(iworldaccess);
    }

    public List a(Entity entity, AxisAlignedBB axisalignedbb) {
        this.B.clear();
        int i = MathHelper.b(axisalignedbb.a);
        int j = MathHelper.b(axisalignedbb.d + 1.0D);
        int k = MathHelper.b(axisalignedbb.b);
        int l = MathHelper.b(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.b(axisalignedbb.c);
        int j1 = MathHelper.b(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.f(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        Block block = Block.byId[this.getTypeId(k1, i2, l1)];

                        if (block != null) {
                            block.a(this, k1, i2, l1, axisalignedbb, this.B);
                        }
                    }
                }
            }
        }

        double d0 = 0.25D;
        List list = this.b(entity, axisalignedbb.b(d0, d0, d0));

        for (int j2 = 0; j2 < list.size(); ++j2) {
            AxisAlignedBB axisalignedbb1 = ((Entity) list.get(j2)).d();

            if (axisalignedbb1 != null && axisalignedbb1.a(axisalignedbb)) {
                this.B.add(axisalignedbb1);
            }

            axisalignedbb1 = entity.a_((Entity) list.get(j2));
            if (axisalignedbb1 != null && axisalignedbb1.a(axisalignedbb)) {
                this.B.add(axisalignedbb1);
            }
        }

        return this.B;
    }

    public int a(float f) {
        float f1 = this.b(f);
        float f2 = 1.0F - (MathHelper.b(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F);

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        return (int) (f2 * 11.0F);
    }

    public float b(float f) {
        return this.m.a(this.q.f(), f);
    }

    public int e(int i, int j) {
        Chunk chunk = this.b(i, j);

        int k;

        for (k = 127; this.getMaterial(i, k, j).isSolid() && k > 0; --k) {
            ;
        }

        i &= 15;

        for (j &= 15; k > 0; --k) {
            int l = chunk.a(i, k, j);

            if (l != 0 && (Block.byId[l].material.isSolid() || Block.byId[l].material.isLiquid())) {
                return k + 1;
            }
        }

        return -1;
    }

    public void c(int i, int j, int k, int l, int i1) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, l);
        byte b0 = 8;

        if (this.a) {
            if (this.a(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                int j1 = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                if (j1 == nextticklistentry.d && j1 > 0) {
                    Block.byId[j1].a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.k);
                }
            }
        } else {
            if (this.a(i - b0, j - b0, k - b0, i + b0, j + b0, k + b0)) {
                if (l > 0) {
                    nextticklistentry.a((long) i1 + this.q.f());
                }

                if (!this.x.contains(nextticklistentry)) {
                    this.x.add(nextticklistentry);
                    this.w.add(nextticklistentry);
                }
            }
        }
    }

    public void e() {
        this.b.removeAll(this.v);

        int i;
        Entity entity;
        int j;
        int k;

        for (i = 0; i < this.v.size(); ++i) {
            entity = (Entity) this.v.get(i);
            j = entity.chunkX;
            k = entity.chunkZ;
            if (entity.bA && this.f(j, k)) {
                this.c(j, k).b(entity);
            }
        }

        for (i = 0; i < this.v.size(); ++i) {
            this.c((Entity) this.v.get(i));
        }

        this.v.clear();

        for (i = 0; i < this.b.size(); ++i) {
            entity = (Entity) this.b.get(i);
            if (entity.vehicle != null) {
                if (!entity.vehicle.dead && entity.vehicle.passenger == entity) {
                    continue;
                }

                entity.vehicle.passenger = null;
                entity.vehicle = null;
            }

            if (!entity.dead) {
                this.f(entity);
            }

            if (entity.dead) {
                j = entity.chunkX;
                k = entity.chunkZ;
                if (entity.bA && this.f(j, k)) {
                    this.c(j, k).b(entity);
                }

                this.b.remove(i--);
                this.c(entity);
            }
        }

        for (i = 0; i < this.c.size(); ++i) {
            TileEntity tileentity = (TileEntity) this.c.get(i);

            tileentity.i_();
        }
    }

    public void f(Entity entity) {
        this.a(entity, true);
    }

    public void a(Entity entity, boolean flag) {
        int i = MathHelper.b(entity.locX);
        int j = MathHelper.b(entity.locZ);
        byte b0 = 32;

        if (!flag || this.a(i - b0, 0, j - b0, i + b0, 128, j + b0)) {
            entity.bi = entity.locX;
            entity.bj = entity.locY;
            entity.bk = entity.locZ;
            entity.lastYaw = entity.yaw;
            entity.lastPitch = entity.pitch;
            if (flag && entity.bA) {
                if (entity.vehicle != null) {
                    entity.o_();
                } else {
                    entity.f_();
                }
            }

            if (Double.isNaN(entity.locX) || Double.isInfinite(entity.locX)) {
                entity.locX = entity.bi;
            }

            if (Double.isNaN(entity.locY) || Double.isInfinite(entity.locY)) {
                entity.locY = entity.bj;
            }

            if (Double.isNaN(entity.locZ) || Double.isInfinite(entity.locZ)) {
                entity.locZ = entity.bk;
            }

            if (Double.isNaN((double) entity.pitch) || Double.isInfinite((double) entity.pitch)) {
                entity.pitch = entity.lastPitch;
            }

            if (Double.isNaN((double) entity.yaw) || Double.isInfinite((double) entity.yaw)) {
                entity.yaw = entity.lastYaw;
            }

            int k = MathHelper.b(entity.locX / 16.0D);
            int l = MathHelper.b(entity.locY / 16.0D);
            int i1 = MathHelper.b(entity.locZ / 16.0D);

            if (!entity.bA || entity.chunkX != k || entity.bC != l || entity.chunkZ != i1) {
                if (entity.bA && this.f(entity.chunkX, entity.chunkZ)) {
                    this.c(entity.chunkX, entity.chunkZ).a(entity, entity.bC);
                }

                if (this.f(k, i1)) {
                    entity.bA = true;
                    this.c(k, i1).a(entity);
                } else {
                    entity.bA = false;
                }
            }

            if (flag && entity.bA && entity.passenger != null) {
                if (!entity.passenger.dead && entity.passenger.vehicle == entity) {
                    this.f(entity.passenger);
                } else {
                    entity.passenger.vehicle = null;
                    entity.passenger = null;
                }
            }
        }
    }

    public boolean a(AxisAlignedBB axisalignedbb) {
        List list = this.b((Entity) null, axisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (!entity.dead && entity.aD) {
                return false;
            }
        }

        return true;
    }

    public boolean b(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.b(axisalignedbb.a);
        int j = MathHelper.b(axisalignedbb.d + 1.0D);
        int k = MathHelper.b(axisalignedbb.b);
        int l = MathHelper.b(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.b(axisalignedbb.c);
        int j1 = MathHelper.b(axisalignedbb.f + 1.0D);

        if (axisalignedbb.a < 0.0D) {
            --i;
        }

        if (axisalignedbb.b < 0.0D) {
            --k;
        }

        if (axisalignedbb.c < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = Block.byId[this.getTypeId(k1, l1, i2)];

                    if (block != null && block.material.isLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean c(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.b(axisalignedbb.a);
        int j = MathHelper.b(axisalignedbb.d + 1.0D);
        int k = MathHelper.b(axisalignedbb.b);
        int l = MathHelper.b(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.b(axisalignedbb.c);
        int j1 = MathHelper.b(axisalignedbb.f + 1.0D);

        if (this.a(i, k, i1, j, l, j1)) {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        int j2 = this.getTypeId(k1, l1, i2);

                        if (j2 == Block.FIRE.id || j2 == Block.LAVA.id || j2 == Block.STATIONARY_LAVA.id) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material, Entity entity) {
        int i = MathHelper.b(axisalignedbb.a);
        int j = MathHelper.b(axisalignedbb.d + 1.0D);
        int k = MathHelper.b(axisalignedbb.b);
        int l = MathHelper.b(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.b(axisalignedbb.c);
        int j1 = MathHelper.b(axisalignedbb.f + 1.0D);

        if (!this.a(i, k, i1, j, l, j1)) {
            return false;
        } else {
            boolean flag = false;
            Vec3D vec3d = Vec3D.b(0.0D, 0.0D, 0.0D);

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = Block.byId[this.getTypeId(k1, l1, i2)];

                        if (block != null && block.material == material) {
                            double d0 = (double) ((float) (l1 + 1) - BlockFluids.c(this.getData(k1, l1, i2)));

                            if ((double) l >= d0) {
                                flag = true;
                                block.a(this, k1, l1, i2, entity, vec3d);
                            }
                        }
                    }
                }
            }

            if (vec3d.c() > 0.0D) {
                vec3d = vec3d.b();
                double d1 = 0.0040D;

                entity.motX += vec3d.a * d1;
                entity.motY += vec3d.b * d1;
                entity.motZ += vec3d.c * d1;
            }

            return flag;
        }
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.b(axisalignedbb.a);
        int j = MathHelper.b(axisalignedbb.d + 1.0D);
        int k = MathHelper.b(axisalignedbb.b);
        int l = MathHelper.b(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.b(axisalignedbb.c);
        int j1 = MathHelper.b(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = Block.byId[this.getTypeId(k1, l1, i2)];

                    if (block != null && block.material == material) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean b(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.b(axisalignedbb.a);
        int j = MathHelper.b(axisalignedbb.d + 1.0D);
        int k = MathHelper.b(axisalignedbb.b);
        int l = MathHelper.b(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.b(axisalignedbb.c);
        int j1 = MathHelper.b(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = Block.byId[this.getTypeId(k1, l1, i2)];

                    if (block != null && block.material == material) {
                        int j2 = this.getData(k1, l1, i2);
                        double d0 = (double) (l1 + 1);

                        if (j2 < 8) {
                            d0 = (double) (l1 + 1) - (double) j2 / 8.0D;
                        }

                        if (d0 >= axisalignedbb.b) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Explosion a(Entity entity, double d0, double d1, double d2, float f) {
        return this.a(entity, d0, d1, d2, f, false);
    }

    public Explosion a(Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f);

        explosion.a = flag;
        explosion.a();
        explosion.b();
        return explosion;
    }

    public float a(Vec3D vec3d, AxisAlignedBB axisalignedbb) {
        double d0 = 1.0D / ((axisalignedbb.d - axisalignedbb.a) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.e - axisalignedbb.b) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.f - axisalignedbb.c) * 2.0D + 1.0D);
        int i = 0;
        int j = 0;

        for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
            for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                    double d3 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) f;
                    double d4 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) f1;
                    double d5 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) f2;

                    if (this.a(Vec3D.b(d3, d4, d5), vec3d) == null) {
                        ++i;
                    }

                    ++j;
                }
            }
        }

        return (float) i / (float) j;
    }

    public TileEntity getTileEntity(int i, int j, int k) {
        Chunk chunk = this.c(i >> 4, k >> 4);

        return chunk != null ? chunk.d(i & 15, j, k & 15) : null;
    }

    public void setTileEntity(int i, int j, int k, TileEntity tileentity) {
        Chunk chunk = this.c(i >> 4, k >> 4);

        if (chunk != null) {
            chunk.a(i & 15, j, k & 15, tileentity);
        }
    }

    public void n(int i, int j, int k) {
        Chunk chunk = this.c(i >> 4, k >> 4);

        if (chunk != null) {
            chunk.e(i & 15, j, k & 15);
        }
    }

    public boolean d(int i, int j, int k) {
        Block block = Block.byId[this.getTypeId(i, j, k)];

        return block == null ? false : block.a();
    }

    public boolean f() {
        if (this.C >= 50) {
            return false;
        } else {
            ++this.C;

            try {
                int i = 500;

                boolean flag;

                while (this.u.size() > 0) {
                    --i;
                    if (i <= 0) {
                        flag = true;
                        return flag;
                    }

                    ((MetadataChunkBlock) this.u.remove(this.u.size() - 1)).a(this);
                }

                flag = false;
                return flag;
            } finally {
                --this.C;
            }
        }
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l, int i1, int j1) {
        this.a(enumskyblock, i, j, k, l, i1, j1, true);
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l, int i1, int j1, boolean flag) {
        if (!this.m.e || enumskyblock != EnumSkyBlock.SKY) {
            ++s;
            if (s == 50) {
                --s;
            } else {
                int k1 = (l + i) / 2;
                int l1 = (j1 + k) / 2;

                if (!this.f(k1, 64, l1)) {
                    --s;
                } else if (!this.b(k1, l1).g()) {
                    int i2 = this.u.size();
                    int j2;

                    if (flag) {
                        j2 = 5;
                        if (j2 > i2) {
                            j2 = i2;
                        }

                        for (int k2 = 0; k2 < j2; ++k2) {
                            MetadataChunkBlock metadatachunkblock = (MetadataChunkBlock) this.u.get(this.u.size() - k2 - 1);

                            if (metadatachunkblock.a == enumskyblock && metadatachunkblock.a(i, j, k, l, i1, j1)) {
                                --s;
                                return;
                            }
                        }
                    }

                    this.u.add(new MetadataChunkBlock(enumskyblock, i, j, k, l, i1, j1));
                    j2 = 1000000;
                    if (this.u.size() > 1000000) {
                        System.out.println("More than " + j2 + " updates, aborting lighting updates");
                        this.u.clear();
                    }

                    --s;
                }
            }
        }
    }

    public void g() {
        int i = this.a(1.0F);

        if (i != this.e) {
            this.e = i;
        }
    }

    public void a(boolean flag, boolean flag1) {
        this.D = flag;
        this.E = flag1;
    }

    public void h() {
        long i;

        if (this.s()) {
            boolean flag = false;

            if (this.D && this.j >= 1) {
                flag = SpawnerCreature.a(this, this.d);
            }

            if (!flag) {
                i = this.q.f() + 24000L;
                this.q.a(i - i % 24000L);
                this.r();
            }
        }

        // CraftBukkit start -- Only call spawner if we have players online and the world allows for mobs or animals
        if ((this.D || this.E) && (this instanceof WorldServer && ((WorldServer) this).getServer().getHandle().b.size() > 0)) {
            SpawnerCreature.a(this, this.D, this.E);
        }
        // CraftBukkit end

        this.o.a();
        int j = this.a(1.0F);

        if (j != this.e) {
            this.e = j;

            for (int k = 0; k < this.n.size(); ++k) {
                ((IWorldAccess) this.n.get(k)).a();
            }
        }

        i = this.q.f() + 1L;
        if (i % (long) this.i == 0L) {
            this.a(false, (IProgressUpdate) null);
        }

        this.q.a(i);
        this.a(false);
        this.i();
    }

    protected void i() {
        this.F.clear();

        int i;
        int j;
        int k;
        int l;

        for (int i1 = 0; i1 < this.d.size(); ++i1) {
            EntityHuman entityhuman = (EntityHuman) this.d.get(i1);

            i = MathHelper.b(entityhuman.locX / 16.0D);
            j = MathHelper.b(entityhuman.locZ / 16.0D);
            byte b0 = 9;

            for (k = -b0; k <= b0; ++k) {
                for (l = -b0; l <= b0; ++l) {
                    this.F.add(new ChunkCoordIntPair(k + i, l + j));
                }
            }
        }

        if (this.G > 0) {
            --this.G;
        }

        Iterator iterator = this.F.iterator();

        while (iterator.hasNext()) {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator.next();

            i = chunkcoordintpair.a * 16;
            j = chunkcoordintpair.b * 16;
            Chunk chunk = this.c(chunkcoordintpair.a, chunkcoordintpair.b);
            int j1;
            int k1;
            int l1;

            if (this.G == 0) {
                this.f = this.f * 3 + this.g;
                k = this.f >> 2;
                l = k & 15;
                j1 = k >> 8 & 15;
                k1 = k >> 16 & 127;
                l1 = chunk.a(l, k1, j1);
                l += i;
                j1 += j;
                if (l1 == 0 && this.j(l, k1, j1) <= this.k.nextInt(8) && this.a(EnumSkyBlock.SKY, l, k1, j1) <= 0) {
                    EntityHuman entityhuman1 = this.a((double) l + 0.5D, (double) k1 + 0.5D, (double) j1 + 0.5D, 8.0D);

                    if (entityhuman1 != null && entityhuman1.d((double) l + 0.5D, (double) k1 + 0.5D, (double) j1 + 0.5D) > 4.0D) {
                        this.a((double) l + 0.5D, (double) k1 + 0.5D, (double) j1 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.k.nextFloat() * 0.2F);
                        this.G = this.k.nextInt(12000) + 6000;
                    }
                }
            }

            for (k = 0; k < 80; ++k) {
                this.f = this.f * 3 + this.g;
                l = this.f >> 2;
                j1 = l & 15;
                k1 = l >> 8 & 15;
                l1 = l >> 16 & 127;
                int i2 = chunk.b[j1 << 11 | k1 << 7 | l1] & 255;

                if (Block.n[i2]) {
                    Block.byId[i2].a(this, j1 + i, l1, k1 + j, this.k);
                }
            }
        }
    }

    public boolean a(boolean flag) {
        int i = this.w.size();

        if (i != this.x.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if (i > 1000) {
                i = 1000;
            }

            for (int j = 0; j < i; ++j) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) this.w.first();

                if (!flag && nextticklistentry.e > this.q.f()) {
                    break;
                }

                this.w.remove(nextticklistentry);
                this.x.remove(nextticklistentry);
                byte b0 = 8;

                if (this.a(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                    int k = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                    if (k == nextticklistentry.d && k > 0) {
                        Block.byId[k].a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.k);
                    }
                }
            }

            return this.w.size() != 0;
        }
    }

    public List b(Entity entity, AxisAlignedBB axisalignedbb) {
        this.H.clear();
        int i = MathHelper.b((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.b((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.b((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.b((axisalignedbb.f + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.f(i1, j1)) {
                    this.c(i1, j1).a(entity, axisalignedbb, this.H);
                }
            }
        }

        return this.H;
    }

    public List a(Class oclass, AxisAlignedBB axisalignedbb) {
        int i = MathHelper.b((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.b((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.b((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.b((axisalignedbb.f + 2.0D) / 16.0D);
        ArrayList arraylist = new ArrayList();

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.f(i1, j1)) {
                    this.c(i1, j1).a(oclass, axisalignedbb, arraylist);
                }
            }
        }

        return arraylist;
    }

    public void b(int i, int j, int k, TileEntity tileentity) {
        if (this.f(i, j, k)) {
            this.b(i, k).f();
        }

        for (int l = 0; l < this.n.size(); ++l) {
            ((IWorldAccess) this.n.get(l)).a(i, j, k, tileentity);
        }
    }

    public int a(Class oclass) {
        int i = 0;

        for (int j = 0; j < this.b.size(); ++j) {
            Entity entity = (Entity) this.b.get(j);

            if (oclass.isAssignableFrom(entity.getClass())) {
                ++i;
            }
        }

        return i;
    }

    public void a(List list) {
        // CraftBukkit start
        Entity entity = null;
        for (int i = 0; i < list.size(); ++i) {
            entity = (Entity) list.get(i);
            // CraftBukkit start
            if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
                CreatureSpawnEvent event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving) entity);
                if (event.isCancelled()) {
                    continue;
                }
            }

            this.b.add(entity);
            // CraftBukkit end
            this.b((Entity) list.get(i));
        }
    }

    public void b(List list) {
        this.v.addAll(list);
    }

    public boolean a(int i, int j, int k, int l, boolean flag) {
        int i1 = this.getTypeId(j, k, l);
        Block block = Block.byId[i1];
        Block block1 = Block.byId[i];
        AxisAlignedBB axisalignedbb = block1.d(this, j, k, l);

        if (flag) {
            axisalignedbb = null;
        }

        // CraftBukkit start - We dont want to allow the user to override the bounding box check
        boolean defaultReturn = axisalignedbb != null && !this.a(axisalignedbb) ? false : (block != Block.WATER && block != Block.STATIONARY_WATER && block != Block.LAVA && block != Block.STATIONARY_LAVA && block != Block.FIRE && block != Block.SNOW ? i > 0 && block == null && block1.a(this, j, k, l) : true);

        if (axisalignedbb != null && !this.a(axisalignedbb)) {
            return false;
        }

        BlockCanBuildEvent event = new BlockCanBuildEvent(((WorldServer) this).getWorld().getBlockAt(j, k, l), i, defaultReturn);
        ((WorldServer) this).getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }

    public PathEntity a(Entity entity, Entity entity1, float f) {
        int i = MathHelper.b(entity.locX);
        int j = MathHelper.b(entity.locY);
        int k = MathHelper.b(entity.locZ);
        int l = (int) (f + 16.0F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache(this, i1, j1, k1, l1, i2, j2);

        return (new Pathfinder(chunkcache)).a(entity, entity1, f);
    }

    public PathEntity a(Entity entity, int i, int j, int k, float f) {
        int l = MathHelper.b(entity.locX);
        int i1 = MathHelper.b(entity.locY);
        int j1 = MathHelper.b(entity.locZ);
        int k1 = (int) (f + 8.0F);
        int l1 = l - k1;
        int i2 = i1 - k1;
        int j2 = j1 - k1;
        int k2 = l + k1;
        int l2 = i1 + k1;
        int i3 = j1 + k1;
        ChunkCache chunkcache = new ChunkCache(this, l1, i2, j2, k2, l2, i3);

        return (new Pathfinder(chunkcache)).a(entity, i, j, k, f);
    }

    public boolean i(int i, int j, int k, int l) {
        int i1 = this.getTypeId(i, j, k);

        return i1 == 0 ? false : Block.byId[i1].c(this, i, j, k, l);
    }

    public boolean o(int i, int j, int k) {
        return this.i(i, j - 1, k, 0) ? true : (this.i(i, j + 1, k, 1) ? true : (this.i(i, j, k - 1, 2) ? true : (this.i(i, j, k + 1, 3) ? true : (this.i(i - 1, j, k, 4) ? true : this.i(i + 1, j, k, 5)))));
    }

    public boolean j(int i, int j, int k, int l) {
        if (this.d(i, j, k)) {
            return this.o(i, j, k);
        } else {
            int i1 = this.getTypeId(i, j, k);

            return i1 == 0 ? false : Block.byId[i1].b((IBlockAccess) this, i, j, k, l);
        }
    }

    public boolean p(int i, int j, int k) {
        return this.j(i, j - 1, k, 0) ? true : (this.j(i, j + 1, k, 1) ? true : (this.j(i, j, k - 1, 2) ? true : (this.j(i, j, k + 1, 3) ? true : (this.j(i - 1, j, k, 4) ? true : this.j(i + 1, j, k, 5)))));
    }

    public EntityHuman a(Entity entity, double d0) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0);
    }

    public EntityHuman a(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;

        for (int i = 0; i < this.d.size(); ++i) {
            EntityHuman entityhuman1 = (EntityHuman) this.d.get(i);
            double d5 = entityhuman1.d(d0, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                d4 = d5;
                entityhuman = entityhuman1;
            }
        }

        return entityhuman;
    }

    public EntityHuman a(String s) {
        for (int i = 0; i < this.d.size(); ++i) {
            if (s.equals(((EntityHuman) this.d.get(i)).name)) {
                return (EntityHuman) this.d.get(i);
            }
        }

        return null;
    }

    public byte[] c(int i, int j, int k, int l, int i1, int j1) {
        byte[] abyte = new byte[l * i1 * j1 * 5 / 2];
        int k1 = i >> 4;
        int l1 = k >> 4;
        int i2 = i + l - 1 >> 4;
        int j2 = k + j1 - 1 >> 4;
        int k2 = 0;
        int l2 = j;
        int i3 = j + i1;

        if (j < 0) {
            l2 = 0;
        }

        if (i3 > 128) {
            i3 = 128;
        }

        for (int j3 = k1; j3 <= i2; ++j3) {
            int k3 = i - j3 * 16;
            int l3 = i + l - j3 * 16;

            if (k3 < 0) {
                k3 = 0;
            }

            if (l3 > 16) {
                l3 = 16;
            }

            for (int i4 = l1; i4 <= j2; ++i4) {
                int j4 = k - i4 * 16;
                int k4 = k + j1 - i4 * 16;

                if (j4 < 0) {
                    j4 = 0;
                }

                if (k4 > 16) {
                    k4 = 16;
                }

                k2 = this.c(j3, i4).a(abyte, k3, l2, j4, l3, i3, k4, k2);
            }
        }

        return abyte;
    }

    public void j() {
        this.p.b();
    }

    public void a(long i) {
        this.q.a(i);
    }

    public long k() {
        return this.q.b();
    }

    public long l() {
        return this.q.f();
    }

    public ChunkCoordinates m() {
        return new ChunkCoordinates(this.q.c(), this.q.d(), this.q.e());
    }

    public boolean a(EntityHuman entityhuman, int i, int j, int k) {
        return true;
    }

    public void a(Entity entity, byte b0) {}

    public IChunkProvider n() {
        return this.o;
    }

    public void d(int i, int j, int k, int l, int i1) {
        int j1 = this.getTypeId(i, j, k);

        if (j1 > 0) {
            Block.byId[j1].a(this, i, j, k, l, i1);
        }
    }

    public IDataManager o() {
        return this.p;
    }

    public WorldData p() {
        return this.q;
    }

    public void q() {
        this.A = !this.d.isEmpty();
        Iterator iterator = this.d.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            // CraftBukkit start
            if (!entityhuman.F() && !entityhuman.fauxSleeping) {
            // CraftBukkit end
                this.A = false;
                break;
            }
        }
    }

    // CraftBukkit start
    // Calls the method that checks to see if players are sleeping
    // Called by CraftPlayer.setPermanentSleeping()
    public void checkSleepStatus() {
        if (!isStatic) {
            q();
        }
    }
    // CraftBukkit end

    protected void r() {
        this.A = false;
        Iterator iterator = this.d.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (entityhuman.F()) {
                entityhuman.a(false, false, false);
            }
        }
    }

    public boolean s() {
        if (this.A && !this.isStatic) {
            Iterator iterator = this.d.iterator();
            
            // CraftBukkit start
            boolean foundActualSleepers = false;
            
            // This allows us to assume that some people are in bed
            // but not really, allowing time to pass in spite of AFKers

            EntityHuman entityhuman;

            do {
                if (!iterator.hasNext()) {
                    return foundActualSleepers;
                }

                entityhuman = (EntityHuman) iterator.next();
                if (entityhuman.G()) {
                    foundActualSleepers = true;
                }
            } while (entityhuman.G() || entityhuman.fauxSleeping);
            // CraftBukkit end

            return false;
        } else {
            return false;
        }
    }
}
