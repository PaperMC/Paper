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
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.LongHash;

import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldServer extends World implements org.bukkit.BlockChangeDelegate {
    // CraftBukkit end

    private final MinecraftServer server;
    public EntityTracker tracker; // CraftBukkit - private final -> public
    private final PlayerManager manager;
    private Set M;
    private TreeSet N;
    public ChunkProviderServer chunkProviderServer;
    public boolean savingDisabled;
    private boolean O;
    private int emptyTime = 0;
    private NoteDataList[] Q = new NoteDataList[] { new NoteDataList((EmptyClass2) null), new NoteDataList((EmptyClass2) null)};
    private int R = 0;
    private static final StructurePieceTreasure[] S = new StructurePieceTreasure[] { new StructurePieceTreasure(Item.STICK.id, 0, 1, 3, 10), new StructurePieceTreasure(Block.WOOD.id, 0, 1, 3, 10), new StructurePieceTreasure(Block.LOG.id, 0, 1, 3, 10), new StructurePieceTreasure(Item.STONE_AXE.id, 0, 1, 1, 3), new StructurePieceTreasure(Item.WOOD_AXE.id, 0, 1, 1, 5), new StructurePieceTreasure(Item.STONE_PICKAXE.id, 0, 1, 1, 3), new StructurePieceTreasure(Item.WOOD_PICKAXE.id, 0, 1, 1, 5), new StructurePieceTreasure(Item.APPLE.id, 0, 2, 3, 5), new StructurePieceTreasure(Item.BREAD.id, 0, 2, 3, 3)};
    private IntHashMap entitiesById;

    // CraftBukkit start
    public final int dimension;

    public WorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, WorldSettings worldsettings, MethodProfiler methodprofiler, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(idatamanager, s, worldsettings, WorldProvider.byDimension(env.getId()), methodprofiler, gen, env);
        this.dimension = i;
        this.pvpMode = minecraftserver.getPvP();
        // CraftBukkit end
        this.server = minecraftserver;
        this.tracker = new EntityTracker(this);
        this.manager = new PlayerManager(this, minecraftserver.getServerConfigurationManager().o());
        if (this.entitiesById == null) {
            this.entitiesById = new IntHashMap();
        }

        if (this.M == null) {
            this.M = new HashSet();
        }

        if (this.N == null) {
            this.N = new TreeSet();
        }
    }

    // CraftBukkit start
    @Override
    public TileEntity getTileEntity(int i, int j, int k) {
        TileEntity result = super.getTileEntity(i, j, k);
        int type = getTypeId(i, j, k);

        if (type == Block.CHEST.id) {
            if (!(result instanceof TileEntityChest)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Block.FURNACE.id) {
            if (!(result instanceof TileEntityFurnace)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Block.DISPENSER.id) {
            if (!(result instanceof TileEntityDispenser)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Block.JUKEBOX.id) {
            if (!(result instanceof TileEntityRecordPlayer)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Block.NOTE_BLOCK.id) {
            if (!(result instanceof TileEntityNote)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Block.MOB_SPAWNER.id) {
            if (!(result instanceof TileEntityMobSpawner)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if ((type == Block.SIGN_POST.id) || (type == Block.WALL_SIGN.id)) {
            if (!(result instanceof TileEntitySign)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        } else if (type == Block.ENDER_CHEST.id) {
            if (!(result instanceof TileEntityEnderChest)) {
                result = fixTileEntity(i, j, k, type, result);
            }
        }

        return result;
    }

    private TileEntity fixTileEntity(int x, int y, int z, int type, TileEntity found) {
        getServer().getLogger().severe("Block at " + x + "," + y + "," + z + " is " + org.bukkit.Material.getMaterial(type).toString() + " but has " + found + ". "
                + "Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.");

        if (Block.byId[type] instanceof BlockContainer) {
            TileEntity replacement = ((BlockContainer) Block.byId[type]).a(this);
            setTileEntity(x, y, z, replacement);
            return replacement;
        } else {
            getServer().getLogger().severe("Don't know how to fix for this type... Can't do anything! :(");
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
        if (this.getWorldData().isHardcore() && this.difficulty < 3) {
            this.difficulty = 3;
        }

        this.worldProvider.d.b();
        if (this.everyoneDeeplySleeping()) {
            boolean flag = false;

            if (this.allowMonsters && this.difficulty >= 1) {
                ;
            }

            if (!flag) {
                long i = this.worldData.g() + 24000L;

                this.worldData.c(i - i % 24000L);
                this.d();
            }
        }

        this.methodProfiler.a("mobSpawner");
        // CraftBukkit start - Only call spawner if we have players online and the world allows for mobs or animals
        long time = this.worldData.getTime();
        if (this.getGameRules().getBoolean("doMobSpawning") && (this.allowMonsters || this.allowAnimals) && (this instanceof WorldServer && this.players.size() > 0)) {
            SpawnerCreature.spawnEntities(this, this.allowMonsters && (this.ticksPerMonsterSpawns != 0 && time % this.ticksPerMonsterSpawns == 0L), this.allowAnimals && (this.ticksPerAnimalSpawns != 0 && time % this.ticksPerAnimalSpawns == 0L), this.worldData.getTime() % 400L == 0L);
        }
        // CraftBukkit end
        this.methodProfiler.c("chunkSource");
        this.chunkProvider.unloadChunks();
        int j = this.a(1.0F);

        if (j != this.j) {
            this.j = j;
        }

        this.U();
        this.worldData.b(this.worldData.getTime() + 1L);
        this.worldData.c(this.worldData.g() + 1L);
        this.methodProfiler.c("tickPending");
        this.a(false);
        this.methodProfiler.c("tickTiles");
        this.g();
        this.methodProfiler.c("chunkMap");
        this.manager.flush();
        this.methodProfiler.c("village");
        this.villages.tick();
        this.siegeManager.a();
        this.methodProfiler.b();
        this.U();
    }

    public BiomeMeta a(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        List list = this.H().getMobsFor(enumcreaturetype, i, j, k);

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

        this.T();
    }

    private void T() {
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
            int chunkX = LongHash.msw(chunkCoord);
            int chunkZ = LongHash.lsw(chunkCoord);
            // ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator.next();
            int k = chunkX * 16;
            int l = chunkZ * 16;

            this.methodProfiler.a("getChunk");
            Chunk chunk = this.getChunkAt(chunkX, chunkZ);
            // CraftBukkit end

            this.a(k, l, chunk);
            this.methodProfiler.c("tickChunk");
            chunk.k();
            this.methodProfiler.c("thunder");
            int i1;
            int j1;
            int k1;
            int l1;

            if (this.random.nextInt(100000) == 0 && this.M() && this.L()) {
                this.k = this.k * 3 + 1013904223;
                i1 = this.k >> 2;
                j1 = k + (i1 & 15);
                k1 = l + (i1 >> 8 & 15);
                l1 = this.h(j1, k1);
                if (this.B(j1, l1, k1)) {
                    this.strikeLightning(new EntityLightning(this, (double) j1, (double) l1, (double) k1));
                    this.q = 2;
                }
            }

            this.methodProfiler.c("iceandsnow");
            int i2;

            if (this.random.nextInt(16) == 0) {
                this.k = this.k * 3 + 1013904223;
                i1 = this.k >> 2;
                j1 = i1 & 15;
                k1 = i1 >> 8 & 15;
                l1 = this.h(j1 + k, k1 + l);
                if (this.v(j1 + k, l1 - 1, k1 + l)) {
                    // CraftBukkit start
                    BlockState blockState = this.getWorld().getBlockAt(j1 + k, l1 - 1, k1 + l).getState();
                    blockState.setTypeId(Block.ICE.id);

                    BlockFormEvent iceBlockForm = new BlockFormEvent(blockState.getBlock(), blockState);
                    this.getServer().getPluginManager().callEvent(iceBlockForm);
                    if (!iceBlockForm.isCancelled()) {
                        blockState.update(true);
                    }
                    // CraftBukkit end
                }

                if (this.M() && this.w(j1 + k, l1, k1 + l)) {
                    // CraftBukkit start
                    BlockState blockState = this.getWorld().getBlockAt(j1 + k, l1, k1 + l).getState();
                    blockState.setTypeId(Block.SNOW.id);

                    BlockFormEvent snow = new BlockFormEvent(blockState.getBlock(), blockState);
                    this.getServer().getPluginManager().callEvent(snow);
                    if (!snow.isCancelled()) {
                        blockState.update(true);
                    }
                    // CraftBukkit end
                }

                if (this.M()) {
                    BiomeBase biomebase = this.getBiome(j1 + k, k1 + l);

                    if (biomebase.d()) {
                        i2 = this.getTypeId(j1 + k, l1 - 1, k1 + l);
                        if (i2 != 0) {
                            Block.byId[i2].f(this, j1 + k, l1 - 1, k1 + l);
                        }
                    }
                }
            }

            this.methodProfiler.c("tickTiles");
            ChunkSection[] achunksection = chunk.i();

            j1 = achunksection.length;

            for (k1 = 0; k1 < j1; ++k1) {
                ChunkSection chunksection = achunksection[k1];

                if (chunksection != null && chunksection.b()) {
                    for (int j2 = 0; j2 < 3; ++j2) {
                        this.k = this.k * 3 + 1013904223;
                        i2 = this.k >> 2;
                        int k2 = i2 & 15;
                        int l2 = i2 >> 8 & 15;
                        int i3 = i2 >> 16 & 15;
                        int j3 = chunksection.a(k2, i3, l2);

                        ++j;
                        Block block = Block.byId[j3];

                        if (block != null && block.isTicking()) {
                            ++i;
                            block.b(this, k2 + k, i3 + chunksection.d(), l2 + l, this.random);
                        }
                    }
                }
            }

            this.methodProfiler.b();
        }
    }

    public void a(int i, int j, int k, int l, int i1) {
        this.a(i, j, k, l, i1, 0);
    }

    public void a(int i, int j, int k, int l, int i1, int j1) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, l);
        byte b0 = 8;

        if (this.d && l > 0) {
            if (Block.byId[l].l()) {
                if (this.d(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                    int k1 = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                    if (k1 == nextticklistentry.d && k1 > 0) {
                        Block.byId[k1].b(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                    }
                }

                return;
            }

            i1 = 1;
        }

        if (this.d(i - b0, j - b0, k - b0, i + b0, j + b0, k + b0)) {
            if (l > 0) {
                nextticklistentry.a((long) i1 + this.worldData.getTime());
                nextticklistentry.a(j1);
            }

            if (!this.M.contains(nextticklistentry)) {
                this.M.add(nextticklistentry);
                this.N.add(nextticklistentry);
            }
        }
    }

    public void b(int i, int j, int k, int l, int i1) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, l);

        if (l > 0) {
            nextticklistentry.a((long) i1 + this.worldData.getTime());
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
                // CraftBukkit start - if the server has too much to process over time, try to alleviate that
                if (i > 20 * 1000) {
                    i = i / 20;
                } else {
                    i = 1000;
                }
                // CraftBukkit end
            }

            for (int j = 0; j < i; ++j) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) this.N.first();

                if (!flag && nextticklistentry.e > this.worldData.getTime()) {
                    break;
                }

                this.N.remove(nextticklistentry);
                this.M.remove(nextticklistentry);
                byte b0 = 8;

                if (this.d(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                    int k = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);

                    if (k == nextticklistentry.d && k > 0) {
                        Block.byId[k].b(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                    }
                }
            }

            return !this.N.isEmpty();
        }
    }

    public List a(Chunk chunk, boolean flag) {
        ArrayList arraylist = null;
        ChunkCoordIntPair chunkcoordintpair = chunk.l();
        int i = chunkcoordintpair.x << 4;
        int j = i + 16;
        int k = chunkcoordintpair.z << 4;
        int l = k + 16;
        Iterator iterator = this.N.iterator();

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

        return arraylist;
    }

    public void entityJoinedWorld(Entity entity, boolean flag) {
        /* CraftBukkit start - We prevent spawning in general, so this butchering is not needed
        if (!this.server.getSpawnAnimals() && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
            entity.die();
        }
        // CraftBukkit end */
        if (!this.server.getSpawnNPCs() && entity instanceof NPC) {
            entity.die();
        }

        if (!(entity.passenger instanceof EntityHuman)) {
            super.entityJoinedWorld(entity, flag);
        }
    }

    public void vehicleEnteredWorld(Entity entity, boolean flag) {
        super.entityJoinedWorld(entity, flag);
    }

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
        Iterator iterator = this.tileEntityList.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            if (tileentity.x >= i && tileentity.y >= j && tileentity.z >= k && tileentity.x < l && tileentity.y < i1 && tileentity.z < j1) {
                arraylist.add(tileentity);
            }
        }

        return arraylist;
    }

    public boolean a(EntityHuman entityhuman, int i, int j, int k) {
        int l = MathHelper.a(i - this.worldData.c());
        int i1 = MathHelper.a(k - this.worldData.e());

        if (l > i1) {
            i1 = l;
        }

        // CraftBukkit - Configurable spawn protection
        return i1 > this.getServer().getSpawnRadius() || this.server.getServerConfigurationManager().isOp(entityhuman.name) || this.server.I();
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
            WorldChunkManager worldchunkmanager = this.worldProvider.d;
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
                System.out.println("Unable to find spawn biome");
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
        WorldGenBonusChest worldgenbonuschest = new WorldGenBonusChest(S, 10);

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

    protected void a() throws ExceptionWorldConflict { // CraftBukkit - added throws
        this.C();
        this.dataManager.saveWorldData(this.worldData, this.server.getServerConfigurationManager().q());
        this.worldMaps.a();
    }

    protected void a(Entity entity) {
        super.a(entity);
        this.entitiesById.a(entity.id, entity);
        Entity[] aentity = entity.ao();

        if (aentity != null) {
            Entity[] aentity1 = aentity;
            int i = aentity.length;

            for (int j = 0; j < i; ++j) {
                Entity entity1 = aentity1[j];

                this.entitiesById.a(entity1.id, entity1);
            }
        }
    }

    protected void b(Entity entity) {
        super.b(entity);
        this.entitiesById.d(entity.id);
        Entity[] aentity = entity.ao();

        if (aentity != null) {
            Entity[] aentity1 = aentity;
            int i = aentity.length;

            for (int j = 0; j < i; ++j) {
                Entity entity1 = aentity1[j];

                this.entitiesById.d(entity1.id);
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
            this.server.getServerConfigurationManager().sendPacketNearby(entity.locX, entity.locY, entity.locZ, 512.0D, this.dimension, new Packet71Weather(entity));
            // CraftBukkit end
            return true;
        } else {
            return false;
        }
    }

    public void broadcastEntityEffect(Entity entity, byte b0) {
        Packet38EntityStatus packet38entitystatus = new Packet38EntityStatus(entity.id, b0);

        this.getTracker().sendPacketToEntity(entity, packet38entitystatus);
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
                ((EntityPlayer) entityhuman).netServerHandler.sendPacket(new Packet60Explosion(d0, d1, d2, f, explosion.blocks, (Vec3D) explosion.b().get(entityhuman)));
            }
        }

        return explosion;
    }

    public void playNote(int i, int j, int k, int l, int i1, int j1) {
        NoteBlockData noteblockdata = new NoteBlockData(i, j, k, l, i1, j1);
        Iterator iterator = this.Q[this.R].iterator();

        NoteBlockData noteblockdata1;

        do {
            if (!iterator.hasNext()) {
                this.Q[this.R].add(noteblockdata);
                return;
            }

            noteblockdata1 = (NoteBlockData) iterator.next();
        } while (!noteblockdata1.equals(noteblockdata));

    }

    private void U() {
        while (!this.Q[this.R].isEmpty()) {
            int i = this.R;

            this.R ^= 1;
            Iterator iterator = this.Q[i].iterator();

            while (iterator.hasNext()) {
                NoteBlockData noteblockdata = (NoteBlockData) iterator.next();

                if (this.a(noteblockdata)) {
                    // CraftBukkit - this.worldProvider.dimension -> this.dimension
                    this.server.getServerConfigurationManager().sendPacketNearby((double) noteblockdata.a(), (double) noteblockdata.b(), (double) noteblockdata.c(), 64.0D, this.dimension, new Packet54PlayNoteBlock(noteblockdata.a(), noteblockdata.b(), noteblockdata.c(), noteblockdata.f(), noteblockdata.d(), noteblockdata.e()));
                }
            }

            this.Q[i].clear();
        }
    }

    private boolean a(NoteBlockData noteblockdata) {
        int i = this.getTypeId(noteblockdata.a(), noteblockdata.b(), noteblockdata.c());

        if (i == noteblockdata.f()) {
            Block.byId[i].b(this, noteblockdata.a(), noteblockdata.b(), noteblockdata.c(), noteblockdata.d(), noteblockdata.e());
            return true;
        } else {
            return false;
        }
    }

    public void saveLevel() {
        this.dataManager.a();
    }

    protected void n() {
        boolean flag = this.M();

        super.n();
        if (flag != this.M()) {
            // CraftBukkit start - only sending weather packets to those affected
            for (int i = 0; i < this.players.size(); ++i) {
                if (((EntityPlayer) this.players.get(i)).world == this) {
                    ((EntityPlayer) this.players.get(i)).netServerHandler.sendPacket(new Packet70Bed(flag ? 2 : 1, 0));
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

    public PlayerManager getPlayerManager() {
        return this.manager;
    }
}
