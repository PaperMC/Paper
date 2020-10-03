package net.minecraft.server;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.WorldUUID;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.TimeSkipEvent;
// CraftBukkit end

public class WorldServer extends World implements GeneratorAccessSeed {

    public static final BlockPosition a = new BlockPosition(100, 50, 0);
    private static final Logger LOGGER = LogManager.getLogger();
    public final Int2ObjectMap<Entity> entitiesById = new Int2ObjectLinkedOpenHashMap();
    private final Map<UUID, Entity> entitiesByUUID = Maps.newHashMap();
    private final Queue<Entity> entitiesToAdd = Queues.newArrayDeque();
    private final List<EntityPlayer> players = Lists.newArrayList();
    private final ChunkProviderServer chunkProvider;
    boolean tickingEntities;
    private final MinecraftServer server;
    public final WorldDataServer worldDataServer; // CraftBukkit - type
    public boolean savingDisabled;
    private boolean everyoneSleeping;
    private int emptyTime;
    private final PortalTravelAgent portalTravelAgent;
    private final TickListServer<Block> nextTickListBlock;
    private final TickListServer<FluidType> nextTickListFluid;
    private final Set<NavigationAbstract> navigators;
    protected final PersistentRaid persistentRaid;
    private final ObjectLinkedOpenHashSet<BlockActionData> L;
    private boolean ticking;
    private final List<MobSpawner> mobSpawners;
    @Nullable
    private final EnderDragonBattle dragonBattle;
    private final StructureManager structureManager;
    private final boolean Q;


    // CraftBukkit start
    private int tickPosition;
    public final Convertable.ConversionSession convertable;
    public final UUID uuid;

    public Chunk getChunkIfLoaded(int x, int z) {
        return this.chunkProvider.getChunkAt(x, z, false);
    }

    // Add env and gen to constructor, WorldData -> WorldDataServer
    public WorldServer(MinecraftServer minecraftserver, Executor executor, Convertable.ConversionSession convertable_conversionsession, IWorldDataServer iworlddataserver, ResourceKey<World> resourcekey, DimensionManager dimensionmanager, WorldLoadListener worldloadlistener, ChunkGenerator chunkgenerator, boolean flag, long i, List<MobSpawner> list, boolean flag1, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(iworlddataserver, resourcekey, dimensionmanager, minecraftserver::getMethodProfiler, false, flag, i, gen, env);
        this.pvpMode = minecraftserver.getPVP();
        convertable = convertable_conversionsession;
        uuid = WorldUUID.getUUID(convertable_conversionsession.folder.toFile());
        // CraftBukkit end
        this.nextTickListBlock = new TickListServer<>(this, (block) -> {
            return block == null || block.getBlockData().isAir();
        }, IRegistry.BLOCK::getKey, this::b);
        this.nextTickListFluid = new TickListServer<>(this, (fluidtype) -> {
            return fluidtype == null || fluidtype == FluidTypes.EMPTY;
        }, IRegistry.FLUID::getKey, this::a);
        this.navigators = Sets.newHashSet();
        this.L = new ObjectLinkedOpenHashSet();
        this.Q = flag1;
        this.server = minecraftserver;
        this.mobSpawners = list;
        // CraftBukkit start
        this.worldDataServer = (WorldDataServer) iworlddataserver;
        worldDataServer.world = this;
        if (gen != null) {
            chunkgenerator = new org.bukkit.craftbukkit.generator.CustomChunkGenerator(this, chunkgenerator, gen);
        }

        this.chunkProvider = new ChunkProviderServer(this, convertable_conversionsession, minecraftserver.getDataFixer(), minecraftserver.getDefinedStructureManager(), executor, chunkgenerator, minecraftserver.getPlayerList().getViewDistance(), minecraftserver.isSyncChunkWrites(), worldloadlistener, () -> {
            return minecraftserver.E().getWorldPersistentData();
        });
        // CraftBukkit end
        this.portalTravelAgent = new PortalTravelAgent(this);
        this.P();
        this.Q();
        this.getWorldBorder().a(minecraftserver.at());
        this.persistentRaid = (PersistentRaid) this.getWorldPersistentData().a(() -> {
            return new PersistentRaid(this);
        }, PersistentRaid.a(this.getDimensionManager()));
        if (!minecraftserver.isEmbeddedServer()) {
            iworlddataserver.setGameType(minecraftserver.getGamemode());
        }

        this.structureManager = new StructureManager(this, this.worldDataServer.getGeneratorSettings()); // CraftBukkit
        if (this.getDimensionManager().isCreateDragonBattle()) {
            this.dragonBattle = new EnderDragonBattle(this, this.worldDataServer.getGeneratorSettings().getSeed(), this.worldDataServer.C()); // CraftBukkit
        } else {
            this.dragonBattle = null;
        }
        this.getServer().addWorld(this.getWorld()); // CraftBukkit
    }

    // CraftBukkit start
    @Override
    protected TileEntity getTileEntity(BlockPosition pos, boolean validate) {
        TileEntity result = super.getTileEntity(pos, validate);
        if (!validate || Thread.currentThread() != this.serverThread) {
            // SPIGOT-5378: avoid deadlock, this can be called in loading logic (i.e lighting) but getType() will block on chunk load
            return result;
        }
        Block type = getType(pos).getBlock();

        if (result != null && type != Blocks.AIR) {
            if (!result.getTileType().isValidBlock(type)) {
                result = fixTileEntity(pos, type, result);
            }
        }

        return result;
    }

    private TileEntity fixTileEntity(BlockPosition pos, Block type, TileEntity found) {
        this.getServer().getLogger().log(Level.SEVERE, "Block at {0}, {1}, {2} is {3} but has {4}" + ". "
                + "Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.", new Object[]{pos.getX(), pos.getY(), pos.getZ(), type, found});

        if (type instanceof ITileEntity) {
            TileEntity replacement = ((ITileEntity) type).createTile(this);
            replacement.world = this;
            this.setTileEntity(pos, replacement);
            return replacement;
        } else {
            return found;
        }
    }
    // CraftBukkit end

    public void a(int i, int j, boolean flag, boolean flag1) {
        this.worldDataServer.a(i);
        this.worldDataServer.setWeatherDuration(j);
        this.worldDataServer.setThunderDuration(j);
        this.worldDataServer.setStorm(flag);
        this.worldDataServer.setThundering(flag1);
    }

    @Override
    public BiomeBase a(int i, int j, int k) {
        return this.getChunkProvider().getChunkGenerator().getWorldChunkManager().getBiome(i, j, k);
    }

    public StructureManager getStructureManager() {
        return this.structureManager;
    }

