package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.block.BlockState;
// CraftBukkit end

public class World implements IBlockAccess {

    public int heightBits = 7;
    public int heightBitsPlusFour;
    public int height;
    public int heightMinusOne;
    public int seaLevel;
    public boolean f;
    public List entityList;
    private List J;
    private TreeSet K;
    private Set L;
    public List h;
    private List M;
    private List N;
    public List players;
    public List j;
    private long O;
    public int k;
    protected int l;
    protected final int m;
    protected float n;
    protected float o;
    protected float p;
    protected float q;
    protected int r;
    public int s;
    public boolean suppressPhysics;
    private long P;
    protected int u;
    public int difficulty;
    public Random random;
    public boolean x;
    public WorldProvider worldProvider; // CraftBukkit - remove final
    protected List z;
    public IChunkProvider chunkProvider; // CraftBukkit - protected -> public
    protected final IDataManager B;
    public WorldData worldData; // CraftBukkit - protected -> public
    public boolean isLoading;
    private boolean Q;
    public WorldMapCollection worldMaps;
    private ArrayList R;
    private boolean S;
    public boolean allowMonsters; // CraftBukkit - private -> public
    public boolean allowAnimals; // CraftBukkit - private -> public
    private Set T;
    private int U;
    int[] H;
    private List V;
    public boolean isStatic;

    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.b;
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

    private boolean canSpawn(int x, int z) {
        if (this.generator != null) {
            return this.generator.canSpawn(this.getWorld(), x, z);
        } else {
            return this.worldProvider.canSpawn(x, z);
        }
    }

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    // CraftBukkit - changed signature
    public World(IDataManager idatamanager, String s, WorldSettings worldsettings, WorldProvider worldprovider, ChunkGenerator gen, org.bukkit.World.Environment env) {
        this.generator = gen;
        this.world = new CraftWorld((WorldServer) this, gen, env);
        // CraftBukkit end

        this.heightBitsPlusFour = this.heightBits + 4;
        this.height = 1 << this.heightBits;
        this.heightMinusOne = this.height - 1;
        this.seaLevel = this.height / 2 - 1;
        this.f = false;
        this.entityList = new ArrayList();
        this.J = new ArrayList();
        this.K = new TreeSet();
        this.L = new HashSet();
        this.h = new ArrayList();
        this.M = new ArrayList();
        this.N = new ArrayList();
        this.players = new ArrayList();
        this.j = new ArrayList();
        this.O = 16777215L;
        this.k = 0;
        this.l = (new Random()).nextInt();
        this.m = 1013904223;
        this.r = 0;
        this.s = 0;
        this.suppressPhysics = false;
        this.P = System.currentTimeMillis();
        this.u = 40;
        this.random = new Random();
        this.x = false;
        this.z = new ArrayList();
        this.R = new ArrayList();
        this.allowMonsters = true;
        this.allowAnimals = true;
        this.T = new HashSet();
        this.U = this.random.nextInt(12000);
        this.H = new int['\u8000'];
        this.V = new ArrayList();
        this.isStatic = false;
        this.B = idatamanager;
        this.worldMaps = new WorldMapCollection(idatamanager);
        this.worldData = idatamanager.c();
        this.x = this.worldData == null;
        if (worldprovider != null) {
            this.worldProvider = worldprovider;
        } else if (this.worldData != null && this.worldData.h() != 0) {
            this.worldProvider = WorldProvider.byDimension(this.worldData.h());
        } else {
            this.worldProvider = WorldProvider.byDimension(0);
        }

        boolean flag = false;

        if (this.worldData == null) {
            this.worldData = new WorldData(worldsettings, s);
            flag = true;
        } else {
            this.worldData.a(s);
        }

        this.worldProvider.a(this);
        this.chunkProvider = this.b();
        if (flag) {
            this.c();
        }

        this.g();
        this.z();

        this.getServer().addWorld(this.world); // CraftBukkit
    }

    protected IChunkProvider b() {
        IChunkLoader ichunkloader = this.B.a(this.worldProvider);

        return new ChunkProviderLoadOrGenerate(this, ichunkloader, this.worldProvider.getChunkProvider());
    }

    protected void c() {
        this.isLoading = true;
        WorldChunkManager worldchunkmanager = this.getWorldChunkManager();
        List list = worldchunkmanager.a();
        Random random = new Random(this.getSeed());
        ChunkPosition chunkposition = worldchunkmanager.a(0, 0, 256, list, random);
        int i = 0;
        int j = this.height / 2;
        int k = 0;

        // CraftBukkit start
        if (this.generator != null) {
            Random rand = new Random(this.getSeed());
            Location spawn = this.generator.getFixedSpawnLocation(((WorldServer) this).getWorld(), rand);

            if (spawn != null) {
                if (spawn.getWorld() != ((WorldServer) this).getWorld()) {
                    throw new IllegalStateException("Cannot set spawn point for " + this.worldData.name + " to be in another world (" + spawn.getWorld().getName() + ")");
                } else {
                    this.worldData.setSpawn(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
                    this.isLoading = false;
                    return;
                }
            }
        }
        // CraftBukkit end

        if (chunkposition != null) {
            i = chunkposition.x;
            k = chunkposition.z;
        } else {
            System.out.println("Unable to find spawn biome");
        }

        int l = 0;

        // CraftBukkit - use out own canSpawn
        while (!this.canSpawn(i, k)) {
            i += random.nextInt(64) - random.nextInt(64);
            k += random.nextInt(64) - random.nextInt(64);
            ++l;
            if (l == 1000) {
                break;
            }
        }

        this.worldData.setSpawn(i, j, k);
        this.isLoading = false;
    }

    public ChunkCoordinates d() {
        return this.worldProvider.d();
    }

    public int a(int i, int j) {
        int k;

        for (k = this.seaLevel; !this.isEmpty(i, k + 1, j); ++k) {
            ;
        }

        return this.getTypeId(i, k, j);
    }

    public void save(boolean flag, IProgressUpdate iprogressupdate) {
        if (this.chunkProvider.canSave()) {
            if (iprogressupdate != null) {
                iprogressupdate.a("Saving level");
            }

            this.y();
            if (iprogressupdate != null) {
                iprogressupdate.b("Saving chunks");
            }

            this.chunkProvider.saveChunks(flag, iprogressupdate);
        }
    }

    private void y() {
        this.l();
        this.B.a(this.worldData, this.players);
        this.worldMaps.a();
    }

    public int getTypeId(int i, int j, int k) {
        return i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 ? (j < 0 ? 0 : (j >= this.height ? 0 : this.getChunkAt(i >> 4, k >> 4).getTypeId(i & 15, j, k & 15))) : 0;
    }

    public boolean isEmpty(int i, int j, int k) {
        return this.getTypeId(i, j, k) == 0;
    }

    public boolean isLoaded(int i, int j, int k) {
        return j >= 0 && j < this.height ? this.isChunkLoaded(i >> 4, k >> 4) : false;
    }

    public boolean areChunksLoaded(int i, int j, int k, int l) {
        return this.a(i - l, j - l, k - l, i + l, j + l, k + l);
    }

    public boolean a(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < this.height) {
            i >>= 4;
            j >>= 4;
            k >>= 4;
            l >>= 4;
            i1 >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    if (!this.isChunkLoaded(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean isChunkLoaded(int i, int j) {
        return this.chunkProvider.isChunkLoaded(i, j);
    }

    public Chunk getChunkAtWorldCoords(int i, int j) {
        return this.getChunkAt(i >> 4, j >> 4);
    }

    // CraftBukkit start
    public Chunk getChunkAt(int i, int j) {
        Chunk result = null;
        synchronized (this.chunkLock) {
            if (this.lastChunkAccessed == null || this.lastXAccessed != i || this.lastZAccessed != j) {
                this.lastXAccessed = i;
                this.lastZAccessed = j;
                this.lastChunkAccessed = this.chunkProvider.getOrCreateChunk(i, j);
            }
            result = this.lastChunkAccessed;
        }
        return result;
    }
    // CraftBukkit end

    public boolean setRawTypeIdAndData(int i, int j, int k, int l, int i1) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= this.height) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                boolean flag = chunk.a(i & 15, j, k & 15, l, i1);

                this.s(i, j, k);
                return flag;
            }
        } else {
            return false;
        }
    }

