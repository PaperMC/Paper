package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

// CraftBukkit start
import java.io.IOException;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;

import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.world.WorldSaveEvent;
// CraftBukkit end

public abstract class MinecraftServer implements ICommandListener, Runnable, IMojangStatistics {

    private static MinecraftServer l;
    public Convertable convertable; // CraftBukkit - private final -> public
    private final MojangStatisticsGenerator n = new MojangStatisticsGenerator("server", this, aq());
    public File universe; // CraftBukkit - private final -> public
    private final List p = new ArrayList();
    private final ICommandHandler q;
    public final MethodProfiler methodProfiler = new MethodProfiler();
    private String serverIp;
    private int s = -1;
    public WorldServer[] worldServer;
    private PlayerList t;
    private boolean isRunning = true;
    private boolean isStopped;
    private int ticks;
    protected Proxy c;
    public String d;
    public int e;
    private boolean onlineMode;
    private boolean spawnAnimals;
    private boolean spawnNPCs;
    private boolean pvpMode;
    private boolean allowFlight;
    private String motd;
    private int D;
    private int E;
    private long F;
    private long G;
    private long H;
    private long I;
    public final long[] f;
    public final long[] g;
    public final long[] h;
    public final long[] i;
    public final long[] j;
    public long[][] k;
    private KeyPair J;
    private String K;
    private String L;
    private boolean demoMode;
    private boolean O;
    private boolean P;
    private String Q;
    private boolean R;
    private long S;
    private String T;
    private boolean U;
    private boolean V;

    // CraftBukkit start
    public List<WorldServer> worlds = new ArrayList<WorldServer>();
    public org.bukkit.craftbukkit.CraftServer server;
    public OptionSet options;
    public org.bukkit.command.ConsoleCommandSender console;
    public org.bukkit.command.RemoteConsoleCommandSender remoteConsole;
    public ConsoleReader reader;
    public static int currentTick = (int) (System.currentTimeMillis() / 50);
    public final Thread primaryThread;
    public java.util.Queue<Runnable> processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
    public int autosavePeriod;
    // CraftBukkit end

