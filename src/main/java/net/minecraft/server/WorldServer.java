package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import org.bukkit.WeatherType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.LongHash;

import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldServer extends World implements org.bukkit.BlockChangeDelegate {
    // CraftBukkit end

    private static final Logger a = LogManager.getLogger();
    private final MinecraftServer server;
    public EntityTracker tracker; // CraftBukkit - private final -> public
    private final PlayerChunkMap manager;
    private Set M;
    private TreeSet N;
    public ChunkProviderServer chunkProviderServer;
    public boolean savingDisabled;
    private boolean O;
    private int emptyTime;
    private final PortalTravelAgent Q;
    private final SpawnerCreature R = new SpawnerCreature();
    private NoteDataList[] S = new NoteDataList[] { new NoteDataList((EmptyClass2) null), new NoteDataList((EmptyClass2) null)};
    private int T;
    private static final StructurePieceTreasure[] U = new StructurePieceTreasure[] { new StructurePieceTreasure(Items.STICK, 0, 1, 3, 10), new StructurePieceTreasure(Item.getItemOf(Blocks.WOOD), 0, 1, 3, 10), new StructurePieceTreasure(Item.getItemOf(Blocks.LOG), 0, 1, 3, 10), new StructurePieceTreasure(Items.STONE_AXE, 0, 1, 1, 3), new StructurePieceTreasure(Items.WOOD_AXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.STONE_PICKAXE, 0, 1, 1, 3), new StructurePieceTreasure(Items.WOOD_PICKAXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.APPLE, 0, 2, 3, 5), new StructurePieceTreasure(Items.BREAD, 0, 2, 3, 3), new StructurePieceTreasure(Item.getItemOf(Blocks.LOG2), 0, 1, 3, 10)};
    private List V = new ArrayList();
    private IntHashMap entitiesById;

    // CraftBukkit start
    public final int dimension;

    // Add env and gen to constructor
    public WorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, WorldSettings worldsettings, MethodProfiler methodprofiler, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(idatamanager, s, worldsettings, WorldProvider.byDimension(env.getId()), methodprofiler, gen, env);
        this.dimension = i;
        this.pvpMode = minecraftserver.getPvP();
        // CraftBukkit end
        this.server = minecraftserver;
        this.tracker = new EntityTracker(this);
        this.manager = new PlayerChunkMap(this, minecraftserver.getPlayerList().o());
        if (this.entitiesById == null) {
            this.entitiesById = new IntHashMap();
        }

        if (this.M == null) {
            this.M = new HashSet();
        }

        if (this.N == null) {
            this.N = new TreeSet();
        }

        this.Q = new org.bukkit.craftbukkit.CraftTravelAgent(this); // CraftBukkit
        this.scoreboard = new ScoreboardServer(minecraftserver);
        PersistentScoreboard persistentscoreboard = (PersistentScoreboard) this.worldMaps.get(PersistentScoreboard.class, "scoreboard");

        if (persistentscoreboard == null) {
            persistentscoreboard = new PersistentScoreboard();
            this.worldMaps.a("scoreboard", persistentscoreboard);
        }

        persistentscoreboard.a(this.scoreboard);
        ((ScoreboardServer) this.scoreboard).a(persistentscoreboard);
    }

    // CraftBukkit start
    @Override
    public TileEntity getTileEntity(int i, int j, int k) {
        TileEntity result = super.getTileEntity(i, j, k);
        Block type = getType(i, j, k);

        if (type == Blocks.CHEST) {
            if (!(result instanceof TileEntityChest)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.FURNACE) {
            if (!(result instanceof TileEntityFurnace)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.DROPPER) {
            if (!(result instanceof TileEntityDropper)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.DISPENSER) {
            if (!(result instanceof TileEntityDispenser)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.JUKEBOX) {
            if (!(result instanceof TileEntityRecordPlayer)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.NOTE_BLOCK) {
            if (!(result instanceof TileEntityNote)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.MOB_SPAWNER) {
            if (!(result instanceof TileEntityMobSpawner)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if ((type == Blocks.SIGN_POST) || (type == Blocks.WALL_SIGN)) {
            if (!(result instanceof TileEntitySign)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.ENDER_CHEST) {
            if (!(result instanceof TileEntityEnderChest)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.BREWING_STAND) {
            if (!(result instanceof TileEntityBrewingStand)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.BEACON) {
            if (!(result instanceof TileEntityBeacon)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Blocks.HOPPER) {
            if (!(result instanceof TileEntityHopper)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        }

        return result;
    }

    private TileEntity fixTileEntity(int x, int y, int z, Block type, TileEntity found) {
        this.getServer().getLogger().severe("Block at " + x + "," + y + "," + z + " is " + org.bukkit.Material.getMaterial(Block.b(type)).toString() + " but has " + found + ". "
                + "Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.");

        if (type instanceof IContainer) {
            TileEntity replacement = ((IContainer) type).a(this, this.getData(x, y, z));
            replacement.world = this;
            this.setTileEntity(x, y, z, replacement);
            return replacement;
        } else {
            this.getServer().getLogger().severe("Don't know how to fix for this type... Can't do anything! :(");
            return found;
        }
    }

    private boolean canSpawn(int x, int z) {
        if (this.generator != null) {
            return this.generator.canSpawn(this.getWorld(), x, z);
        } else {
            return this.worldProvider.canSpawn(x, z);
        }
    }
    // CraftBukkit end

    public void doTick() {
        super.doTick();
        if (this.getWorldData().isHardcore() && this.difficulty != EnumDifficulty.HARD) {
            this.difficulty = EnumDifficulty.HARD;
        }

        this.worldProvider.e.b();
        if (this.everyoneDeeplySleeping()) {
            if (this.getGameRules().getBoolean("doDaylightCycle")) {
                long i = this.worldData.getDayTime() + 24000L;

                this.worldData.setDayTime(i - i % 24000L);
            }

            this.d();
        }

        this.methodProfiler.a("mobSpawner");
        // CraftBukkit start - Only call spawner if we have players online and the world allows for mobs or animals
        long time = this.worldData.getTime();
        if (this.getGameRules().getBoolean("doMobSpawning") && (this.allowMonsters || this.allowAnimals) && (this instanceof WorldServer && this.players.size() > 0)) {
            this.R.spawnEntities(this, this.allowMonsters && (this.ticksPerMonsterSpawns != 0 && time % this.ticksPerMonsterSpawns == 0L), this.allowAnimals && (this.ticksPerAnimalSpawns != 0 && time % this.ticksPerAnimalSpawns == 0L), this.worldData.getTime() % 400L == 0L);
            // CraftBukkit end
        }

        this.methodProfiler.c("chunkSource");
        this.chunkProvider.unloadChunks();
        int j = this.a(1.0F);

        if (j != this.j) {
            this.j = j;
        }

        this.worldData.setTime(this.worldData.getTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.worldData.setDayTime(this.worldData.getDayTime() + 1L);
        }

        this.methodProfiler.c("tickPending");
        this.a(false);
        this.methodProfiler.c("tickBlocks");
        this.g();
        this.methodProfiler.c("chunkMap");
        this.manager.flush();
        this.methodProfiler.c("village");
        this.villages.tick();
        this.siegeManager.a();
        this.methodProfiler.c("portalForcer");
        this.Q.a(this.getTime());
        this.methodProfiler.b();
        this.Z();

        this.getWorld().processChunkGC(); // CraftBukkit
    }

    public BiomeMeta a(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        List list = this.K().getMobsFor(enumcreaturetype, i, j, k);

        return list != null && !list.isEmpty() ? (BiomeMeta) WeightedRandom.a(this.random, (Collection) list) : null;
    }

    public void everyoneSleeping() {
        this.O = !this.players.isEmpty();
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (!entityhuman.isSleeping() && !entityhuman.fauxSleeping) { // CraftBukkit
                this.O = false;
                break;
            }
        }
    }

    protected void d() {
        this.O = false;
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (entityhuman.isSleeping()) {
                entityhuman.a(false, false, true);
            }
        }

        this.Y();
    }

    private void Y() {
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

    public boolean everyoneDeeplySleeping() {
        if (this.O && !this.isStatic) {
            Iterator iterator = this.players.iterator();

            // CraftBukkit - This allows us to assume that some people are in bed but not really, allowing time to pass in spite of AFKers
            boolean foundActualSleepers = false;

            EntityHuman entityhuman;

            do {
                if (!iterator.hasNext()) {
                    return foundActualSleepers; // CraftBukkit
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

    protected void g() {
        super.g();
        int i = 0;
        int j = 0;
        // CraftBukkit start
        // Iterator iterator = this.chunkTickList.iterator();

        for (long chunkCoord : this.chunkTickList.popAll()) {
            // ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator.next();
            int chunkX = LongHash.msw(chunkCoord);
            int chunkZ = LongHash.lsw(chunkCoord);
            int k = chunkX * 16;
            int l = chunkZ * 16;

            this.methodProfiler.a("getChunk");
            Chunk chunk = this.getChunkAt(chunkX, chunkZ);
            // CraftBukkit end

            this.a(k, l, chunk);
            this.methodProfiler.c("tickChunk");
            chunk.b(false);
            this.methodProfiler.c("thunder");
            int i1;
            int j1;
            int k1;
            int l1;

            if (this.random.nextInt(100000) == 0 && this.P() && this.O()) {
                this.k = this.k * 3 + 1013904223;
                i1 = this.k >> 2;
                j1 = k + (i1 & 15);
                k1 = l + (i1 >> 8 & 15);
                l1 = this.h(j1, k1);
                if (this.isRainingAt(j1, l1, k1)) {
                    this.strikeLightning(new EntityLightning(this, (double) j1, (double) l1, (double) k1));
                }
            }

            this.methodProfiler.c("iceandsnow");
            if (this.random.nextInt(16) == 0) {
                this.k = this.k * 3 + 1013904223;
                i1 = this.k >> 2;
                j1 = i1 & 15;
                k1 = i1 >> 8 & 15;
                l1 = this.h(j1 + k, k1 + l);
                if (this.s(j1 + k, l1 - 1, k1 + l)) {
                    // CraftBukkit start
                    BlockState blockState = this.getWorld().getBlockAt(j1 + k, l1 - 1, k1 + l).getState();
                    blockState.setTypeId(Block.b(Blocks.ICE));

                    BlockFormEvent iceBlockForm = new BlockFormEvent(blockState.getBlock(), blockState);
                    this.getServer().getPluginManager().callEvent(iceBlockForm);
                    if (!iceBlockForm.isCancelled()) {
                        blockState.update(true);
                    }
                    // CraftBukkit end
                }

                if (this.P() && this.e(j1 + k, l1, k1 + l, true)) {
                    // CraftBukkit start
                    BlockState blockState = this.getWorld().getBlockAt(j1 + k, l1, k1 + l).getState();
                    blockState.setTypeId(Block.b(Blocks.SNOW));

                    BlockFormEvent snow = new BlockFormEvent(blockState.getBlock(), blockState);
                    this.getServer().getPluginManager().callEvent(snow);
                    if (!snow.isCancelled()) {
                        blockState.update(true);
                    }
                    // CraftBukkit end
                }

                if (this.P()) {
                    BiomeBase biomebase = this.getBiome(j1 + k, k1 + l);

                    if (biomebase.e()) {
                        this.getType(j1 + k, l1 - 1, k1 + l).l(this, j1 + k, l1 - 1, k1 + l);
                    }
                }
            }

            this.methodProfiler.c("tickBlocks");
            ChunkSection[] achunksection = chunk.i();

            j1 = achunksection.length;

            for (k1 = 0; k1 < j1; ++k1) {
                ChunkSection chunksection = achunksection[k1];

                if (chunksection != null && chunksection.shouldTick()) {
                    for (int i2 = 0; i2 < 3; ++i2) {
                        this.k = this.k * 3 + 1013904223;
                        int j2 = this.k >> 2;
                        int k2 = j2 & 15;
                        int l2 = j2 >> 8 & 15;
                        int i3 = j2 >> 16 & 15;

                        ++j;
                        Block block = chunksection.getTypeId(k2, i3, l2);

                        if (block.isTicking()) {
                            ++i;
                            block.a(this, k2 + k, i3 + chunksection.getYPosition(), l2 + l, this.random);
                        }
                    }
                }
            }

            this.methodProfiler.b();
        }
    }

    public boolean a(int i, int j, int k, Block block) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, block);

        return this.V.contains(nextticklistentry);
    }

    public void a(int i, int j, int k, Block block, int l) {
        this.a(i, j, k, block, l, 0);
    }

    public void a(int i, int j, int k, Block block, int l, int i1) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, block);
        byte b0 = 0;

        if (this.d && block.getMaterial() != Material.AIR) {
            if (block.L()) {
                b0 = 8;
                if (this.b(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                    Block block1 = this.getType(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                    if (block1.getMaterial() != Material.AIR && block1 == nextticklistentry.a()) {
                        block1.a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                    }
                }

                return;
            }

            l = 1;
        }

        if (this.b(i - b0, j - b0, k - b0, i + b0, j + b0, k + b0)) {
            if (block.getMaterial() != Material.AIR) {
                nextticklistentry.a((long) l + this.worldData.getTime());
                nextticklistentry.a(i1);
            }

            if (!this.M.contains(nextticklistentry)) {
                this.M.add(nextticklistentry);
                this.N.add(nextticklistentry);
            }
        }
    }

    public void b(int i, int j, int k, Block block, int l, int i1) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, block);

        nextticklistentry.a(i1);
        if (block.getMaterial() != Material.AIR) {
            nextticklistentry.a((long) l + this.worldData.getTime());
        }

        if (!this.M.contains(nextticklistentry)) {
            this.M.add(nextticklistentry);
            this.N.add(nextticklistentry);
        }
    }

    public void tickEntities() {
        if (false && this.players.isEmpty()) { // CraftBukkit - this prevents entity cleanup, other issues on servers with no players
            if (this.emptyTime++ >= 1200) {
                return;
            }
        } else {
            this.i();
        }

        super.tickEntities();
    }

    public void i() {
        this.emptyTime = 0;
    }

    public boolean a(boolean flag) {
        int i = this.N.size();

        if (i != this.M.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if (i > 1000) {
                // CraftBukkit start - If the server has too much to process over time, try to alleviate that
                if (i > 20 * 1000) {
                    i = i / 20;
                } else {
                    i = 1000;
                }
                // CraftBukkit end
            }

            this.methodProfiler.a("cleaning");

            NextTickListEntry nextticklistentry;

            for (int j = 0; j < i; ++j) {
                nextticklistentry = (NextTickListEntry) this.N.first();
                if (!flag && nextticklistentry.d > this.worldData.getTime()) {
                    break;
                }

                this.N.remove(nextticklistentry);
                this.M.remove(nextticklistentry);
                this.V.add(nextticklistentry);
            }

            this.methodProfiler.b();
            this.methodProfiler.a("ticking");
            Iterator iterator = this.V.iterator();

            while (iterator.hasNext()) {
                nextticklistentry = (NextTickListEntry) iterator.next();
                iterator.remove();
                byte b0 = 0;

                if (this.b(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                    Block block = this.getType(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                    if (block.getMaterial() != Material.AIR && Block.a(block, nextticklistentry.a())) {
                        try {
                            block.a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.a(throwable, "Exception while ticking a block");
                            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being ticked");

                            int k;

                            try {
                                k = this.getData(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);
                            } catch (Throwable throwable1) {
                                k = -1;
                            }

                            CrashReportSystemDetails.a(crashreportsystemdetails, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, block, k);
                            throw new ReportedException(crashreport);
                        }
                    }
                } else {
                    this.a(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, nextticklistentry.a(), 0);
                }
            }

            this.methodProfiler.b();
            this.V.clear();
            return !this.N.isEmpty();
        }
    }

    public List a(Chunk chunk, boolean flag) {
        ArrayList arraylist = null;
        ChunkCoordIntPair chunkcoordintpair = chunk.l();
        int i = (chunkcoordintpair.x << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkcoordintpair.z << 4) - 2;
        int l = k + 16 + 2;

        for (int i1 = 0; i1 < 2; ++i1) {
            Iterator iterator;

            if (i1 == 0) {
                iterator = this.N.iterator();
            } else {
                iterator = this.V.iterator();
                if (!this.V.isEmpty()) {
                    a.debug("toBeTicked = " + this.V.size());
                }
            }

            while (iterator.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator.next();

                if (nextticklistentry.a >= i && nextticklistentry.a < j && nextticklistentry.c >= k && nextticklistentry.c < l) {
                    if (flag) {
                        this.M.remove(nextticklistentry);
                        iterator.remove();
                    }

                    if (arraylist == null) {
                        arraylist = new ArrayList();
                    }

                    arraylist.add(nextticklistentry);
                }
            }
        }

        return arraylist;
    }

    /* CraftBukkit start - We prevent spawning in general, so this butchering is not needed
    public void entityJoinedWorld(Entity entity, boolean flag) {
        if (!this.server.getSpawnAnimals() && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
            entity.die();
        }

        if (!this.server.getSpawnNPCs() && entity instanceof NPC) {
            entity.die();
        }

        super.entityJoinedWorld(entity, flag);
    }
    // CraftBukkit end */

    protected IChunkProvider j() {
        IChunkLoader ichunkloader = this.dataManager.createChunkLoader(this.worldProvider);

        // CraftBukkit start
        org.bukkit.craftbukkit.generator.InternalChunkGenerator gen;

        if (this.generator != null) {
            gen = new org.bukkit.craftbukkit.generator.CustomChunkGenerator(this, this.getSeed(), this.generator);
        } else if (this.worldProvider instanceof WorldProviderHell) {
            gen = new org.bukkit.craftbukkit.generator.NetherChunkGenerator(this, this.getSeed());
        } else if (this.worldProvider instanceof WorldProviderTheEnd) {
            gen = new org.bukkit.craftbukkit.generator.SkyLandsChunkGenerator(this, this.getSeed());
        } else {
            gen = new org.bukkit.craftbukkit.generator.NormalChunkGenerator(this, this.getSeed());
        }

        this.chunkProviderServer = new ChunkProviderServer(this, ichunkloader, gen);
        // CraftBukkit end

        return this.chunkProviderServer;
    }

    public List getTileEntities(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = new ArrayList();

        // CraftBukkit start - Get tile entities from chunks instead of world
        for (int chunkX = (i >> 4); chunkX <= ((l - 1) >> 4); chunkX++) {
            for (int chunkZ = (k >> 4); chunkZ <= ((j1 - 1) >> 4); chunkZ++) {
                Chunk chunk = getChunkAt(chunkX, chunkZ);
                if (chunk == null) {
                    continue;
                }

                for (Object te : chunk.tileEntities.values()) {
                    TileEntity tileentity = (TileEntity) te;
                    if ((tileentity.x >= i) && (tileentity.y >= j) && (tileentity.z >= k) && (tileentity.x < l) && (tileentity.y < i1) && (tileentity.z < j1)) {
                        arraylist.add(tileentity);
                    }
                }
            }
        }
        // CraftBukkit end

        return arraylist;
    }

    public boolean a(EntityHuman entityhuman, int i, int j, int k) {
        return !this.server.a(this, i, j, k, entityhuman);
    }

    protected void a(WorldSettings worldsettings) {
        if (this.entitiesById == null) {
            this.entitiesById = new IntHashMap();
        }

        if (this.M == null) {
            this.M = new HashSet();
        }

        if (this.N == null) {
            this.N = new TreeSet();
        }

        this.b(worldsettings);
        super.a(worldsettings);
    }

    protected void b(WorldSettings worldsettings) {
        if (!this.worldProvider.e()) {
            this.worldData.setSpawn(0, this.worldProvider.getSeaLevel(), 0);
        } else {
            this.isLoading = true;
            WorldChunkManager worldchunkmanager = this.worldProvider.e;
            List list = worldchunkmanager.a();
            Random random = new Random(this.getSeed());
            ChunkPosition chunkposition = worldchunkmanager.a(0, 0, 256, list, random);
            int i = 0;
            int j = this.worldProvider.getSeaLevel();
            int k = 0;

            // CraftBukkit start
            if (this.generator != null) {
                Random rand = new Random(this.getSeed());
                org.bukkit.Location spawn = this.generator.getFixedSpawnLocation(((WorldServer) this).getWorld(), rand);

                if (spawn != null) {
                    if (spawn.getWorld() != ((WorldServer) this).getWorld()) {
                        throw new IllegalStateException("Cannot set spawn point for " + this.worldData.getName() + " to be in another world (" + spawn.getWorld().getName() + ")");
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
                a.warn("Unable to find spawn biome");
            }

            int l = 0;

            while (!this.canSpawn(i, k)) { // CraftBukkit - use our own canSpawn
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                ++l;
                if (l == 1000) {
                    break;
                }
            }

            this.worldData.setSpawn(i, j, k);
            this.isLoading = false;
            if (worldsettings.c()) {
                this.k();
            }
        }
    }

    protected void k() {
        WorldGenBonusChest worldgenbonuschest = new WorldGenBonusChest(U, 10);

        for (int i = 0; i < 10; ++i) {
            int j = this.worldData.c() + this.random.nextInt(6) - this.random.nextInt(6);
            int k = this.worldData.e() + this.random.nextInt(6) - this.random.nextInt(6);
            int l = this.i(j, k) + 1;

            if (worldgenbonuschest.a(this, this.random, j, l, k)) {
                break;
            }
        }
    }

    public ChunkCoordinates getDimensionSpawn() {
        return this.worldProvider.h();
    }

    public void save(boolean flag, IProgressUpdate iprogressupdate) throws ExceptionWorldConflict { // CraftBukkit - added throws
        if (this.chunkProvider.canSave()) {
            if (iprogressupdate != null) {
                iprogressupdate.a("Saving level");
            }

            this.a();
            if (iprogressupdate != null) {
                iprogressupdate.c("Saving chunks");
            }

            this.chunkProvider.saveChunks(flag, iprogressupdate);
        }
    }

    public void flushSave() {
        if (this.chunkProvider.canSave()) {
            this.chunkProvider.b();
        }
    }

    protected void a() throws ExceptionWorldConflict { // CraftBukkit - added throws
        this.F();
        this.dataManager.saveWorldData(this.worldData, this.server.getPlayerList().q());
        // CraftBukkit start - save worldMaps once, rather than once per shared world
        if (!(this instanceof SecondaryWorldServer)) {
            this.worldMaps.a();
        }
        // CraftBukkit end
    }

    protected void a(Entity entity) {
        super.a(entity);
        this.entitiesById.a(entity.getId(), entity);
        Entity[] aentity = entity.at();

        if (aentity != null) {
            for (int i = 0; i < aentity.length; ++i) {
                this.entitiesById.a(aentity[i].getId(), aentity[i]);
            }
        }
    }

    protected void b(Entity entity) {
        super.b(entity);
        this.entitiesById.d(entity.getId());
        Entity[] aentity = entity.at();

        if (aentity != null) {
            for (int i = 0; i < aentity.length; ++i) {
                this.entitiesById.d(aentity[i].getId());
            }
        }
    }

    public Entity getEntity(int i) {
        return (Entity) this.entitiesById.get(i);
    }

    public boolean strikeLightning(Entity entity) {
        // CraftBukkit start
        LightningStrikeEvent lightning = new LightningStrikeEvent(this.getWorld(), (org.bukkit.entity.LightningStrike) entity.getBukkitEntity());
        this.getServer().getPluginManager().callEvent(lightning);

        if (lightning.isCancelled()) {
            return false;
        }

        if (super.strikeLightning(entity)) {
            this.server.getPlayerList().sendPacketNearby(entity.locX, entity.locY, entity.locZ, 512.0D, this.dimension, new PacketPlayOutSpawnEntityWeather(entity));
            // CraftBukkit end
            return true;
        } else {
            return false;
        }
    }

    public void broadcastEntityEffect(Entity entity, byte b0) {
        this.getTracker().sendPacketToEntity(entity, new PacketPlayOutEntityStatus(entity, b0));
    }

    public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        // CraftBukkit start
        Explosion explosion = super.createExplosion(entity, d0, d1, d2, f, flag, flag1);

        if (explosion.wasCanceled) {
            return explosion;
        }

        /* Remove
        explosion.a = flag;
        explosion.b = flag1;
        explosion.a();
        explosion.a(false);
        */
        // CraftBukkit end - TODO: Check if explosions are still properly implemented

        if (!flag1) {
            explosion.blocks.clear();
        }

        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (entityhuman.e(d0, d1, d2) < 4096.0D) {
                ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutExplosion(d0, d1, d2, f, explosion.blocks, (Vec3D) explosion.b().get(entityhuman)));
            }
        }

        return explosion;
    }

    public void playNote(int i, int j, int k, Block block, int l, int i1) {
        NoteBlockData noteblockdata = new NoteBlockData(i, j, k, block, l, i1);
        Iterator iterator = this.S[this.T].iterator();

        NoteBlockData noteblockdata1;

        do {
            if (!iterator.hasNext()) {
                this.S[this.T].add(noteblockdata);
                return;
            }

            noteblockdata1 = (NoteBlockData) iterator.next();
        } while (!noteblockdata1.equals(noteblockdata));

    }

    private void Z() {
        while (!this.S[this.T].isEmpty()) {
            int i = this.T;

            this.T ^= 1;
            Iterator iterator = this.S[i].iterator();

            while (iterator.hasNext()) {
                NoteBlockData noteblockdata = (NoteBlockData) iterator.next();

                if (this.a(noteblockdata)) {
                    // CraftBukkit - this.worldProvider.dimension -> this.dimension
                    this.server.getPlayerList().sendPacketNearby((double) noteblockdata.a(), (double) noteblockdata.b(), (double) noteblockdata.c(), 64.0D, this.dimension, new PacketPlayOutBlockAction(noteblockdata.a(), noteblockdata.b(), noteblockdata.c(), noteblockdata.f(), noteblockdata.d(), noteblockdata.e()));
                }
            }

            this.S[i].clear();
        }
    }

    private boolean a(NoteBlockData noteblockdata) {
        Block block = this.getType(noteblockdata.a(), noteblockdata.b(), noteblockdata.c());

        return block == noteblockdata.f() ? block.a(this, noteblockdata.a(), noteblockdata.b(), noteblockdata.c(), noteblockdata.d(), noteblockdata.e()) : false;
    }

    public void saveLevel() {
        this.dataManager.a();
    }

    protected void o() {
        boolean flag = this.P();

        super.o();
        /* CraftBukkit start
        if (this.m != this.n) {
            this.server.getPlayerList().a(new PacketPlayOutGameStateChange(7, this.n), this.worldProvider.dimension);
        }

        if (this.o != this.p) {
            this.server.getPlayerList().a(new PacketPlayOutGameStateChange(8, this.p), this.worldProvider.dimension);
        }

        if (flag != this.P()) {
            if (flag) {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(2, 0.0F));
            } else {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(1, 0.0F));
            }

            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(7, this.n));
            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(8, this.p));
        }
        // */
        if (flag != this.P()) {
            // Only send weather packets to those affected
            for (int i = 0; i < this.players.size(); ++i) {
                if (((EntityPlayer) this.players.get(i)).world == this) {
                    ((EntityPlayer) this.players.get(i)).setPlayerWeather((!flag ? WeatherType.DOWNFALL : WeatherType.CLEAR), false);
                }
            }
            // CraftBukkit end
        }
    }

    public MinecraftServer getMinecraftServer() {
        return this.server;
    }

    public EntityTracker getTracker() {
        return this.tracker;
    }

    public PlayerChunkMap getPlayerChunkMap() {
        return this.manager;
    }

    public PortalTravelAgent t() {
        return this.Q;
    }

    public void a(String s, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6) {
        PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(s, (float) d0, (float) d1, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, i);

        for (int j = 0; j < this.players.size(); ++j) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(j);
            ChunkCoordinates chunkcoordinates = entityplayer.getChunkCoordinates();
            double d7 = d0 - (double) chunkcoordinates.x;
            double d8 = d1 - (double) chunkcoordinates.y;
            double d9 = d2 - (double) chunkcoordinates.z;
            double d10 = d7 * d7 + d8 * d8 + d9 * d9;

            if (d10 <= 256.0D) {
                entityplayer.playerConnection.sendPacket(packetplayoutworldparticles);
            }
        }
    }

    // CraftBukkit start - Compatibility methods for BlockChangeDelegate
    public boolean setRawTypeId(int x, int y, int z, int typeId) {
        return this.setTypeAndData(x, y, z, Block.e(typeId), 0, 4);
    }

    public boolean setRawTypeIdAndData(int x, int y, int z, int typeId, int data) {
        return this.setTypeAndData(x, y, z, Block.e(typeId), data, 4);
    }

    public boolean setTypeId(int x, int y, int z, int typeId) {
        return this.setTypeAndData(x, y, z, Block.e(typeId), 0, 3);
    }

    public boolean setTypeIdAndData(int x, int y, int z, int typeId, int data) {
        return this.setTypeAndData(x, y, z, Block.e(typeId), data, 3);
    }

    public int getTypeId(int x, int y, int z) {
        return Block.b(getType(x, y, z));
    }
    // CraftBukkit end
}
