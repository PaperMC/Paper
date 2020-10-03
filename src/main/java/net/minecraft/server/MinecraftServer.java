package net.minecraft.server;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.longs.LongIterator;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// CraftBukkit start
import com.google.common.collect.ImmutableSet;
import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.Main;
import org.bukkit.event.server.ServerLoadEvent;
// CraftBukkit end

public abstract class MinecraftServer extends IAsyncTaskHandlerReentrant<TickTask> implements IMojangStatistics, ICommandListener, AutoCloseable {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final File b = new File("usercache.json");
    public static final WorldSettings c = new WorldSettings("Demo World", EnumGamemode.SURVIVAL, false, EnumDifficulty.NORMAL, false, new GameRules(), DataPackConfiguration.a);
    public Convertable.ConversionSession convertable;
    public final WorldNBTStorage worldNBTStorage;
    private final MojangStatisticsGenerator snooper = new MojangStatisticsGenerator("server", this, SystemUtils.getMonotonicMillis());
    private final List<Runnable> tickables = Lists.newArrayList();
    private final GameProfilerSwitcher m;
    private GameProfilerFiller methodProfiler;
    private ServerConnection serverConnection;
    public final WorldLoadListenerFactory worldLoadListenerFactory;
    private final ServerPing serverPing;
    private final Random r;
    public final DataFixer dataConverterManager;
    private String serverIp;
    private int serverPort;
    public final IRegistryCustom.Dimension customRegistry;
    public final Map<ResourceKey<World>, WorldServer> worldServer;
    private PlayerList playerList;
    private volatile boolean isRunning;
    private boolean isStopped;
    private int ticks;
    protected final Proxy proxy;
    private boolean onlineMode;
    private boolean B;
    private boolean pvpMode;
    private boolean allowFlight;
    @Nullable
    private String motd;
    private int F;
    private int G;
    public final long[] h;
    @Nullable
    private KeyPair H;
    @Nullable
    private String I;
    private boolean demoMode;
    private String K;
    private String L;
    private volatile boolean hasTicked;
    private long lastOverloadTime;
    private boolean O;
    private boolean P;
    private final MinecraftSessionService minecraftSessionService;
    private final GameProfileRepository gameProfileRepository;
    private final UserCache userCache;
    private long T;
    public final Thread serverThread;
    private long nextTick;
    private long W;
    private boolean X;
    private final ResourcePackRepository resourcePackRepository;
    private final ScoreboardServer scoreboardServer;
    @Nullable
    private PersistentCommandStorage persistentCommandStorage;
    private final BossBattleCustomData bossBattleCustomData;
    private final CustomFunctionData customFunctionData;
    private final CircularTimer circularTimer;
    private boolean af;
    private float ag;
    public final Executor executorService;
    @Nullable
    private String ai;
    public DataPackResources dataPackResources;
    private final DefinedStructureManager ak;
    protected SaveData saveData;

    // CraftBukkit start
    public DataPackConfiguration datapackconfiguration;
    public org.bukkit.craftbukkit.CraftServer server;
    public OptionSet options;
    public org.bukkit.command.ConsoleCommandSender console;
    public org.bukkit.command.RemoteConsoleCommandSender remoteConsole;
    public ConsoleReader reader;
    public static int currentTick = (int) (System.currentTimeMillis() / 50);
    public java.util.Queue<Runnable> processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
    public int autosavePeriod;
    public CommandDispatcher vanillaCommandDispatcher;
    private boolean forceTicks;
    // CraftBukkit end

    public static <S extends MinecraftServer> S a(Function<Thread, S> function) {
        AtomicReference<S> atomicreference = new AtomicReference();
        Thread thread = new Thread(() -> {
            ((MinecraftServer) atomicreference.get()).w();
        }, "Server thread");

        thread.setUncaughtExceptionHandler((thread1, throwable) -> {
            MinecraftServer.LOGGER.error(throwable);
        });
        S s0 = function.apply(thread); // CraftBukkit - decompile error

        atomicreference.set(s0);
        thread.start();
        return s0;
    }

    public MinecraftServer(OptionSet options, DataPackConfiguration datapackconfiguration, Thread thread, IRegistryCustom.Dimension iregistrycustom_dimension, Convertable.ConversionSession convertable_conversionsession, SaveData savedata, ResourcePackRepository resourcepackrepository, Proxy proxy, DataFixer datafixer, DataPackResources datapackresources, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, UserCache usercache, WorldLoadListenerFactory worldloadlistenerfactory) {
        super("Server");
        this.m = new GameProfilerSwitcher(SystemUtils.a, this::ah);
        this.methodProfiler = GameProfilerDisabled.a;
        this.serverPing = new ServerPing();
        this.r = new Random();
        this.serverPort = -1;
        this.worldServer = Maps.newLinkedHashMap(); // CraftBukkit - keep order, k+v already use identity methods
        this.isRunning = true;
        this.h = new long[100];
        this.K = "";
        this.L = "";
        this.nextTick = SystemUtils.getMonotonicMillis();
        this.scoreboardServer = new ScoreboardServer(this);
        this.bossBattleCustomData = new BossBattleCustomData();
        this.circularTimer = new CircularTimer();
        this.customRegistry = iregistrycustom_dimension;
        this.saveData = savedata;
        this.proxy = proxy;
        this.resourcePackRepository = resourcepackrepository;
        this.dataPackResources = datapackresources;
        this.minecraftSessionService = minecraftsessionservice;
        this.gameProfileRepository = gameprofilerepository;
        this.userCache = usercache;
        this.serverConnection = new ServerConnection(this);
        this.worldLoadListenerFactory = worldloadlistenerfactory;
        this.convertable = convertable_conversionsession;
        this.worldNBTStorage = convertable_conversionsession.b();
        this.dataConverterManager = datafixer;
        this.customFunctionData = new CustomFunctionData(this, datapackresources.a());
        this.ak = new DefinedStructureManager(datapackresources.h(), convertable_conversionsession, datafixer);
        this.serverThread = thread;
        this.executorService = SystemUtils.f();
        // CraftBukkit start
        this.options = options;
        this.datapackconfiguration = datapackconfiguration;
        this.vanillaCommandDispatcher = datapackresources.commandDispatcher; // CraftBukkit
        // Try to see if we're actually running in a terminal, disable jline if not
        if (System.console() == null && System.getProperty("jline.terminal") == null) {
            System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
            Main.useJline = false;
        }

        try {
            reader = new ConsoleReader(System.in, System.out);
            reader.setExpandEvents(false); // Avoid parsing exceptions for uncommonly used event designators
        } catch (Throwable e) {
            try {
                // Try again with jline disabled for Windows users without C++ 2008 Redistributable
                System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
                System.setProperty("user.language", "en");
                Main.useJline = false;
                reader = new ConsoleReader(System.in, System.out);
                reader.setExpandEvents(false);
            } catch (IOException ex) {
                LOGGER.warn((String) null, ex);
            }
        }
        Runtime.getRuntime().addShutdownHook(new org.bukkit.craftbukkit.util.ServerShutdownThread(this));
    }
    // CraftBukkit end

