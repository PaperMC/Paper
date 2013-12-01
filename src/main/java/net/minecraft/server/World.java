package net.minecraft.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.LongHashSet;
import org.bukkit.craftbukkit.util.UnsafeList;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
// CraftBukkit end

public abstract class World implements IBlockAccess {

    public boolean d;
    public List entityList = new ArrayList();
    protected List f = new ArrayList();
    public Set tileEntityList = new HashSet(); // CraftBukkit - ArrayList -> HashSet
    private List a = new ArrayList();
    private List b = new ArrayList();
    public List players = new ArrayList();
    public List i = new ArrayList();
    private long c = 16777215L;
    public int j;
    protected int k = (new Random()).nextInt();
    protected final int l = 1013904223;
    protected float m;
    protected float n;
    protected float o;
    protected float p;
    public int q;
    public EnumDifficulty difficulty;
    public Random random = new Random();
    public WorldProvider worldProvider; // CraftBukkit - remove final
    protected List u = new ArrayList();
    public IChunkProvider chunkProvider; // CraftBukkit - public
    protected final IDataManager dataManager;
    public WorldData worldData; // CraftBukkit - public
    public boolean isLoading;
    public PersistentCollection worldMaps;
    public final PersistentVillage villages;
    protected final VillageSiege siegeManager = new VillageSiege(this);
    public final MethodProfiler methodProfiler;
    private final Vec3DPool J = new Vec3DPool(300, 2000);
    private final Calendar K = Calendar.getInstance();
    public Scoreboard scoreboard = new Scoreboard(); // CraftBukkit - protected -> public
    public boolean isStatic;
    // CraftBukkit start - public, longhashset
    protected LongHashSet chunkTickList = new LongHashSet();
    private int L;
    public boolean allowMonsters;
    public boolean allowAnimals;
    // Added the following
    public boolean callingPlaceEvent = false;
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    // CraftBukkit end
    private ArrayList M;
    private boolean N;
    int[] I;