    public MinecraftServer(OptionSet options) { // CraftBukkit - signature file -> OptionSet
        this.c = Proxy.NO_PROXY;
        this.E = 0;
        this.f = new long[100];
        this.g = new long[100];
        this.h = new long[100];
        this.i = new long[100];
        this.j = new long[100];
        this.Q = "";
        l = this;
        // this.universe = file1; // CraftBukkit
        this.q = new CommandDispatcher();
        // this.convertable = new WorldLoaderServer(server.getWorldContainer()); // CraftBukkit - moved to DedicatedServer.init
        this.as();

        // CraftBukkit start
        this.options = options;
        try {
            this.reader = new ConsoleReader(System.in, System.out);
            this.reader.setExpandEvents(false); // Avoid parsing exceptions for uncommonly used event designators
        } catch (Exception e) {
            try {
                // Try again with jline disabled for Windows users without C++ 2008 Redistributable
                System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
                System.setProperty("user.language", "en");
                org.bukkit.craftbukkit.Main.useJline = false;
                this.reader = new ConsoleReader(System.in, System.out);
                this.reader.setExpandEvents(false);
            } catch (IOException ex) {
                Logger.getLogger(MinecraftServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Runtime.getRuntime().addShutdownHook(new org.bukkit.craftbukkit.util.ServerShutdownThread(this));

        primaryThread = new ThreadServerApplication(this, "Server thread"); // Moved from main
    }

    public abstract PropertyManager getPropertyManager();
    // CraftBukkit end

    private void as() {
        DispenserRegistry.a();
    }

    protected abstract boolean init() throws java.net.UnknownHostException; // CraftBukkit - throws UnknownHostException

    protected void a(String s) {
        if (this.getConvertable().isConvertable(s)) {
            this.getLogger().info("Converting map!");
            this.b("menu.convertingLevel");
            this.getConvertable().convert(s, new ConvertProgressUpdater(this));
        }
    }

    protected synchronized void b(String s) {
        this.T = s;
    }

    protected void a(String s, String s1, long i, WorldType worldtype, String s2) {
        this.a(s);
        this.b("menu.loadingLevel");
        this.worldServer = new WorldServer[3];
        // CraftBukkit - Removed ticktime arrays
        IDataManager idatamanager = this.convertable.a(s, true);
        WorldData worlddata = idatamanager.getWorldData();
        // CraftBukkit start - Removed worldsettings
        int worldCount = 3;

        for (int j = 0; j < worldCount; ++j) {
            WorldServer world;
            int dimension = 0;

            if (j == 1) {
                if (this.getAllowNether()) {
                    dimension = -1;
                } else {
                    continue;
                }
            }

            if (j == 2) {
                if (this.server.getAllowEnd()) {
                    dimension = 1;
                } else {
                    continue;
                }
            }

            String worldType = Environment.getEnvironment(dimension).toString().toLowerCase();
            String name = (dimension == 0) ? s : s + "_" + worldType;

            org.bukkit.generator.ChunkGenerator gen = this.server.getGenerator(name);
            WorldSettings worldsettings = new WorldSettings(i, this.getGamemode(), this.getGenerateStructures(), this.isHardcore(), worldtype);
            worldsettings.a(s2);

            if (j == 0) {
                if (this.O()) { // Strip out DEMO?
                    // CraftBukkit
                    world = new DemoWorldServer(this, new ServerNBTManager(server.getWorldContainer(), s1, true), s1, dimension, this.methodProfiler, this.getLogger());
                } else {
                    // CraftBukkit
                    world = new WorldServer(this, new ServerNBTManager(server.getWorldContainer(), s1, true), s1, dimension, worldsettings, this.methodProfiler, this.getLogger(), Environment.getEnvironment(dimension), gen);
                }
            } else {
                String dim = "DIM" + dimension;

                File newWorld = new File(new File(name), dim);
                File oldWorld = new File(new File(s), dim);

                if ((!newWorld.isDirectory()) && (oldWorld.isDirectory())) {
                    final IConsoleLogManager log = this.getLogger();
                    log.info("---- Migration of old " + worldType + " folder required ----");
                    log.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
                    log.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
                    log.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

                    if (newWorld.exists()) {
                        log.severe("A file or folder already exists at " + newWorld + "!");
                        log.info("---- Migration of old " + worldType + " folder failed ----");
                    } else if (newWorld.getParentFile().mkdirs()) {
                        if (oldWorld.renameTo(newWorld)) {
                            log.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                            // Migrate world data too.
                            try {
                                com.google.common.io.Files.copy(new File(new File(s), "level.dat"), new File(new File(name), "level.dat"));
                            } catch (IOException exception) {
                                log.severe("Unable to migrate world data.");
                            }
                            log.info("---- Migration of old " + worldType + " folder complete ----");
                        } else {
                            log.severe("Could not move folder " + oldWorld + " to " + newWorld + "!");
                            log.info("---- Migration of old " + worldType + " folder failed ----");
                        }
                    } else {
                        log.severe("Could not create path for " + newWorld + "!");
                        log.info("---- Migration of old " + worldType + " folder failed ----");
                    }
                }

                // CraftBukkit
                world = new SecondaryWorldServer(this, new ServerNBTManager(server.getWorldContainer(), name, true), name, dimension, worldsettings, this.worlds.get(0), this.methodProfiler, this.getLogger(), Environment.getEnvironment(dimension), gen);
            }

            if (gen != null) {
                world.getWorld().getPopulators().addAll(gen.getDefaultPopulators(world.getWorld()));
            }

            this.server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(this, world.getScoreboard());

            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(world.getWorld()));

            world.addIWorldAccess(new WorldManager(this, world));
            if (!this.K()) {
                world.getWorldData().setGameType(this.getGamemode());
            }
            this.worlds.add(world);
            this.t.setPlayerFileData(this.worlds.toArray(new WorldServer[this.worlds.size()]));
            // CraftBukkit end
        }

        this.c(this.getDifficulty());
        this.f();
    }

    protected void f() {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        int i = 0;

        this.b("menu.generatingTerrain");
        byte b0 = 0;

        // CraftBukkit start
        for (int m = 0; m < this.worlds.size(); ++m) {
            WorldServer worldserver = this.worlds.get(m);
            this.getLogger().info("Preparing start region for level " + m + " (Seed: " + worldserver.getSeed() + ")");
            if (!worldserver.getWorld().getKeepSpawnInMemory()) {
                continue;
            }

            ChunkCoordinates chunkcoordinates = worldserver.getSpawn();
            long j = aq();
            i = 0;

            for (int k = -192; k <= 192 && this.isRunning(); k += 16) {
                for (int l = -192; l <= 192 && this.isRunning(); l += 16) {
                    long i1 = aq();

                    if (i1 - j > 1000L) {
                        this.a_("Preparing spawn area", i * 100 / 625);
                        j = i1;
                    }

                    ++i;
                    worldserver.chunkProviderServer.getChunkAt(chunkcoordinates.x + k >> 4, chunkcoordinates.z + l >> 4);
                }
            }
        }
        // CraftBukkit end
        this.l();
    }

    public abstract boolean getGenerateStructures();

    public abstract EnumGamemode getGamemode();

    public abstract int getDifficulty();

    public abstract boolean isHardcore();

    public abstract int k();

    protected void a_(String s, int i) {
        this.d = s;
        this.e = i;
        this.getLogger().info(s + ": " + i + "%");
    }

    protected void l() {
        this.d = null;
        this.e = 0;

        this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD); // CraftBukkit
    }

    protected void saveChunks(boolean flag) throws ExceptionWorldConflict { // CraftBukkit - added throws
        if (!this.P) {
            // CraftBukkit start
            for (int j = 0; j < this.worlds.size(); ++j) {
                WorldServer worldserver = this.worlds.get(j);

                if (worldserver != null) {
                    if (!flag) {
                        this.getLogger().info("Saving chunks for level \'" + worldserver.getWorldData().getName() + "\'/" + worldserver.worldProvider.getName());
                    }

                    worldserver.save(true, (IProgressUpdate) null);
                    worldserver.saveLevel();

                    WorldSaveEvent event = new WorldSaveEvent(worldserver.getWorld());
                    this.server.getPluginManager().callEvent(event);
                }
            }
            // CraftBukkit end
        }
    }

    public void stop() throws ExceptionWorldConflict { // CraftBukkit - added throws
        if (!this.P) {
            this.getLogger().info("Stopping server");
            // CraftBukkit start
            if (this.server != null) {
                this.server.disablePlugins();
            }
            // CraftBukkit end

            if (this.ag() != null) {
                this.ag().a();
            }

            if (this.t != null) {
                this.getLogger().info("Saving players");
                this.t.savePlayers();
                this.t.r();
            }

            this.getLogger().info("Saving worlds");
            this.saveChunks(false);

            /* CraftBukkit start - Handled in saveChunks
            for (int i = 0; i < this.worldServer.length; ++i) {
                WorldServer worldserver = this.worldServer[i];

                worldserver.saveLevel();
            }
            // CraftBukkit end */
            if (this.n != null && this.n.d()) {
                this.n.e();
            }
        }
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void c(String s) {
        this.serverIp = s;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void safeShutdown() {
        this.isRunning = false;
    }

    public void run() {
        try {
            if (this.init()) {
                long i = aq();

                for (long j = 0L; this.isRunning; this.R = true) {
                    long k = aq();
                    long l = k - i;

                    if (l > 2000L && i - this.S >= 15000L) {
                        if (this.server.getWarnOnOverload()) // CraftBukkit - Added option to suppress warning messages
                        this.getLogger().warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        l = 2000L;
                        this.S = i;
                    }

                    if (l < 0L) {
                        this.getLogger().warning("Time ran backwards! Did the system time change?");
                        l = 0L;
                    }

                    j += l;
                    i = k;
                    if (this.worlds.get(0).everyoneDeeplySleeping()) { // CraftBukkit
                        this.s();
                        j = 0L;
                    } else {
                        while (j > 50L) {
                            MinecraftServer.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
                            j -= 50L;
                            this.s();
                        }
                    }

                    Thread.sleep(1L);
                }
            } else {
                this.a((CrashReport) null);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            this.getLogger().severe("Encountered an unexpected exception " + throwable.getClass().getSimpleName(), throwable);
            CrashReport crashreport = null;

            if (throwable instanceof ReportedException) {
                crashreport = this.b(((ReportedException) throwable).a());
            } else {
                crashreport = this.b(new CrashReport("Exception in server tick loop", throwable));
            }

            File file1 = new File(new File(this.q(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.a(file1, this.getLogger())) {
                this.getLogger().severe("This crash report has been saved to: " + file1.getAbsolutePath());
            } else {
                this.getLogger().severe("We were unable to save this crash report to disk.");
            }

            this.a(crashreport);
        } finally {
            try {
                this.stop();
                this.isStopped = true;
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            } finally {
                // CraftBukkit start - Restore terminal to original settings
                try {
                    this.reader.getTerminal().restore();
                } catch (Exception e) {
                }
                // CraftBukkit end
                this.r();
            }
        }
    }

    protected File q() {
        return new File(".");
    }

    protected void a(CrashReport crashreport) {}

    protected void r() {}

    protected void s() throws ExceptionWorldConflict { // CraftBukkit - added throws
        long i = System.nanoTime();

        AxisAlignedBB.a().a();
        ++this.ticks;
        if (this.U) {
            this.U = false;
            this.methodProfiler.a = true;
            this.methodProfiler.a();
        }

        this.methodProfiler.a("root");
        this.t();
        if ((this.autosavePeriod > 0) && ((this.ticks % this.autosavePeriod) == 0)) { // CraftBukkit
            this.methodProfiler.a("save");
            this.t.savePlayers();
            this.saveChunks(true);
            this.methodProfiler.b();
        }

        this.methodProfiler.a("tallying");
        this.j[this.ticks % 100] = System.nanoTime() - i;
        this.f[this.ticks % 100] = Packet.q - this.F;
        this.F = Packet.q;
        this.g[this.ticks % 100] = Packet.r - this.G;
        this.G = Packet.r;
        this.h[this.ticks % 100] = Packet.o - this.H;
        this.H = Packet.o;
        this.i[this.ticks % 100] = Packet.p - this.I;
        this.I = Packet.p;
        this.methodProfiler.b();
        this.methodProfiler.a("snooper");
        if (!this.n.d() && this.ticks > 100) {
            this.n.a();
        }

        if (this.ticks % 6000 == 0) {
            this.n.b();
        }

        this.methodProfiler.b();
        this.methodProfiler.b();
    }

    public void t() {
        this.methodProfiler.a("levels");

        // CraftBukkit start
        this.server.getScheduler().mainThreadHeartbeat(this.ticks);

        // Run tasks that are waiting on processing
        while (!processQueue.isEmpty()) {
            processQueue.remove().run();
        }

        org.bukkit.craftbukkit.chunkio.ChunkIOExecutor.tick();

        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (this.ticks % 20 == 0) {
            for (int i = 0; i < this.getPlayerList().players.size(); ++i) {
                EntityPlayer entityplayer = (EntityPlayer) this.getPlayerList().players.get(i);
                entityplayer.playerConnection.sendPacket(new Packet4UpdateTime(entityplayer.world.getTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean("doDaylightCycle"))); // Add support for per player time
            }
        }

        int i;

        for (i = 0; i < this.worlds.size(); ++i) {
            long j = System.nanoTime();

            // if (i == 0 || this.getAllowNether()) {
                WorldServer worldserver = this.worlds.get(i);

                this.methodProfiler.a(worldserver.getWorldData().getName());
                this.methodProfiler.a("pools");
                worldserver.getVec3DPool().a();
                this.methodProfiler.b();
                /* Drop global time updates
                if (this.ticks % 20 == 0) {
                    this.methodProfiler.a("timeSync");
                    this.t.a(new Packet4UpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.worldProvider.dimension);
                    this.methodProfiler.b();
                }
                // CraftBukkit end */

                this.methodProfiler.a("tick");

                CrashReport crashreport;

                try {
                    worldserver.doTick();
                } catch (Throwable throwable) {
                    crashreport = CrashReport.a(throwable, "Exception ticking world");
                    worldserver.a(crashreport);
                    throw new ReportedException(crashreport);
                }

                try {
                    worldserver.tickEntities();
                } catch (Throwable throwable1) {
                    crashreport = CrashReport.a(throwable1, "Exception ticking world entities");
                    worldserver.a(crashreport);
                    throw new ReportedException(crashreport);
                }

                this.methodProfiler.b();
                this.methodProfiler.a("tracker");
                worldserver.getTracker().updatePlayers();
                this.methodProfiler.b();
                this.methodProfiler.b();
            // } // CraftBukkit

            // this.k[i][this.ticks % 100] = System.nanoTime() - j; // CraftBukkit
        }

        this.methodProfiler.c("connection");
        this.ag().b();
        this.methodProfiler.c("players");
        this.t.tick();
        this.methodProfiler.c("tickables");

        for (i = 0; i < this.p.size(); ++i) {
            ((IUpdatePlayerListBox) this.p.get(i)).a();
        }

        this.methodProfiler.b();
    }

    public boolean getAllowNether() {
        return true;
    }

    public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
        this.p.add(iupdateplayerlistbox);
    }

    public static void main(final OptionSet options) { // CraftBukkit - replaces main(String[] astring)
        StatisticList.a();
        IConsoleLogManager iconsolelogmanager = null;

        try {
            /* CraftBukkit start - Replace everything
            boolean flag = !GraphicsEnvironment.isHeadless();
            String s = null;
            String s1 = ".";
            String s2 = null;
            boolean flag1 = false;
            boolean flag2 = false;
            int i = -1;

            for (int j = 0; j < astring.length; ++j) {
                String s3 = astring[j];
                String s4 = j == astring.length - 1 ? null : astring[j + 1];
                boolean flag3 = false;

                if (!s3.equals("nogui") && !s3.equals("--nogui")) {
                    if (s3.equals("--port") && s4 != null) {
                        flag3 = true;

                        try {
                            i = Integer.parseInt(s4);
                        } catch (NumberFormatException numberformatexception) {
                            ;
                        }
                    } else if (s3.equals("--singleplayer") && s4 != null) {
                        flag3 = true;
                        s = s4;
                    } else if (s3.equals("--universe") && s4 != null) {
                        flag3 = true;
                        s1 = s4;
                    } else if (s3.equals("--world") && s4 != null) {
                        flag3 = true;
                        s2 = s4;
                    } else if (s3.equals("--demo")) {
                        flag1 = true;
                    } else if (s3.equals("--bonusChest")) {
                        flag2 = true;
                    }
                } else {
                    flag = false;
                }

                if (flag3) {
                    ++j;
                }
            }
            // */

            DedicatedServer dedicatedserver = new DedicatedServer(options);

            iconsolelogmanager = dedicatedserver.getLogger();
            if (options.has("port")) {
                int port = (Integer) options.valueOf("port");
                if (port > 0) {
                    dedicatedserver.setPort(port);
                }
            }

            if (options.has("universe")) {
                dedicatedserver.universe = (File) options.valueOf("universe");
            }

            if (options.has("world")) {
                dedicatedserver.k((String) options.valueOf("world"));
            }
            /*
            if (s != null) {
                dedicatedserver.j(s);
            }

            if (s2 != null) {
                dedicatedserver.k(s2);
            }

            if (i >= 0) {
                dedicatedserver.setPort(i);
            }

            if (flag1) {
                dedicatedserver.b(true);
            }

            if (flag2) {
                dedicatedserver.c(true);
            }

            if (flag) {
                dedicatedserver.au();
            }
            */

            dedicatedserver.primaryThread.start();
            // Runtime.getRuntime().addShutdownHook(new ThreadShutdown(dedicatedserver));
            // CraftBukkit end
        } catch (Exception exception) {
            if (iconsolelogmanager != null) {
                iconsolelogmanager.severe("Failed to start the minecraft server", exception);
            } else {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Failed to start the minecraft server", exception);
            }
        }
    }

    public void v() {
        // (new ThreadServerApplication(this, "Server thread")).start(); // CraftBukkit - prevent abuse
    }

    public File d(String s) {
        return new File(this.q(), s);
    }

    public void info(String s) {
        this.getLogger().info(s);
    }

    public void warning(String s) {
        this.getLogger().warning(s);
    }

    public WorldServer getWorldServer(int i) {
        // CraftBukkit start
        for (WorldServer world : this.worlds) {
            if (world.dimension == i) {
                return world;
            }
        }

        return this.worlds.get(0);
        // CraftBukkit end
    }

    public String w() {
        return this.serverIp;
    }

    public int x() {
        return this.s;
    }

    public String y() {
        return this.motd;
    }

    public String getVersion() {
        return "1.6.4";
    }

    public int A() {
        return this.t.getPlayerCount();
    }

    public int B() {
        return this.t.getMaxPlayers();
    }

    public String[] getPlayers() {
        return this.t.d();
    }

    public String getPlugins() {
        // CraftBukkit start - Whole method
        StringBuilder result = new StringBuilder();
        org.bukkit.plugin.Plugin[] plugins = server.getPluginManager().getPlugins();

        result.append(server.getName());
        result.append(" on Bukkit ");
        result.append(server.getBukkitVersion());

        if (plugins.length > 0 && this.server.getQueryPlugins()) {
            result.append(": ");

            for (int i = 0; i < plugins.length; i++) {
                if (i > 0) {
                    result.append("; ");
                }

                result.append(plugins[i].getDescription().getName());
                result.append(" ");
                result.append(plugins[i].getDescription().getVersion().replaceAll(";", ","));
            }
        }

        return result.toString();
        // CraftBukkit end
    }

    // CraftBukkit start
    public String g(final String s) { // CraftBukkit - final parameter
        Waitable<String> waitable = new Waitable<String>() {
            @Override
            protected String evaluate() {
                RemoteControlCommandListener.instance.d();
                // Event changes start
                RemoteServerCommandEvent event = new RemoteServerCommandEvent(MinecraftServer.this.remoteConsole, s);
                MinecraftServer.this.server.getPluginManager().callEvent(event);
                // Event changes end
                ServerCommand servercommand = new ServerCommand(event.getCommand(), RemoteControlCommandListener.instance);
                // this.p.a(RemoteControlCommandListener.instance, s);
                MinecraftServer.this.server.dispatchServerCommand(MinecraftServer.this.remoteConsole, servercommand); // CraftBukkit
                return RemoteControlCommandListener.instance.e();
            }};
        processQueue.add(waitable);
        try {
            return waitable.get();
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Exception processing rcon command " + s, e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Maintain interrupted state
            throw new RuntimeException("Interrupted processing rcon command " + s, e);
        }
        // CraftBukkit end
    }

    public boolean isDebugging() {
        return this.getPropertyManager().getBoolean("debug", false); // CraftBukkit - don't hardcode
    }

    public void h(String s) {
        this.getLogger().severe(s);
    }

    public void i(String s) {
        if (this.isDebugging()) {
            this.getLogger().info(s);
        }
    }

    public String getServerModName() {
        return "craftbukkit"; // CraftBukkit - cb > vanilla!
    }

    public CrashReport b(CrashReport crashreport) {
        crashreport.g().a("Profiler Position", (Callable) (new CrashReportProfilerPosition(this)));
        if (this.worlds != null && this.worlds.size() > 0 && this.worlds.get(0) != null) { // CraftBukkit
            crashreport.g().a("Vec3 Pool Size", (Callable) (new CrashReportVec3DPoolSize(this)));
        }

        if (this.t != null) {
            crashreport.g().a("Player Count", (Callable) (new CrashReportPlayerCount(this)));
        }

        return crashreport;
    }

    public List a(ICommandListener icommandlistener, String s) {
        // CraftBukkit start - Allow tab-completion of Bukkit commands
        /*
        ArrayList arraylist = new ArrayList();

        if (s.startsWith("/")) {
            s = s.substring(1);
            boolean flag = !s.contains(" ");
            List list = this.p.b(icommandlistener, s);

            if (list != null) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    String s1 = (String) iterator.next();

                    if (flag) {
                        arraylist.add("/" + s1);
                    } else {
                        arraylist.add(s1);
                    }
                }
            }

            return arraylist;
        } else {
            String[] astring = s.split(" ", -1);
            String s2 = astring[astring.length - 1];
            String[] astring1 = this.s.d();
            int i = astring1.length;

            for (int j = 0; j < i; ++j) {
                String s3 = astring1[j];

                if (CommandAbstract.a(s2, s3)) {
                    arraylist.add(s3);
                }
            }

            return arraylist;
        }
        */
        return this.server.tabComplete(icommandlistener, s);
        // CraftBukkit end
    }

    public static MinecraftServer getServer() {
        return l;
    }

    public String getName() {
        return "Server";
    }

    public void sendMessage(ChatMessage chatmessage) {
        this.getLogger().info(chatmessage.toString());
    }

    public boolean a(int i, String s) {
        return true;
    }

    public ICommandHandler getCommandHandler() {
        return this.q;
    }

    public KeyPair H() {
        return this.J;
    }

    public int I() {
        return this.s;
    }

    public void setPort(int i) {
        this.s = i;
    }

    public String J() {
        return this.K;
    }

    public void j(String s) {
        this.K = s;
    }

    public boolean K() {
        return this.K != null;
    }

    public String L() {
        return this.L;
    }

    public void k(String s) {
        this.L = s;
    }

    public void a(KeyPair keypair) {
        this.J = keypair;
    }

    public void c(int i) {
        // CraftBukkit start
        for (int j = 0; j < this.worlds.size(); ++j) {
            WorldServer worldserver = this.worlds.get(j);
            // CraftBukkit end

            if (worldserver != null) {
                if (worldserver.getWorldData().isHardcore()) {
                    worldserver.difficulty = 3;
                    worldserver.setSpawnFlags(true, true);
                } else if (this.K()) {
                    worldserver.difficulty = i;
                    worldserver.setSpawnFlags(worldserver.difficulty > 0, true);
                } else {
                    worldserver.difficulty = i;
                    worldserver.setSpawnFlags(this.getSpawnMonsters(), this.spawnAnimals);
                }
            }
        }
    }

    protected boolean getSpawnMonsters() {
        return true;
    }

    public boolean O() {
        return this.demoMode;
    }

    public void b(boolean flag) {
        this.demoMode = flag;
    }

    public void c(boolean flag) {
        this.O = flag;
    }

    public Convertable getConvertable() {
        return this.convertable;
    }

    public void R() {
        this.P = true;
        this.getConvertable().d();

        // CraftBukkit start - This needs review, what does it do? (it's new)
        for (int i = 0; i < this.worlds.size(); ++i) {
            WorldServer worldserver = this.worlds.get(i);
            // CraftBukkit end

            if (worldserver != null) {
                worldserver.saveLevel();
            }
        }

        this.getConvertable().e(this.worlds.get(0).getDataManager().g()); // CraftBukkit
        this.safeShutdown();
    }

    public String getTexturePack() {
        return this.Q;
    }

    public void setTexturePack(String s) {
        this.Q   = s;
    }

    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", Boolean.valueOf(false));
        mojangstatisticsgenerator.a("whitelist_count", Integer.valueOf(0));
        mojangstatisticsgenerator.a("players_current", Integer.valueOf(this.A()));
        mojangstatisticsgenerator.a("players_max", Integer.valueOf(this.B()));
        mojangstatisticsgenerator.a("players_seen", Integer.valueOf(this.t.getSeenPlayers().length));
        mojangstatisticsgenerator.a("uses_auth", Boolean.valueOf(this.onlineMode));
        mojangstatisticsgenerator.a("gui_state", this.ai() ? "enabled" : "disabled");
        mojangstatisticsgenerator.a("run_time", Long.valueOf((aq() - mojangstatisticsgenerator.g()) / 60L * 1000L));
        mojangstatisticsgenerator.a("avg_tick_ms", Integer.valueOf((int) (MathHelper.a(this.j) * 1.0E-6D)));
        mojangstatisticsgenerator.a("avg_sent_packet_count", Integer.valueOf((int) MathHelper.a(this.f)));
        mojangstatisticsgenerator.a("avg_sent_packet_size", Integer.valueOf((int) MathHelper.a(this.g)));
        mojangstatisticsgenerator.a("avg_rec_packet_count", Integer.valueOf((int) MathHelper.a(this.h)));
        mojangstatisticsgenerator.a("avg_rec_packet_size", Integer.valueOf((int) MathHelper.a(this.i)));
        int i = 0;

        // CraftBukkit start
        for (int j = 0; j < this.worlds.size(); ++j) {
            // if (this.worldServer[j] != null) {
                WorldServer worldserver = this.worlds.get(j);
                // CraftBukkit end
                WorldData worlddata = worldserver.getWorldData();

                mojangstatisticsgenerator.a("world[" + i + "][dimension]", Integer.valueOf(worldserver.worldProvider.dimension));
                mojangstatisticsgenerator.a("world[" + i + "][mode]", worlddata.getGameType());
                mojangstatisticsgenerator.a("world[" + i + "][difficulty]", Integer.valueOf(worldserver.difficulty));
                mojangstatisticsgenerator.a("world[" + i + "][hardcore]", Boolean.valueOf(worlddata.isHardcore()));
                mojangstatisticsgenerator.a("world[" + i + "][generator_name]", worlddata.getType().name());
                mojangstatisticsgenerator.a("world[" + i + "][generator_version]", Integer.valueOf(worlddata.getType().getVersion()));
                mojangstatisticsgenerator.a("world[" + i + "][height]", Integer.valueOf(this.D));
                mojangstatisticsgenerator.a("world[" + i + "][chunks_loaded]", Integer.valueOf(worldserver.L().getLoadedChunks()));
                ++i;
            // } // CraftBukkit
        }

        mojangstatisticsgenerator.a("worlds", Integer.valueOf(i));
    }

    public void b(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("singleplayer", Boolean.valueOf(this.K()));
        mojangstatisticsgenerator.a("server_brand", this.getServerModName());
        mojangstatisticsgenerator.a("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        mojangstatisticsgenerator.a("dedicated", Boolean.valueOf(this.V()));
    }

    public boolean getSnooperEnabled() {
        return true;
    }

    public int U() {
        return 16;
    }

    public abstract boolean V();

    public boolean getOnlineMode() {
        return this.server.getOnlineMode(); // CraftBukkit
    }

    public void setOnlineMode(boolean flag) {
        this.onlineMode = flag;
    }

    public boolean getSpawnAnimals() {
        return this.spawnAnimals;
    }

    public void setSpawnAnimals(boolean flag) {
        this.spawnAnimals = flag;
    }

    public boolean getSpawnNPCs() {
        return this.spawnNPCs;
    }

    public void setSpawnNPCs(boolean flag) {
        this.spawnNPCs = flag;
    }

    public boolean getPvP() {
        return this.pvpMode;
    }

    public void setPvP(boolean flag) {
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
        return this.D;
    }

    public void d(int i) {
        this.D = i;
    }

    public boolean isStopped() {
        return this.isStopped;
    }

    public PlayerList getPlayerList() {
        return this.t;
    }

    public void a(PlayerList playerlist) {
        this.t = playerlist;
    }

    public void a(EnumGamemode enumgamemode) {
        // CraftBukkit start
        for (int i = 0; i < this.worlds.size(); ++i) {
            getServer().worlds.get(i).getWorldData().setGameType(enumgamemode);
            // CraftBukkit end
        }
    }

    public abstract ServerConnection ag();

    public boolean ai() {
        return false;
    }

    public abstract String a(EnumGamemode enumgamemode, boolean flag);

    public int aj() {
        return this.ticks;
    }

    public void ak() {
        this.U = true;
    }

    public ChunkCoordinates b() {
        return new ChunkCoordinates(0, 0, 0);
    }

    public World f_() {
        return this.worlds.get(0); // CraftBukkit
    }

    public int getSpawnProtection() {
        return 16;
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        return false;
    }

    public abstract IConsoleLogManager getLogger();

    public void setForceGamemode(boolean flag) {
        this.V = flag;
    }

    public boolean getForceGamemode() {
        return this.V;
    }

    public Proxy ap() {
        return this.c;
    }

    public static long aq() {
        return System.currentTimeMillis();
    }

    public int ar() {
        return this.E;
    }

    public void e(int i) {
        this.E = i;
    }

    public static PlayerList a(MinecraftServer minecraftserver) {
        return minecraftserver.t;
    }
}