    private void initializeScoreboards(WorldPersistentData worldpersistentdata) {
        PersistentScoreboard persistentscoreboard = (PersistentScoreboard) worldpersistentdata.a(PersistentScoreboard::new, "scoreboard");

        persistentscoreboard.a((Scoreboard) this.getScoreboard());
        this.getScoreboard().a((Runnable) (new RunnableSaveScoreboard(persistentscoreboard)));
    }

    protected abstract boolean init() throws IOException;

    public static void convertWorld(Convertable.ConversionSession convertable_conversionsession) {
        if (convertable_conversionsession.isConvertable()) {
            MinecraftServer.LOGGER.info("Converting map! {}", convertable_conversionsession.getLevelName()); // CraftBukkit
            convertable_conversionsession.convert(new IProgressUpdate() {
                private long a = SystemUtils.getMonotonicMillis();

                @Override
                public void a(IChatBaseComponent ichatbasecomponent) {}

                @Override
                public void a(int i) {
                    if (SystemUtils.getMonotonicMillis() - this.a >= 1000L) {
                        this.a = SystemUtils.getMonotonicMillis();
                        MinecraftServer.LOGGER.info("Converting... {}%", i);
                    }

                }

                @Override
                public void c(IChatBaseComponent ichatbasecomponent) {}
            });
        }

    }