    public BiomeBase getBiome(int i, int j) {
        if (this.isLoaded(i, 0, j)) {
            Chunk chunk = this.getChunkAtWorldCoords(i, j);

            try {
                return chunk.a(i & 15, j & 15, this.worldProvider.e);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Getting biome");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Coordinates of biome request");

                crashreportsystemdetails.a("Location", (Callable) (new CrashReportWorldLocation(this, i, j)));
                throw new ReportedException(crashreport);
            }
        } else {
            return this.worldProvider.e.getBiome(i, j);
        }
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.e;
    }

    // CraftBukkit start
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory = true;
    public ChunkGenerator generator;
    Chunk lastChunkAccessed;
    int lastXAccessed = Integer.MIN_VALUE;
    int lastZAccessed = Integer.MIN_VALUE;
    final Object chunkLock = new Object();

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    // Changed signature - added gen and env
    public World(IDataManager idatamanager, String s, WorldSettings worldsettings, WorldProvider worldprovider, MethodProfiler methodprofiler, ChunkGenerator gen, org.bukkit.World.Environment env) {
        this.generator = gen;
        this.world = new CraftWorld((WorldServer) this, gen, env);
        this.ticksPerAnimalSpawns = this.getServer().getTicksPerAnimalSpawns(); // CraftBukkit
        this.ticksPerMonsterSpawns = this.getServer().getTicksPerMonsterSpawns(); // CraftBukkit
        // CraftBukkit end

        this.L = this.random.nextInt(12000);
        this.allowMonsters = true;
        this.allowAnimals = true;
        this.M = new ArrayList();
        this.I = new int['\u8000'];
        this.dataManager = idatamanager;
        this.methodProfiler = methodprofiler;
        this.worldMaps = new PersistentCollection(idatamanager);
        this.worldData = idatamanager.getWorldData();
        if (worldprovider != null) {
            this.worldProvider = worldprovider;
        } else if (this.worldData != null && this.worldData.j() != 0) {
            this.worldProvider = WorldProvider.byDimension(this.worldData.j());
        } else {
            this.worldProvider = WorldProvider.byDimension(0);
        }

        if (this.worldData == null) {
            this.worldData = new WorldData(worldsettings, s);
        } else {
            this.worldData.setName(s);
        }

        this.worldProvider.a(this);
        this.chunkProvider = this.j();
        if (!this.worldData.isInitialized()) {
            try {
                this.a(worldsettings);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Exception initializing level");

                try {
                    this.a(crashreport);
                } catch (Throwable throwable1) {
                    ;
                }

                throw new ReportedException(crashreport);
            }

            this.worldData.d(true);
        }

        PersistentVillage persistentvillage = (PersistentVillage) this.worldMaps.get(PersistentVillage.class, "villages");

        if (persistentvillage == null) {
            this.villages = new PersistentVillage(this);
            this.worldMaps.a("villages", this.villages);
        } else {
            this.villages = persistentvillage;
            this.villages.a(this);
        }

        this.A();
        this.a();

        this.getServer().addWorld(this.world); // CraftBukkit
    }

    protected abstract IChunkProvider j();

    protected void a(WorldSettings worldsettings) {
        this.worldData.d(true);
    }

    public Block b(int i, int j) {
        int k;

        for (k = 63; !this.isEmpty(i, k + 1, j); ++k) {
            ;
        }

        return this.getType(i, k, j);
    }

    public Block getType(int i, int j, int k) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 && j >= 0 && j < 256) {
            Chunk chunk = null;

            try {
                chunk = this.getChunkAt(i >> 4, k >> 4);
                return chunk.getType(i & 15, j, k & 15);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Exception getting block type in world");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Requested block coordinates");

                crashreportsystemdetails.a("Found chunk", Boolean.valueOf(chunk == null));
                crashreportsystemdetails.a("Location", CrashReportSystemDetails.a(i, j, k));
                throw new ReportedException(crashreport);
            }
        } else {
            return Blocks.AIR;
        }
    }

    public boolean isEmpty(int i, int j, int k) {
        return this.getType(i, j, k).getMaterial() == Material.AIR;
    }

    public boolean isLoaded(int i, int j, int k) {
        return j >= 0 && j < 256 ? this.isChunkLoaded(i >> 4, k >> 4) : false;
    }

    public boolean areChunksLoaded(int i, int j, int k, int l) {
        return this.b(i - l, j - l, k - l, i + l, j + l, k + l);
    }

    public boolean b(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    // CraftBukkit - check unload queue too so we don't leak a chunk
                    if (!this.isChunkLoaded(k1, l1) || ((WorldServer) this).chunkProviderServer.unloadQueue.contains(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean isChunkLoaded(int i, int j) {
        return this.chunkProvider.isChunkLoaded(i, j);
    }

    public Chunk getChunkAtWorldCoords(int i, int j) {
        return this.getChunkAt(i >> 4, j >> 4);
    }

    public Chunk getChunkAt(int i, int j) {
        // CraftBukkit start
        Chunk result = null;
        synchronized (this.chunkLock) {
            if (this.lastChunkAccessed == null || this.lastXAccessed != i || this.lastZAccessed != j) {
                this.lastChunkAccessed = this.chunkProvider.getOrCreateChunk(i, j);
                this.lastXAccessed = i;
                this.lastZAccessed = j;
            }
            result = this.lastChunkAccessed;
        }
        return result;
        // CraftBukkit end
    }

    public boolean setTypeAndData(int i, int j, int k, Block block, int l, int i1) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                Block block1 = null;

                if ((i1 & 1) != 0) {
                    block1 = chunk.getType(i & 15, j, k & 15);
                }

                boolean flag = chunk.a(i & 15, j, k & 15, block, l);

                this.methodProfiler.a("checkLight");
                this.t(i, j, k);
                this.methodProfiler.b();
                if (flag) {
                    if ((i1 & 2) != 0 && (!this.isStatic || (i1 & 4) == 0) && chunk.k()) {
                        this.notify(i, j, k);
                    }

                    if (!this.isStatic && (i1 & 1) != 0) {
                        this.update(i, j, k, block1);
                        if (block.M()) {
                            this.f(i, j, k, block);
                        }
                    }
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public int getData(int i, int j, int k) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return 0;
            } else if (j >= 256) {
                return 0;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.getData(i, j, k);
            }
        } else {
            return 0;
        }
    }

    public boolean setData(int i, int j, int k, int l, int i1) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                int j1 = i & 15;
                int k1 = k & 15;
                boolean flag = chunk.a(j1, j, k1, l);

                if (flag) {
                    Block block = chunk.getType(j1, j, k1);

                    if ((i1 & 2) != 0 && (!this.isStatic || (i1 & 4) == 0) && chunk.k()) {
                        this.notify(i, j, k);
                    }

                    if (!this.isStatic && (i1 & 1) != 0) {
                        this.update(i, j, k, block);
                        if (block.M()) {
                            this.f(i, j, k, block);
                        }
                    }
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public boolean setAir(int i, int j, int k) {
        return this.setTypeAndData(i, j, k, Blocks.AIR, 0, 3);
    }

    public boolean setAir(int i, int j, int k, boolean flag) {
        Block block = this.getType(i, j, k);

        if (block.getMaterial() == Material.AIR) {
            return false;
        } else {
            int l = this.getData(i, j, k);

            this.triggerEffect(2001, i, j, k, Block.b(block) + (l << 12));
            if (flag) {
                block.b(this, i, j, k, l, 0);
            }

            return this.setTypeAndData(i, j, k, Blocks.AIR, 0, 3);
        }
    }

    public boolean setTypeUpdate(int i, int j, int k, Block block) {
        return this.setTypeAndData(i, j, k, block, 0, 3);
    }

    public void notify(int i, int j, int k) {
        for (int l = 0; l < this.u.size(); ++l) {
            ((IWorldAccess) this.u.get(l)).a(i, j, k);
        }
    }

    public void update(int i, int j, int k, Block block) {
        this.applyPhysics(i, j, k, block);
    }

    public void b(int i, int j, int k, int l) {
        int i1;

        if (k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if (!this.worldProvider.g) {
            for (i1 = k; i1 <= l; ++i1) {
                this.c(EnumSkyBlock.SKY, i, i1, j);
            }
        }

        this.c(i, k, j, i, l, j);
    }

    public void c(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < this.u.size(); ++k1) {
            ((IWorldAccess) this.u.get(k1)).a(i, j, k, l, i1, j1);
        }
    }

    public void applyPhysics(int i, int j, int k, Block block) {
        this.e(i - 1, j, k, block);
        this.e(i + 1, j, k, block);
        this.e(i, j - 1, k, block);
        this.e(i, j + 1, k, block);
        this.e(i, j, k - 1, block);
        this.e(i, j, k + 1, block);
    }

    public void b(int i, int j, int k, Block block, int l) {
        if (l != 4) {
            this.e(i - 1, j, k, block);
        }

        if (l != 5) {
            this.e(i + 1, j, k, block);
        }

        if (l != 0) {
            this.e(i, j - 1, k, block);
        }

        if (l != 1) {
            this.e(i, j + 1, k, block);
        }

        if (l != 2) {
            this.e(i, j, k - 1, block);
        }

        if (l != 3) {
            this.e(i, j, k + 1, block);
        }
    }

    public void e(int i, int j, int k, Block block) {
        if (!this.isStatic) {
            Block block1 = this.getType(i, j, k);

            try {
                // CraftBukkit start
                CraftWorld world = ((WorldServer) this).getWorld();
                if (world != null) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(i, j, k), l);
                    this.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit end

                block1.doPhysics(this, i, j, k, block);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Exception while updating neighbours");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being updated");

                int l;

                try {
                    l = this.getData(i, j, k);
                } catch (Throwable throwable1) {
                    l = -1;
                }

                crashreportsystemdetails.a("Source block type", (Callable) (new CrashReportSourceBlockType(this, block)));
                CrashReportSystemDetails.a(crashreportsystemdetails, i, j, k, block1, l);
                throw new ReportedException(crashreport);
            }
        }
    }

    public boolean a(int i, int j, int k, Block block) {
        return false;
    }

    public boolean i(int i, int j, int k) {
        return this.getChunkAt(i >> 4, k >> 4).d(i & 15, j, k & 15);
    }

    public int j(int i, int j, int k) {
        if (j < 0) {
            return 0;
        } else {
            if (j >= 256) {
                j = 255;
            }

            return this.getChunkAt(i >> 4, k >> 4).b(i & 15, j, k & 15, 0);
        }
    }

    public int getLightLevel(int i, int j, int k) {
        return this.b(i, j, k, true);
    }

    public int b(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (flag && this.getType(i, j, k).n()) {
                int l = this.b(i, j + 1, k, false);
                int i1 = this.b(i + 1, j, k, false);
                int j1 = this.b(i - 1, j, k, false);
                int k1 = this.b(i, j, k + 1, false);
                int l1 = this.b(i, j, k - 1, false);

                if (i1 > l) {
                    l = i1;
                }

                if (j1 > l) {
                    l = j1;
                }

                if (k1 > l) {
                    l = k1;
                }

                if (l1 > l) {
                    l = l1;
                }

                return l;
            } else if (j < 0) {
                return 0;
            } else {
                if (j >= 256) {
                    j = 255;
                }

                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.b(i, j, k, this.j);
            }
        } else {
            return 15;
        }
    }

    public int getHighestBlockYAt(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.isChunkLoaded(i >> 4, j >> 4)) {
                return 0;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, j >> 4);

                return chunk.b(i & 15, j & 15);
            }
        } else {
            return 64;
        }
    }

    public int g(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.isChunkLoaded(i >> 4, j >> 4)) {
                return 0;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, j >> 4);

                return chunk.r;
            }
        } else {
            return 64;
        }
    }

    public int b(EnumSkyBlock enumskyblock, int i, int j, int k) {
        if (j < 0) {
            j = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            int l = i >> 4;
            int i1 = k >> 4;

            if (!this.isChunkLoaded(l, i1)) {
                return enumskyblock.c;
            } else {
                Chunk chunk = this.getChunkAt(l, i1);

                return chunk.getBrightness(enumskyblock, i & 15, j, k & 15);
            }
        } else {
            return enumskyblock.c;
        }
    }

    public void b(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j >= 0) {
                if (j < 256) {
                    if (this.isChunkLoaded(i >> 4, k >> 4)) {
                        Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                        chunk.a(enumskyblock, i & 15, j, k & 15, l);

                        for (int i1 = 0; i1 < this.u.size(); ++i1) {
                            ((IWorldAccess) this.u.get(i1)).b(i, j, k);
                        }
                    }
                }
            }
        }
    }

    public void m(int i, int j, int k) {
        for (int l = 0; l < this.u.size(); ++l) {
            ((IWorldAccess) this.u.get(l)).b(i, j, k);
        }
    }

    public float n(int i, int j, int k) {
        return this.worldProvider.h[this.getLightLevel(i, j, k)];
    }

    public boolean v() {
        return this.j < 4;
    }

    public MovingObjectPosition a(Vec3D vec3d, Vec3D vec3d1) {
        return this.rayTrace(vec3d, vec3d1, false, false, false);
    }

    public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag) {
        return this.rayTrace(vec3d, vec3d1, flag, false, false);
    }

    public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag, boolean flag1, boolean flag2) {
        if (!Double.isNaN(vec3d.c) && !Double.isNaN(vec3d.d) && !Double.isNaN(vec3d.e)) {
            if (!Double.isNaN(vec3d1.c) && !Double.isNaN(vec3d1.d) && !Double.isNaN(vec3d1.e)) {
                int i = MathHelper.floor(vec3d1.c);
                int j = MathHelper.floor(vec3d1.d);
                int k = MathHelper.floor(vec3d1.e);
                int l = MathHelper.floor(vec3d.c);
                int i1 = MathHelper.floor(vec3d.d);
                int j1 = MathHelper.floor(vec3d.e);
                Block block = this.getType(l, i1, j1);
                int k1 = this.getData(l, i1, j1);

                if ((!flag1 || block.a(this, l, i1, j1) != null) && block.a(k1, flag)) {
                    MovingObjectPosition movingobjectposition = block.a(this, l, i1, j1, vec3d, vec3d1);

                    if (movingobjectposition != null) {
                        return movingobjectposition;
                    }
                }

                MovingObjectPosition movingobjectposition1 = null;

                k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec3d.c) || Double.isNaN(vec3d.d) || Double.isNaN(vec3d.e)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return flag2 ? movingobjectposition1 : null;
                    }

                    boolean flag3 = true;
                    boolean flag4 = true;
                    boolean flag5 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag5 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec3d1.c - vec3d.c;
                    double d7 = vec3d1.d - vec3d.d;
                    double d8 = vec3d1.e - vec3d.e;

                    if (flag3) {
                        d3 = (d0 - vec3d.c) / d6;
                    }

                    if (flag4) {
                        d4 = (d1 - vec3d.d) / d7;
                    }

                    if (flag5) {
                        d5 = (d2 - vec3d.e) / d8;
                    }

                    boolean flag6 = false;
                    byte b0;

                    if (d3 < d4 && d3 < d5) {
                        if (i > l) {
                            b0 = 4;
                        } else {
                            b0 = 5;
                        }

                        vec3d.c = d0;
                        vec3d.d += d7 * d3;
                        vec3d.e += d8 * d3;
                    } else if (d4 < d5) {
                        if (j > i1) {
                            b0 = 0;
                        } else {
                            b0 = 1;
                        }

                        vec3d.c += d6 * d4;
                        vec3d.d = d1;
                        vec3d.e += d8 * d4;
                    } else {
                        if (k > j1) {
                            b0 = 2;
                        } else {
                            b0 = 3;
                        }

                        vec3d.c += d6 * d5;
                        vec3d.d += d7 * d5;
                        vec3d.e = d2;
                    }

                    Vec3D vec3d2 = this.getVec3DPool().create(vec3d.c, vec3d.d, vec3d.e);

                    l = (int) (vec3d2.c = (double) MathHelper.floor(vec3d.c));
                    if (b0 == 5) {
                        --l;
                        ++vec3d2.c;
                    }

                    i1 = (int) (vec3d2.d = (double) MathHelper.floor(vec3d.d));
                    if (b0 == 1) {
                        --i1;
                        ++vec3d2.d;
                    }

                    j1 = (int) (vec3d2.e = (double) MathHelper.floor(vec3d.e));
                    if (b0 == 3) {
                        --j1;
                        ++vec3d2.e;
                    }

                    Block block1 = this.getType(l, i1, j1);
                    int l1 = this.getData(l, i1, j1);

                    if (!flag1 || block1.a(this, l, i1, j1) != null) {
                        if (block1.a(l1, flag)) {
                            MovingObjectPosition movingobjectposition2 = block1.a(this, l, i1, j1, vec3d, vec3d1);

                            if (movingobjectposition2 != null) {
                                vec3d2.b.release(vec3d2); // CraftBukkit
                                return movingobjectposition2;
                            }
                        } else {
                            movingobjectposition1 = new MovingObjectPosition(l, i1, j1, b0, vec3d, false);
                        }
                    }
                    vec3d2.b.release(vec3d2); // CraftBukkit
                }

                return flag2 ? movingobjectposition1 : null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void makeSound(Entity entity, String s, float f, float f1) {
        for (int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess) this.u.get(i)).a(s, entity.locX, entity.locY - (double) entity.height, entity.locZ, f, f1);
        }
    }

    public void a(EntityHuman entityhuman, String s, float f, float f1) {
        for (int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess) this.u.get(i)).a(entityhuman, s, entityhuman.locX, entityhuman.locY - (double) entityhuman.height, entityhuman.locZ, f, f1);
        }
    }

    public void makeSound(double d0, double d1, double d2, String s, float f, float f1) {
        for (int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess) this.u.get(i)).a(s, d0, d1, d2, f, f1);
        }
    }

    public void a(double d0, double d1, double d2, String s, float f, float f1, boolean flag) {}

    public void a(String s, int i, int j, int k) {
        for (int l = 0; l < this.u.size(); ++l) {
            ((IWorldAccess) this.u.get(l)).a(s, i, j, k);
        }
    }

    public void addParticle(String s, double d0, double d1, double d2, double d3, double d4, double d5) {
        for (int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess) this.u.get(i)).a(s, d0, d1, d2, d3, d4, d5);
        }
    }

    public boolean strikeLightning(Entity entity) {
        this.i.add(entity);
        return true;
    }

    public boolean addEntity(Entity entity) {
        // CraftBukkit start - Used for entities other than creatures
        return this.addEntity(entity, SpawnReason.DEFAULT); // Set reason as DEFAULT
    }

    public boolean addEntity(Entity entity, SpawnReason spawnReason) { // Changed signature, added SpawnReason
        if (entity == null) return false;
        // CraftBukkit end

        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);
        boolean flag = entity.o;

        if (entity instanceof EntityHuman) {
            flag = true;
        }

        // CraftBukkit start
        org.bukkit.event.Cancellable event = null;
        if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
            boolean isAnimal = entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal || entity instanceof EntityGolem;
            boolean isMonster = entity instanceof EntityMonster || entity instanceof EntityGhast || entity instanceof EntitySlime;

            if (spawnReason != SpawnReason.CUSTOM) {
                if (isAnimal && !allowAnimals || isMonster && !allowMonsters)  {
                    entity.dead = true;
                    return false;
                }
            }

            event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving) entity, spawnReason);
        } else if (entity instanceof EntityItem) {
            event = CraftEventFactory.callItemSpawnEvent((EntityItem) entity);
        } else if (entity.getBukkitEntity() instanceof org.bukkit.entity.Projectile) {
            // Not all projectiles extend EntityProjectile, so check for Bukkit interface instead
            event = CraftEventFactory.callProjectileLaunchEvent(entity);
        }

        if (event != null && (event.isCancelled() || entity.dead)) {
            entity.dead = true;
            return false;
        }
        // CraftBukkit end

        if (!flag && !this.isChunkLoaded(i, j)) {
            entity.dead = true; // CraftBukkit
            return false;
        } else {
            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;

                this.players.add(entityhuman);
                this.everyoneSleeping();
            }

            this.getChunkAt(i, j).a(entity);
            this.entityList.add(entity);
            this.a(entity);
            return true;
        }
    }

    protected void a(Entity entity) {
        for (int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess) this.u.get(i)).a(entity);
        }

        entity.valid = true; // CraftBukkit
    }

    protected void b(Entity entity) {
        for (int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess) this.u.get(i)).b(entity);
        }

        entity.valid = false; // CraftBukkit
    }

    public void kill(Entity entity) {
        if (entity.passenger != null) {
            entity.passenger.mount((Entity) null);
        }

        if (entity.vehicle != null) {
            entity.mount((Entity) null);
        }

        entity.die();
        if (entity instanceof EntityHuman) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }
    }

    public void removeEntity(Entity entity) {
        entity.die();
        if (entity instanceof EntityHuman) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }

        int i = entity.ai;
        int j = entity.ak;

        if (entity.ah && this.isChunkLoaded(i, j)) {
            this.getChunkAt(i, j).b(entity);
        }

        this.entityList.remove(entity);
        this.b(entity);
    }

    public void addIWorldAccess(IWorldAccess iworldaccess) {
        this.u.add(iworldaccess);
    }

    public List getCubes(Entity entity, AxisAlignedBB axisalignedbb) {
        this.M.clear();
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.isLoaded(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        Block block;

                        if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000) {
                            block = this.getType(k1, i2, l1);
                        } else {
                            block = Blocks.STONE;
                        }

                        block.a(this, k1, i2, l1, axisalignedbb, this.M, entity);
                    }
                }
            }
        }

        double d0 = 0.25D;
        List list = this.getEntities(entity, axisalignedbb.grow(d0, d0, d0));

        for (int j2 = 0; j2 < list.size(); ++j2) {
            AxisAlignedBB axisalignedbb1 = ((Entity) list.get(j2)).J();

            if (axisalignedbb1 != null && axisalignedbb1.b(axisalignedbb)) {
                this.M.add(axisalignedbb1);
            }

            axisalignedbb1 = entity.g((Entity) list.get(j2));
            if (axisalignedbb1 != null && axisalignedbb1.b(axisalignedbb)) {
                this.M.add(axisalignedbb1);
            }
        }

        return this.M;
    }

    public List a(AxisAlignedBB axisalignedbb) {
        this.M.clear();
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.isLoaded(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        Block block;

                        if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000) {
                            block = this.getType(k1, i2, l1);
                        } else {
                            block = Blocks.BEDROCK;
                        }

                        block.a(this, k1, i2, l1, axisalignedbb, this.M, (Entity) null);
                    }
                }
            }
        }

        return this.M;
    }

    public int a(float f) {
        float f1 = this.c(f);
        float f2 = 1.0F - (MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F);

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        f2 = 1.0F - f2;
        f2 = (float) ((double) f2 * (1.0D - (double) (this.j(f) * 5.0F) / 16.0D));
        f2 = (float) ((double) f2 * (1.0D - (double) (this.h(f) * 5.0F) / 16.0D));
        f2 = 1.0F - f2;
        return (int) (f2 * 11.0F);
    }

    public float c(float f) {
        return this.worldProvider.a(this.worldData.getDayTime(), f);
    }

    public float x() {
        return WorldProvider.a[this.worldProvider.a(this.worldData.getDayTime())];
    }

    public float d(float f) {
        float f1 = this.c(f);

        return f1 * 3.1415927F * 2.0F;
    }

    public int h(int i, int j) {
        return this.getChunkAtWorldCoords(i, j).d(i & 15, j & 15);
    }

    public int i(int i, int j) {
        Chunk chunk = this.getChunkAtWorldCoords(i, j);
        int k = chunk.h() + 15;

        i &= 15;

        for (j &= 15; k > 0; --k) {
            Block block = chunk.getType(i, k, j);

            if (block.getMaterial().isSolid() && block.getMaterial() != Material.LEAVES) {
                return k + 1;
            }
        }

        return -1;
    }

    public void a(int i, int j, int k, Block block, int l) {}

    public void a(int i, int j, int k, Block block, int l, int i1) {}

    public void b(int i, int j, int k, Block block, int l, int i1) {}

    public void tickEntities() {
        this.methodProfiler.a("entities");
        this.methodProfiler.a("global");

        int i;
        Entity entity;
        CrashReport crashreport;
        CrashReportSystemDetails crashreportsystemdetails;

        for (i = 0; i < this.i.size(); ++i) {
            entity = (Entity) this.i.get(i);
            // CraftBukkit start - Fixed an NPE, don't process entities in chunks queued for unload
            if (entity == null) {
                continue;
            }

            ChunkProviderServer chunkProviderServer = ((WorldServer) this).chunkProviderServer;
            if (chunkProviderServer.unloadQueue.contains(MathHelper.floor(entity.locX) >> 4, MathHelper.floor(entity.locZ) >> 4)) {
                continue;
            }
            // CraftBukkit end

            try {
                ++entity.ticksLived;
                entity.h();
            } catch (Throwable throwable) {
                crashreport = CrashReport.a(throwable, "Ticking entity");
                crashreportsystemdetails = crashreport.a("Entity being ticked");
                if (entity == null) {
                    crashreportsystemdetails.a("Entity", "~~NULL~~");
                } else {
                    entity.a(crashreportsystemdetails);
                }

                throw new ReportedException(crashreport);
            }

            if (entity.dead) {
                this.i.remove(i--);
            }
        }

        this.methodProfiler.c("remove");
        this.entityList.removeAll(this.f);

        int j;
        int k;

        for (i = 0; i < this.f.size(); ++i) {
            entity = (Entity) this.f.get(i);
            j = entity.ai;
            k = entity.ak;
            if (entity.ah && this.isChunkLoaded(j, k)) {
                this.getChunkAt(j, k).b(entity);
            }
        }

        for (i = 0; i < this.f.size(); ++i) {
            this.b((Entity) this.f.get(i));
        }

        this.f.clear();
        this.methodProfiler.c("regular");

        for (i = 0; i < this.entityList.size(); ++i) {
            entity = (Entity) this.entityList.get(i);

            // CraftBukkit start - Don't tick entities in chunks queued for unload
            ChunkProviderServer chunkProviderServer = ((WorldServer) this).chunkProviderServer;
            if (chunkProviderServer.unloadQueue.contains(MathHelper.floor(entity.locX) >> 4, MathHelper.floor(entity.locZ) >> 4)) {
                continue;
            }
            // CraftBukkit end

            if (entity.vehicle != null) {
                if (!entity.vehicle.dead && entity.vehicle.passenger == entity) {
                    continue;
                }

                entity.vehicle.passenger = null;
                entity.vehicle = null;
            }

            this.methodProfiler.a("tick");
            if (!entity.dead) {
                try {
                    this.playerJoinedWorld(entity);
                } catch (Throwable throwable1) {
                    crashreport = CrashReport.a(throwable1, "Ticking entity");
                    crashreportsystemdetails = crashreport.a("Entity being ticked");
                    entity.a(crashreportsystemdetails);
                    throw new ReportedException(crashreport);
                }
            }

            this.methodProfiler.b();
            this.methodProfiler.a("remove");
            if (entity.dead) {
                j = entity.ai;
                k = entity.ak;
                if (entity.ah && this.isChunkLoaded(j, k)) {
                    this.getChunkAt(j, k).b(entity);
                }

                this.entityList.remove(i--);
                this.b(entity);
            }

            this.methodProfiler.b();
        }

        this.methodProfiler.c("blockEntities");
        this.N = true;
        Iterator iterator = this.tileEntityList.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();
            // CraftBukkit start - Don't tick entities in chunks queued for unload
            ChunkProviderServer chunkProviderServer = ((WorldServer) this).chunkProviderServer;
            if (chunkProviderServer.unloadQueue.contains(tileentity.x >> 4, tileentity.z >> 4)) {
                continue;
            }
            // CraftBukkit end

            if (!tileentity.r() && tileentity.o() && this.isLoaded(tileentity.x, tileentity.y, tileentity.z)) {
                try {
                    tileentity.h();
                } catch (Throwable throwable2) {
                    crashreport = CrashReport.a(throwable2, "Ticking block entity");
                    crashreportsystemdetails = crashreport.a("Block entity being ticked");
                    tileentity.a(crashreportsystemdetails);
                    throw new ReportedException(crashreport);
                }
            }

            if (tileentity.r()) {
                iterator.remove();
                if (this.isChunkLoaded(tileentity.x >> 4, tileentity.z >> 4)) {
                    Chunk chunk = this.getChunkAt(tileentity.x >> 4, tileentity.z >> 4);

                    if (chunk != null) {
                        chunk.f(tileentity.x & 15, tileentity.y, tileentity.z & 15);
                    }
                }
            }
        }

        this.N = false;
        if (!this.b.isEmpty()) {
            this.tileEntityList.removeAll(this.b);
            this.b.clear();
        }

        this.methodProfiler.c("pendingBlockEntities");
        if (!this.a.isEmpty()) {
            for (int l = 0; l < this.a.size(); ++l) {
                TileEntity tileentity1 = (TileEntity) this.a.get(l);

                if (!tileentity1.r()) {
                    /* CraftBukkit start - Order matters, moved down
                    if (!this.tileEntityList.contains(tileentity1)) {
                        this.tileEntityList.add(tileentity1);
                    }
                    // CraftBukkit end */

                    if (this.isChunkLoaded(tileentity1.x >> 4, tileentity1.z >> 4)) {
                        Chunk chunk1 = this.getChunkAt(tileentity1.x >> 4, tileentity1.z >> 4);

                        if (chunk1 != null) {
                            chunk1.a(tileentity1.x & 15, tileentity1.y, tileentity1.z & 15, tileentity1);
                            // CraftBukkit start - Moved down from above
                            if (!this.tileEntityList.contains(tileentity1)) {
                                this.tileEntityList.add(tileentity1);
                            }
                            // CraftBukkit end
                        }
                    }

                    this.notify(tileentity1.x, tileentity1.y, tileentity1.z);
                }
            }

            this.a.clear();
        }

        this.methodProfiler.b();
        this.methodProfiler.b();
    }

    public void a(Collection collection) {
        if (this.N) {
            this.a.addAll(collection);
        } else {
            this.tileEntityList.addAll(collection);
        }
    }

    public void playerJoinedWorld(Entity entity) {
        this.entityJoinedWorld(entity, true);
    }

    public void entityJoinedWorld(Entity entity, boolean flag) {
        int i = MathHelper.floor(entity.locX);
        int j = MathHelper.floor(entity.locZ);
        byte b0 = 32;

        if (!flag || this.b(i - b0, 0, j - b0, i + b0, 0, j + b0)) {
            entity.T = entity.locX;
            entity.U = entity.locY;
            entity.V = entity.locZ;
            entity.lastYaw = entity.yaw;
            entity.lastPitch = entity.pitch;
            if (flag && entity.ah) {
                ++entity.ticksLived;
                if (entity.vehicle != null) {
                    entity.ab();
                } else {
                    entity.h();
                }
            }

            this.methodProfiler.a("chunkCheck");
            if (Double.isNaN(entity.locX) || Double.isInfinite(entity.locX)) {
                entity.locX = entity.T;
            }

            if (Double.isNaN(entity.locY) || Double.isInfinite(entity.locY)) {
                entity.locY = entity.U;
            }

            if (Double.isNaN(entity.locZ) || Double.isInfinite(entity.locZ)) {
                entity.locZ = entity.V;
            }

            if (Double.isNaN((double) entity.pitch) || Double.isInfinite((double) entity.pitch)) {
                entity.pitch = entity.lastPitch;
            }

            if (Double.isNaN((double) entity.yaw) || Double.isInfinite((double) entity.yaw)) {
                entity.yaw = entity.lastYaw;
            }

            int k = MathHelper.floor(entity.locX / 16.0D);
            int l = MathHelper.floor(entity.locY / 16.0D);
            int i1 = MathHelper.floor(entity.locZ / 16.0D);

            if (!entity.ah || entity.ai != k || entity.aj != l || entity.ak != i1) {
                if (entity.ah && this.isChunkLoaded(entity.ai, entity.ak)) {
                    this.getChunkAt(entity.ai, entity.ak).a(entity, entity.aj);
                }

                if (this.isChunkLoaded(k, i1)) {
                    entity.ah = true;
                    this.getChunkAt(k, i1).a(entity);
                } else {
                    entity.ah = false;
                }
            }

            this.methodProfiler.b();
            if (flag && entity.ah && entity.passenger != null) {
                if (!entity.passenger.dead && entity.passenger.vehicle == entity) {
                    this.playerJoinedWorld(entity.passenger);
                } else {
                    entity.passenger.vehicle = null;
                    entity.passenger = null;
                }
            }
        }
    }

    public boolean b(AxisAlignedBB axisalignedbb) {
        return this.a(axisalignedbb, (Entity) null);
    }

    public boolean a(AxisAlignedBB axisalignedbb, Entity entity) {
        List list = this.getEntities((Entity) null, axisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (!entity1.dead && entity1.l && entity1 != entity) {
                return false;
            }
        }

        return true;
    }

    public boolean c(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

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
                    Block block = this.getType(k1, l1, i2);

                    if (block.getMaterial() != Material.AIR) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean containsLiquid(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

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
                    Block block = this.getType(k1, l1, i2);

                    if (block.getMaterial().isLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean e(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        if (this.b(i, k, i1, j, l, j1)) {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = this.getType(k1, l1, i2);

                        if (block == Blocks.FIRE || block == Blocks.LAVA || block == Blocks.STATIONARY_LAVA) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material, Entity entity) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        if (!this.b(i, k, i1, j, l, j1)) {
            return false;
        } else {
            boolean flag = false;
            Vec3D vec3d = this.getVec3DPool().create(0.0D, 0.0D, 0.0D);

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = this.getType(k1, l1, i2);

                        if (block.getMaterial() == material) {
                            double d0 = (double) ((float) (l1 + 1) - BlockFluids.b(this.getData(k1, l1, i2)));

                            if ((double) l >= d0) {
                                flag = true;
                                block.a(this, k1, l1, i2, entity, vec3d);
                            }
                        }
                    }
                }
            }

            if (vec3d.b() > 0.0D && entity.aC()) {
                vec3d = vec3d.a();
                double d1 = 0.014D;

                entity.motX += vec3d.c * d1;
                entity.motY += vec3d.d * d1;
                entity.motZ += vec3d.e * d1;
            }
            vec3d.b.release(vec3d); // CraftBukkit - pop it - we're done

            return flag;
        }
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    if (this.getType(k1, l1, i2).getMaterial() == material) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean b(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = this.getType(k1, l1, i2);

                    if (block.getMaterial() == material) {
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

    public Explosion explode(Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        return this.createExplosion(entity, d0, d1, d2, f, false, flag);
    }

    public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f);

        explosion.a = flag;
        explosion.b = flag1;
        explosion.a();
        explosion.a(true);
        return explosion;
    }

    public float a(Vec3D vec3d, AxisAlignedBB axisalignedbb) {
        double d0 = 1.0D / ((axisalignedbb.d - axisalignedbb.a) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.e - axisalignedbb.b) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.f - axisalignedbb.c) * 2.0D + 1.0D);
        int i = 0;
        int j = 0;

        Vec3D vec3d2 = vec3d.b.create(0, 0, 0); // CraftBukkit
        for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
            for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                    double d3 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) f;
                    double d4 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) f1;
                    double d5 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) f2;

                    if (this.a(vec3d2.b(d3, d4, d5), vec3d) == null) { // CraftBukkit
                        ++i;
                    }

                    ++j;
                }
            }
        }
        vec3d2.b.release(vec3d2); // CraftBukkit

        return (float) i / (float) j;
    }

    public boolean douseFire(EntityHuman entityhuman, int i, int j, int k, int l) {
        if (l == 0) {
            --j;
        }

        if (l == 1) {
            ++j;
        }

        if (l == 2) {
            --k;
        }

        if (l == 3) {
            ++k;
        }

        if (l == 4) {
            --i;
        }

        if (l == 5) {
            ++i;
        }

        if (this.getType(i, j, k) == Blocks.FIRE) {
            this.a(entityhuman, 1004, i, j, k, 0);
            this.setAir(i, j, k);
            return true;
        } else {
            return false;
        }
    }

    public TileEntity getTileEntity(int i, int j, int k) {
        if (j >= 0 && j < 256) {
            TileEntity tileentity = null;
            int l;
            TileEntity tileentity1;

            if (this.N) {
                for (l = 0; l < this.a.size(); ++l) {
                    tileentity1 = (TileEntity) this.a.get(l);
                    if (!tileentity1.r() && tileentity1.x == i && tileentity1.y == j && tileentity1.z == k) {
                        tileentity = tileentity1;
                        break;
                    }
                }
            }

            if (tileentity == null) {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                if (chunk != null) {
                    tileentity = chunk.e(i & 15, j, k & 15);
                }
            }

            if (tileentity == null) {
                for (l = 0; l < this.a.size(); ++l) {
                    tileentity1 = (TileEntity) this.a.get(l);
                    if (!tileentity1.r() && tileentity1.x == i && tileentity1.y == j && tileentity1.z == k) {
                        tileentity = tileentity1;
                        break;
                    }
                }
            }

            return tileentity;
        } else {
            return null;
        }
    }

    public void setTileEntity(int i, int j, int k, TileEntity tileentity) {
        if (tileentity != null && !tileentity.r()) {
            if (this.N) {
                tileentity.x = i;
                tileentity.y = j;
                tileentity.z = k;
                Iterator iterator = this.a.iterator();

                while (iterator.hasNext()) {
                    TileEntity tileentity1 = (TileEntity) iterator.next();

                    if (tileentity1.x == i && tileentity1.y == j && tileentity1.z == k) {
                        tileentity1.s();
                        iterator.remove();
                    }
                }

                this.a.add(tileentity);
            } else {
                this.tileEntityList.add(tileentity);
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                if (chunk != null) {
                    chunk.a(i & 15, j, k & 15, tileentity);
                }
            }
        }
    }

    public void p(int i, int j, int k) {
        TileEntity tileentity = this.getTileEntity(i, j, k);

        if (tileentity != null && this.N) {
            tileentity.s();
            this.a.remove(tileentity);
        } else {
            if (tileentity != null) {
                this.a.remove(tileentity);
                this.tileEntityList.remove(tileentity);
            }

            Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

            if (chunk != null) {
                chunk.f(i & 15, j, k & 15);
            }
        }
    }

    public void a(TileEntity tileentity) {
        this.b.add(tileentity);
    }

    public boolean q(int i, int j, int k) {
        AxisAlignedBB axisalignedbb = this.getType(i, j, k).a(this, i, j, k);

        return axisalignedbb != null && axisalignedbb.b() >= 1.0D;
    }

    public static boolean a(IBlockAccess iblockaccess, int i, int j, int k) {
        Block block = iblockaccess.getType(i, j, k);
        int l = iblockaccess.getData(i, j, k);

        return block.getMaterial().k() && block.d() ? true : (block instanceof BlockStairs ? (l & 4) == 4 : (block instanceof BlockStepAbstract ? (l & 8) == 8 : (block instanceof BlockHopper ? true : (block instanceof BlockSnow ? (l & 7) == 7 : false))));
    }

    public boolean c(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            Chunk chunk = this.chunkProvider.getOrCreateChunk(i >> 4, k >> 4);

            if (chunk != null && !chunk.isEmpty()) {
                Block block = this.getType(i, j, k);

                return block.getMaterial().k() && block.d();
            } else {
                return flag;
            }
        } else {
            return flag;
        }
    }

    public void A() {
        int i = this.a(1.0F);

        if (i != this.j) {
            this.j = i;
        }
    }

    public void setSpawnFlags(boolean flag, boolean flag1) {
        this.allowMonsters = flag;
        this.allowAnimals = flag1;
    }

    public void doTick() {
        this.o();
    }

    private void a() {
        if (this.worldData.hasStorm()) {
            this.n = 1.0F;
            if (this.worldData.isThundering()) {
                this.p = 1.0F;
            }
        }
    }

    protected void o() {
        if (!this.worldProvider.g) {
            if (!this.isStatic) {
                int i = this.worldData.getThunderDuration();

                if (i <= 0) {
                    if (this.worldData.isThundering()) {
                        this.worldData.setThunderDuration(this.random.nextInt(12000) + 3600);
                    } else {
                        this.worldData.setThunderDuration(this.random.nextInt(168000) + 12000);
                    }
                } else {
                    --i;
                    this.worldData.setThunderDuration(i);
                    if (i <= 0) {
                        // CraftBukkit start
                        ThunderChangeEvent thunder = new ThunderChangeEvent(this.getWorld(), !this.worldData.isThundering());
                        this.getServer().getPluginManager().callEvent(thunder);
                        if (!thunder.isCancelled()) {
                            this.worldData.setThundering(!this.worldData.isThundering());
                        }
                        // CraftBukkit end
                    }
                }

                this.o = this.p;
                if (this.worldData.isThundering()) {
                    this.p = (float) ((double) this.p + 0.01D);
                } else {
                    this.p = (float) ((double) this.p - 0.01D);
                }

                this.p = MathHelper.a(this.p, 0.0F, 1.0F);
                int j = this.worldData.getWeatherDuration();

                if (j <= 0) {
                    if (this.worldData.hasStorm()) {
                        this.worldData.setWeatherDuration(this.random.nextInt(12000) + 12000);
                    } else {
                        this.worldData.setWeatherDuration(this.random.nextInt(168000) + 12000);
                    }
                } else {
                    --j;
                    this.worldData.setWeatherDuration(j);
                    if (j <= 0) {
                        // CraftBukkit start
                        WeatherChangeEvent weather = new WeatherChangeEvent(this.getWorld(), !this.worldData.hasStorm());
                        this.getServer().getPluginManager().callEvent(weather);

                        if (!weather.isCancelled()) {
                            this.worldData.setStorm(!this.worldData.hasStorm());
                        }
                        // CraftBukkit end
                    }
                }

                this.m = this.n;
                if (this.worldData.hasStorm()) {
                    this.n = (float) ((double) this.n + 0.01D);
                } else {
                    this.n = (float) ((double) this.n - 0.01D);
                }

                this.n = MathHelper.a(this.n, 0.0F, 1.0F);
            }
        }
    }

    protected void B() {
        // this.chunkTickList.clear(); // CraftBukkit - removed
        this.methodProfiler.a("buildList");

        int i;
        EntityHuman entityhuman;
        int j;
        int k;

        for (i = 0; i < this.players.size(); ++i) {
            entityhuman = (EntityHuman) this.players.get(i);
            j = MathHelper.floor(entityhuman.locX / 16.0D);
            k = MathHelper.floor(entityhuman.locZ / 16.0D);
            byte b0 = 7;

            for (int l = -b0; l <= b0; ++l) {
                for (int i1 = -b0; i1 <= b0; ++i1) {
                    // CraftBukkit start - Don't tick chunks queued for unload
                    ChunkProviderServer chunkProviderServer = ((WorldServer) entityhuman.world).chunkProviderServer;
                    if (chunkProviderServer.unloadQueue.contains(l + j, i1 + k)) {
                        continue;
                    }
                    // CraftBukkit end

                    this.chunkTickList.add(org.bukkit.craftbukkit.util.LongHash.toLong(l + j, i1 + k)); // CraftBukkit
                }
            }
        }

        this.methodProfiler.b();
        if (this.L > 0) {
            --this.L;
        }

        this.methodProfiler.a("playerCheckLight");
        if (!this.players.isEmpty()) {
            i = this.random.nextInt(this.players.size());
            entityhuman = (EntityHuman) this.players.get(i);
            j = MathHelper.floor(entityhuman.locX) + this.random.nextInt(11) - 5;
            k = MathHelper.floor(entityhuman.locY) + this.random.nextInt(11) - 5;
            int j1 = MathHelper.floor(entityhuman.locZ) + this.random.nextInt(11) - 5;

            this.t(j, k, j1);
        }

        this.methodProfiler.b();
    }

    protected void a(int i, int j, Chunk chunk) {
        this.methodProfiler.c("moodSound");
        if (this.L == 0 && !this.isStatic) {
            this.k = this.k * 3 + 1013904223;
            int k = this.k >> 2;
            int l = k & 15;
            int i1 = k >> 8 & 15;
            int j1 = k >> 16 & 255;
            Block block = chunk.getType(l, j1, i1);

            l += i;
            i1 += j;
            if (block.getMaterial() == Material.AIR && this.j(l, j1, i1) <= this.random.nextInt(8) && this.b(EnumSkyBlock.SKY, l, j1, i1) <= 0) {
                EntityHuman entityhuman = this.findNearbyPlayer((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, 8.0D);

                if (entityhuman != null && entityhuman.e((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D) > 4.0D) {
                    this.makeSound((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.random.nextFloat() * 0.2F);
                    this.L = this.random.nextInt(12000) + 6000;
                }
            }
        }

        this.methodProfiler.c("checkLight");
        chunk.o();
    }

    protected void g() {
        this.B();
    }

    public boolean r(int i, int j, int k) {
        return this.d(i, j, k, false);
    }

    public boolean s(int i, int j, int k) {
        return this.d(i, j, k, true);
    }

    public boolean d(int i, int j, int k, boolean flag) {
        BiomeBase biomebase = this.getBiome(i, k);
        float f = biomebase.a(i, j, k);

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < 256 && this.b(EnumSkyBlock.BLOCK, i, j, k) < 10) {
                Block block = this.getType(i, j, k);

                if ((block == Blocks.STATIONARY_WATER || block == Blocks.WATER) && this.getData(i, j, k) == 0) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = true;

                    if (flag1 && this.getType(i - 1, j, k).getMaterial() != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getType(i + 1, j, k).getMaterial() != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getType(i, j, k - 1).getMaterial() != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getType(i, j, k + 1).getMaterial() != Material.WATER) {
                        flag1 = false;
                    }

                    if (!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean e(int i, int j, int k, boolean flag) {
        BiomeBase biomebase = this.getBiome(i, k);
        float f = biomebase.a(i, j, k);

        if (f > 0.15F) {
            return false;
        } else if (!flag) {
            return true;
        } else {
            if (j >= 0 && j < 256 && this.b(EnumSkyBlock.BLOCK, i, j, k) < 10) {
                Block block = this.getType(i, j, k);

                if (block.getMaterial() == Material.AIR && Blocks.SNOW.canPlace(this, i, j, k)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean t(int i, int j, int k) {
        boolean flag = false;

        if (!this.worldProvider.g) {
            flag |= this.c(EnumSkyBlock.SKY, i, j, k);
        }

        flag |= this.c(EnumSkyBlock.BLOCK, i, j, k);
        return flag;
    }

    private int a(int i, int j, int k, EnumSkyBlock enumskyblock) {
        if (enumskyblock == EnumSkyBlock.SKY && this.i(i, j, k)) {
            return 15;
        } else {
            Block block = this.getType(i, j, k);
            int l = enumskyblock == EnumSkyBlock.SKY ? 0 : block.m();
            int i1 = block.k();

            if (i1 >= 15 && block.m() > 0) {
                i1 = 1;
            }

            if (i1 < 1) {
                i1 = 1;
            }

            if (i1 >= 15) {
                return 0;
            } else if (l >= 14) {
                return l;
            } else {
                for (int j1 = 0; j1 < 6; ++j1) {
                    int k1 = i + Facing.b[j1];
                    int l1 = j + Facing.c[j1];
                    int i2 = k + Facing.d[j1];
                    int j2 = this.b(enumskyblock, k1, l1, i2) - i1;

                    if (j2 > l) {
                        l = j2;
                    }

                    if (l >= 14) {
                        return l;
                    }
                }

                return l;
            }
        }
    }

    public boolean c(EnumSkyBlock enumskyblock, int i, int j, int k) {
        if (!this.areChunksLoaded(i, j, k, 17)) {
            return false;
        } else {
            int l = 0;
            int i1 = 0;

            this.methodProfiler.a("getBrightness");
            int j1 = this.b(enumskyblock, i, j, k);
            int k1 = this.a(i, j, k, enumskyblock);
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;
            int l3;

            if (k1 > j1) {
                this.I[i1++] = 133152;
            } else if (k1 < j1) {
                this.I[i1++] = 133152 | j1 << 18;

                while (l < i1) {
                    l1 = this.I[l++];
                    i2 = (l1 & 63) - 32 + i;
                    j2 = (l1 >> 6 & 63) - 32 + j;
                    k2 = (l1 >> 12 & 63) - 32 + k;
                    l2 = l1 >> 18 & 15;
                    i3 = this.b(enumskyblock, i2, j2, k2);
                    if (i3 == l2) {
                        this.b(enumskyblock, i2, j2, k2, 0);
                        if (l2 > 0) {
                            j3 = MathHelper.a(i2 - i);
                            l3 = MathHelper.a(j2 - j);
                            k3 = MathHelper.a(k2 - k);
                            if (j3 + l3 + k3 < 17) {
                                for (int i4 = 0; i4 < 6; ++i4) {
                                    int j4 = i2 + Facing.b[i4];
                                    int k4 = j2 + Facing.c[i4];
                                    int l4 = k2 + Facing.d[i4];
                                    int i5 = Math.max(1, this.getType(j4, k4, l4).k());

                                    i3 = this.b(enumskyblock, j4, k4, l4);
                                    if (i3 == l2 - i5 && i1 < this.I.length) {
                                        this.I[i1++] = j4 - i + 32 | k4 - j + 32 << 6 | l4 - k + 32 << 12 | l2 - i5 << 18;
                                    }
                                }
                            }
                        }
                    }
                }

                l = 0;
            }

            this.methodProfiler.b();
            this.methodProfiler.a("checkedPosition < toCheckCount");

            while (l < i1) {
                l1 = this.I[l++];
                i2 = (l1 & 63) - 32 + i;
                j2 = (l1 >> 6 & 63) - 32 + j;
                k2 = (l1 >> 12 & 63) - 32 + k;
                l2 = this.b(enumskyblock, i2, j2, k2);
                i3 = this.a(i2, j2, k2, enumskyblock);
                if (i3 != l2) {
                    this.b(enumskyblock, i2, j2, k2, i3);
                    if (i3 > l2) {
                        j3 = Math.abs(i2 - i);
                        l3 = Math.abs(j2 - j);
                        k3 = Math.abs(k2 - k);
                        boolean flag = i1 < this.I.length - 6;

                        if (j3 + l3 + k3 < 17 && flag) {
                            if (this.b(enumskyblock, i2 - 1, j2, k2) < i3) {
                                this.I[i1++] = i2 - 1 - i + 32 + (j2 - j + 32 << 6) + (k2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, i2 + 1, j2, k2) < i3) {
                                this.I[i1++] = i2 + 1 - i + 32 + (j2 - j + 32 << 6) + (k2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, i2, j2 - 1, k2) < i3) {
                                this.I[i1++] = i2 - i + 32 + (j2 - 1 - j + 32 << 6) + (k2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, i2, j2 + 1, k2) < i3) {
                                this.I[i1++] = i2 - i + 32 + (j2 + 1 - j + 32 << 6) + (k2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, i2, j2, k2 - 1) < i3) {
                                this.I[i1++] = i2 - i + 32 + (j2 - j + 32 << 6) + (k2 - 1 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, i2, j2, k2 + 1) < i3) {
                                this.I[i1++] = i2 - i + 32 + (j2 - j + 32 << 6) + (k2 + 1 - k + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.methodProfiler.b();
            return true;
        }
    }

    public boolean a(boolean flag) {
        return false;
    }

    public List a(Chunk chunk, boolean flag) {
        return null;
    }

    public List getEntities(Entity entity, AxisAlignedBB axisalignedbb) {
        return this.getEntities(entity, axisalignedbb, (IEntitySelector) null);
    }

    public List getEntities(Entity entity, AxisAlignedBB axisalignedbb, IEntitySelector ientityselector) {
        ArrayList arraylist = new ArrayList();
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.isChunkLoaded(i1, j1)) {
                    this.getChunkAt(i1, j1).a(entity, axisalignedbb, arraylist, ientityselector);
                }
            }
        }

        return arraylist;
    }

    public List a(Class oclass, AxisAlignedBB axisalignedbb) {
        return this.a(oclass, axisalignedbb, (IEntitySelector) null);
    }

    public List a(Class oclass, AxisAlignedBB axisalignedbb, IEntitySelector ientityselector) {
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);
        ArrayList arraylist = new ArrayList();

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.isChunkLoaded(i1, j1)) {
                    this.getChunkAt(i1, j1).a(oclass, axisalignedbb, arraylist, ientityselector);
                }
            }
        }

        return arraylist;
    }

    public Entity a(Class oclass, AxisAlignedBB axisalignedbb, Entity entity) {
        List list = this.a(oclass, axisalignedbb);
        Entity entity1 = null;
        double d0 = Double.MAX_VALUE;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity2 = (Entity) list.get(i);

            if (entity2 != entity) {
                double d1 = entity.e(entity2);

                if (d1 <= d0) {
                    entity1 = entity2;
                    d0 = d1;
                }
            }
        }

        return entity1;
    }

    public abstract Entity getEntity(int i);

    public void b(int i, int j, int k, TileEntity tileentity) {
        if (this.isLoaded(i, j, k)) {
            this.getChunkAtWorldCoords(i, k).e();
        }
    }

    public int a(Class oclass) {
        int i = 0;

        for (int j = 0; j < this.entityList.size(); ++j) {
            Entity entity = (Entity) this.entityList.get(j);

            // CraftBukkit start - Split out persistent check, don't apply it to special persistent mobs
            if (entity instanceof EntityInsentient) {
                EntityInsentient entityinsentient = (EntityInsentient) entity;
                if (entityinsentient.isTypeNotPersistent() && entityinsentient.isPersistent()) {
                    continue;
                }
            }

            if (oclass.isAssignableFrom(entity.getClass())) {
            // if ((!(entity instanceof EntityInsentient) || !((EntityInsentient) entity).isPersistent()) && oclass.isAssignableFrom(entity.getClass())) {
                // CraftBukkit end
                ++i;
            }
        }

        return i;
    }

    public void a(List list) {
        // CraftBukkit start
        // this.entityList.addAll(list);
        Entity entity = null;

        for (int i = 0; i < list.size(); ++i) {
            entity = (Entity) list.get(i);
            if (entity == null) {
                continue;
            }
            this.entityList.add(entity);
            // CraftBukkit end
            this.a((Entity) list.get(i));
        }
    }

    public void b(List list) {
        this.f.addAll(list);
    }

    public boolean mayPlace(Block block, int i, int j, int k, boolean flag, int l, Entity entity, ItemStack itemstack) {
        Block block1 = this.getType(i, j, k);
        AxisAlignedBB axisalignedbb = flag ? null : block.a(this, i, j, k);

        // CraftBukkit start - store default return
        boolean defaultReturn = axisalignedbb != null && !this.a(axisalignedbb, entity) ? false : (block1.getMaterial() == Material.ORIENTABLE && block == Blocks.ANVIL ? true : block1.getMaterial().isReplaceable() && block.canPlace(this, i, j, k, l, itemstack));

        // CraftBukkit start
        BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(i, j, k), CraftMagicNumbers.getId(block), defaultReturn);
        this.getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }

    public PathEntity findPath(Entity entity, Entity entity1, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        this.methodProfiler.a("pathfind");
        int i = MathHelper.floor(entity.locX);
        int j = MathHelper.floor(entity.locY + 1.0D);
        int k = MathHelper.floor(entity.locZ);
        int l = (int) (f + 16.0F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache(this, i1, j1, k1, l1, i2, j2, 0);
        PathEntity pathentity = (new Pathfinder(chunkcache, flag, flag1, flag2, flag3)).a(entity, entity1, f);

        this.methodProfiler.b();
        return pathentity;
    }

    public PathEntity a(Entity entity, int i, int j, int k, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        this.methodProfiler.a("pathfind");
        int l = MathHelper.floor(entity.locX);
        int i1 = MathHelper.floor(entity.locY);
        int j1 = MathHelper.floor(entity.locZ);
        int k1 = (int) (f + 8.0F);
        int l1 = l - k1;
        int i2 = i1 - k1;
        int j2 = j1 - k1;
        int k2 = l + k1;
        int l2 = i1 + k1;
        int i3 = j1 + k1;
        ChunkCache chunkcache = new ChunkCache(this, l1, i2, j2, k2, l2, i3, 0);
        PathEntity pathentity = (new Pathfinder(chunkcache, flag, flag1, flag2, flag3)).a(entity, i, j, k, f);

        this.methodProfiler.b();
        return pathentity;
    }

    public int getBlockPower(int i, int j, int k, int l) {
        return this.getType(i, j, k).c(this, i, j, k, l);
    }

    public int getBlockPower(int i, int j, int k) {
        byte b0 = 0;
        int l = Math.max(b0, this.getBlockPower(i, j - 1, k, 0));

        if (l >= 15) {
            return l;
        } else {
            l = Math.max(l, this.getBlockPower(i, j + 1, k, 1));
            if (l >= 15) {
                return l;
            } else {
                l = Math.max(l, this.getBlockPower(i, j, k - 1, 2));
                if (l >= 15) {
                    return l;
                } else {
                    l = Math.max(l, this.getBlockPower(i, j, k + 1, 3));
                    if (l >= 15) {
                        return l;
                    } else {
                        l = Math.max(l, this.getBlockPower(i - 1, j, k, 4));
                        if (l >= 15) {
                            return l;
                        } else {
                            l = Math.max(l, this.getBlockPower(i + 1, j, k, 5));
                            return l >= 15 ? l : l;
                        }
                    }
                }
            }
        }
    }

    public boolean isBlockFacePowered(int i, int j, int k, int l) {
        return this.getBlockFacePower(i, j, k, l) > 0;
    }

    public int getBlockFacePower(int i, int j, int k, int l) {
        return this.getType(i, j, k).r() ? this.getBlockPower(i, j, k) : this.getType(i, j, k).b(this, i, j, k, l);
    }

    public boolean isBlockIndirectlyPowered(int i, int j, int k) {
        return this.getBlockFacePower(i, j - 1, k, 0) > 0 ? true : (this.getBlockFacePower(i, j + 1, k, 1) > 0 ? true : (this.getBlockFacePower(i, j, k - 1, 2) > 0 ? true : (this.getBlockFacePower(i, j, k + 1, 3) > 0 ? true : (this.getBlockFacePower(i - 1, j, k, 4) > 0 ? true : this.getBlockFacePower(i + 1, j, k, 5) > 0))));
    }

    public int getHighestNeighborSignal(int i, int j, int k) {
        int l = 0;

        for (int i1 = 0; i1 < 6; ++i1) {
            int j1 = this.getBlockFacePower(i + Facing.b[i1], j + Facing.c[i1], k + Facing.d[i1], i1);

            if (j1 >= 15) {
                return 15;
            }

            if (j1 > l) {
                l = j1;
            }
        }

        return l;
    }

    public EntityHuman findNearbyPlayer(Entity entity, double d0) {
        return this.findNearbyPlayer(entity.locX, entity.locY, entity.locZ, d0);
    }

    public EntityHuman findNearbyPlayer(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;

        for (int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman1 = (EntityHuman) this.players.get(i);
            // CraftBukkit start - Fixed an NPE
            if (entityhuman1 == null || entityhuman1.dead) {
                continue;
            }
            // CraftBukkit end
            double d5 = entityhuman1.e(d0, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                d4 = d5;
                entityhuman = entityhuman1;
            }
        }

        return entityhuman;
    }

    public EntityHuman findNearbyVulnerablePlayer(Entity entity, double d0) {
        return this.findNearbyVulnerablePlayer(entity.locX, entity.locY, entity.locZ, d0);
    }

    public EntityHuman findNearbyVulnerablePlayer(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;

        for (int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman1 = (EntityHuman) this.players.get(i);
            // CraftBukkit start - Fixed an NPE
            if (entityhuman1 == null || entityhuman1.dead) {
                continue;
            }
            // CraftBukkit end

            if (!entityhuman1.abilities.isInvulnerable && entityhuman1.isAlive()) {
                double d5 = entityhuman1.e(d0, d1, d2);
                double d6 = d3;

                if (entityhuman1.isSneaking()) {
                    d6 = d3 * 0.800000011920929D;
                }

                if (entityhuman1.isInvisible()) {
                    float f = entityhuman1.bC();

                    if (f < 0.1F) {
                        f = 0.1F;
                    }

                    d6 *= (double) (0.7F * f);
                }

                if ((d3 < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    public EntityHuman a(String s) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (s.equals(((EntityHuman) this.players.get(i)).getName())) {
                return (EntityHuman) this.players.get(i);
            }
        }

        return null;
    }

    public void F() throws ExceptionWorldConflict { // CraftBukkit - added throws
        this.dataManager.checkSession();
    }

    public long getSeed() {
        return this.worldData.getSeed();
    }

    public long getTime() {
        return this.worldData.getTime();
    }

    public long getDayTime() {
        return this.worldData.getDayTime();
    }

    public void setDayTime(long i) {
        this.worldData.setDayTime(i);
    }

    public ChunkCoordinates getSpawn() {
        return new ChunkCoordinates(this.worldData.c(), this.worldData.d(), this.worldData.e());
    }

    public void x(int i, int j, int k) {
        this.worldData.setSpawn(i, j, k);
    }

    public boolean a(EntityHuman entityhuman, int i, int j, int k) {
        return true;
    }

    public void broadcastEntityEffect(Entity entity, byte b0) {}

    public IChunkProvider K() {
        return this.chunkProvider;
    }

    public void playNote(int i, int j, int k, Block block, int l, int i1) {
        block.a(this, i, j, k, l, i1);
    }

    public IDataManager getDataManager() {
        return this.dataManager;
    }

    public WorldData getWorldData() {
        return this.worldData;
    }

    public GameRules getGameRules() {
        return this.worldData.getGameRules();
    }

    public void everyoneSleeping() {}

    // CraftBukkit start
    // Calls the method that checks to see if players are sleeping
    // Called by CraftPlayer.setPermanentSleeping()
    public void checkSleepStatus() {
        if (!this.isStatic) {
            this.everyoneSleeping();
        }
    }
    // CraftBukkit end

    public float h(float f) {
        return (this.o + (this.p - this.o) * f) * this.j(f);
    }

    public float j(float f) {
        return this.m + (this.n - this.m) * f;
    }

    public boolean O() {
        return (double) this.h(1.0F) > 0.9D;
    }

    public boolean P() {
        return (double) this.j(1.0F) > 0.2D;
    }

    public boolean isRainingAt(int i, int j, int k) {
        if (!this.P()) {
            return false;
        } else if (!this.i(i, j, k)) {
            return false;
        } else if (this.h(i, k) > j) {
            return false;
        } else {
            BiomeBase biomebase = this.getBiome(i, k);

            return biomebase.d() ? false : (this.e(i, j, k, false) ? false : biomebase.e());
        }
    }

    public boolean z(int i, int j, int k) {
        BiomeBase biomebase = this.getBiome(i, k);

        return biomebase.f();
    }

    public void a(String s, PersistentBase persistentbase) {
        this.worldMaps.a(s, persistentbase);
    }

    public PersistentBase a(Class oclass, String s) {
        return this.worldMaps.get(oclass, s);
    }

    public int b(String s) {
        return this.worldMaps.a(s);
    }

    public void b(int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < this.u.size(); ++j1) {
            ((IWorldAccess) this.u.get(j1)).a(i, j, k, l, i1);
        }
    }

    public void triggerEffect(int i, int j, int k, int l, int i1) {
        this.a((EntityHuman) null, i, j, k, l, i1);
    }

    public void a(EntityHuman entityhuman, int i, int j, int k, int l, int i1) {
        try {
            for (int j1 = 0; j1 < this.u.size(); ++j1) {
                ((IWorldAccess) this.u.get(j1)).a(entityhuman, i, j, k, l, i1);
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Playing level event");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Level event being played");

            crashreportsystemdetails.a("Block coordinates", CrashReportSystemDetails.a(j, k, l));
            crashreportsystemdetails.a("Event source", entityhuman);
            crashreportsystemdetails.a("Event type", Integer.valueOf(i));
            crashreportsystemdetails.a("Event data", Integer.valueOf(i1));
            throw new ReportedException(crashreport);
        }
    }

    public int getHeight() {
        return 256;
    }

    public int R() {
        return this.worldProvider.g ? 128 : 256;
    }

    public Random A(int i, int j, int k) {
        long l = (long) i * 341873128712L + (long) j * 132897987541L + this.getWorldData().getSeed() + (long) k;

        this.random.setSeed(l);
        return this.random;
    }

    public ChunkPosition b(String s, int i, int j, int k) {
        return this.K().findNearestMapFeature(this, s, i, j, k);
    }

    public CrashReportSystemDetails a(CrashReport crashreport) {
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Affected level", 1);

        crashreportsystemdetails.a("Level name", (this.worldData == null ? "????" : this.worldData.getName()));
        crashreportsystemdetails.a("All players", (Callable) (new CrashReportPlayers(this)));
        crashreportsystemdetails.a("Chunk stats", (Callable) (new CrashReportChunkStats(this)));

        try {
            this.worldData.a(crashreportsystemdetails);
        } catch (Throwable throwable) {
            crashreportsystemdetails.a("Level Data Unobtainable", throwable);
        }

        return crashreportsystemdetails;
    }

    public void d(int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < this.u.size(); ++j1) {
            IWorldAccess iworldaccess = (IWorldAccess) this.u.get(j1);

            iworldaccess.b(i, j, k, l, i1);
        }
    }

    public Vec3DPool getVec3DPool() {
        return this.J;
    }

    public Calendar V() {
        if (this.getTime() % 600L == 0L) {
            this.K.setTimeInMillis(MinecraftServer.ap());
        }

        return this.K;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public void f(int i, int j, int k, Block block) {
        for (int l = 0; l < 4; ++l) {
            int i1 = i + Direction.a[l];
            int j1 = k + Direction.b[l];
            Block block1 = this.getType(i1, j, j1);

            if (Blocks.REDSTONE_COMPARATOR_OFF.e(block1)) {
                block1.doPhysics(this, i1, j, j1, block);
            } else if (block1.r()) {
                i1 += Direction.a[l];
                j1 += Direction.b[l];
                Block block2 = this.getType(i1, j, j1);

                if (Blocks.REDSTONE_COMPARATOR_OFF.e(block2)) {
                    block2.doPhysics(this, i1, j, j1, block);
                }
            }
        }
    }

    public float b(double d0, double d1, double d2) {
        return this.B(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
    }

    public float B(int i, int j, int k) {
        float f = 0.0F;
        boolean flag = this.difficulty == EnumDifficulty.HARD;

        if (this.isLoaded(i, j, k)) {
            float f1 = this.x();

            f += MathHelper.a((float) this.getChunkAtWorldCoords(i, k).s / 3600000.0F, 0.0F, 1.0F) * (flag ? 1.0F : 0.75F);
            f += f1 * 0.25F;
        }

        if (this.difficulty == EnumDifficulty.EASY || this.difficulty == EnumDifficulty.PEACEFUL) {
            f *= (float) this.difficulty.a() / 2.0F;
        }

        return MathHelper.a(f, 0.0F, flag ? 1.5F : 1.0F);
    }

    public void X() {
        Iterator iterator = this.u.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.b();
        }
    }
}