    public void doTick(BooleanSupplier booleansupplier) {
        GameProfilerFiller gameprofilerfiller = this.getMethodProfiler();

        this.ticking = true;
        gameprofilerfiller.enter("world border");
        this.getWorldBorder().s();
        gameprofilerfiller.exitEnter("weather");
        boolean flag = this.isRaining();

        if (this.getDimensionManager().hasSkyLight()) {
            if (this.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
                int i = this.worldDataServer.h();
                int j = this.worldDataServer.getThunderDuration();
                int k = this.worldDataServer.getWeatherDuration();
                boolean flag1 = this.worldData.isThundering();
                boolean flag2 = this.worldData.hasStorm();

                if (i > 0) {
                    --i;
                    j = flag1 ? 0 : 1;
                    k = flag2 ? 0 : 1;
                    flag1 = false;
                    flag2 = false;
                } else {
                    if (j > 0) {
                        --j;
                        if (j == 0) {
                            flag1 = !flag1;
                        }
                    } else if (flag1) {
                        j = this.random.nextInt(12000) + 3600;
                    } else {
                        j = this.random.nextInt(168000) + 12000;
                    }

                    if (k > 0) {
                        --k;
                        if (k == 0) {
                            flag2 = !flag2;
                        }
                    } else if (flag2) {
                        k = this.random.nextInt(12000) + 12000;
                    } else {
                        k = this.random.nextInt(168000) + 12000;
                    }
                }

                this.worldDataServer.setThunderDuration(j);
                this.worldDataServer.setWeatherDuration(k);
                this.worldDataServer.a(i);
                this.worldDataServer.setThundering(flag1);
                this.worldDataServer.setStorm(flag2);
            }

            this.lastThunderLevel = this.thunderLevel;
            if (this.worldData.isThundering()) {
                this.thunderLevel = (float) ((double) this.thunderLevel + 0.01D);
            } else {
                this.thunderLevel = (float) ((double) this.thunderLevel - 0.01D);
            }

            this.thunderLevel = MathHelper.a(this.thunderLevel, 0.0F, 1.0F);
            this.lastRainLevel = this.rainLevel;
            if (this.worldData.hasStorm()) {
                this.rainLevel = (float) ((double) this.rainLevel + 0.01D);
            } else {
                this.rainLevel = (float) ((double) this.rainLevel - 0.01D);
            }

            this.rainLevel = MathHelper.a(this.rainLevel, 0.0F, 1.0F);
        }

        /* CraftBukkit start
        if (this.lastRainLevel != this.rainLevel) {
            this.server.getPlayerList().a((Packet) (new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.h, this.rainLevel)), this.getDimensionKey());
        }

        if (this.lastThunderLevel != this.thunderLevel) {
            this.server.getPlayerList().a((Packet) (new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.i, this.thunderLevel)), this.getDimensionKey());
        }

        if (flag != this.isRaining()) {
            if (flag) {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.c, 0.0F));
            } else {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.b, 0.0F));
            }

            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.h, this.rainLevel));
            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.i, this.thunderLevel));
        }
        // */
        for (int idx = 0; idx < this.players.size(); ++idx) {
            if (((EntityPlayer) this.players.get(idx)).world == this) {
                ((EntityPlayer) this.players.get(idx)).tickWeather();
            }
        }

        if (flag != this.isRaining()) {
            // Only send weather packets to those affected
            for (int idx = 0; idx < this.players.size(); ++idx) {
                if (((EntityPlayer) this.players.get(idx)).world == this) {
                    ((EntityPlayer) this.players.get(idx)).setPlayerWeather((!flag ? WeatherType.DOWNFALL : WeatherType.CLEAR), false);
                }
            }
        }
        for (int idx = 0; idx < this.players.size(); ++idx) {
            if (((EntityPlayer) this.players.get(idx)).world == this) {
                ((EntityPlayer) this.players.get(idx)).updateWeather(this.lastRainLevel, this.rainLevel, this.lastThunderLevel, this.thunderLevel);
            }
        }
        // CraftBukkit end

        if (this.everyoneSleeping && this.players.stream().noneMatch((entityplayer) -> {
            return !entityplayer.isSpectator() && !entityplayer.isDeeplySleeping() && !entityplayer.fauxSleeping; // CraftBukkit
        })) {
            // CraftBukkit start
            long l = this.worldData.getDayTime() + 24000L;
            TimeSkipEvent event = new TimeSkipEvent(this.getWorld(), TimeSkipEvent.SkipReason.NIGHT_SKIP, (l - l % 24000L) - this.getDayTime());
            if (this.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.setDayTime(this.getDayTime() + event.getSkipAmount());
                }

            }

            if (!event.isCancelled()) {
                this.everyoneSleeping = false;
                this.wakeupPlayers();
            }
            // CraftBukkit end
            if (this.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
                this.clearWeather();
            }
        }

        this.P();
        this.b();
        gameprofilerfiller.exitEnter("chunkSource");
        this.getChunkProvider().tick(booleansupplier);
        gameprofilerfiller.exitEnter("tickPending");
        if (!this.isDebugWorld()) {
            this.nextTickListBlock.b();
            this.nextTickListFluid.b();
        }

        gameprofilerfiller.exitEnter("raid");
        this.persistentRaid.a();
        gameprofilerfiller.exitEnter("blockEvents");
        this.aj();
        this.ticking = false;
        gameprofilerfiller.exitEnter("entities");
        boolean flag3 = true || !this.players.isEmpty() || !this.getForceLoadedChunks().isEmpty(); // CraftBukkit - this prevents entity cleanup, other issues on servers with no players

        if (flag3) {
            this.resetEmptyTime();
        }

        if (flag3 || this.emptyTime++ < 300) {
            if (this.dragonBattle != null) {
                this.dragonBattle.b();
            }

            this.tickingEntities = true;
            ObjectIterator objectiterator = this.entitiesById.int2ObjectEntrySet().iterator();

            while (objectiterator.hasNext()) {
                Entry<Entity> entry = (Entry) objectiterator.next();
                Entity entity = (Entity) entry.getValue();
                Entity entity1 = entity.getVehicle();

                /* CraftBukkit start - We prevent spawning in general, so this butchering is not needed
                if (!this.server.getSpawnAnimals() && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
                    entity.die();
                }

                if (!this.server.getSpawnNPCs() && entity instanceof NPC) {
                    entity.die();
                }
                // CraftBukkit end */

                gameprofilerfiller.enter("checkDespawn");
                if (!entity.dead) {
                    entity.checkDespawn();
                }

                gameprofilerfiller.exit();
                if (entity1 != null) {
                    if (!entity1.dead && entity1.w(entity)) {
                        continue;
                    }

                    entity.stopRiding();
                }

                gameprofilerfiller.enter("tick");
                if (!entity.dead && !(entity instanceof EntityComplexPart)) {
                    this.a(this::entityJoinedWorld, entity);
                }

                gameprofilerfiller.exit();
                gameprofilerfiller.enter("remove");
                if (entity.dead) {
                    this.removeEntityFromChunk(entity);
                    objectiterator.remove();
                    this.unregisterEntity(entity);
                }

                gameprofilerfiller.exit();
            }

            this.tickingEntities = false;

            Entity entity2;

            while ((entity2 = (Entity) this.entitiesToAdd.poll()) != null) {
                this.registerEntity(entity2);
            }

            this.tickBlockEntities();
        }