    protected void loadWorld(String s) {
        int worldCount = 3;

        for (int worldId = 0; worldId < worldCount; ++worldId) {
            WorldServer world;
            WorldDataServer worlddata;
            byte dimension = 0;
            ResourceKey<WorldDimension> dimensionKey = WorldDimension.OVERWORLD;

            if (worldId == 1) {
                if (getAllowNether()) {
                    dimension = -1;
                    dimensionKey = WorldDimension.THE_NETHER;
                } else {
                    continue;
                }
            }

            if (worldId == 2) {
                if (server.getAllowEnd()) {
                    dimension = 1;
                    dimensionKey = WorldDimension.THE_END;
                } else {
                    continue;
                }
            }

            String worldType = org.bukkit.World.Environment.getEnvironment(dimension).toString().toLowerCase();
            String name = (dimension == 0) ? s : s + "_" + worldType;
            Convertable.ConversionSession worldSession;
            if (dimension == 0) {
                worldSession = this.convertable;
            } else {
                String dim = "DIM" + dimension;

                File newWorld = new File(new File(name), dim);
                File oldWorld = new File(new File(s), dim);
                File oldLevelDat = new File(new File(s), "level.dat"); // The data folders exist on first run as they are created in the PersistentCollection constructor above, but the level.dat won't

                if (!newWorld.isDirectory() && oldWorld.isDirectory() && oldLevelDat.isFile()) {
                    MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder required ----");
                    MinecraftServer.LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
                    MinecraftServer.LOGGER.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
                    MinecraftServer.LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

                    if (newWorld.exists()) {
                        MinecraftServer.LOGGER.warn("A file or folder already exists at " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    } else if (newWorld.getParentFile().mkdirs()) {
                        if (oldWorld.renameTo(newWorld)) {
                            MinecraftServer.LOGGER.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                            // Migrate world data too.
                            try {
                                com.google.common.io.Files.copy(oldLevelDat, new File(new File(name), "level.dat"));
                                org.apache.commons.io.FileUtils.copyDirectory(new File(new File(s), "data"), new File(new File(name), "data"));
                            } catch (IOException exception) {
                                MinecraftServer.LOGGER.warn("Unable to migrate world data.");
                            }
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
                        } else {
                            MinecraftServer.LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                        }
                    } else {
                        MinecraftServer.LOGGER.warn("Could not create path for " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    }
                }

                try {
                    worldSession = Convertable.a(server.getWorldContainer().toPath()).c(name, dimensionKey);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                MinecraftServer.convertWorld(worldSession); // Run conversion now
            }

            org.bukkit.generator.ChunkGenerator gen = this.server.getGenerator(name);

            IRegistryCustom.Dimension iregistrycustom_dimension = this.customRegistry;

            RegistryReadOps<NBTBase> registryreadops = RegistryReadOps.a((DynamicOps) DynamicOpsNBT.a, this.dataPackResources.h(), iregistrycustom_dimension);
            worlddata = (WorldDataServer) worldSession.a((DynamicOps) registryreadops, datapackconfiguration);
            if (worlddata == null) {
                WorldSettings worldsettings;
                GeneratorSettings generatorsettings;

                if (this.isDemoMode()) {
                    worldsettings = MinecraftServer.c;
                    generatorsettings = GeneratorSettings.a((IRegistryCustom) iregistrycustom_dimension);
                } else {
                    DedicatedServerProperties dedicatedserverproperties = ((DedicatedServer) this).getDedicatedServerProperties();

                    worldsettings = new WorldSettings(dedicatedserverproperties.levelName, dedicatedserverproperties.gamemode, dedicatedserverproperties.hardcore, dedicatedserverproperties.difficulty, false, new GameRules(), datapackconfiguration);
                    generatorsettings = options.has("bonusChest") ? dedicatedserverproperties.generatorSettings.j() : dedicatedserverproperties.generatorSettings;
                }

                worlddata = new WorldDataServer(worldsettings, generatorsettings, Lifecycle.stable());
            }
            worlddata.checkName(name); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
            if (options.has("forceUpgrade")) {
                net.minecraft.server.Main.convertWorld(worldSession, DataConverterRegistry.a(), options.has("eraseCache"), () -> {
                    return true;
                }, worlddata.getGeneratorSettings().d().d().stream().map((entry) -> {
                    return ResourceKey.a(IRegistry.K, ((ResourceKey) entry.getKey()).a());
                }).collect(ImmutableSet.toImmutableSet()));
            }

            IWorldDataServer iworlddataserver = worlddata;
            GeneratorSettings generatorsettings = worlddata.getGeneratorSettings();
            boolean flag = generatorsettings.isDebugWorld();
            long i = generatorsettings.getSeed();
            long j = BiomeManager.a(i);
            List<MobSpawner> list = ImmutableList.of(new MobSpawnerPhantom(), new MobSpawnerPatrol(), new MobSpawnerCat(), new VillageSiege(), new MobSpawnerTrader(iworlddataserver));
            RegistryMaterials<WorldDimension> registrymaterials = generatorsettings.d();
            WorldDimension worlddimension = (WorldDimension) registrymaterials.a(dimensionKey);
            DimensionManager dimensionmanager;
            ChunkGenerator chunkgenerator;

            if (worlddimension == null) {
                dimensionmanager = (DimensionManager) this.customRegistry.a().d(DimensionManager.OVERWORLD);
                chunkgenerator = GeneratorSettings.a(customRegistry.b(IRegistry.ay), customRegistry.b(IRegistry.ar), (new Random()).nextLong());
            } else {
                dimensionmanager = worlddimension.b();
                chunkgenerator = worlddimension.c();
            }

            ResourceKey<World> worldKey = ResourceKey.a(IRegistry.L, dimensionKey.a());

            if (worldId == 0) {
                this.saveData = worlddata;
                this.saveData.setGameType(((DedicatedServer) this).getDedicatedServerProperties().gamemode); // From DedicatedServer.init

                WorldLoadListener worldloadlistener = this.worldLoadListenerFactory.create(11);

                world = new WorldServer(this, this.executorService, worldSession, iworlddataserver, worldKey, dimensionmanager, worldloadlistener, chunkgenerator, flag, j, list, true, org.bukkit.World.Environment.getEnvironment(dimension), gen);
                WorldPersistentData worldpersistentdata = world.getWorldPersistentData();
                this.initializeScoreboards(worldpersistentdata);
                this.server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(this, world.getScoreboard());
                this.persistentCommandStorage = new PersistentCommandStorage(worldpersistentdata);
            } else {
                WorldLoadListener worldloadlistener = this.worldLoadListenerFactory.create(11);
                world = new WorldServer(this, this.executorService, worldSession, iworlddataserver, worldKey, dimensionmanager, worldloadlistener, chunkgenerator, flag, j, ImmutableList.of(), true, org.bukkit.World.Environment.getEnvironment(dimension), gen);
            }

            worlddata.a(this.getServerModName(), this.getModded().isPresent());
            this.initWorld(world, worlddata, saveData, worlddata.getGeneratorSettings());
            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(world.getWorld()));

            this.worldServer.put(world.getDimensionKey(), world);
            this.getPlayerList().setPlayerFileData(world);

            if (worlddata.getCustomBossEvents() != null) {
                this.getBossBattleCustomData().load(worlddata.getCustomBossEvents());
            }
        }
        this.updateWorldSettings();
        for (WorldServer worldserver : this.getWorlds()) {
            this.loadSpawn(worldserver.getChunkProvider().playerChunkMap.worldLoadListener, worldserver);
            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(worldserver.getWorld()));
        }

        this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
        this.server.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
        this.serverConnection.acceptConnections();
        // CraftBukkit end

    }

    protected void updateWorldSettings() {}

    // CraftBukkit start
    public void initWorld(WorldServer worldserver, IWorldDataServer iworlddataserver, SaveData saveData, GeneratorSettings generatorsettings) {
        boolean flag = generatorsettings.isDebugWorld();
        // CraftBukkit start
        if (worldserver.generator != null) {
            worldserver.getWorld().getPopulators().addAll(worldserver.generator.getDefaultPopulators(worldserver.getWorld()));
        }
        WorldBorder worldborder = worldserver.getWorldBorder();

        worldborder.a(iworlddataserver.r());
        if (!iworlddataserver.p()) {
            try {
                a(worldserver, iworlddataserver, generatorsettings.c(), flag, true);
                iworlddataserver.c(true);
                if (flag) {
                    this.a(this.saveData);
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Exception initializing level");

                try {
                    worldserver.a(crashreport);
                } catch (Throwable throwable1) {
                    ;
                }

                throw new ReportedException(crashreport);
            }

            iworlddataserver.c(true);
        }
    }
    // CraftBukkit end

    private static void a(WorldServer worldserver, IWorldDataServer iworlddataserver, boolean flag, boolean flag1, boolean flag2) {
        ChunkGenerator chunkgenerator = worldserver.getChunkProvider().getChunkGenerator();

        if (!flag2) {
            iworlddataserver.setSpawn(BlockPosition.ZERO.up(chunkgenerator.getSpawnHeight()), 0.0F);
        } else if (flag1) {
            iworlddataserver.setSpawn(BlockPosition.ZERO.up(), 0.0F);
        } else {
            WorldChunkManager worldchunkmanager = chunkgenerator.getWorldChunkManager();
            Random random = new Random(worldserver.getSeed());
            BlockPosition blockposition = worldchunkmanager.a(0, worldserver.getSeaLevel(), 0, 256, (biomebase) -> {
                return biomebase.b().b();
            }, random);
            ChunkCoordIntPair chunkcoordintpair = blockposition == null ? new ChunkCoordIntPair(0, 0) : new ChunkCoordIntPair(blockposition);
            // CraftBukkit start
            if (worldserver.generator != null) {
                Random rand = new Random(worldserver.getSeed());
                org.bukkit.Location spawn = worldserver.generator.getFixedSpawnLocation(worldserver.getWorld(), rand);

                if (spawn != null) {
                    if (spawn.getWorld() != worldserver.getWorld()) {
                        throw new IllegalStateException("Cannot set spawn point for " + iworlddataserver.getName() + " to be in another world (" + spawn.getWorld().getName() + ")");
                    } else {
                        iworlddataserver.setSpawn(new BlockPosition(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()), spawn.getYaw());
                        return;
                    }
                }
            }
            // CraftBukkit end

            if (blockposition == null) {
                MinecraftServer.LOGGER.warn("Unable to find spawn biome");
            }

            boolean flag3 = false;
            Iterator iterator = TagsBlock.VALID_SPAWN.getTagged().iterator();

            while (iterator.hasNext()) {
                Block block = (Block) iterator.next();

                if (worldchunkmanager.c().contains(block.getBlockData())) {
                    flag3 = true;
                    break;
                }
            }

            iworlddataserver.setSpawn(chunkcoordintpair.l().b(8, chunkgenerator.getSpawnHeight(), 8), 0.0F);
            int i = 0;
            int j = 0;
            int k = 0;
            int l = -1;
            boolean flag4 = true;

            for (int i1 = 0; i1 < 1024; ++i1) {
                if (i > -16 && i <= 16 && j > -16 && j <= 16) {
                    BlockPosition blockposition1 = WorldProviderNormal.a(worldserver, new ChunkCoordIntPair(chunkcoordintpair.x + i, chunkcoordintpair.z + j), flag3);

                    if (blockposition1 != null) {
                        iworlddataserver.setSpawn(blockposition1, 0.0F);
                        break;
                    }
                }

                if (i == j || i < 0 && i == -j || i > 0 && i == 1 - j) {
                    int j1 = k;

                    k = -l;
                    l = j1;
                }

                i += k;
                j += l;
            }

            if (flag) {
                WorldGenFeatureConfigured<?, ?> worldgenfeatureconfigured = BiomeDecoratorGroups.BONUS_CHEST;

                worldgenfeatureconfigured.a(worldserver, chunkgenerator, worldserver.random, new BlockPosition(iworlddataserver.a(), iworlddataserver.b(), iworlddataserver.c()));
            }

        }
    }

    private void a(SaveData savedata) {
        savedata.setDifficulty(EnumDifficulty.PEACEFUL);
        savedata.d(true);
        IWorldDataServer iworlddataserver = savedata.H();

        iworlddataserver.setStorm(false);
        iworlddataserver.setThundering(false);
        iworlddataserver.a(1000000000);
        iworlddataserver.setDayTime(6000L);
        iworlddataserver.setGameType(EnumGamemode.SPECTATOR);
    }

    // CraftBukkit start
    public void loadSpawn(WorldLoadListener worldloadlistener, WorldServer worldserver) {
        if (!worldserver.getWorld().getKeepSpawnInMemory()) {
            return;
        }

        // WorldServer worldserver = this.E();
        this.forceTicks = true;
        // CraftBukkit end

        MinecraftServer.LOGGER.info("Preparing start region for dimension {}", worldserver.getDimensionKey().a());
        BlockPosition blockposition = worldserver.getSpawn();

        worldloadlistener.a(new ChunkCoordIntPair(blockposition));
        ChunkProviderServer chunkproviderserver = worldserver.getChunkProvider();

        chunkproviderserver.getLightEngine().a(500);
        this.nextTick = SystemUtils.getMonotonicMillis();
        chunkproviderserver.addTicket(TicketType.START, new ChunkCoordIntPair(blockposition), 11, Unit.INSTANCE);

        while (chunkproviderserver.b() != 441) {
            // CraftBukkit start
            // this.nextTick = SystemUtils.getMonotonicMillis() + 10L;
            this.executeModerately();
            // CraftBukkit end
        }

        // CraftBukkit start
        // this.nextTick = SystemUtils.getMonotonicMillis() + 10L;
        this.executeModerately();
        // Iterator iterator = this.worldServer.values().iterator();

        if (true) {
            WorldServer worldserver1 = worldserver;
            ForcedChunk forcedchunk = (ForcedChunk) worldserver.getWorldPersistentData().b(ForcedChunk::new, "chunks");
            // CraftBukkit end

            if (forcedchunk != null) {
                LongIterator longiterator = forcedchunk.a().iterator();

                while (longiterator.hasNext()) {
                    long i = longiterator.nextLong();
                    ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i);

                    worldserver1.getChunkProvider().a(chunkcoordintpair, true);
                }
            }
        }

        // CraftBukkit start
        // this.nextTick = SystemUtils.getMonotonicMillis() + 10L;
        this.executeModerately();
        // CraftBukkit end
        worldloadlistener.b();
        chunkproviderserver.getLightEngine().a(5);
        this.bb();

        // CraftBukkit start
        this.forceTicks = false;
        // CraftBukkit end
    }

    protected void loadResourcesZip() {
        File file = this.convertable.getWorldFolder(SavedFile.RESOURCES_ZIP).toFile();

        if (file.isFile()) {
            String s = this.convertable.getLevelName();

            try {
                this.setResourcePack("level://" + URLEncoder.encode(s, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                MinecraftServer.LOGGER.warn("Something went wrong url encoding {}", s);
            }
        }

    }

    public EnumGamemode getGamemode() {
        return this.saveData.getGameType();
    }

    public boolean isHardcore() {
        return this.saveData.isHardcore();
    }

    public abstract int g();

    public abstract int h();

    public abstract boolean i();

    public boolean saveChunks(boolean flag, boolean flag1, boolean flag2) {
        boolean flag3 = false;

        for (Iterator iterator = this.getWorlds().iterator(); iterator.hasNext(); flag3 = true) {
            WorldServer worldserver = (WorldServer) iterator.next();

            if (!flag) {
                MinecraftServer.LOGGER.info("Saving chunks for level '{}'/{}", worldserver, worldserver.getDimensionKey().a());
            }

            worldserver.save((IProgressUpdate) null, flag1, worldserver.savingDisabled && !flag2);
        }

        // CraftBukkit start - moved to WorldServer.save
        /*
        WorldServer worldserver1 = this.E();
        IWorldDataServer iworlddataserver = this.saveData.H();

        iworlddataserver.a(worldserver1.getWorldBorder().t());
        this.saveData.setCustomBossEvents(this.getBossBattleCustomData().save());
        this.convertable.a(this.customRegistry, this.saveData, this.getPlayerList().save());
        */
        // CraftBukkit end
        return flag3;
    }

    @Override
    public void close() {
        this.stop();
    }

    // CraftBukkit start
    private boolean hasStopped = false;
    private final Object stopLock = new Object();
    public final boolean hasStopped() {
        synchronized (stopLock) {
            return hasStopped;
        }
    }
    // CraftBukkit end

    protected void stop() {
        // CraftBukkit start - prevent double stopping on multiple threads
        synchronized(stopLock) {
            if (hasStopped) return;
            hasStopped = true;
        }
        // CraftBukkit end
        MinecraftServer.LOGGER.info("Stopping server");
        // CraftBukkit start
        if (this.server != null) {
            this.server.disablePlugins();
        }
        // CraftBukkit end
        if (this.getServerConnection() != null) {
            this.getServerConnection().b();
        }

        if (this.playerList != null) {
            MinecraftServer.LOGGER.info("Saving players");
            this.playerList.savePlayers();
            this.playerList.shutdown();
            try { Thread.sleep(100); } catch (InterruptedException ex) {} // CraftBukkit - SPIGOT-625 - give server at least a chance to send packets
        }

        MinecraftServer.LOGGER.info("Saving worlds");
        Iterator iterator = this.getWorlds().iterator();

        WorldServer worldserver;

        while (iterator.hasNext()) {
            worldserver = (WorldServer) iterator.next();
            if (worldserver != null) {
                worldserver.savingDisabled = false;
            }
        }

        this.saveChunks(false, true, false);
        iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            worldserver = (WorldServer) iterator.next();
            if (worldserver != null) {
                try {
                    worldserver.close();
                } catch (IOException ioexception) {
                    MinecraftServer.LOGGER.error("Exception closing the level", ioexception);
                }
            }
        }

        if (this.snooper.d()) {
            this.snooper.e();
        }

        this.dataPackResources.close();

        try {
            this.convertable.close();
        } catch (IOException ioexception1) {
            MinecraftServer.LOGGER.error("Failed to unlock level {}", this.convertable.getLevelName(), ioexception1);
        }

    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void a_(String s) {
        this.serverIp = s;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void safeShutdown(boolean flag) {
        this.isRunning = false;
        if (flag) {
            try {
                this.serverThread.join();
            } catch (InterruptedException interruptedexception) {
                MinecraftServer.LOGGER.error("Error while shutting down", interruptedexception);
            }
        }

    }

    protected void w() {
        try {
            if (this.init()) {
                this.nextTick = SystemUtils.getMonotonicMillis();
                this.serverPing.setMOTD(new ChatComponentText(this.motd));
                this.serverPing.setServerInfo(new ServerPing.ServerData(SharedConstants.getGameVersion().getName(), SharedConstants.getGameVersion().getProtocolVersion()));
                this.a(this.serverPing);

                while (this.isRunning) {
                    long i = SystemUtils.getMonotonicMillis() - this.nextTick;

                    if (i > 5000L && this.nextTick - this.lastOverloadTime >= 30000L) { // CraftBukkit
                        long j = i / 50L;

                        if (server.getWarnOnOverload()) // CraftBukkit
                        MinecraftServer.LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", i, j);
                        this.nextTick += j * 50L;
                        this.lastOverloadTime = this.nextTick;
                    }

                    MinecraftServer.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
                    this.nextTick += 50L;
                    GameProfilerTick gameprofilertick = GameProfilerTick.a("Server");

                    this.a(gameprofilertick);
                    this.methodProfiler.a();
                    this.methodProfiler.enter("tick");
                    this.a(this::canSleepForTick);
                    this.methodProfiler.exitEnter("nextTickWait");
                    this.X = true;
                    this.W = Math.max(SystemUtils.getMonotonicMillis() + 50L, this.nextTick);
                    this.sleepForTick();
                    this.methodProfiler.exit();
                    this.methodProfiler.b();
                    this.b(gameprofilertick);
                    this.hasTicked = true;
                }
            } else {
                this.a((CrashReport) null);
            }
        } catch (Throwable throwable) {
            MinecraftServer.LOGGER.error("Encountered an unexpected exception", throwable);
            CrashReport crashreport;

            if (throwable instanceof ReportedException) {
                crashreport = this.b(((ReportedException) throwable).a());
            } else {
                crashreport = this.b(new CrashReport("Exception in server tick loop", throwable));
            }

            File file = new File(new File(this.B(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.a(file)) {
                MinecraftServer.LOGGER.error("This crash report has been saved to: {}", file.getAbsolutePath());
            } else {
                MinecraftServer.LOGGER.error("We were unable to save this crash report to disk.");
            }

            this.a(crashreport);
        } finally {
            try {
                this.isStopped = true;
                this.stop();
            } catch (Throwable throwable1) {
                MinecraftServer.LOGGER.error("Exception stopping the server", throwable1);
            } finally {
                // CraftBukkit start - Restore terminal to original settings
                try {
                    reader.getTerminal().restore();
                } catch (Exception ignored) {
                }
                // CraftBukkit end
                this.exit();
            }

        }

    }

    private boolean canSleepForTick() {
        // CraftBukkit start
        return this.forceTicks || this.isEntered() || SystemUtils.getMonotonicMillis() < (this.X ? this.W : this.nextTick);
    }

    private void executeModerately() {
        this.executeAll();
        java.util.concurrent.locks.LockSupport.parkNanos("executing tasks", 1000L);
    }
    // CraftBukkit end

    protected void sleepForTick() {
        this.executeAll();
        this.awaitTasks(() -> {
            return !this.canSleepForTick();
        });
    }

    @Override
    protected TickTask postToMainThread(Runnable runnable) {
        return new TickTask(this.ticks, runnable);
    }

    protected boolean canExecute(TickTask ticktask) {
        return ticktask.a() + 3 < this.ticks || this.canSleepForTick();
    }

    @Override
    public boolean executeNext() {
        boolean flag = this.ba();

        this.X = flag;
        return flag;
    }

    private boolean ba() {
        if (super.executeNext()) {
            return true;
        } else {
            if (this.canSleepForTick()) {
                Iterator iterator = this.getWorlds().iterator();

                while (iterator.hasNext()) {
                    WorldServer worldserver = (WorldServer) iterator.next();

                    if (worldserver.getChunkProvider().runTasks()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    protected void c(TickTask ticktask) {
        this.getMethodProfiler().c("runTask");
        super.executeTask(ticktask);
    }

    private void a(ServerPing serverping) {
        File file = this.c("server-icon.png");

        if (!file.exists()) {
            file = this.convertable.f();
        }

        if (file.isFile()) {
            ByteBuf bytebuf = Unpooled.buffer();

            try {
                BufferedImage bufferedimage = ImageIO.read(file);

                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuffer bytebuffer = Base64.getEncoder().encode(bytebuf.nioBuffer());

                serverping.setFavicon("data:image/png;base64," + StandardCharsets.UTF_8.decode(bytebuffer));
            } catch (Exception exception) {
                MinecraftServer.LOGGER.error("Couldn't load server icon", exception);
            } finally {
                bytebuf.release();
            }
        }

    }

    public File B() {
        return new File(".");
    }

    protected void a(CrashReport crashreport) {}

    protected void exit() {}

    protected void a(BooleanSupplier booleansupplier) {
        long i = SystemUtils.getMonotonicNanos();

        ++this.ticks;
        this.b(booleansupplier);
        if (i - this.T >= 5000000000L) {
            this.T = i;
            this.serverPing.setPlayerSample(new ServerPing.ServerPingPlayerSample(this.getMaxPlayers(), this.getPlayerCount()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.getPlayerCount(), 12)];
            int j = MathHelper.nextInt(this.r, 0, this.getPlayerCount() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k) {
                agameprofile[k] = ((EntityPlayer) this.playerList.getPlayers().get(j + k)).getProfile();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            this.serverPing.b().a(agameprofile);
        }

        if (autosavePeriod > 0 && this.ticks % autosavePeriod == 0) { // CraftBukkit
            MinecraftServer.LOGGER.debug("Autosave started");
            this.methodProfiler.enter("save");
            this.playerList.savePlayers();
            this.saveChunks(true, false, false);
            this.methodProfiler.exit();
            MinecraftServer.LOGGER.debug("Autosave finished");
        }

        this.methodProfiler.enter("snooper");
        if (!this.snooper.d() && this.ticks > 100) {
            this.snooper.a();
        }

        if (this.ticks % 6000 == 0) {
            this.snooper.b();
        }

        this.methodProfiler.exit();
        this.methodProfiler.enter("tallying");
        long l = this.h[this.ticks % 100] = SystemUtils.getMonotonicNanos() - i;

        this.ag = this.ag * 0.8F + (float) l / 1000000.0F * 0.19999999F;
        long i1 = SystemUtils.getMonotonicNanos();

        this.circularTimer.a(i1 - i);
        this.methodProfiler.exit();
    }

    protected void b(BooleanSupplier booleansupplier) {
        this.server.getScheduler().mainThreadHeartbeat(this.ticks); // CraftBukkit
        this.methodProfiler.enter("commandFunctions");
        this.getFunctionData().tick();
        this.methodProfiler.exitEnter("levels");
        Iterator iterator = this.getWorlds().iterator();

        // CraftBukkit start
        // Run tasks that are waiting on processing
        while (!processQueue.isEmpty()) {
            processQueue.remove().run();
        }

        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (this.ticks % 20 == 0) {
            for (int i = 0; i < this.getPlayerList().players.size(); ++i) {
                EntityPlayer entityplayer = (EntityPlayer) this.getPlayerList().players.get(i);
                entityplayer.playerConnection.sendPacket(new PacketPlayOutUpdateTime(entityplayer.world.getTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))); // Add support for per player time
            }
        }

        while (iterator.hasNext()) {
            WorldServer worldserver = (WorldServer) iterator.next();

            this.methodProfiler.a(() -> {
                return worldserver + " " + worldserver.getDimensionKey().a();
            });
            /* Drop global time updates
            if (this.ticks % 20 == 0) {
                this.methodProfiler.enter("timeSync");
                this.playerList.a((Packet) (new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))), worldserver.getDimensionKey());
                this.methodProfiler.exit();
            }
            // CraftBukkit end */

            this.methodProfiler.enter("tick");

            try {
                worldserver.doTick(booleansupplier);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Exception ticking world");

                worldserver.a(crashreport);
                throw new ReportedException(crashreport);
            }

            this.methodProfiler.exit();
            this.methodProfiler.exit();
        }

        this.methodProfiler.exitEnter("connection");
        this.getServerConnection().c();
        this.methodProfiler.exitEnter("players");
        this.playerList.tick();
        if (SharedConstants.d) {
            GameTestHarnessTicker.a.b();
        }

        this.methodProfiler.exitEnter("server gui refresh");

        for (int i = 0; i < this.tickables.size(); ++i) {
            ((Runnable) this.tickables.get(i)).run();
        }

        this.methodProfiler.exit();
    }

    public boolean getAllowNether() {
        return true;
    }

    public void b(Runnable runnable) {
        this.tickables.add(runnable);
    }

    protected void b(String s) {
        this.ai = s;
    }

    public File c(String s) {
        return new File(this.B(), s);
    }

    public final WorldServer E() {
        return (WorldServer) this.worldServer.get(World.OVERWORLD);
    }

    @Nullable
    public WorldServer getWorldServer(ResourceKey<World> resourcekey) {
        return (WorldServer) this.worldServer.get(resourcekey);
    }

    public Set<ResourceKey<World>> F() {
        return this.worldServer.keySet();
    }

    public Iterable<WorldServer> getWorlds() {
        return this.worldServer.values();
    }

    public String getVersion() {
        return SharedConstants.getGameVersion().getName();
    }

    public int getPlayerCount() {
        return this.playerList.getPlayerCount();
    }

    public int getMaxPlayers() {
        return this.playerList.getMaxPlayers();
    }

    public String[] getPlayers() {
        return this.playerList.e();
    }

    public String getServerModName() {
        return server.getName(); // CraftBukkit - cb > vanilla!
    }

    public CrashReport b(CrashReport crashreport) {
        if (this.playerList != null) {
            crashreport.g().a("Player Count", () -> {
                return this.playerList.getPlayerCount() + " / " + this.playerList.getMaxPlayers() + "; " + this.playerList.getPlayers();
            });
        }

        crashreport.g().a("Data Packs", () -> {
            StringBuilder stringbuilder = new StringBuilder();
            Iterator iterator = this.resourcePackRepository.e().iterator();

            while (iterator.hasNext()) {
                ResourcePackLoader resourcepackloader = (ResourcePackLoader) iterator.next();

                if (stringbuilder.length() > 0) {
                    stringbuilder.append(", ");
                }

                stringbuilder.append(resourcepackloader.e());
                if (!resourcepackloader.c().a()) {
                    stringbuilder.append(" (incompatible)");
                }
            }

            return stringbuilder.toString();
        });
        if (this.ai != null) {
            crashreport.g().a("Server Id", () -> {
                return this.ai;
            });
        }

        return crashreport;
    }

    public abstract Optional<String> getModded();

    @Override
    public void sendMessage(IChatBaseComponent ichatbasecomponent, UUID uuid) {
        MinecraftServer.LOGGER.info(ichatbasecomponent.getString());
    }

    public KeyPair getKeyPair() {
        return this.H;
    }

    public int getPort() {
        return this.serverPort;
    }

    public void setPort(int i) {
        this.serverPort = i;
    }

    public String getSinglePlayerName() {
        return this.I;
    }

    public void d(String s) {
        this.I = s;
    }

    public boolean isEmbeddedServer() {
        return this.I != null;
    }

    public void a(KeyPair keypair) {
        this.H = keypair;
    }

    public void a(EnumDifficulty enumdifficulty, boolean flag) {
        if (flag || !this.saveData.isDifficultyLocked()) {
            this.saveData.setDifficulty(this.saveData.isHardcore() ? EnumDifficulty.HARD : enumdifficulty);
            this.bb();
            this.getPlayerList().getPlayers().forEach(this::a);
        }
    }

    public int b(int i) {
        return i;
    }

    private void bb() {
        Iterator iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            WorldServer worldserver = (WorldServer) iterator.next();

            worldserver.setSpawnFlags(this.getSpawnMonsters(), this.getSpawnAnimals());
        }

    }

    public void b(boolean flag) {
        this.saveData.d(flag);
        this.getPlayerList().getPlayers().forEach(this::a);
    }

    private void a(EntityPlayer entityplayer) {
        WorldData worlddata = entityplayer.getWorldServer().getWorldData();

        entityplayer.playerConnection.sendPacket(new PacketPlayOutServerDifficulty(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
    }

    protected boolean getSpawnMonsters() {
        return this.saveData.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public boolean isDemoMode() {
        return this.demoMode;
    }

    public void c(boolean flag) {
        this.demoMode = flag;
    }

    public String getResourcePack() {
        return this.K;
    }

    public String getResourcePackHash() {
        return this.L;
    }

    public void setResourcePack(String s, String s1) {
        this.K = s;
        this.L = s1;
    }

    @Override
    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", false);
        mojangstatisticsgenerator.a("whitelist_count", 0);
        if (this.playerList != null) {
            mojangstatisticsgenerator.a("players_current", this.getPlayerCount());
            mojangstatisticsgenerator.a("players_max", this.getMaxPlayers());
            mojangstatisticsgenerator.a("players_seen", this.worldNBTStorage.getSeenPlayers().length);
        }

        mojangstatisticsgenerator.a("uses_auth", this.onlineMode);
        mojangstatisticsgenerator.a("gui_state", this.ag() ? "enabled" : "disabled");
        mojangstatisticsgenerator.a("run_time", (SystemUtils.getMonotonicMillis() - mojangstatisticsgenerator.g()) / 60L * 1000L);
        mojangstatisticsgenerator.a("avg_tick_ms", (int) (MathHelper.a(this.h) * 1.0E-6D));
        int i = 0;
        Iterator iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            WorldServer worldserver = (WorldServer) iterator.next();

            if (worldserver != null) {
                mojangstatisticsgenerator.a("world[" + i + "][dimension]", worldserver.getDimensionKey().a());
                mojangstatisticsgenerator.a("world[" + i + "][mode]", this.saveData.getGameType());
                mojangstatisticsgenerator.a("world[" + i + "][difficulty]", worldserver.getDifficulty());
                mojangstatisticsgenerator.a("world[" + i + "][hardcore]", this.saveData.isHardcore());
                mojangstatisticsgenerator.a("world[" + i + "][height]", this.F);
                mojangstatisticsgenerator.a("world[" + i + "][chunks_loaded]", worldserver.getChunkProvider().h());
                ++i;
            }
        }

        mojangstatisticsgenerator.a("worlds", i);
    }

    public abstract boolean j();

    public abstract int k();

    public boolean getOnlineMode() {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean flag) {
        this.onlineMode = flag;
    }

    public boolean V() {
        return this.B;
    }

    public void e(boolean flag) {
        this.B = flag;
    }

    public boolean getSpawnAnimals() {
        return true;
    }

    public boolean getSpawnNPCs() {
        return true;
    }

    public abstract boolean l();

    public boolean getPVP() {
        return this.pvpMode;
    }

    public void setPVP(boolean flag) {
        this.pvpMode = flag;
    }

    public boolean getAllowFlight() {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean flag) {
        this.allowFlight = flag;
    }

    public abstract boolean getEnableCommandBlock();

    public String getMotd() {
        return this.motd;
    }

    public void setMotd(String s) {
        this.motd = s;
    }

    public int getMaxBuildHeight() {
        return this.F;
    }

    public void c(int i) {
        this.F = i;
    }

    public boolean isStopped() {
        return this.isStopped;
    }

    public PlayerList getPlayerList() {
        return this.playerList;
    }

    public void a(PlayerList playerlist) {
        this.playerList = playerlist;
    }

    public abstract boolean n();

    public void a(EnumGamemode enumgamemode) {
        this.saveData.setGameType(enumgamemode);
    }

    @Nullable
    public ServerConnection getServerConnection() {
        return this.serverConnection;
    }

    public boolean ag() {
        return false;
    }

    public abstract boolean a(EnumGamemode enumgamemode, boolean flag, int i);

    public int ah() {
        return this.ticks;
    }

    public int getSpawnProtection() {
        return 16;
    }

    public boolean a(WorldServer worldserver, BlockPosition blockposition, EntityHuman entityhuman) {
        return false;
    }

    public void setForceGamemode(boolean flag) {
        this.P = flag;
    }

    public boolean getForceGamemode() {
        return this.P;
    }

    public boolean al() {
        return true;
    }

    public int getIdleTimeout() {
        return this.G;
    }

    public void setIdleTimeout(int i) {
        this.G = i;
    }

    public MinecraftSessionService getMinecraftSessionService() {
        return this.minecraftSessionService;
    }

    public GameProfileRepository getGameProfileRepository() {
        return this.gameProfileRepository;
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    public ServerPing getServerPing() {
        return this.serverPing;
    }

    public void invalidatePingSample() {
        this.T = 0L;
    }

    public int at() {
        return 29999984;
    }

    @Override
    public boolean isNotMainThread() {
        return super.isNotMainThread() && !this.isStopped();
    }

    @Override
    public Thread getThread() {
        return this.serverThread;
    }

    public int aw() {
        return 256;
    }

    public long ax() {
        return this.nextTick;
    }

    public DataFixer getDataFixer() {
        return this.dataConverterManager;
    }

    public int a(@Nullable WorldServer worldserver) {
        return worldserver != null ? worldserver.getGameRules().getInt(GameRules.SPAWN_RADIUS) : 10;
    }

    public AdvancementDataWorld getAdvancementData() {
        return this.dataPackResources.g();
    }

    public CustomFunctionData getFunctionData() {
        return this.customFunctionData;
    }

    public CompletableFuture<Void> a(Collection<String> collection) {
        CompletableFuture<Void> completablefuture = CompletableFuture.supplyAsync(() -> {
            Stream<String> stream = collection.stream(); // CraftBukkit - decompile error
            ResourcePackRepository resourcepackrepository = this.resourcePackRepository;

            this.resourcePackRepository.getClass();
            return stream.map(resourcepackrepository::a).filter(Objects::nonNull).map(ResourcePackLoader::d).collect(ImmutableList.toImmutableList()); // CraftBukkit - decompile error
        }, this).thenCompose((immutablelist) -> {
            return DataPackResources.a(immutablelist, this.j() ? CommandDispatcher.ServerType.DEDICATED : CommandDispatcher.ServerType.INTEGRATED, this.h(), this.executorService, this);
        }).thenAcceptAsync((datapackresources) -> {
            this.dataPackResources.close();
            this.dataPackResources = datapackresources;
            this.server.syncCommands(); // SPIGOT-5884: Lost on reload
            this.resourcePackRepository.a(collection);
            this.saveData.a(a(this.resourcePackRepository));
            datapackresources.i();
            this.getPlayerList().savePlayers();
            this.getPlayerList().reload();
            this.customFunctionData.a(this.dataPackResources.a());
            this.ak.a(this.dataPackResources.h());
        }, this);

        if (this.isMainThread()) {
            this.awaitTasks(completablefuture::isDone);
        }

        return completablefuture;
    }

    public static DataPackConfiguration a(ResourcePackRepository resourcepackrepository, DataPackConfiguration datapackconfiguration, boolean flag) {
        resourcepackrepository.a();
        if (flag) {
            resourcepackrepository.a((Collection) Collections.singleton("vanilla"));
            return new DataPackConfiguration(ImmutableList.of("vanilla"), ImmutableList.of());
        } else {
            Set<String> set = Sets.newLinkedHashSet();
            Iterator iterator = datapackconfiguration.a().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                if (resourcepackrepository.b(s)) {
                    set.add(s);
                } else {
                    MinecraftServer.LOGGER.warn("Missing data pack {}", s);
                }
            }

            iterator = resourcepackrepository.c().iterator();

            while (iterator.hasNext()) {
                ResourcePackLoader resourcepackloader = (ResourcePackLoader) iterator.next();
                String s1 = resourcepackloader.e();

                if (!datapackconfiguration.b().contains(s1) && !set.contains(s1)) {
                    MinecraftServer.LOGGER.info("Found new data pack {}, loading it automatically", s1);
                    set.add(s1);
                }
            }

            if (set.isEmpty()) {
                MinecraftServer.LOGGER.info("No datapacks selected, forcing vanilla");
                set.add("vanilla");
            }

            resourcepackrepository.a((Collection) set);
            return a(resourcepackrepository);
        }
    }

    private static DataPackConfiguration a(ResourcePackRepository resourcepackrepository) {
        Collection<String> collection = resourcepackrepository.d();
        List<String> list = ImmutableList.copyOf(collection);
        List<String> list1 = (List) resourcepackrepository.b().stream().filter((s) -> {
            return !collection.contains(s);
        }).collect(ImmutableList.toImmutableList());

        return new DataPackConfiguration(list, list1);
    }

    public void a(CommandListenerWrapper commandlistenerwrapper) {
        if (this.aM()) {
            PlayerList playerlist = commandlistenerwrapper.getServer().getPlayerList();
            WhiteList whitelist = playerlist.getWhitelist();
            List<EntityPlayer> list = Lists.newArrayList(playerlist.getPlayers());
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                if (!whitelist.isWhitelisted(entityplayer.getProfile())) {
                    entityplayer.playerConnection.disconnect(new ChatMessage("multiplayer.disconnect.not_whitelisted"));
                }
            }

        }
    }

    public ResourcePackRepository getResourcePackRepository() {
        return this.resourcePackRepository;
    }

    public CommandDispatcher getCommandDispatcher() {
        return this.dataPackResources.f();
    }

    public CommandListenerWrapper getServerCommandListener() {
        WorldServer worldserver = this.E();

        return new CommandListenerWrapper(this, worldserver == null ? Vec3D.ORIGIN : Vec3D.b((BaseBlockPosition) worldserver.getSpawn()), Vec2F.a, worldserver, 4, "Server", new ChatComponentText("Server"), this, (Entity) null);
    }

    @Override
    public boolean shouldSendSuccess() {
        return true;
    }

    @Override
    public boolean shouldSendFailure() {
        return true;
    }

    public CraftingManager getCraftingManager() {
        return this.dataPackResources.e();
    }

    public ITagRegistry getTagRegistry() {
        return this.dataPackResources.d();
    }

    public ScoreboardServer getScoreboard() {
        return this.scoreboardServer;
    }

    public PersistentCommandStorage aH() {
        if (this.persistentCommandStorage == null) {
            throw new NullPointerException("Called before server init");
        } else {
            return this.persistentCommandStorage;
        }
    }

    public LootTableRegistry getLootTableRegistry() {
        return this.dataPackResources.c();
    }

    public LootPredicateManager getLootPredicateManager() {
        return this.dataPackResources.b();
    }

    public GameRules getGameRules() {
        return this.E().getGameRules();
    }

    public BossBattleCustomData getBossBattleCustomData() {
        return this.bossBattleCustomData;
    }

    public boolean aM() {
        return this.af;
    }

    public void i(boolean flag) {
        this.af = flag;
    }

    public float aN() {
        return this.ag;
    }

    public int b(GameProfile gameprofile) {
        if (this.getPlayerList().isOp(gameprofile)) {
            OpListEntry oplistentry = (OpListEntry) this.getPlayerList().getOPs().get(gameprofile);

            return oplistentry != null ? oplistentry.a() : (this.a(gameprofile) ? 4 : (this.isEmbeddedServer() ? (this.getPlayerList().u() ? 4 : 0) : this.g()));
        } else {
            return 0;
        }
    }

    public GameProfilerFiller getMethodProfiler() {
        return this.methodProfiler;
    }

    public abstract boolean a(GameProfile gameprofile);

    public void a(java.nio.file.Path java_nio_file_path) throws IOException {
        java.nio.file.Path java_nio_file_path1 = java_nio_file_path.resolve("levels");
        Iterator iterator = this.worldServer.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<ResourceKey<World>, WorldServer> entry = (Entry) iterator.next();
            MinecraftKey minecraftkey = ((ResourceKey) entry.getKey()).a();
            java.nio.file.Path java_nio_file_path2 = java_nio_file_path1.resolve(minecraftkey.getNamespace()).resolve(minecraftkey.getKey());

            Files.createDirectories(java_nio_file_path2);
            ((WorldServer) entry.getValue()).a(java_nio_file_path2);
        }

        this.d(java_nio_file_path.resolve("gamerules.txt"));
        this.e(java_nio_file_path.resolve("classpath.txt"));
        this.c(java_nio_file_path.resolve("example_crash.txt"));
        this.b(java_nio_file_path.resolve("stats.txt"));
        this.f(java_nio_file_path.resolve("threads.txt"));
    }

    private void b(java.nio.file.Path java_nio_file_path) throws IOException {
        BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path);
        Throwable throwable = null;

        try {
            bufferedwriter.write(String.format("pending_tasks: %d\n", this.bh()));
            bufferedwriter.write(String.format("average_tick_time: %f\n", this.aN()));
            bufferedwriter.write(String.format("tick_times: %s\n", Arrays.toString(this.h)));
            bufferedwriter.write(String.format("queue: %s\n", SystemUtils.f()));
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

    }

    private void c(java.nio.file.Path java_nio_file_path) throws IOException {
        CrashReport crashreport = new CrashReport("Server dump", new Exception("dummy"));

        this.b(crashreport);
        BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path);
        Throwable throwable = null;

        try {
            bufferedwriter.write(crashreport.e());
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

    }

    private void d(java.nio.file.Path java_nio_file_path) throws IOException {
        BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path);
        Throwable throwable = null;

        try {
            final List<String> list = Lists.newArrayList();
            final GameRules gamerules = this.getGameRules();

            GameRules.a(new GameRules.GameRuleVisitor() {
                @Override
                public <T extends GameRules.GameRuleValue<T>> void a(GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
                    list.add(String.format("%s=%s\n", gamerules_gamerulekey.a(), gamerules.get(gamerules_gamerulekey).toString()));
                }
            });
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                bufferedwriter.write(s);
            }
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

    }

    private void e(java.nio.file.Path java_nio_file_path) throws IOException {
        BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path);
        Throwable throwable = null;

        try {
            String s = System.getProperty("java.class.path");
            String s1 = System.getProperty("path.separator");
            Iterator iterator = Splitter.on(s1).split(s).iterator();

            while (iterator.hasNext()) {
                String s2 = (String) iterator.next();

                bufferedwriter.write(s2);
                bufferedwriter.write("\n");
            }
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

    }

    private void f(java.nio.file.Path java_nio_file_path) throws IOException {
        ThreadMXBean threadmxbean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] athreadinfo = threadmxbean.dumpAllThreads(true, true);

        Arrays.sort(athreadinfo, Comparator.comparing(ThreadInfo::getThreadName));
        BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path);
        Throwable throwable = null;

        try {
            ThreadInfo[] athreadinfo1 = athreadinfo;
            int i = athreadinfo.length;

            for (int j = 0; j < i; ++j) {
                ThreadInfo threadinfo = athreadinfo1[j];

                bufferedwriter.write(threadinfo.toString());
                bufferedwriter.write(10);
            }
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

    }

    // CraftBukkit start
    @Override
    public boolean isMainThread() {
        return super.isMainThread() || this.isStopped(); // CraftBukkit - MC-142590
    }

    public boolean isDebugging() {
        return false;
    }

    @Deprecated
    public static MinecraftServer getServer() {
        return (Bukkit.getServer() instanceof CraftServer) ? ((CraftServer) Bukkit.getServer()).getServer() : null;
    }
    // CraftBukkit end

    private void a(@Nullable GameProfilerTick gameprofilertick) {
        if (this.O) {
            this.O = false;
            this.m.c();
        }

        this.methodProfiler = GameProfilerTick.a(this.m.d(), gameprofilertick);
    }

    private void b(@Nullable GameProfilerTick gameprofilertick) {
        if (gameprofilertick != null) {
            gameprofilertick.b();
        }

        this.methodProfiler = this.m.d();
    }

    public boolean aR() {
        return this.m.a();
    }

    public void aS() {
        this.O = true;
    }

    public MethodProfilerResults aT() {
        MethodProfilerResults methodprofilerresults = this.m.e();

        this.m.b();
        return methodprofilerresults;
    }

    public java.nio.file.Path a(SavedFile savedfile) {
        return this.convertable.getWorldFolder(savedfile);
    }

    public boolean isSyncChunkWrites() {
        return true;
    }

    public DefinedStructureManager getDefinedStructureManager() {
        return this.ak;
    }

    public SaveData getSaveData() {
        return this.saveData;
    }

    public IRegistryCustom getCustomRegistry() {
        return this.customRegistry;
    }
}