    public boolean setRawTypeId(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= this.height) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                boolean flag = chunk.a(i & 15, j, k & 15, l);

                this.s(i, j, k);
                return flag;
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
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return 0;
            } else if (j >= this.height) {
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

    public void setData(int i, int j, int k, int l) {
        if (this.setRawData(i, j, k, l)) {
            int i1 = this.getTypeId(i, j, k);

            if (Block.t[i1 & 255]) {
                this.update(i, j, k, i1);
            } else {
                this.applyPhysics(i, j, k, i1);
            }
        }
    }

    public boolean setRawData(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= this.height) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.b(i, j, k, l);
            }
        } else {
            return false;
        }
    }

    public boolean setTypeId(int i, int j, int k, int l) {
        // CraftBukkit start
        int old = this.getTypeId(i, j, k);
        if (this.setRawTypeId(i, j, k, l)) {
            this.update(i, j, k, l == 0 ? old : l);
            return true;
        } else {
            return false;
        }
        // CraftBukkit end
    }

    public boolean setTypeIdAndData(int i, int j, int k, int l, int i1) {
        // CraftBukkit start
        int old = this.getTypeId(i, j, k);
        if (this.setRawTypeIdAndData(i, j, k, l, i1)) {
            this.update(i, j, k, l == 0 ? old : l);
            return true;
        } else {
            return false;
        }
        // CraftBukkit end
    }

    public void notify(int i, int j, int k) {
        for (int l = 0; l < this.z.size(); ++l) {
            ((IWorldAccess) this.z.get(l)).a(i, j, k);
        }
    }

    protected void update(int i, int j, int k, int l) {
        this.notify(i, j, k);
        this.applyPhysics(i, j, k, l);
    }

    public void g(int i, int j, int k, int l) {
        int i1;

        if (k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if (!this.worldProvider.e) {
            for (i1 = k; i1 <= l; ++i1) {
                this.b(EnumSkyBlock.SKY, i, i1, j);
            }
        }

        this.b(i, k, j, i, l, j);
    }

    public void i(int i, int j, int k) {
        for (int l = 0; l < this.z.size(); ++l) {
            ((IWorldAccess) this.z.get(l)).a(i, j, k, i, j, k);
        }
    }

    public void b(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < this.z.size(); ++k1) {
            ((IWorldAccess) this.z.get(k1)).a(i, j, k, l, i1, j1);
        }
    }

    public void applyPhysics(int i, int j, int k, int l) {
        this.k(i - 1, j, k, l);
        this.k(i + 1, j, k, l);
        this.k(i, j - 1, k, l);
        this.k(i, j + 1, k, l);
        this.k(i, j, k - 1, l);
        this.k(i, j, k + 1, l);
    }

    private void k(int i, int j, int k, int l) {
        if (!this.suppressPhysics && !this.isStatic) {
            Block block = Block.byId[this.getTypeId(i, j, k)];

            if (block != null) {
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

                block.doPhysics(this, i, j, k, l);
            }
        }
    }

    public boolean isChunkLoaded(int i, int j, int k) {
        return this.getChunkAt(i >> 4, k >> 4).c(i & 15, j, k & 15);
    }

    public int k(int i, int j, int k) {
        if (j < 0) {
            return 0;
        } else {
            if (j >= this.height) {
                j = this.height - 1;
            }

            return this.getChunkAt(i >> 4, k >> 4).c(i & 15, j, k & 15, 0);
        }
    }

    public int getLightLevel(int i, int j, int k) {
        return this.a(i, j, k, true);
    }

    public int a(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (flag) {
                int l = this.getTypeId(i, j, k);

                if (l == Block.STEP.id || l == Block.SOIL.id || l == Block.COBBLESTONE_STAIRS.id || l == Block.WOOD_STAIRS.id) {
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
            } else {
                if (j >= this.height) {
                    j = this.height - 1;
                }

                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.c(i, j, k, this.k);
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
            return 0;
        }
    }

    public int a(EnumSkyBlock enumskyblock, int i, int j, int k) {
        if (j < 0) {
            j = 0;
        }

        if (j >= this.height) {
            j = this.height - 1;
        }

        if (j >= 0 && j < this.height && i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            int l = i >> 4;
            int i1 = k >> 4;

            if (!this.isChunkLoaded(l, i1)) {
                return 0;
            } else {
                Chunk chunk = this.getChunkAt(l, i1);

                return chunk.a(enumskyblock, i & 15, j, k & 15);
            }
        } else {
            return enumskyblock.c;
        }
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j >= 0) {
                if (j < this.height) {
                    if (this.isChunkLoaded(i >> 4, k >> 4)) {
                        Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                        chunk.a(enumskyblock, i & 15, j, k & 15, l);

                        for (int i1 = 0; i1 < this.z.size(); ++i1) {
                            ((IWorldAccess) this.z.get(i1)).a(i, j, k);
                        }
                    }
                }
            }
        }
    }

    public float m(int i, int j, int k) {
        return this.worldProvider.f[this.getLightLevel(i, j, k)];
    }

    public boolean e() {
        return this.k < 4;
    }

    public MovingObjectPosition a(Vec3D vec3d, Vec3D vec3d1) {
        return this.rayTrace(vec3d, vec3d1, false, false);
    }

    public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag) {
        return this.rayTrace(vec3d, vec3d1, flag, false);
    }

    public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag, boolean flag1) {
        if (!Double.isNaN(vec3d.a) && !Double.isNaN(vec3d.b) && !Double.isNaN(vec3d.c)) {
            if (!Double.isNaN(vec3d1.a) && !Double.isNaN(vec3d1.b) && !Double.isNaN(vec3d1.c)) {
                int i = MathHelper.floor(vec3d1.a);
                int j = MathHelper.floor(vec3d1.b);
                int k = MathHelper.floor(vec3d1.c);
                int l = MathHelper.floor(vec3d.a);
                int i1 = MathHelper.floor(vec3d.b);
                int j1 = MathHelper.floor(vec3d.c);
                int k1 = this.getTypeId(l, i1, j1);
                int l1 = this.getData(l, i1, j1);
                Block block = Block.byId[k1];

                if ((!flag1 || block == null || block.e(this, l, i1, j1) != null) && k1 > 0 && block.a(l1, flag)) {
                    MovingObjectPosition movingobjectposition = block.a(this, l, i1, j1, vec3d, vec3d1);

                    if (movingobjectposition != null) {
                        return movingobjectposition;
                    }
                }

                k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec3d.a) || Double.isNaN(vec3d.b) || Double.isNaN(vec3d.c)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return null;
                    }

                    boolean flag2 = true;
                    boolean flag3 = true;
                    boolean flag4 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec3d1.a - vec3d.a;
                    double d7 = vec3d1.b - vec3d.b;
                    double d8 = vec3d1.c - vec3d.c;

                    if (flag2) {
                        d3 = (d0 - vec3d.a) / d6;
                    }

                    if (flag3) {
                        d4 = (d1 - vec3d.b) / d7;
                    }

                    if (flag4) {
                        d5 = (d2 - vec3d.c) / d8;
                    }

                    boolean flag5 = false;
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

                    Vec3D vec3d2 = Vec3D.create(vec3d.a, vec3d.b, vec3d.c);

                    l = (int) (vec3d2.a = (double) MathHelper.floor(vec3d.a));
                    if (b0 == 5) {
                        --l;
                        ++vec3d2.a;
                    }

                    i1 = (int) (vec3d2.b = (double) MathHelper.floor(vec3d.b));
                    if (b0 == 1) {
                        --i1;
                        ++vec3d2.b;
                    }

                    j1 = (int) (vec3d2.c = (double) MathHelper.floor(vec3d.c));
                    if (b0 == 3) {
                        --j1;
                        ++vec3d2.c;
                    }

                    int i2 = this.getTypeId(l, i1, j1);
                    int j2 = this.getData(l, i1, j1);
                    Block block1 = Block.byId[i2];

                    if ((!flag1 || block1 == null || block1.e(this, l, i1, j1) != null) && i2 > 0 && block1.a(j2, flag)) {
                        MovingObjectPosition movingobjectposition1 = block1.a(this, l, i1, j1, vec3d, vec3d1);

                        if (movingobjectposition1 != null) {
                            return movingobjectposition1;
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

    public void makeSound(Entity entity, String s, float f, float f1) {
        for (int i = 0; i < this.z.size(); ++i) {
            ((IWorldAccess) this.z.get(i)).a(s, entity.locX, entity.locY - (double) entity.height, entity.locZ, f, f1);
        }
    }

    public void makeSound(double d0, double d1, double d2, String s, float f, float f1) {
        for (int i = 0; i < this.z.size(); ++i) {
            ((IWorldAccess) this.z.get(i)).a(s, d0, d1, d2, f, f1);
        }
    }

    public void a(String s, int i, int j, int k) {
        for (int l = 0; l < this.z.size(); ++l) {
            ((IWorldAccess) this.z.get(l)).a(s, i, j, k);
        }
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {
        for (int i = 0; i < this.z.size(); ++i) {
            ((IWorldAccess) this.z.get(i)).a(s, d0, d1, d2, d3, d4, d5);
        }
    }

    public boolean strikeLightning(Entity entity) {
        this.j.add(entity);
        return true;
    }

    // CraftBukkit start - used for entities other than creatures
    public boolean addEntity(Entity entity) {
        return this.addEntity(entity, SpawnReason.CUSTOM); // Set reason as Custom by default
    }


    public boolean addEntity(Entity entity, SpawnReason spawnReason) { // Changed signature, added SpawnReason
    // CraftBukkit end
        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);
        boolean flag = false;

        if (entity instanceof EntityHuman) {
            flag = true;
        }

        // CraftBukkit start
        if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
            boolean isAnimal = entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal;
            boolean isMonster = entity instanceof EntityMonster || entity instanceof EntityGhast || entity instanceof EntitySlime;

            if (spawnReason == SpawnReason.NATURAL || spawnReason == SpawnReason.SPAWNER || spawnReason == SpawnReason.BED || spawnReason == SpawnReason.EGG) {
                if (isAnimal && !allowAnimals || isMonster && !allowMonsters) return false;
            }

            CreatureSpawnEvent event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving) entity, spawnReason);

            if (event.isCancelled()) {
                return false;
            }
        } else if (entity instanceof EntityItem) {
            ItemSpawnEvent event = CraftEventFactory.callItemSpawnEvent((EntityItem) entity);
            if (event.isCancelled()) {
                return false;
            }
        }
        // CraftBukkit end

        if (!flag && !this.isChunkLoaded(i, j)) {
            return false;
        } else {
            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;

                this.players.add(entityhuman);
                this.everyoneSleeping();
            }

            this.getChunkAt(i, j).a(entity);
            this.entityList.add(entity);
            this.c(entity);
            return true;
        }
    }

    protected void c(Entity entity) {
        for (int i = 0; i < this.z.size(); ++i) {
            ((IWorldAccess) this.z.get(i)).a(entity);
        }
    }

    protected void d(Entity entity) {
        for (int i = 0; i < this.z.size(); ++i) {
            ((IWorldAccess) this.z.get(i)).b(entity);
        }
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
            this.players.remove((EntityHuman) entity);
            this.everyoneSleeping();
        }
    }

    public void removeEntity(Entity entity) {
        entity.die();
        if (entity instanceof EntityHuman) {
            this.players.remove((EntityHuman) entity);
            this.everyoneSleeping();
        }

        int i = entity.bX;
        int j = entity.bZ;

        if (entity.bW && this.isChunkLoaded(i, j)) {
            this.getChunkAt(i, j).b(entity);
        }

        this.entityList.remove(entity);
        this.d(entity);
    }

    public void addIWorldAccess(IWorldAccess iworldaccess) {
        this.z.add(iworldaccess);
    }

    public List getEntities(Entity entity, AxisAlignedBB axisalignedbb) {
        this.R.clear();
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.isLoaded(k1, this.height / 2, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        Block block = Block.byId[this.getTypeId(k1, i2, l1)];

                        if (block != null) {
                            block.a(this, k1, i2, l1, axisalignedbb, this.R);
                        }
                    }
                }
            }
        }

        double d0 = 0.25D;
        List list = this.b(entity, axisalignedbb.b(d0, d0, d0));

        for (int j2 = 0; j2 < list.size(); ++j2) {
            AxisAlignedBB axisalignedbb1 = ((Entity) list.get(j2)).h_();

            if (axisalignedbb1 != null && axisalignedbb1.a(axisalignedbb)) {
                this.R.add(axisalignedbb1);
            }

            axisalignedbb1 = entity.a_((Entity) list.get(j2));
            if (axisalignedbb1 != null && axisalignedbb1.a(axisalignedbb)) {
                this.R.add(axisalignedbb1);
            }
        }

        return this.R;
    }

    public int a(float f) {
        float f1 = this.b(f);
        float f2 = 1.0F - (MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F);

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        f2 = 1.0F - f2;
        f2 = (float) ((double) f2 * (1.0D - (double) (this.d(f) * 5.0F) / 16.0D));
        f2 = (float) ((double) f2 * (1.0D - (double) (this.c(f) * 5.0F) / 16.0D));
        f2 = 1.0F - f2;
        return (int) (f2 * 11.0F);
    }

    public float b(float f) {
        return this.worldProvider.a(this.worldData.f(), f);
    }

    public int e(int i, int j) {
        return this.getChunkAtWorldCoords(i, j).c(i & 15, j & 15);
    }

    public int f(int i, int j) {
        Chunk chunk = this.getChunkAtWorldCoords(i, j);
        int k = this.height - 1;

        i &= 15;

        for (j &= 15; k > 0; --k) {
            int l = chunk.getTypeId(i, k, j);

            if (l != 0 && Block.byId[l].material.isSolid() && Block.byId[l].material != Material.LEAVES) {
                return k + 1;
            }
        }

        return -1;
    }

    public void c(int i, int j, int k, int l, int i1) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, l);
        byte b0 = 8;

        if (this.f) {
            if (this.a(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                int j1 = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                if (j1 == nextticklistentry.d && j1 > 0) {
                    Block.byId[j1].a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                }
            }
        } else {
            if (this.a(i - b0, j - b0, k - b0, i + b0, j + b0, k + b0)) {
                if (l > 0) {
                    nextticklistentry.a((long) i1 + this.worldData.f());
                }

                if (!this.L.contains(nextticklistentry)) {
                    this.L.add(nextticklistentry);
                    this.K.add(nextticklistentry);
                }
            }
        }
    }

    public void d(int i, int j, int k, int l, int i1) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, l);

        if (l > 0) {
            nextticklistentry.a((long) i1 + this.worldData.f());
        }

        if (!this.L.contains(nextticklistentry)) {
            this.L.add(nextticklistentry);
            this.K.add(nextticklistentry);
        }
    }

    public void tickEntities() {
        MethodProfiler.a("entities");
        MethodProfiler.a("global");

        int i;
        Entity entity;

        for (i = 0; i < this.j.size(); ++i) {
            entity = (Entity) this.j.get(i);
            // CraftBukkit start - fixed an NPE
            if (entity == null) {
                continue;
            }
            // CraftBukkit end
            entity.w_();
            if (entity.dead) {
                this.j.remove(i--);
            }
        }

        MethodProfiler.b("remove");
        this.entityList.removeAll(this.J);

        int j;
        int k;

        for (i = 0; i < this.J.size(); ++i) {
            entity = (Entity) this.J.get(i);
            j = entity.bX;
            k = entity.bZ;
            if (entity.bW && this.isChunkLoaded(j, k)) {
                this.getChunkAt(j, k).b(entity);
            }
        }

        for (i = 0; i < this.J.size(); ++i) {
            this.d((Entity) this.J.get(i));
        }

        this.J.clear();
        MethodProfiler.b("regular");

        for (i = 0; i < this.entityList.size(); ++i) {
            entity = (Entity) this.entityList.get(i);
            if (entity.vehicle != null) {
                if (!entity.vehicle.dead && entity.vehicle.passenger == entity) {
                    continue;
                }

                entity.vehicle.passenger = null;
                entity.vehicle = null;
            }

            if (!entity.dead) {
                this.playerJoinedWorld(entity);
            }

            MethodProfiler.a("remove");
            if (entity.dead) {
                j = entity.bX;
                k = entity.bZ;
                if (entity.bW && this.isChunkLoaded(j, k)) {
                    this.getChunkAt(j, k).b(entity);
                }

                this.entityList.remove(i--);
                this.d(entity);
            }

            MethodProfiler.a();
        }

        MethodProfiler.b("tileEntities");
        this.S = true;
        Iterator iterator = this.h.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            if (!tileentity.l() && tileentity.world != null && this.isLoaded(tileentity.x, tileentity.y, tileentity.z)) {
                tileentity.l_();
            }

            if (tileentity.l()) {
                iterator.remove();
                if (this.isChunkLoaded(tileentity.x >> 4, tileentity.z >> 4)) {
                    Chunk chunk = this.getChunkAt(tileentity.x >> 4, tileentity.z >> 4);

                    if (chunk != null) {
                        chunk.e(tileentity.x & 15, tileentity.y, tileentity.z & 15);
                    }
                }
            }
        }

        this.S = false;
        if (!this.N.isEmpty()) {
            this.h.removeAll(this.N);
            this.N.clear();
        }

        MethodProfiler.b("pendingTileEntities");
        if (!this.M.isEmpty()) {
            Iterator iterator1 = this.M.iterator();

            while (iterator1.hasNext()) {
                TileEntity tileentity1 = (TileEntity) iterator1.next();

                if (!tileentity1.l()) {
                    // CraftBukkit - order matters, moved down
                    /* if (!this.h.contains(tileentity1)) {
                        this.h.add(tileentity1);
                    } */

                    if (this.isChunkLoaded(tileentity1.x >> 4, tileentity1.z >> 4)) {
                        Chunk chunk1 = this.getChunkAt(tileentity1.x >> 4, tileentity1.z >> 4);

                        if (chunk1 != null) {
                            chunk1.a(tileentity1.x & 15, tileentity1.y, tileentity1.z & 15, tileentity1);
                            // CraftBukkit start - moved in from above
                            if (!this.h.contains(tileentity1)) {
                                this.h.add(tileentity1);
                            }
                            // CraftBukkit end
                        }
                    }

                    this.notify(tileentity1.x, tileentity1.y, tileentity1.z);
                }
            }

            this.M.clear();
        }

        MethodProfiler.a();
        MethodProfiler.a();
    }

    public void a(Collection collection) {
        if (this.S) {
            this.M.addAll(collection);
        } else {
            this.h.addAll(collection);
        }
    }

    public void playerJoinedWorld(Entity entity) {
        this.entityJoinedWorld(entity, true);
    }

    public void entityJoinedWorld(Entity entity, boolean flag) {
        int i = MathHelper.floor(entity.locX);
        int j = MathHelper.floor(entity.locZ);
        byte b0 = 32;

        if (!flag || this.a(i - b0, 0, j - b0, i + b0, this.height, j + b0)) {
            entity.bI = entity.locX;
            entity.bJ = entity.locY;
            entity.bK = entity.locZ;
            entity.lastYaw = entity.yaw;
            entity.lastPitch = entity.pitch;
            if (flag && entity.bW) {
                if (entity.vehicle != null) {
                    entity.M();
                } else {
                    entity.w_();
                }
            }

            MethodProfiler.a("chunkCheck");
            if (Double.isNaN(entity.locX) || Double.isInfinite(entity.locX)) {
                entity.locX = entity.bI;
            }

            if (Double.isNaN(entity.locY) || Double.isInfinite(entity.locY)) {
                entity.locY = entity.bJ;
            }

            if (Double.isNaN(entity.locZ) || Double.isInfinite(entity.locZ)) {
                entity.locZ = entity.bK;
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

            if (!entity.bW || entity.bX != k || entity.bY != l || entity.bZ != i1) {
                if (entity.bW && this.isChunkLoaded(entity.bX, entity.bZ)) {
                    this.getChunkAt(entity.bX, entity.bZ).a(entity, entity.bY);
                }

                if (this.isChunkLoaded(k, i1)) {
                    entity.bW = true;
                    this.getChunkAt(k, i1).a(entity);
                } else {
                    entity.bW = false;
                }
            }

            MethodProfiler.a();
            if (flag && entity.bW && entity.passenger != null) {
                if (!entity.passenger.dead && entity.passenger.vehicle == entity) {
                    this.playerJoinedWorld(entity.passenger);
                } else {
                    entity.passenger.vehicle = null;
                    entity.passenger = null;
                }
            }
        }
    }

    public boolean containsEntity(AxisAlignedBB axisalignedbb) {
        List list = this.b((Entity) null, axisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (!entity.dead && entity.bc) {
                return false;
            }
        }

        return true;
    }

    public boolean b(AxisAlignedBB axisalignedbb) {
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
                    Block block = Block.byId[this.getTypeId(k1, l1, i2)];

                    if (block != null) {
                        return true;
                    }
                }
            }
        }

        return false;
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
                    Block block = Block.byId[this.getTypeId(k1, l1, i2)];

                    if (block != null && block.material.isLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean d(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

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
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        if (!this.a(i, k, i1, j, l, j1)) {
            return false;
        } else {
            boolean flag = false;
            Vec3D vec3d = Vec3D.create(0.0D, 0.0D, 0.0D);

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = Block.byId[this.getTypeId(k1, l1, i2)];

                        if (block != null && block.material == material) {
                            double d0 = (double) ((float) (l1 + 1) - BlockFluids.d(this.getData(k1, l1, i2)));

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
                double d1 = 0.014D;

                entity.motX += vec3d.a * d1;
                entity.motY += vec3d.b * d1;
                entity.motZ += vec3d.c * d1;
            }

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
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

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
        return this.createExplosion(entity, d0, d1, d2, f, false);
    }

    public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f);

        explosion.a = flag;
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

        for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
            for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                    double d3 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) f;
                    double d4 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) f1;
                    double d5 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) f2;

                    if (this.a(Vec3D.create(d3, d4, d5), vec3d) == null) {
                        ++i;
                    }

                    ++j;
                }
            }
        }

        return (float) i / (float) j;
    }

    public void douseFire(EntityHuman entityhuman, int i, int j, int k, int l) {
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

        if (this.getTypeId(i, j, k) == Block.FIRE.id) {
            this.a(entityhuman, 1004, i, j, k, 0);
            this.setTypeId(i, j, k, 0);
        }
    }

    public TileEntity getTileEntity(int i, int j, int k) {
        Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

        if (chunk == null) {
            return null;
        } else {
            TileEntity tileentity = chunk.d(i & 15, j, k & 15);

            if (tileentity == null) {
                Iterator iterator = this.M.iterator();

                while (iterator.hasNext()) {
                    TileEntity tileentity1 = (TileEntity) iterator.next();

                    if (!tileentity1.l() && tileentity1.x == i && tileentity1.y == j && tileentity1.z == k) {
                        tileentity = tileentity1;
                        break;
                    }
                }
            }

            return tileentity;
        }
    }

    public void setTileEntity(int i, int j, int k, TileEntity tileentity) {
        if (tileentity != null && !tileentity.l()) {
            if (this.S) {
                tileentity.x = i;
                tileentity.y = j;
                tileentity.z = k;
                this.M.add(tileentity);
            } else {
                // CraftBukkit - order matters, moved down
                // this.h.add(tileentity);
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                if (chunk != null) {
                    chunk.a(i & 15, j, k & 15, tileentity);
                    this.h.add(tileentity); // CraftBukkit - moved in from above
                }
            }
        }
    }

    public void n(int i, int j, int k) {
        TileEntity tileentity = this.getTileEntity(i, j, k);

        if (tileentity != null && this.S) {
            tileentity.i();
            this.M.remove(tileentity);
        } else {
            if (tileentity != null) {
                this.M.remove(tileentity);
                this.h.remove(tileentity);
            }

            Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

            if (chunk != null) {
                chunk.e(i & 15, j, k & 15);
            }
        }
    }

    public void a(TileEntity tileentity) {
        this.N.add(tileentity);
    }

    public boolean o(int i, int j, int k) {
        Block block = Block.byId[this.getTypeId(i, j, k)];

        return block == null ? false : block.a();
    }

    public boolean e(int i, int j, int k) {
        Block block = Block.byId[this.getTypeId(i, j, k)];

        return block == null ? false : block.material.j() && block.b();
    }

    public boolean b(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            Chunk chunk = this.chunkProvider.getOrCreateChunk(i >> 4, k >> 4);

            if (chunk != null && !chunk.isEmpty()) {
                Block block = Block.byId[this.getTypeId(i, j, k)];

                return block == null ? false : block.material.j() && block.b();
            } else {
                return flag;
            }
        } else {
            return flag;
        }
    }

    public void g() {
        int i = this.a(1.0F);

        if (i != this.k) {
            this.k = i;
        }
    }

    public void setSpawnFlags(boolean flag, boolean flag1) {
        this.allowMonsters = flag;
        this.allowAnimals = flag1;
    }

    public void doTick() {
        if (this.r().isHardcore() && this.difficulty < 3) {
            this.difficulty = 3;
        }

        this.getWorldChunkManager().b();
        this.i();
        long i;

        if (this.everyoneDeeplySleeping()) {
            boolean flag = false;

            if (this.allowMonsters && this.difficulty >= 1) {
                ;
            }

            if (!flag) {
                i = this.worldData.f() + 24000L;
                this.worldData.a(i - i % 24000L);
                this.t();
            }
        }

        MethodProfiler.a("mobSpawner");
        // CraftBukkit start - Only call spawner if we have players online and the world allows for mobs or animals
        if ((this.allowMonsters || this.allowAnimals) && (this instanceof WorldServer && this.getServer().getHandle().players.size() > 0)) {
            SpawnerCreature.spawnEntities(this, this.allowMonsters, this.allowAnimals && this.worldData.f() % 400L == 0L);
        }
        // CraftBukkit end
        MethodProfiler.b("chunkSource");
        this.chunkProvider.unloadChunks();
        int j = this.a(1.0F);

        if (j != this.k) {
            this.k = j;
        }

        i = this.worldData.f() + 1L;
        if (i % (long) this.u == 0L) {
            MethodProfiler.b("save");
            this.save(false, (IProgressUpdate) null);
        }

        this.worldData.a(i);
        MethodProfiler.b("tickPending");
        this.a(false);
        MethodProfiler.b("tickTiles");
        this.k();
        MethodProfiler.a();
    }

    private void z() {
        if (this.worldData.hasStorm()) {
            this.o = 1.0F;
            if (this.worldData.isThundering()) {
                this.q = 1.0F;
            }
        }
    }

    protected void i() {
        if (!this.worldProvider.e) {
            if (this.r > 0) {
                --this.r;
            }

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

            this.n = this.o;
            if (this.worldData.hasStorm()) {
                this.o = (float) ((double) this.o + 0.01D);
            } else {
                this.o = (float) ((double) this.o - 0.01D);
            }

            if (this.o < 0.0F) {
                this.o = 0.0F;
            }

            if (this.o > 1.0F) {
                this.o = 1.0F;
            }

            this.p = this.q;
            if (this.worldData.isThundering()) {
                this.q = (float) ((double) this.q + 0.01D);
            } else {
                this.q = (float) ((double) this.q - 0.01D);
            }

            if (this.q < 0.0F) {
                this.q = 0.0F;
            }

            if (this.q > 1.0F) {
                this.q = 1.0F;
            }
        }
    }

    private void A() {
        // CraftBukkit start
        WeatherChangeEvent weather = new WeatherChangeEvent(this.getWorld(), false);
        this.getServer().getPluginManager().callEvent(weather);

        ThunderChangeEvent thunder = new ThunderChangeEvent(this.getWorld(), false);
        this.getServer().getPluginManager().callEvent(thunder);
        if (!weather.isCancelled()) {
            this.worldData.setWeatherDuration(0);
            this.worldData.setStorm(false);
        }
        if (!thunder.isCancelled()) {
            this.worldData.setThunderDuration(0);
            this.worldData.setThundering(false);
        }
        // CraftBukkit end
    }

    public void j() {
        this.worldData.setWeatherDuration(1);
    }

    protected void k() {
        this.T.clear();
        MethodProfiler.a("buildList");

        int i;
        int j;

        for (i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman = (EntityHuman) this.players.get(i);
            int k = MathHelper.floor(entityhuman.locX / 16.0D);
            int l = MathHelper.floor(entityhuman.locZ / 16.0D);
            byte b0 = 7;

            for (j = -b0; j <= b0; ++j) {
                for (int i1 = -b0; i1 <= b0; ++i1) {
                    this.T.add(new ChunkCoordIntPair(j + k, i1 + l));
                }
            }
        }

        if (this.U > 0) {
            --this.U;
        }

        i = 0;
        int j1 = 0;

        MethodProfiler.a();
        Iterator iterator = this.T.iterator();

        while (iterator.hasNext()) {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator.next();
            int k1 = chunkcoordintpair.x * 16;

            j = chunkcoordintpair.z * 16;
            MethodProfiler.a("getChunk");
            Chunk chunk = this.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z);

            MethodProfiler.b("tickChunk");
            chunk.i();
            MethodProfiler.b("moodSound");
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;

            if (this.U == 0) {
                this.l = this.l * 3 + 1013904223;
                l1 = this.l >> 2;
                i2 = l1 & 15;
                j2 = l1 >> 8 & 15;
                k2 = l1 >> 16 & this.heightMinusOne;
                l2 = chunk.getTypeId(i2, k2, j2);
                i2 += k1;
                j2 += j;
                if (l2 == 0 && this.k(i2, k2, j2) <= this.random.nextInt(8) && this.a(EnumSkyBlock.SKY, i2, k2, j2) <= 0) {
                    EntityHuman entityhuman1 = this.a((double) i2 + 0.5D, (double) k2 + 0.5D, (double) j2 + 0.5D, 8.0D);

                    if (entityhuman1 != null && entityhuman1.e((double) i2 + 0.5D, (double) k2 + 0.5D, (double) j2 + 0.5D) > 4.0D) {
                        this.makeSound((double) i2 + 0.5D, (double) k2 + 0.5D, (double) j2 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.random.nextFloat() * 0.2F);
                        this.U = this.random.nextInt(12000) + 6000;
                    }
                }
            }

            MethodProfiler.b("thunder");
            if (this.random.nextInt(100000) == 0 && this.w() && this.v()) {
                this.l = this.l * 3 + 1013904223;
                l1 = this.l >> 2;
                i2 = k1 + (l1 & 15);
                j2 = j + (l1 >> 8 & 15);
                k2 = this.e(i2, j2);
                if (this.v(i2, k2, j2)) {
                    this.strikeLightning(new EntityWeatherStorm(this, (double) i2, (double) k2, (double) j2));
                    this.r = 2;
                }
            }

            MethodProfiler.b("iceandsnow");
            this.l = this.l * 3 + 1013904223;
            l1 = this.l >> 2;
            i2 = l1 & 15;
            j2 = l1 >> 8 & 15;
            k2 = this.e(i2 + k1, j2 + j);
            if (this.q(i2 + k1, k2 - 1, j2 + j)) {
                // CraftBukkit start
                BlockState blockState = this.getWorld().getBlockAt(i2 + k1, k2 - 1, j2 + j).getState();
                blockState.setTypeId(Block.ICE.id);

                BlockFormEvent iceBlockForm = new BlockFormEvent(blockState.getBlock(), blockState);
                this.getServer().getPluginManager().callEvent(iceBlockForm);
                if (!iceBlockForm.isCancelled()) {
                    blockState.update(true);
                }
                // CraftBukkit end
            }

            if (this.w() && this.r(i2 + k1, k2, j2 + j)) {
                // CraftBukkit start
                BlockState blockState = this.getWorld().getBlockAt(i2 + k1, k2, j2 + j).getState();
                blockState.setTypeId(Block.SNOW.id);

                BlockFormEvent snow = new BlockFormEvent(blockState.getBlock(), blockState);
                this.getServer().getPluginManager().callEvent(snow);
                if (!snow.isCancelled()) {
                    blockState.update(true);
                }
                // CraftBukkit end
            }

            MethodProfiler.b("checkLight");
            this.s(k1 + this.random.nextInt(16), this.random.nextInt(this.height), j + this.random.nextInt(16));
            MethodProfiler.b("tickTiles");

            for (l1 = 0; l1 < 20; ++l1) {
                this.l = this.l * 3 + 1013904223;
                i2 = this.l >> 2;
                j2 = i2 & 15;
                k2 = i2 >> 8 & 15;
                l2 = i2 >> 16 & this.heightMinusOne;
                int i3 = chunk.b[j2 << this.heightBitsPlusFour | k2 << this.heightBits | l2] & 255;

                ++j1;
                if (Block.n[i3]) {
                    ++i;
                    Block.byId[i3].a(this, j2 + k1, l2, k2 + j, this.random);
                }
            }

            MethodProfiler.a();
        }
    }

    public boolean p(int i, int j, int k) {
        return this.c(i, j, k, false);
    }

    public boolean q(int i, int j, int k) {
        return this.c(i, j, k, true);
    }

    public boolean c(int i, int j, int k, boolean flag) {
        float f = this.getWorldChunkManager().a(i, j, k);

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < this.height && this.a(EnumSkyBlock.BLOCK, i, j, k) < 10) {
                int l = this.getTypeId(i, j, k);

                if ((l == Block.STATIONARY_WATER.id || l == Block.WATER.id) && this.getData(i, j, k) == 0) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = true;

                    if (flag1 && this.getMaterial(i - 1, j, k) != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getMaterial(i + 1, j, k) != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getMaterial(i, j, k - 1) != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getMaterial(i, j, k + 1) != Material.WATER) {
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

    public boolean r(int i, int j, int k) {
        float f = this.getWorldChunkManager().a(i, j, k);

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < this.height && this.a(EnumSkyBlock.BLOCK, i, j, k) < 10) {
                int l = this.getTypeId(i, j - 1, k);
                int i1 = this.getTypeId(i, j, k);

                if (i1 == 0 && Block.SNOW.canPlace(this, i, j, k) && l != 0 && l != Block.ICE.id && Block.byId[l].material.isSolid()) {
                    return true;
                }
            }

            return false;
        }
    }

    public void s(int i, int j, int k) {
        if (!this.worldProvider.e) {
            this.b(EnumSkyBlock.SKY, i, j, k);
        }

        this.b(EnumSkyBlock.BLOCK, i, j, k);
    }

    private int d(int i, int j, int k, int l, int i1, int j1) {
        int k1 = 0;

        if (this.isChunkLoaded(j, k, l)) {
            k1 = 15;
        } else {
            if (j1 == 0) {
                j1 = 1;
            }

            for (int l1 = 0; l1 < 6; ++l1) {
                int i2 = l1 % 2 * 2 - 1;
                int j2 = j + l1 / 2 % 3 / 2 * i2;
                int k2 = k + (l1 / 2 + 1) % 3 / 2 * i2;
                int l2 = l + (l1 / 2 + 2) % 3 / 2 * i2;
                int i3 = this.a(EnumSkyBlock.SKY, j2, k2, l2) - j1;

                if (i3 > k1) {
                    k1 = i3;
                }
            }
        }

        return k1;
    }

    private int e(int i, int j, int k, int l, int i1, int j1) {
        int k1 = Block.s[i1];
        int l1 = this.a(EnumSkyBlock.BLOCK, j - 1, k, l) - j1;
        int i2 = this.a(EnumSkyBlock.BLOCK, j + 1, k, l) - j1;
        int j2 = this.a(EnumSkyBlock.BLOCK, j, k - 1, l) - j1;
        int k2 = this.a(EnumSkyBlock.BLOCK, j, k + 1, l) - j1;
        int l2 = this.a(EnumSkyBlock.BLOCK, j, k, l - 1) - j1;
        int i3 = this.a(EnumSkyBlock.BLOCK, j, k, l + 1) - j1;

        if (l1 > k1) {
            k1 = l1;
        }

        if (i2 > k1) {
            k1 = i2;
        }

        if (j2 > k1) {
            k1 = j2;
        }

        if (k2 > k1) {
            k1 = k2;
        }

        if (l2 > k1) {
            k1 = l2;
        }

        if (i3 > k1) {
            k1 = i3;
        }

        return k1;
    }

    public void b(EnumSkyBlock enumskyblock, int i, int j, int k) {
        if (this.areChunksLoaded(i, j, k, 17)) {
            int l = 0;
            int i1 = 0;
            int j1 = this.a(enumskyblock, i, j, k);
            boolean flag = false;
            int k1 = this.getTypeId(i, j, k);
            int l1 = Block.q[k1];

            if (l1 == 0) {
                l1 = 1;
            }

            boolean flag1 = false;
            int i2;

            if (enumskyblock == EnumSkyBlock.SKY) {
                i2 = this.d(j1, i, j, k, k1, l1);
            } else {
                i2 = this.e(j1, i, j, k, k1, l1);
            }

            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;

            if (i2 > j1) {
                this.H[i1++] = 133152;
            } else if (i2 < j1) {
                if (enumskyblock != EnumSkyBlock.BLOCK) {
                    ;
                }

                this.H[i1++] = 133152 + (j1 << 18);

                while (l < i1) {
                    j2 = this.H[l++];
                    k1 = (j2 & 63) - 32 + i;
                    l1 = (j2 >> 6 & 63) - 32 + j;
                    i2 = (j2 >> 12 & 63) - 32 + k;
                    k2 = j2 >> 18 & 15;
                    l2 = this.a(enumskyblock, k1, l1, i2);
                    if (l2 == k2) {
                        this.a(enumskyblock, k1, l1, i2, 0);
                        if (k2 > 0) {
                            i3 = k1 - i;
                            k3 = l1 - j;
                            j3 = i2 - k;
                            if (i3 < 0) {
                                i3 = -i3;
                            }

                            if (k3 < 0) {
                                k3 = -k3;
                            }

                            if (j3 < 0) {
                                j3 = -j3;
                            }

                            if (i3 + k3 + j3 < 17) {
                                for (int l3 = 0; l3 < 6; ++l3) {
                                    int i4 = l3 % 2 * 2 - 1;
                                    int j4 = k1 + l3 / 2 % 3 / 2 * i4;
                                    int k4 = l1 + (l3 / 2 + 1) % 3 / 2 * i4;
                                    int l4 = i2 + (l3 / 2 + 2) % 3 / 2 * i4;

                                    l2 = this.a(enumskyblock, j4, k4, l4);
                                    int i5 = Block.q[this.getTypeId(j4, k4, l4)];

                                    if (i5 == 0) {
                                        i5 = 1;
                                    }

                                    if (l2 == k2 - i5) {
                                        this.H[i1++] = j4 - i + 32 + (k4 - j + 32 << 6) + (l4 - k + 32 << 12) + (k2 - i5 << 18);
                                    }
                                }
                            }
                        }
                    }
                }

                l = 0;
            }

            while (l < i1) {
                j1 = this.H[l++];
                int j5 = (j1 & 63) - 32 + i;

                j2 = (j1 >> 6 & 63) - 32 + j;
                k1 = (j1 >> 12 & 63) - 32 + k;
                l1 = this.a(enumskyblock, j5, j2, k1);
                i2 = this.getTypeId(j5, j2, k1);
                k2 = Block.q[i2];
                if (k2 == 0) {
                    k2 = 1;
                }

                boolean flag2 = false;

                if (enumskyblock == EnumSkyBlock.SKY) {
                    l2 = this.d(l1, j5, j2, k1, i2, k2);
                } else {
                    l2 = this.e(l1, j5, j2, k1, i2, k2);
                }

                if (l2 != l1) {
                    this.a(enumskyblock, j5, j2, k1, l2);
                    if (l2 > l1) {
                        i3 = j5 - i;
                        k3 = j2 - j;
                        j3 = k1 - k;
                        if (i3 < 0) {
                            i3 = -i3;
                        }

                        if (k3 < 0) {
                            k3 = -k3;
                        }

                        if (j3 < 0) {
                            j3 = -j3;
                        }

                        if (i3 + k3 + j3 < 17 && i1 < this.H.length - 6) {
                            if (this.a(enumskyblock, j5 - 1, j2, k1) < l2) {
                                this.H[i1++] = j5 - 1 - i + 32 + (j2 - j + 32 << 6) + (k1 - k + 32 << 12);
                            }

                            if (this.a(enumskyblock, j5 + 1, j2, k1) < l2) {
                                this.H[i1++] = j5 + 1 - i + 32 + (j2 - j + 32 << 6) + (k1 - k + 32 << 12);
                            }

                            if (this.a(enumskyblock, j5, j2 - 1, k1) < l2) {
                                this.H[i1++] = j5 - i + 32 + (j2 - 1 - j + 32 << 6) + (k1 - k + 32 << 12);
                            }

                            if (this.a(enumskyblock, j5, j2 + 1, k1) < l2) {
                                this.H[i1++] = j5 - i + 32 + (j2 + 1 - j + 32 << 6) + (k1 - k + 32 << 12);
                            }

                            if (this.a(enumskyblock, j5, j2, k1 - 1) < l2) {
                                this.H[i1++] = j5 - i + 32 + (j2 - j + 32 << 6) + (k1 - 1 - k + 32 << 12);
                            }

                            if (this.a(enumskyblock, j5, j2, k1 + 1) < l2) {
                                this.H[i1++] = j5 - i + 32 + (j2 - j + 32 << 6) + (k1 + 1 - k + 32 << 12);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean a(boolean flag) {
        int i = this.K.size();

        if (i != this.L.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if (i > 1000) {
                i = 1000;
            }

            for (int j = 0; j < i; ++j) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) this.K.first();

                if (!flag && nextticklistentry.e > this.worldData.f()) {
                    break;
                }

                this.K.remove(nextticklistentry);
                this.L.remove(nextticklistentry);
                byte b0 = 8;

                if (this.a(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                    int k = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                    if (k == nextticklistentry.d && k > 0) {
                        Block.byId[k].a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                    }
                }
            }

            return this.K.size() != 0;
        }
    }

    public List a(Chunk chunk, boolean flag) {
        ArrayList arraylist = null;
        ChunkCoordIntPair chunkcoordintpair = chunk.j();
        int i = chunkcoordintpair.x << 4;
        int j = i + 16;
        int k = chunkcoordintpair.z << 4;
        int l = k + 16;
        Iterator iterator = this.L.iterator();

        while (iterator.hasNext()) {
            NextTickListEntry nextticklistentry = (NextTickListEntry) iterator.next();

            if (nextticklistentry.a >= i && nextticklistentry.a < j && nextticklistentry.c >= k && nextticklistentry.c < l) {
                if (flag) {
                    this.K.remove(nextticklistentry);
                    iterator.remove();
                }

                if (arraylist == null) {
                    arraylist = new ArrayList();
                }

                arraylist.add(nextticklistentry);
            }
        }

        return arraylist;
    }

    public List b(Entity entity, AxisAlignedBB axisalignedbb) {
        this.V.clear();
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.isChunkLoaded(i1, j1)) {
                    this.getChunkAt(i1, j1).a(entity, axisalignedbb, this.V);
                }
            }
        }

        return this.V;
    }

    public List a(Class oclass, AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);
        ArrayList arraylist = new ArrayList();

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.isChunkLoaded(i1, j1)) {
                    this.getChunkAt(i1, j1).a(oclass, axisalignedbb, arraylist);
                }
            }
        }

        return arraylist;
    }

    public void b(int i, int j, int k, TileEntity tileentity) {
        if (this.isLoaded(i, j, k)) {
            this.getChunkAtWorldCoords(i, k).f();
        }

        for (int l = 0; l < this.z.size(); ++l) {
            ((IWorldAccess) this.z.get(l)).a(i, j, k, tileentity);
        }
    }

    public int a(Class oclass) {
        int i = 0;

        for (int j = 0; j < this.entityList.size(); ++j) {
            Entity entity = (Entity) this.entityList.get(j);

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
            // CraftBukkit start - fixed an NPE
            if (entity == null) {
                continue;
            }
            // CraftBukkit end
            this.entityList.add(entity);
            // CraftBukkit end
            this.c((Entity) list.get(i));
        }
    }

    public void b(List list) {
        this.J.addAll(list);
    }

    public boolean a(int i, int j, int k, int l, boolean flag, int i1) {
        int j1 = this.getTypeId(j, k, l);
        Block block = Block.byId[j1];
        Block block1 = Block.byId[i];
        AxisAlignedBB axisalignedbb = block1.e(this, j, k, l);

        if (flag) {
            axisalignedbb = null;
        }

        boolean defaultReturn; // CraftBukkit - store the default action

        if (axisalignedbb != null && !this.containsEntity(axisalignedbb)) {
            defaultReturn = false; // CraftBukkit
        } else {
            if (block == Block.WATER || block == Block.STATIONARY_WATER || block == Block.LAVA || block == Block.STATIONARY_LAVA || block == Block.FIRE || block == Block.SNOW || block == Block.VINE) {
                block = null;
            }

            defaultReturn = i > 0 && block == null && block1.canPlace(this, j, k, l, i1); // CraftBukkit
        }

        // CraftBukkit start
        BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(j, k, l), i, defaultReturn);
        this.getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }

    public PathEntity findPath(Entity entity, Entity entity1, float f) {
        MethodProfiler.a("pathfind");
        int i = MathHelper.floor(entity.locX);
        int j = MathHelper.floor(entity.locY);
        int k = MathHelper.floor(entity.locZ);
        int l = (int) (f + 16.0F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache(this, i1, j1, k1, l1, i2, j2);
        PathEntity pathentity = (new Pathfinder(chunkcache)).a(entity, entity1, f);

        MethodProfiler.a();
        return pathentity;
    }

    public PathEntity a(Entity entity, int i, int j, int k, float f) {
        MethodProfiler.a("pathfind");
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
        ChunkCache chunkcache = new ChunkCache(this, l1, i2, j2, k2, l2, i3);
        PathEntity pathentity = (new Pathfinder(chunkcache)).a(entity, i, j, k, f);

        MethodProfiler.a();
        return pathentity;
    }

    public boolean isBlockFacePowered(int i, int j, int k, int l) {
        int i1 = this.getTypeId(i, j, k);

        return i1 == 0 ? false : Block.byId[i1].d(this, i, j, k, l);
    }

    public boolean isBlockPowered(int i, int j, int k) {
        return this.isBlockFacePowered(i, j - 1, k, 0) ? true : (this.isBlockFacePowered(i, j + 1, k, 1) ? true : (this.isBlockFacePowered(i, j, k - 1, 2) ? true : (this.isBlockFacePowered(i, j, k + 1, 3) ? true : (this.isBlockFacePowered(i - 1, j, k, 4) ? true : this.isBlockFacePowered(i + 1, j, k, 5)))));
    }

    public boolean isBlockFaceIndirectlyPowered(int i, int j, int k, int l) {
        if (this.e(i, j, k)) {
            return this.isBlockPowered(i, j, k);
        } else {
            int i1 = this.getTypeId(i, j, k);

            return i1 == 0 ? false : Block.byId[i1].a(this, i, j, k, l);
        }
    }

    public boolean isBlockIndirectlyPowered(int i, int j, int k) {
        return this.isBlockFaceIndirectlyPowered(i, j - 1, k, 0) ? true : (this.isBlockFaceIndirectlyPowered(i, j + 1, k, 1) ? true : (this.isBlockFaceIndirectlyPowered(i, j, k - 1, 2) ? true : (this.isBlockFaceIndirectlyPowered(i, j, k + 1, 3) ? true : (this.isBlockFaceIndirectlyPowered(i - 1, j, k, 4) ? true : this.isBlockFaceIndirectlyPowered(i + 1, j, k, 5)))));
    }

    public EntityHuman findNearbyPlayer(Entity entity, double d0) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0);
    }

    public EntityHuman a(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;

        for (int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman1 = (EntityHuman) this.players.get(i);
            // CraftBukkit start - fixed an NPE
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

    public EntityHuman b(Entity entity, double d0) {
        return this.b(entity.locX, entity.locY, entity.locZ, d0);
    }

    public EntityHuman b(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;

        for (int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman1 = (EntityHuman) this.players.get(i);

            if (!entityhuman1.abilities.isInvulnerable) {
                double d5 = entityhuman1.e(d0, d1, d2);

                if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    public EntityHuman a(String s) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (s.equals(((EntityHuman) this.players.get(i)).name)) {
                return (EntityHuman) this.players.get(i);
            }
        }

        return null;
    }

    public byte[] getMultiChunkData(int i, int j, int k, int l, int i1, int j1) {
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

        if (i3 > this.height) {
            i3 = this.height;
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

                k2 = this.getChunkAt(j3, i4).getData(abyte, k3, l2, j4, l3, i3, k4, k2);
            }
        }

        return abyte;
    }

    public void l() {
        this.B.b();
    }

    public void setTime(long i) {
        this.worldData.a(i);
    }

    public void setTimeAndFixTicklists(long i) {
        long j = i - this.worldData.f();

        NextTickListEntry nextticklistentry;

        for (Iterator iterator = this.L.iterator(); iterator.hasNext(); nextticklistentry.e += j) {
            nextticklistentry = (NextTickListEntry) iterator.next();
        }

        this.setTime(i);
    }

    public long getSeed() {
        return this.worldData.getSeed();
    }

    public long getTime() {
        return this.worldData.f();
    }

    public ChunkCoordinates getSpawn() {
        return new ChunkCoordinates(this.worldData.c(), this.worldData.d(), this.worldData.e());
    }

    public boolean a(EntityHuman entityhuman, int i, int j, int k) {
        return true;
    }

    public void a(Entity entity, byte b0) {}

    public IChunkProvider p() {
        return this.chunkProvider;
    }

    public void playNote(int i, int j, int k, int l, int i1) {
        int j1 = this.getTypeId(i, j, k);

        if (j1 > 0) {
            Block.byId[j1].a(this, i, j, k, l, i1);
        }
    }

    public IDataManager q() {
        return this.B;
    }

    public WorldData r() {
        return this.worldData;
    }

    public void everyoneSleeping() {
        this.Q = !this.players.isEmpty();
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            // CraftBukkit
            if (!entityhuman.isSleeping() && !entityhuman.fauxSleeping) {
                this.Q = false;
                break;
            }
        }
    }

    // CraftBukkit start
    // Calls the method that checks to see if players are sleeping
    // Called by CraftPlayer.setPermanentSleeping()
    public void checkSleepStatus() {
        if (!this.isStatic) {
            this.everyoneSleeping();
        }
    }
    // CraftBukkit end

    protected void t() {
        this.Q = false;
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (entityhuman.isSleeping()) {
                entityhuman.a(false, false, true);
            }
        }

        this.A();
    }

    public boolean everyoneDeeplySleeping() {
        if (this.Q && !this.isStatic) {
            Iterator iterator = this.players.iterator();

            // CraftBukkit - This allows us to assume that some people are in bed but not really, allowing time to pass in spite of AFKers
            boolean foundActualSleepers = false;

            EntityHuman entityhuman;

            do {
                if (!iterator.hasNext()) {
                    // CraftBukkit
                    return foundActualSleepers;
                }

                entityhuman = (EntityHuman) iterator.next();
                // CraftBukkit start
                if (entityhuman.isDeeplySleeping()) {
                    foundActualSleepers = true;
                }
            } while (entityhuman.isDeeplySleeping() || entityhuman.fauxSleeping);
            // CraftBukkit end

            return false;
        } else {
            return false;
        }
    }

    public float c(float f) {
        return (this.p + (this.q - this.p) * f) * this.d(f);
    }

    public float d(float f) {
        return this.n + (this.o - this.n) * f;
    }

    public boolean v() {
        return (double) this.c(1.0F) > 0.9D;
    }

    public boolean w() {
        return (double) this.d(1.0F) > 0.2D;
    }

    public boolean v(int i, int j, int k) {
        if (!this.w()) {
            return false;
        } else if (!this.isChunkLoaded(i, j, k)) {
            return false;
        } else if (this.e(i, k) > j) {
            return false;
        } else {
            BiomeBase biomebase = this.getWorldChunkManager().getBiome(i, k);

            return biomebase.b() ? false : biomebase.c();
        }
    }

    public void a(String s, WorldMapBase worldmapbase) {
        this.worldMaps.a(s, worldmapbase);
    }

    public WorldMapBase a(Class oclass, String s) {
        return this.worldMaps.a(oclass, s);
    }

    public int b(String s) {
        return this.worldMaps.a(s);
    }

    public void f(int i, int j, int k, int l, int i1) {
        this.a((EntityHuman) null, i, j, k, l, i1);
    }

    public void a(EntityHuman entityhuman, int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < this.z.size(); ++j1) {
            ((IWorldAccess) this.z.get(j1)).a(entityhuman, i, j, k, l, i1);
        }
    }

    public Random w(int i, int j, int k) {
        long l = (long) i * 341873128712L + (long) j * 132897987541L + this.r().getSeed() + (long) k;

        this.random.setSeed(l);
        return this.random;
    }

    public boolean x() {
        return false;
    }

    public void a(EnumSkyBlock enumskyblock, int i, int j, int k, int l, int i1, int j1) {}

    public BiomeMeta a(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        List list = this.p().a(enumcreaturetype, i, j, k);

        return list != null && !list.isEmpty() ? (BiomeMeta) WeightedRandom.a(this.random, (Collection) list) : null;
    }

    public ChunkPosition b(String s, int i, int j, int k) {
        return this.p().a(this, s, i, j, k);
    }

    // CraftBukkit start
    public UUID getUUID() {
        return this.B.getUUID();
    }
    // CraftBukkit end
}