        gameprofilerfiller.exit();
    }

    protected void b() {
        if (this.Q) {
            long i = this.worldData.getTime() + 1L;

            this.worldDataServer.setTime(i);
            this.worldDataServer.u().a(this.server, i);
            if (this.worldData.q().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                this.setDayTime(this.worldData.getDayTime() + 1L);
            }

        }
    }

    public void setDayTime(long i) {
        this.worldDataServer.setDayTime(i);
    }

    public void doMobSpawning(boolean flag, boolean flag1) {
        Iterator iterator = this.mobSpawners.iterator();

        while (iterator.hasNext()) {
            MobSpawner mobspawner = (MobSpawner) iterator.next();

            mobspawner.a(this, flag, flag1);
        }

    }

    private void wakeupPlayers() {
        (this.players.stream().filter(EntityLiving::isSleeping).collect(Collectors.toList())).forEach((entityplayer) -> { // CraftBukkit - decompile error
            entityplayer.wakeup(false, false);
        });
    }

    public void a(Chunk chunk, int i) {
        ChunkCoordIntPair chunkcoordintpair = chunk.getPos();
        boolean flag = this.isRaining();
        int j = chunkcoordintpair.d();
        int k = chunkcoordintpair.e();
        GameProfilerFiller gameprofilerfiller = this.getMethodProfiler();

        gameprofilerfiller.enter("thunder");
        BlockPosition blockposition;

        if (flag && this.V() && this.random.nextInt(100000) == 0) {
            blockposition = this.a(this.a(j, 0, k, 15));
            if (this.isRainingAt(blockposition)) {
                DifficultyDamageScaler difficultydamagescaler = this.getDamageScaler(blockposition);
                boolean flag1 = this.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && this.random.nextDouble() < (double) difficultydamagescaler.b() * 0.01D;

                if (flag1) {
                    EntityHorseSkeleton entityhorseskeleton = (EntityHorseSkeleton) EntityTypes.SKELETON_HORSE.a((World) this);

                    entityhorseskeleton.t(true);
                    entityhorseskeleton.setAgeRaw(0);
                    entityhorseskeleton.setPosition((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());
                    this.addEntity(entityhorseskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.LIGHTNING); // CraftBukkit
                }

                EntityLightning entitylightning = (EntityLightning) EntityTypes.LIGHTNING_BOLT.a((World) this);

                entitylightning.d(Vec3D.c((BaseBlockPosition) blockposition));
                entitylightning.setEffect(flag1);
                this.strikeLightning(entitylightning, org.bukkit.event.weather.LightningStrikeEvent.Cause.WEATHER); // CraftBukkit
            }
        }

        gameprofilerfiller.exitEnter("iceandsnow");
        if (this.random.nextInt(16) == 0) {
            blockposition = this.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, this.a(j, 0, k, 15));
            BlockPosition blockposition1 = blockposition.down();
            BiomeBase biomebase = this.getBiome(blockposition);

            if (biomebase.a(this, blockposition1)) {
                org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition1, Blocks.ICE.getBlockData(), null); // CraftBukkit
            }

            if (flag && biomebase.b(this, blockposition)) {
                org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition, Blocks.SNOW.getBlockData(), null); // CraftBukkit
            }

            if (flag && this.getBiome(blockposition1).c() == BiomeBase.Precipitation.RAIN) {
                this.getType(blockposition1).getBlock().c((World) this, blockposition1);
            }
        }

        gameprofilerfiller.exitEnter("tickBlocks");
        if (i > 0) {
            ChunkSection[] achunksection = chunk.getSections();
            int l = achunksection.length;

            for (int i1 = 0; i1 < l; ++i1) {
                ChunkSection chunksection = achunksection[i1];

                if (chunksection != Chunk.a && chunksection.d()) {
                    int j1 = chunksection.getYPosition();

                    for (int k1 = 0; k1 < i; ++k1) {
                        BlockPosition blockposition2 = this.a(j, j1, k, 15);

                        gameprofilerfiller.enter("randomTick");
                        IBlockData iblockdata = chunksection.getType(blockposition2.getX() - j, blockposition2.getY() - j1, blockposition2.getZ() - k);

                        if (iblockdata.isTicking()) {
                            iblockdata.b(this, blockposition2, this.random);
                        }

                        Fluid fluid = iblockdata.getFluid();

                        if (fluid.f()) {
                            fluid.b(this, blockposition2, this.random);
                        }

                        gameprofilerfiller.exit();
                    }
                }
            }
        }

        gameprofilerfiller.exit();
    }

    protected BlockPosition a(BlockPosition blockposition) {
        BlockPosition blockposition1 = this.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, blockposition);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockposition1, new BlockPosition(blockposition1.getX(), this.getBuildHeight(), blockposition1.getZ()))).g(3.0D);
        List<EntityLiving> list = this.a(EntityLiving.class, axisalignedbb, (java.util.function.Predicate<EntityLiving>) (entityliving) -> { // CraftBukkit - decompile error
            return entityliving != null && entityliving.isAlive() && this.e(entityliving.getChunkCoordinates());
        });

        if (!list.isEmpty()) {
            return ((EntityLiving) list.get(this.random.nextInt(list.size()))).getChunkCoordinates();
        } else {
            if (blockposition1.getY() == -1) {
                blockposition1 = blockposition1.up(2);
            }

            return blockposition1;
        }
    }

    public boolean m_() {
        return this.ticking;
    }

    public void everyoneSleeping() {
        this.everyoneSleeping = false;
        if (!this.players.isEmpty()) {
            int i = 0;
            int j = 0;
            Iterator iterator = this.players.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                if (entityplayer.isSpectator() || (entityplayer.fauxSleeping && !entityplayer.isSleeping())) { // CraftBukkit
                    ++i;
                } else if (entityplayer.isSleeping()) {
                    ++j;
                }
            }

            this.everyoneSleeping = j > 0 && j >= this.players.size() - i;
        }

    }

    @Override
    public ScoreboardServer getScoreboard() {
        return this.server.getScoreboard();
    }

    private void clearWeather() {
        // CraftBukkit start
        this.worldDataServer.setStorm(false);
        // If we stop due to everyone sleeping we should reset the weather duration to some other random value.
        // Not that everyone ever manages to get the whole server to sleep at the same time....
        if (!this.worldDataServer.hasStorm()) {
            this.worldDataServer.setWeatherDuration(0);
        }
        // CraftBukkit end
        this.worldDataServer.setThundering(false);
        // CraftBukkit start
        // If we stop due to everyone sleeping we should reset the weather duration to some other random value.
        // Not that everyone ever manages to get the whole server to sleep at the same time....
        if (!this.worldDataServer.isThundering()) {
            this.worldDataServer.setThunderDuration(0);
        }
        // CraftBukkit end
    }

    public void resetEmptyTime() {
        this.emptyTime = 0;
    }

    private void a(NextTickListEntry<FluidType> nextticklistentry) {
        Fluid fluid = this.getFluid(nextticklistentry.a);

        if (fluid.getType() == nextticklistentry.b()) {
            fluid.a((World) this, nextticklistentry.a);
        }

    }

    private void b(NextTickListEntry<Block> nextticklistentry) {
        IBlockData iblockdata = this.getType(nextticklistentry.a);

        if (iblockdata.a((Block) nextticklistentry.b())) {
            iblockdata.a(this, nextticklistentry.a, this.random);
        }

    }

    public void entityJoinedWorld(Entity entity) {
        if (!(entity instanceof EntityHuman) && !this.getChunkProvider().a(entity)) {
            this.chunkCheck(entity);
        } else {
            entity.g(entity.locX(), entity.locY(), entity.locZ());
            entity.lastYaw = entity.yaw;
            entity.lastPitch = entity.pitch;
            if (entity.inChunk) {
                ++entity.ticksLived;
                GameProfilerFiller gameprofilerfiller = this.getMethodProfiler();

                gameprofilerfiller.a(() -> {
                    return IRegistry.ENTITY_TYPE.getKey(entity.getEntityType()).toString();
                });
                gameprofilerfiller.c("tickNonPassenger");
                entity.tick();
                entity.postTick(); // CraftBukkit
                gameprofilerfiller.exit();
            }

            this.chunkCheck(entity);
            if (entity.inChunk) {
                Iterator iterator = entity.getPassengers().iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    this.a(entity, entity1);
                }
            }

        }
    }

    public void a(Entity entity, Entity entity1) {
        if (!entity1.dead && entity1.getVehicle() == entity) {
            if (entity1 instanceof EntityHuman || this.getChunkProvider().a(entity1)) {
                entity1.g(entity1.locX(), entity1.locY(), entity1.locZ());
                entity1.lastYaw = entity1.yaw;
                entity1.lastPitch = entity1.pitch;
                if (entity1.inChunk) {
                    ++entity1.ticksLived;
                    GameProfilerFiller gameprofilerfiller = this.getMethodProfiler();

                    gameprofilerfiller.a(() -> {
                        return IRegistry.ENTITY_TYPE.getKey(entity1.getEntityType()).toString();
                    });
                    gameprofilerfiller.c("tickPassenger");
                    entity1.passengerTick();
                    gameprofilerfiller.exit();
                }

                this.chunkCheck(entity1);
                if (entity1.inChunk) {
                    Iterator iterator = entity1.getPassengers().iterator();

                    while (iterator.hasNext()) {
                        Entity entity2 = (Entity) iterator.next();

                        this.a(entity1, entity2);
                    }
                }

            }
        } else {
            entity1.stopRiding();
        }
    }

    public void chunkCheck(Entity entity) {
        if (entity.ck()) {
            this.getMethodProfiler().enter("chunkCheck");
            int i = MathHelper.floor(entity.locX() / 16.0D);
            int j = MathHelper.floor(entity.locY() / 16.0D);
            int k = MathHelper.floor(entity.locZ() / 16.0D);

            if (!entity.inChunk || entity.chunkX != i || entity.chunkY != j || entity.chunkZ != k) {
                if (entity.inChunk && this.isChunkLoaded(entity.chunkX, entity.chunkZ)) {
                    this.getChunkAt(entity.chunkX, entity.chunkZ).a(entity, entity.chunkY);
                }

                if (!entity.cj() && !this.isChunkLoaded(i, k)) {
                    if (entity.inChunk) {
                        WorldServer.LOGGER.warn("Entity {} left loaded chunk area", entity);
                    }

                    entity.inChunk = false;
                } else {
                    this.getChunkAt(i, k).a(entity);
                }
            }

            this.getMethodProfiler().exit();
        }
    }

    @Override
    public boolean a(EntityHuman entityhuman, BlockPosition blockposition) {
        return !this.server.a(this, blockposition, entityhuman) && this.getWorldBorder().a(blockposition);
    }

    public void save(@Nullable IProgressUpdate iprogressupdate, boolean flag, boolean flag1) {
        ChunkProviderServer chunkproviderserver = this.getChunkProvider();

        if (!flag1) {
            org.bukkit.Bukkit.getPluginManager().callEvent(new org.bukkit.event.world.WorldSaveEvent(getWorld())); // CraftBukkit
            if (iprogressupdate != null) {
                iprogressupdate.a(new ChatMessage("menu.savingLevel"));
            }

            this.ai();
            if (iprogressupdate != null) {
                iprogressupdate.c(new ChatMessage("menu.savingChunks"));
            }

            chunkproviderserver.save(flag);
        }

        // CraftBukkit start - moved from MinecraftServer.saveChunks
        WorldServer worldserver1 = this;

        worldDataServer.a(worldserver1.getWorldBorder().t());
        worldDataServer.setCustomBossEvents(this.server.getBossBattleCustomData().save());
        convertable.a(this.server.customRegistry, this.worldDataServer, this.server.getPlayerList().save());
        // CraftBukkit end
    }

    private void ai() {
        if (this.dragonBattle != null) {
            this.worldDataServer.a(this.dragonBattle.a()); // CraftBukkit
        }

        this.getChunkProvider().getWorldPersistentData().a();
    }

    public List<Entity> a(@Nullable EntityTypes<?> entitytypes, Predicate<? super Entity> predicate) {
        List<Entity> list = Lists.newArrayList();
        ChunkProviderServer chunkproviderserver = this.getChunkProvider();
        ObjectIterator objectiterator = this.entitiesById.values().iterator();

        while (objectiterator.hasNext()) {
            Entity entity = (Entity) objectiterator.next();

            if ((entitytypes == null || entity.getEntityType() == entitytypes) && chunkproviderserver.isLoaded(MathHelper.floor(entity.locX()) >> 4, MathHelper.floor(entity.locZ()) >> 4) && predicate.test(entity)) {
                list.add(entity);
            }
        }

        return list;
    }

    public List<EntityEnderDragon> g() {
        List<EntityEnderDragon> list = Lists.newArrayList();
        ObjectIterator objectiterator = this.entitiesById.values().iterator();

        while (objectiterator.hasNext()) {
            Entity entity = (Entity) objectiterator.next();

            if (entity instanceof EntityEnderDragon && entity.isAlive()) {
                list.add((EntityEnderDragon) entity);
            }
        }

        return list;
    }

    public List<EntityPlayer> a(Predicate<? super EntityPlayer> predicate) {
        List<EntityPlayer> list = Lists.newArrayList();
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (predicate.test(entityplayer)) {
                list.add(entityplayer);
            }
        }

        return list;
    }

    @Nullable
    public EntityPlayer q_() {
        List<EntityPlayer> list = this.a(EntityLiving::isAlive);

        return list.isEmpty() ? null : (EntityPlayer) list.get(this.random.nextInt(list.size()));
    }

    @Override
    public boolean addEntity(Entity entity) {
        // CraftBukkit start
        return this.addEntity0(entity, CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    @Override
    public boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return this.addEntity0(entity, reason);
        // CraftBukkit end
    }

    public boolean addEntitySerialized(Entity entity) {
        // CraftBukkit start
        return this.addEntitySerialized(entity, CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public boolean addEntitySerialized(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return this.addEntity0(entity, reason);
        // CraftBukkit end
    }

    public void addEntityTeleport(Entity entity) {
        boolean flag = entity.attachedToPlayer;

        entity.attachedToPlayer = true;
        this.addEntitySerialized(entity);
        entity.attachedToPlayer = flag;
        this.chunkCheck(entity);
    }

    public void addPlayerCommand(EntityPlayer entityplayer) {
        this.addPlayer0(entityplayer);
        this.chunkCheck(entityplayer);
    }

    public void addPlayerPortal(EntityPlayer entityplayer) {
        this.addPlayer0(entityplayer);
        this.chunkCheck(entityplayer);
    }

    public void addPlayerJoin(EntityPlayer entityplayer) {
        this.addPlayer0(entityplayer);
    }

    public void addPlayerRespawn(EntityPlayer entityplayer) {
        this.addPlayer0(entityplayer);
    }

    private void addPlayer0(EntityPlayer entityplayer) {
        Entity entity = (Entity) this.entitiesByUUID.get(entityplayer.getUniqueID());

        if (entity != null) {
            WorldServer.LOGGER.warn("Force-added player with duplicate UUID {}", entityplayer.getUniqueID().toString());
            entity.decouple();
            this.removePlayer((EntityPlayer) entity);
        }

        this.players.add(entityplayer);
        this.everyoneSleeping();
        IChunkAccess ichunkaccess = this.getChunkAt(MathHelper.floor(entityplayer.locX() / 16.0D), MathHelper.floor(entityplayer.locZ() / 16.0D), ChunkStatus.FULL, true);

        if (ichunkaccess instanceof Chunk) {
            ichunkaccess.a((Entity) entityplayer);
        }

        this.registerEntity(entityplayer);
    }

    // CraftBukkit start
    private boolean addEntity0(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (entity.dead) {
            // WorldServer.LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityTypes.getName(entity.getEntityType())); // CraftBukkit
            return false;
        } else if (this.isUUIDTaken(entity)) {
            return false;
        } else {
            if (!CraftEventFactory.doEntityAddEventCalling(this, entity, spawnReason)) {
                return false;
            }
            // CraftBukkit end
            IChunkAccess ichunkaccess = this.getChunkAt(MathHelper.floor(entity.locX() / 16.0D), MathHelper.floor(entity.locZ() / 16.0D), ChunkStatus.FULL, entity.attachedToPlayer);

            if (!(ichunkaccess instanceof Chunk)) {
                return false;
            } else {
                ichunkaccess.a(entity);
                this.registerEntity(entity);
                return true;
            }
        }
    }

    public boolean addEntityChunk(Entity entity) {
        if (this.isUUIDTaken(entity)) {
            return false;
        } else {
            this.registerEntity(entity);
            return true;
        }
    }

    private boolean isUUIDTaken(Entity entity) {
        UUID uuid = entity.getUniqueID();
        Entity entity1 = this.c(uuid);

        if (entity1 == null) {
            return false;
        } else {
            // WorldServer.LOGGER.warn("Trying to add entity with duplicated UUID {}. Existing {}#{}, new: {}#{}", uuid, EntityTypes.getName(entity1.getEntityType()), entity1.getId(), EntityTypes.getName(entity.getEntityType()), entity.getId()); // CraftBukkit
            return true;
        }
    }

    @Nullable
    private Entity c(UUID uuid) {
        Entity entity = (Entity) this.entitiesByUUID.get(uuid);

        if (entity != null) {
            return entity;
        } else {
            if (this.tickingEntities) {
                Iterator iterator = this.entitiesToAdd.iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    if (entity1.getUniqueID().equals(uuid)) {
                        return entity1;
                    }
                }
            }

            return null;
        }
    }

    public boolean addAllEntitiesSafely(Entity entity) {
        // CraftBukkit start
        return this.addAllEntitiesSafely(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public boolean addAllEntitiesSafely(Entity entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason reason) {
        // CraftBukkit end
        if (entity.co().anyMatch(this::isUUIDTaken)) {
            return false;
        } else {
            return this.addAllEntities(entity, reason); // CraftBukkit
        }
    }

    public void unloadChunk(Chunk chunk) {
        this.tileEntityListUnload.addAll(chunk.getTileEntities().values());
        EntitySlice[] aentityslice = chunk.getEntitySlices();
        int i = aentityslice.length;

        for (int j = 0; j < i; ++j) {
            EntitySlice<Entity> entityslice = aentityslice[j];
            Iterator iterator = entityslice.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (!(entity instanceof EntityPlayer)) {
                    if (this.tickingEntities) {
                        throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("Removing entity while ticking!")));
                    }

                    this.entitiesById.remove(entity.getId());
                    this.unregisterEntity(entity);
                }
            }
        }

    }

    public void unregisterEntity(Entity entity) {
        if (entity instanceof EntityEnderDragon) {
            EntityComplexPart[] aentitycomplexpart = ((EntityEnderDragon) entity).eJ();
            int i = aentitycomplexpart.length;

            for (int j = 0; j < i; ++j) {
                EntityComplexPart entitycomplexpart = aentitycomplexpart[j];

                entitycomplexpart.die();
            }
        }

        this.entitiesByUUID.remove(entity.getUniqueID());
        this.getChunkProvider().removeEntity(entity);
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entity;

            this.players.remove(entityplayer);
        }

        this.getScoreboard().a(entity);
        // CraftBukkit start - SPIGOT-5278
        if (entity instanceof EntityDrowned) {
            this.navigators.remove(((EntityDrowned) entity).navigationWater);
            this.navigators.remove(((EntityDrowned) entity).navigationLand);
        } else
        // CraftBukkit end
        if (entity instanceof EntityInsentient) {
            this.navigators.remove(((EntityInsentient) entity).getNavigation());
        }

        entity.valid = false; // CraftBukkit
    }

    private void registerEntity(Entity entity) {
        if (this.tickingEntities) {
            this.entitiesToAdd.add(entity);
        } else {
            this.entitiesById.put(entity.getId(), entity);
            if (entity instanceof EntityEnderDragon) {
                EntityComplexPart[] aentitycomplexpart = ((EntityEnderDragon) entity).eJ();
                int i = aentitycomplexpart.length;

                for (int j = 0; j < i; ++j) {
                    EntityComplexPart entitycomplexpart = aentitycomplexpart[j];

                    this.entitiesById.put(entitycomplexpart.getId(), entitycomplexpart);
                }
            }

            this.entitiesByUUID.put(entity.getUniqueID(), entity);
            this.getChunkProvider().addEntity(entity);
            // CraftBukkit start - SPIGOT-5278
            if (entity instanceof EntityDrowned) {
                this.navigators.add(((EntityDrowned) entity).navigationWater);
                this.navigators.add(((EntityDrowned) entity).navigationLand);
            } else
            // CraftBukkit end
            if (entity instanceof EntityInsentient) {
                this.navigators.add(((EntityInsentient) entity).getNavigation());
            }
            entity.valid = true; // CraftBukkit
        }

    }

    public void removeEntity(Entity entity) {
        if (this.tickingEntities) {
            throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("Removing entity while ticking!")));
        } else {
            this.removeEntityFromChunk(entity);
            this.entitiesById.remove(entity.getId());
            this.unregisterEntity(entity);
        }
    }

    private void removeEntityFromChunk(Entity entity) {
        IChunkAccess ichunkaccess = this.getChunkAt(entity.chunkX, entity.chunkZ, ChunkStatus.FULL, false);

        if (ichunkaccess instanceof Chunk) {
            ((Chunk) ichunkaccess).b(entity);
        }

    }

    public void removePlayer(EntityPlayer entityplayer) {
        entityplayer.die();
        this.removeEntity(entityplayer);
        this.everyoneSleeping();
    }

    // CraftBukkit start
    public boolean strikeLightning(Entity entitylightning) {
        return this.strikeLightning(entitylightning, LightningStrikeEvent.Cause.UNKNOWN);
    }

    public boolean strikeLightning(Entity entitylightning, LightningStrikeEvent.Cause cause) {
        LightningStrikeEvent lightning = new LightningStrikeEvent(this.getWorld(), (org.bukkit.entity.LightningStrike) entitylightning.getBukkitEntity(), cause);
        this.getServer().getPluginManager().callEvent(lightning);

        if (lightning.isCancelled()) {
            return false;
        }

        return this.addEntity(entitylightning);
    }
    // CraftBukkit end

    @Override
    public void a(int i, BlockPosition blockposition, int j) {
        Iterator iterator = this.server.getPlayerList().getPlayers().iterator();

        // CraftBukkit start
        EntityHuman entityhuman = null;
        Entity entity = this.getEntity(i);
        if (entity instanceof EntityHuman) entityhuman = (EntityHuman) entity;
        // CraftBukkit end

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer != null && entityplayer.world == this && entityplayer.getId() != i) {
                double d0 = (double) blockposition.getX() - entityplayer.locX();
                double d1 = (double) blockposition.getY() - entityplayer.locY();
                double d2 = (double) blockposition.getZ() - entityplayer.locZ();

                // CraftBukkit start
                if (entityhuman != null && entityhuman instanceof EntityPlayer && !entityplayer.getBukkitEntity().canSee(((EntityPlayer) entityhuman).getBukkitEntity())) {
                    continue;
                }
                // CraftBukkit end

                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
                    entityplayer.playerConnection.sendPacket(new PacketPlayOutBlockBreakAnimation(i, blockposition, j));
                }
            }
        }

    }

    @Override
    public void playSound(@Nullable EntityHuman entityhuman, double d0, double d1, double d2, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
        this.server.getPlayerList().sendPacketNearby(entityhuman, d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D, this.getDimensionKey(), new PacketPlayOutNamedSoundEffect(soundeffect, soundcategory, d0, d1, d2, f, f1));
    }

    @Override
    public void playSound(@Nullable EntityHuman entityhuman, Entity entity, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
        this.server.getPlayerList().sendPacketNearby(entityhuman, entity.locX(), entity.locY(), entity.locZ(), f > 1.0F ? (double) (16.0F * f) : 16.0D, this.getDimensionKey(), new PacketPlayOutEntitySound(soundeffect, soundcategory, entity, f, f1));
    }

    @Override
    public void b(int i, BlockPosition blockposition, int j) {
        this.server.getPlayerList().sendAll(new PacketPlayOutWorldEvent(i, blockposition, j, true));
    }

    @Override
    public void a(@Nullable EntityHuman entityhuman, int i, BlockPosition blockposition, int j) {
        this.server.getPlayerList().sendPacketNearby(entityhuman, (double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), 64.0D, this.getDimensionKey(), new PacketPlayOutWorldEvent(i, blockposition, j, false));
    }

    @Override
    public void notify(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1, int i) {
        this.getChunkProvider().flagDirty(blockposition);
        VoxelShape voxelshape = iblockdata.getCollisionShape(this, blockposition);
        VoxelShape voxelshape1 = iblockdata1.getCollisionShape(this, blockposition);

        if (VoxelShapes.c(voxelshape, voxelshape1, OperatorBoolean.NOT_SAME)) {
            Iterator iterator = this.navigators.iterator();

            while (iterator.hasNext()) {
                NavigationAbstract navigationabstract = (NavigationAbstract) iterator.next();

                if (!navigationabstract.i()) {
                    navigationabstract.b(blockposition);
                }
            }

        }
    }

    @Override
    public void broadcastEntityEffect(Entity entity, byte b0) {
        this.getChunkProvider().broadcastIncludingSelf(entity, new PacketPlayOutEntityStatus(entity, b0));
    }

    @Override
    public ChunkProviderServer getChunkProvider() {
        return this.chunkProvider;
    }

    @Override
    public Explosion createExplosion(@Nullable Entity entity, @Nullable DamageSource damagesource, @Nullable ExplosionDamageCalculator explosiondamagecalculator, double d0, double d1, double d2, float f, boolean flag, Explosion.Effect explosion_effect) {
        // CraftBukkit start
        Explosion explosion = super.createExplosion(entity, damagesource, explosiondamagecalculator, d0, d1, d2, f, flag, explosion_effect);

        if (explosion.wasCanceled) {
            return explosion;
        }

        /* Remove
        Explosion explosion = new Explosion(this, entity, damagesource, explosiondamagecalculator, d0, d1, d2, f, flag, explosion_effect);

        explosion.a();
        explosion.a(false);
        */
        // CraftBukkit end - TODO: Check if explosions are still properly implemented
        if (explosion_effect == Explosion.Effect.NONE) {
            explosion.clearBlocks();
        }

        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer.h(d0, d1, d2) < 4096.0D) {
                entityplayer.playerConnection.sendPacket(new PacketPlayOutExplosion(d0, d1, d2, f, explosion.getBlocks(), (Vec3D) explosion.c().get(entityplayer)));
            }
        }

        return explosion;
    }

    @Override
    public void playBlockAction(BlockPosition blockposition, Block block, int i, int j) {
        this.L.add(new BlockActionData(blockposition, block, i, j));
    }

    private void aj() {
        while (!this.L.isEmpty()) {
            BlockActionData blockactiondata = (BlockActionData) this.L.removeFirst();

            if (this.a(blockactiondata)) {
                this.server.getPlayerList().sendPacketNearby((EntityHuman) null, (double) blockactiondata.a().getX(), (double) blockactiondata.a().getY(), (double) blockactiondata.a().getZ(), 64.0D, this.getDimensionKey(), new PacketPlayOutBlockAction(blockactiondata.a(), blockactiondata.b(), blockactiondata.c(), blockactiondata.d()));
            }
        }

    }

    private boolean a(BlockActionData blockactiondata) {
        IBlockData iblockdata = this.getType(blockactiondata.a());

        return iblockdata.a(blockactiondata.b()) ? iblockdata.a((World) this, blockactiondata.a(), blockactiondata.c(), blockactiondata.d()) : false;
    }

    @Override
    public TickListServer<Block> getBlockTickList() {
        return this.nextTickListBlock;
    }

    @Override
    public TickListServer<FluidType> getFluidTickList() {
        return this.nextTickListFluid;
    }

    @Nonnull
    @Override
    public MinecraftServer getMinecraftServer() {
        return this.server;
    }

    public PortalTravelAgent getTravelAgent() {
        return this.portalTravelAgent;
    }

    public DefinedStructureManager n() {
        return this.server.getDefinedStructureManager();
    }

    public <T extends ParticleParam> int a(T t0, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6) {
        // CraftBukkit - visibility api support
        return sendParticles(null, t0, d0, d1, d2, i, d3, d4, d5, d6, false);
    }

    public <T extends ParticleParam> int sendParticles(EntityPlayer sender, T t0, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, boolean force) {
        PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(t0, force, d0, d1, d2, (float) d3, (float) d4, (float) d5, (float) d6, i);
        // CraftBukkit end
        int j = 0;

        for (int k = 0; k < this.players.size(); ++k) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(k);
            if (sender != null && !entityplayer.getBukkitEntity().canSee(sender.getBukkitEntity())) continue; // CraftBukkit

            if (this.a(entityplayer, force, d0, d1, d2, packetplayoutworldparticles)) { // CraftBukkit
                ++j;
            }
        }

        return j;
    }

    public <T extends ParticleParam> boolean a(EntityPlayer entityplayer, T t0, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6) {
        Packet<?> packet = new PacketPlayOutWorldParticles(t0, flag, d0, d1, d2, (float) d3, (float) d4, (float) d5, (float) d6, i);

        return this.a(entityplayer, flag, d0, d1, d2, packet);
    }

    private boolean a(EntityPlayer entityplayer, boolean flag, double d0, double d1, double d2, Packet<?> packet) {
        if (entityplayer.getWorldServer() != this) {
            return false;
        } else {
            BlockPosition blockposition = entityplayer.getChunkCoordinates();

            if (blockposition.a((IPosition) (new Vec3D(d0, d1, d2)), flag ? 512.0D : 32.0D)) {
                entityplayer.playerConnection.sendPacket(packet);
                return true;
            } else {
                return false;
            }
        }
    }

    @Nullable
    @Override
    public Entity getEntity(int i) {
        return (Entity) this.entitiesById.get(i);
    }

    @Nullable
    public Entity getEntity(UUID uuid) {
        return (Entity) this.entitiesByUUID.get(uuid);
    }

    @Nullable
    public BlockPosition a(StructureGenerator<?> structuregenerator, BlockPosition blockposition, int i, boolean flag) {
        return !this.worldDataServer.getGeneratorSettings().shouldGenerateMapFeatures() ? null : this.getChunkProvider().getChunkGenerator().findNearestMapFeature(this, structuregenerator, blockposition, i, flag); // CraftBukkit
    }

    @Nullable
    public BlockPosition a(BiomeBase biomebase, BlockPosition blockposition, int i, int j) {
        return this.getChunkProvider().getChunkGenerator().getWorldChunkManager().a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), i, j, (biomebase1) -> {
            return biomebase1 == biomebase;
        }, this.random, true);
    }

    @Override
    public CraftingManager getCraftingManager() {
        return this.server.getCraftingManager();
    }

    @Override
    public ITagRegistry p() {
        return this.server.getTagRegistry();
    }

    @Override
    public boolean isSavingDisabled() {
        return this.savingDisabled;
    }

    @Override
    public IRegistryCustom r() {
        return this.server.getCustomRegistry();
    }

    public WorldPersistentData getWorldPersistentData() {
        return this.getChunkProvider().getWorldPersistentData();
    }

    @Nullable
    @Override
    public WorldMap a(String s) {
        return (WorldMap) this.getMinecraftServer().E().getWorldPersistentData().b(() -> {
            // CraftBukkit start
            // We only get here when the data file exists, but is not a valid map
            WorldMap newMap = new WorldMap(s);
            MapInitializeEvent event = new MapInitializeEvent(newMap.mapView);
            Bukkit.getServer().getPluginManager().callEvent(event);
            return newMap;
            // CraftBukkit end
        }, s);
    }

    @Override
    public void a(WorldMap worldmap) {
        this.getMinecraftServer().E().getWorldPersistentData().a((PersistentBase) worldmap);
    }

    @Override
    public int getWorldMapCount() {
        return ((PersistentIdCounts) this.getMinecraftServer().E().getWorldPersistentData().a(PersistentIdCounts::new, "idcounts")).a();
    }

    public void a(BlockPosition blockposition, float f) {
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(new BlockPosition(this.worldData.a(), 0, this.worldData.c()));

        this.worldData.setSpawn(blockposition, f);
        this.getChunkProvider().removeTicket(TicketType.START, chunkcoordintpair, 11, Unit.INSTANCE);
        this.getChunkProvider().addTicket(TicketType.START, new ChunkCoordIntPair(blockposition), 11, Unit.INSTANCE);
        this.getMinecraftServer().getPlayerList().sendAll(new PacketPlayOutSpawnPosition(blockposition, f));
    }

    public BlockPosition getSpawn() {
        BlockPosition blockposition = new BlockPosition(this.worldData.a(), this.worldData.b(), this.worldData.c());

        if (!this.getWorldBorder().a(blockposition)) {
            blockposition = this.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, new BlockPosition(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
        }

        return blockposition;
    }

    public float v() {
        return this.worldData.d();
    }

    public LongSet getForceLoadedChunks() {
        ForcedChunk forcedchunk = (ForcedChunk) this.getWorldPersistentData().b(ForcedChunk::new, "chunks");

        return (LongSet) (forcedchunk != null ? LongSets.unmodifiable(forcedchunk.a()) : LongSets.EMPTY_SET);
    }

    public boolean setForceLoaded(int i, int j, boolean flag) {
        ForcedChunk forcedchunk = (ForcedChunk) this.getWorldPersistentData().a(ForcedChunk::new, "chunks");
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
        long k = chunkcoordintpair.pair();
        boolean flag1;

        if (flag) {
            flag1 = forcedchunk.a().add(k);
            if (flag1) {
                this.getChunkAt(i, j);
            }
        } else {
            flag1 = forcedchunk.a().remove(k);
        }

        forcedchunk.a(flag1);
        if (flag1) {
            this.getChunkProvider().a(chunkcoordintpair, flag);
        }

        return flag1;
    }

    @Override
    public List<EntityPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public void a(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1) {
        Optional<VillagePlaceType> optional = VillagePlaceType.b(iblockdata);
        Optional<VillagePlaceType> optional1 = VillagePlaceType.b(iblockdata1);

        if (!Objects.equals(optional, optional1)) {
            BlockPosition blockposition1 = blockposition.immutableCopy();

            optional.ifPresent((villageplacetype) -> {
                this.getMinecraftServer().execute(() -> {
                    this.y().a(blockposition1);
                    PacketDebug.b(this, blockposition1);
                });
            });
            optional1.ifPresent((villageplacetype) -> {
                this.getMinecraftServer().execute(() -> {
                    this.y().a(blockposition1, villageplacetype);
                    PacketDebug.a(this, blockposition1);
                });
            });
        }
    }

    public VillagePlace y() {
        return this.getChunkProvider().j();
    }

    public boolean a_(BlockPosition blockposition) {
        return this.a(blockposition, 1);
    }

    public boolean a(SectionPosition sectionposition) {
        return this.a_(sectionposition.q());
    }

    public boolean a(BlockPosition blockposition, int i) {
        return i > 6 ? false : this.b(SectionPosition.a(blockposition)) <= i;
    }

    public int b(SectionPosition sectionposition) {
        return this.y().a(sectionposition);
    }

    public PersistentRaid getPersistentRaid() {
        return this.persistentRaid;
    }

    @Nullable
    public Raid b_(BlockPosition blockposition) {
        return this.persistentRaid.getNearbyRaid(blockposition, 9216);
    }

    public boolean c_(BlockPosition blockposition) {
        return this.b_(blockposition) != null;
    }

    public void a(ReputationEvent reputationevent, Entity entity, ReputationHandler reputationhandler) {
        reputationhandler.a(reputationevent, entity);
    }

    public void a(java.nio.file.Path java_nio_file_path) throws IOException {
        PlayerChunkMap playerchunkmap = this.getChunkProvider().playerChunkMap;
        BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path.resolve("stats.txt"));
        Throwable throwable = null;

        try {
            bufferedwriter.write(String.format("spawning_chunks: %d\n", playerchunkmap.e().b()));
            SpawnerCreature.d spawnercreature_d = this.getChunkProvider().k();

            if (spawnercreature_d != null) {
                ObjectIterator objectiterator = spawnercreature_d.b().object2IntEntrySet().iterator();

                while (objectiterator.hasNext()) {
                    it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<EnumCreatureType> it_unimi_dsi_fastutil_objects_object2intmap_entry = (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry) objectiterator.next();

                    bufferedwriter.write(String.format("spawn_count.%s: %d\n", ((EnumCreatureType) it_unimi_dsi_fastutil_objects_object2intmap_entry.getKey()).b(), it_unimi_dsi_fastutil_objects_object2intmap_entry.getIntValue()));
                }
            }

            bufferedwriter.write(String.format("entities: %d\n", this.entitiesById.size()));
            bufferedwriter.write(String.format("block_entities: %d\n", this.tileEntityList.size()));
            bufferedwriter.write(String.format("block_ticks: %d\n", this.getBlockTickList().a()));
            bufferedwriter.write(String.format("fluid_ticks: %d\n", this.getFluidTickList().a()));
            bufferedwriter.write("distance_manager: " + playerchunkmap.e().c() + "\n");
            bufferedwriter.write(String.format("pending_tasks: %d\n", this.getChunkProvider().f()));
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (bufferedwriter != null) {
                if (throwable != null) {
                    try {
                        bufferedwriter.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                } else {
                    bufferedwriter.close();
                }
            }

        }

        CrashReport crashreport = new CrashReport("Level dump", new Exception("dummy"));

        this.a(crashreport);
        BufferedWriter bufferedwriter1 = Files.newBufferedWriter(java_nio_file_path.resolve("example_crash.txt"));
        Throwable throwable3 = null;

        try {
            bufferedwriter1.write(crashreport.e());
        } catch (Throwable throwable4) {
            throwable3 = throwable4;
            throw throwable4;
        } finally {
            if (bufferedwriter1 != null) {
                if (throwable3 != null) {
                    try {
                        bufferedwriter1.close();
                    } catch (Throwable throwable5) {
                        throwable3.addSuppressed(throwable5);
                    }
                } else {
                    bufferedwriter1.close();
                }
            }

        }

        java.nio.file.Path java_nio_file_path1 = java_nio_file_path.resolve("chunks.csv");
        BufferedWriter bufferedwriter2 = Files.newBufferedWriter(java_nio_file_path1);
        Throwable throwable6 = null;

        try {
            playerchunkmap.a((Writer) bufferedwriter2);
        } catch (Throwable throwable7) {
            throwable6 = throwable7;
            throw throwable7;
        } finally {
            if (bufferedwriter2 != null) {
                if (throwable6 != null) {
                    try {
                        bufferedwriter2.close();
                    } catch (Throwable throwable8) {
                        throwable6.addSuppressed(throwable8);
                    }
                } else {
                    bufferedwriter2.close();
                }
            }

        }

        java.nio.file.Path java_nio_file_path2 = java_nio_file_path.resolve("entities.csv");
        BufferedWriter bufferedwriter3 = Files.newBufferedWriter(java_nio_file_path2);
        Throwable throwable9 = null;

        try {
            a((Writer) bufferedwriter3, (Iterable) this.entitiesById.values());
        } catch (Throwable throwable10) {
            throwable9 = throwable10;
            throw throwable10;
        } finally {
            if (bufferedwriter3 != null) {
                if (throwable9 != null) {
                    try {
                        bufferedwriter3.close();
                    } catch (Throwable throwable11) {
                        throwable9.addSuppressed(throwable11);
                    }
                } else {
                    bufferedwriter3.close();
                }
            }

        }

        java.nio.file.Path java_nio_file_path3 = java_nio_file_path.resolve("block_entities.csv");
        BufferedWriter bufferedwriter4 = Files.newBufferedWriter(java_nio_file_path3);
        Throwable throwable12 = null;

        try {
            this.a((Writer) bufferedwriter4);
        } catch (Throwable throwable13) {
            throwable12 = throwable13;
            throw throwable13;
        } finally {
            if (bufferedwriter4 != null) {
                if (throwable12 != null) {
                    try {
                        bufferedwriter4.close();
                    } catch (Throwable throwable14) {
                        throwable12.addSuppressed(throwable14);
                    }
                } else {
                    bufferedwriter4.close();
                }
            }

        }

    }

    private static void a(Writer writer, Iterable<Entity> iterable) throws IOException {
        CSVWriter csvwriter = CSVWriter.a().a("x").a("y").a("z").a("uuid").a("type").a("alive").a("display_name").a("custom_name").a(writer);
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            IChatBaseComponent ichatbasecomponent = entity.getCustomName();
            IChatBaseComponent ichatbasecomponent1 = entity.getScoreboardDisplayName();

            csvwriter.a(entity.locX(), entity.locY(), entity.locZ(), entity.getUniqueID(), IRegistry.ENTITY_TYPE.getKey(entity.getEntityType()), entity.isAlive(), ichatbasecomponent1.getString(), ichatbasecomponent != null ? ichatbasecomponent.getString() : null);
        }

    }

    private void a(Writer writer) throws IOException {
        CSVWriter csvwriter = CSVWriter.a().a("x").a("y").a("z").a("type").a(writer);
        Iterator iterator = this.tileEntityList.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();
            BlockPosition blockposition = tileentity.getPosition();

            csvwriter.a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), IRegistry.BLOCK_ENTITY_TYPE.getKey(tileentity.getTileType()));
        }

    }

    @VisibleForTesting
    public void a(StructureBoundingBox structureboundingbox) {
        this.L.removeIf((blockactiondata) -> {
            return structureboundingbox.b((BaseBlockPosition) blockactiondata.a());
        });
    }

    @Override
    public void update(BlockPosition blockposition, Block block) {
        if (!this.isDebugWorld()) {
            // CraftBukkit start
            if (populating) {
                return;
            }
            // CraftBukkit end
            this.applyPhysics(blockposition, block);
        }

    }

    public Iterable<Entity> A() {
        return Iterables.unmodifiableIterable(this.entitiesById.values());
    }

    public String toString() {
        return "ServerLevel[" + this.worldDataServer.getName() + "]";
    }

    public boolean isFlatWorld() {
        return this.worldDataServer.getGeneratorSettings().isFlatWorld(); // CraftBukkit
    }

    @Override
    public long getSeed() {
        return this.worldDataServer.getGeneratorSettings().getSeed(); // CraftBukkit
    }

    @Nullable
    public EnderDragonBattle getDragonBattle() {
        return this.dragonBattle;
    }

    @Override
    public Stream<? extends StructureStart<?>> a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator) {
        return this.getStructureManager().a(sectionposition, structuregenerator);
    }

    @Override
    public WorldServer getMinecraftWorld() {
        return this;
    }

    public static void a(WorldServer worldserver) {
        // CraftBukkit start
        WorldServer.a(worldserver, null);
    }

    public static void a(WorldServer worldserver, Entity entity) {
        // CraftBukkit end
        BlockPosition blockposition = WorldServer.a;
        int i = blockposition.getX();
        int j = blockposition.getY() - 2;
        int k = blockposition.getZ();

        // CraftBukkit start
        org.bukkit.craftbukkit.util.BlockStateListPopulator blockList = new org.bukkit.craftbukkit.util.BlockStateListPopulator(worldserver);
        BlockPosition.b(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2).forEach((blockposition1) -> {
            blockList.setTypeAndData(blockposition1, Blocks.AIR.getBlockData(), 3);
        });
        BlockPosition.b(i - 2, j, k - 2, i + 2, j, k + 2).forEach((blockposition1) -> {
            blockList.setTypeAndData(blockposition1, Blocks.OBSIDIAN.getBlockData(), 3);
        });
        org.bukkit.World bworld = worldserver.getWorld();
        org.bukkit.event.world.PortalCreateEvent portalEvent = new org.bukkit.event.world.PortalCreateEvent((List<org.bukkit.block.BlockState>) (List) blockList.getList(), bworld, (entity == null) ? null : entity.getBukkitEntity(), org.bukkit.event.world.PortalCreateEvent.CreateReason.END_PLATFORM);

        worldserver.getServer().getPluginManager().callEvent(portalEvent);
        if (!portalEvent.isCancelled()) {
            blockList.updateList();
        }
        // CraftBukkit end
    }
}
