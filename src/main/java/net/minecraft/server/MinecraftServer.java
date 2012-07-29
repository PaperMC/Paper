package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
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
import jline.console.ConsoleReader;
import joptsimple.OptionSet;

import org.bukkit.World.Environment;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.world.WorldSaveEvent;
// CraftBukkit end

public abstract class MinecraftServer implements Runnable, IMojangStatistics, ICommandListener {

    public static Logger log = Logger.getLogger("Minecraft");
    private static MinecraftServer l = null;
    public Convertable convertable; // CraftBukkit - private final -> public
    private final MojangStatisticsGenerator n = new MojangStatisticsGenerator("server", this);
    public File universe; // CraftBukkit - private final -> public
    private final List p = new ArrayList();
    private final ICommandHandler q;
    public final MethodProfiler methodProfiler = new MethodProfiler();
    private String serverIp;
    private int s = -1;
    // public WorldServer[] worldServer; // CraftBukkit - removed!
    private ServerConfigurationManagerAbstract t;
    private boolean isRunning = true;
    private boolean isStopped = false;
    private int ticks = 0;
    public String d;
    public int e;
    private boolean onlineMode;
    private boolean spawnAnimals;
    private boolean spawnNPCs;
    private boolean pvpMode;
    private boolean allowFlight;
    private String motd;
    private int D;
    private long E;
    private long F;
    private long G;
    private long H;
    public final long[] f = new long[100];
    public final long[] g = new long[100];
    public final long[] h = new long[100];
    public final long[] i = new long[100];
    public final long[] j = new long[100];
    public long[][] k;
    private KeyPair I;
    private String J;
    private String K;
    private boolean demoMode;
    private boolean N;
    private boolean O;
    private String P = "";
    private boolean Q = false;
    private long R;
    private String S;
    private boolean T;

    // CraftBukkit start
    public List<WorldServer> worlds = new ArrayList<WorldServer>();
    public org.bukkit.craftbukkit.CraftServer server;
    public OptionSet options;
    public org.bukkit.command.ConsoleCommandSender console;
    public org.bukkit.command.RemoteConsoleCommandSender remoteConsole;
    public ConsoleReader reader;
    public static int currentTick;
    public final Thread primaryThread;
    // CraftBukkit end

    public MinecraftServer(OptionSet options) { // CraftBukkit - signature file -> OptionSet
        l = this;
        // this.universe = file1; // CraftBukkit
        this.q = new CommandDispatcher();
        // this.convertable = new WorldLoaderServer(server.getWorldContainer()); // CraftBukkit - moved to DedicatedServer.init

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
            } catch (java.io.IOException ex) {
                Logger.getLogger(MinecraftServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Runtime.getRuntime().addShutdownHook(new org.bukkit.craftbukkit.util.ServerShutdownThread(this));

        primaryThread = new ThreadServerApplication(this, "Server thread"); // Moved from main
    }

    public abstract PropertyManager getPropertyManager(); // protected -> public
    // CraftBukkit end

    protected abstract boolean init() throws java.net.UnknownHostException; // CraftBukkit - throws UnknownHostException

    protected void c(String s) {
        if (this.getConvertable().isConvertable(s)) {
            log.info("Converting map!");
            this.d("menu.convertingLevel");
            this.getConvertable().convert(s, new ConvertProgressUpdater(this));
        }
    }

    protected synchronized void d(String s) {
        this.S = s;
    }

    protected void a(String s, String s1, long i, WorldType worldtype) {
        this.c(s);
        this.d("menu.loadingLevel");
        // CraftBukkit - removed world and ticktime arrays
        IDataManager idatamanager = this.convertable.a(s, true);
        WorldData worlddata = idatamanager.getWorldData();
        // CraftBukkit start - removed worldsettings
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
                // don't do this in server.properties, do it in bukkit.yml
                if (this.server.getAllowEnd()) {
                    dimension = 1;
                } else {
                    continue;
                }
            }

            String worldType = Environment.getEnvironment(dimension).toString().toLowerCase();
            String name = (dimension == 0) ? s : s + "_" + worldType;

            org.bukkit.generator.ChunkGenerator gen = this.server.getGenerator(name);
            WorldSettings worldsettings = new WorldSettings(i, this.getGamemode(), this.getGenerateStructures(), false, worldtype);

            if (j == 0) {
                if (this.L()) { // Strip out DEMO?
                    // CraftBukkit
                    world = new DemoWorldServer(this, new ServerNBTManager(server.getWorldContainer(), s1, true), s1, dimension, this.methodProfiler);
                } else {
                    // CraftBukkit
                    world = new WorldServer(this, new ServerNBTManager(server.getWorldContainer(), s1, true), s1, dimension, worldsettings, this.methodProfiler, org.bukkit.World.Environment.getEnvironment(dimension), gen);
                }
            } else {
                String dim = "DIM" + dimension;

                File newWorld = new File(new File(name), dim);
                File oldWorld = new File(new File(s), dim);

                if ((!newWorld.isDirectory()) && (oldWorld.isDirectory())) {
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

                this.c(name);

                // CraftBukkit
                world = new SecondaryWorldServer(this, new ServerNBTManager(server.getWorldContainer(), name, true), name, dimension, worldsettings, this.worlds.get(0), this.methodProfiler, org.bukkit.World.Environment.getEnvironment(dimension), gen);
            }

            if (gen != null) {
                world.getWorld().getPopulators().addAll(gen.getDefaultPopulators(world.getWorld()));
            }

            world.addIWorldAccess(new WorldManager(this, world));
            if (!this.H()) {
                world.getWorldData().setGameType(this.getGamemode());
            }
            this.worlds.add(world);
            this.t.setPlayerFileData(this.worlds.toArray(new WorldServer[this.worlds.size()]));
            // CraftBukkit end
        }

        this.c(this.getDifficulty());
        this.d();
    }

    protected void d() {
        short short1 = 196;
        long i = System.currentTimeMillis();

        this.d("menu.generatingTerrain");

        // CraftBukkit start
        for (int j = 0; j < this.worlds.size(); ++j) {
            WorldServer worldserver = this.worlds.get(j);
            log.info("Preparing start region for level " + j + " (Seed: " + worldserver.getSeed() + ")");
            if (!worldserver.getWorld().getKeepSpawnInMemory()) {
                continue;
            }
            // CraftBukkit end
            ChunkCoordinates chunkcoordinates = worldserver.getSpawn();

            for (int k = -short1; k <= short1 && this.isRunning(); k += 16) {
                for (int l = -short1; l <= short1 && this.isRunning(); l += 16) {
                    long i1 = System.currentTimeMillis();

                    if (i1 < i) {
                        i = i1;
                    }

                    if (i1 > i + 1000L) {
                        int j1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                        int k1 = (k + short1) * (short1 * 2 + 1) + l + 1;

                        this.a_("Preparing spawn area", k1 * 100 / j1);
                        i = i1;
                    }

                    worldserver.chunkProviderServer.getChunkAt(chunkcoordinates.x + k >> 4, chunkcoordinates.z + l >> 4);

                    while (worldserver.updateLights() && this.isRunning()) {
                        ;
                    }
                }
            }
        }

        // CraftBukkit start
        for (World world : this.worlds) {
            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(world.getWorld()));
        }
        // CraftBukkit end

        this.i();
    }

    public abstract boolean getGenerateStructures();

    public abstract EnumGamemode getGamemode();

    public abstract int getDifficulty();

    public abstract boolean isHardcore();

    protected void a_(String s, int i) {
        this.d = s;
        this.e = i;
        log.info(s + ": " + i + "%");
    }

    protected void i() {
        this.d = null;
        this.e = 0;

        this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD); // CraftBukkit
    }

    protected void saveChunks(boolean flag) throws ExceptionWorldConflict { // CraftBukkit - added throws
        if (!this.O) {
            // CraftBukkit start
            for (int j = 0; j < this.worlds.size(); ++j) {
                WorldServer worldserver = this.worlds.get(j);

                if (worldserver != null) {
                    if (!flag) {
                        log.info("Saving chunks for level \'" + worldserver.getWorldData().getName() + "\'/" + worldserver.worldProvider);
                    }

                    worldserver.save(true, (IProgressUpdate) null);
                    worldserver.saveLevel();

                    WorldSaveEvent event = new WorldSaveEvent(worldserver.getWorld());
                    this.server.getPluginManager().callEvent(event);
                }
            }

            if (!this.worlds.get(0).savingDisabled) {
                this.getServerConfigurationManager().savePlayers();
            }
            // CraftBukkit end
        }
    }

    public void stop() throws ExceptionWorldConflict { // CraftBukkit - added throws
        if (!this.O) {
            log.info("Stopping server");
            // CraftBukkit start
            if (this.server != null) {
                this.server.disablePlugins();
            }
            // CraftBukkit end

            if (this.ac() != null) {
                this.ac().a();
            }

            if (this.t != null) {
                log.info("Saving players");
                this.t.savePlayers();
                this.t.r();
            }

            log.info("Saving worlds");
            this.saveChunks(false);
            /* CraftBukkit start - handled in saveChunks
            WorldServer[] aworldserver = this.worldServer;
            int i = aworldserver.length;

            for (int j = 0; j < i; ++j) {
                WorldServer worldserver = aworldserver[j];

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

    public void e(String s) {
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
                long i = System.currentTimeMillis();

                for (long j = 0L; this.isRunning; this.Q = true) {
                    long k = System.currentTimeMillis();
                    long l = k - i;

                    if (l > 2000L && i - this.R >= 15000L) {
                        if (this.server.getWarnOnOverload()) // CraftBukkit - Added option to suppress warning messages
                        log.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        l = 2000L;
                        this.R = i;
                    }

                    if (l < 0L) {
                        log.warning("Time ran backwards! Did the system time change?");
                        l = 0L;
                    }

                    j += l;
                    i = k;
                    if (this.worlds.get(0).everyoneDeeplySleeping()) { // CraftBukkit
                        this.p();
                        j = 0L;
                    } else {
                        while (j > 50L) {
                            MinecraftServer.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
                            j -= 50L;
                            this.p();
                        }
                    }

                    Thread.sleep(1L);
                }
            } else {
                this.a((CrashReport) null);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.log(Level.SEVERE, "Encountered an unexpected exception " + throwable.getClass().getSimpleName(), throwable);
            CrashReport crashreport = null;

            if (throwable instanceof ReportedException) {
                crashreport = this.b(((ReportedException) throwable).a());
            } else {
                crashreport = this.b(new CrashReport("Exception in server tick loop", throwable));
            }

            File file1 = new File(new File(this.n(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.a(file1)) {
                log.severe("This crash report has been saved to: " + file1.getAbsolutePath());
            } else {
                log.severe("We were unable to save this crash report to disk.");
            }

            this.a(crashreport);
        } finally {
            try {
                this.stop();
                this.isStopped = true;
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            } finally {
                // CraftBukkit start - restore terminal to original settings
                try {
                    this.reader.getTerminal().restore();
                } catch (Exception e) {
                }
                // CraftBukkit end
                this.o();
            }
        }
    }

    protected File n() {
        return new File(".");
    }

    protected void a(CrashReport crashreport) {}

    protected void o() {}

    protected void p() throws ExceptionWorldConflict { // CraftBukkit - added throws
        long i = System.nanoTime();

        AxisAlignedBB.a().a();
        Vec3D.a().a();
        ++this.ticks;
        if (this.T) {
            this.T = false;
            // this.methodProfiler.a = true; // CraftBukkit - not in production code
            // this.methodProfiler.a(); // CraftBukkit - not in production code
        }

        // this.methodProfiler.a("root"); // CraftBukkit - not in production code
        this.q();
        if (this.ticks % 900 == 0) {
            // this.methodProfiler.a("save"); // CraftBukkit - not in production code
            this.t.savePlayers();
            this.saveChunks(true);
            // this.methodProfiler.b(); // CraftBukkit - not in production code
        }

        // this.methodProfiler.a("tallying"); // CraftBukkit - not in production code
        this.j[this.ticks % 100] = System.nanoTime() - i;
        this.f[this.ticks % 100] = Packet.p - this.E;
        this.E = Packet.p;
        this.g[this.ticks % 100] = Packet.q - this.F;
        this.F = Packet.q;
        this.h[this.ticks % 100] = Packet.n - this.G;
        this.G = Packet.n;
        this.i[this.ticks % 100] = Packet.o - this.H;
        this.H = Packet.o;
        // this.methodProfiler.b(); // CraftBukkit - not in production code
        // this.methodProfiler.a("snooper"); // CraftBukkit - not in production code
        if (!this.n.d() && this.ticks > 100) {
            this.n.a();
        }

        if (this.ticks % 6000 == 0) {
            this.n.b();
        }

        // this.methodProfiler.b(); // CraftBukkit - not in production code
        // this.methodProfiler.b(); // CraftBukkit - not in production code
    }

    public void q() {
        // this.methodProfiler.a("levels"); // CraftBukkit - not in production code

        // CraftBukkit start - only send timeupdates to the people in that world
        this.server.getScheduler().mainThreadHeartbeat(this.ticks);

        // Send timeupdates to everyone, it will get the right time from the world the player is in.
        if (this.ticks % 20 == 0) {
            for (int i = 0; i < this.getServerConfigurationManager().players.size(); ++i) {
                EntityPlayer entityplayer = (EntityPlayer) this.getServerConfigurationManager().players.get(i);
                entityplayer.netServerHandler.sendPacket(new Packet4UpdateTime(entityplayer.getPlayerTime())); // Add support for per player time
            }
        }

        for (int i = 0; i < this.worlds.size(); ++i) {
            long j = System.nanoTime();

            // if (i == 0 || this.getAllowNether()) {
                WorldServer worldserver = this.worlds.get(i);

                /* Drop global timeupdates
                this.b.a(worldserver.getWorldData().j());
                if (this.ticks % 20 == 0) {
                    this.b.a("timeSync");
                    this.t.a(new Packet4UpdateTime(worldserver.getTime()), worldserver.worldProvider.dimension);
                    this.b.b();
                }
                // CraftBukkit end */

                // this.methodProfiler.a("tick"); // CraftBukkit - not in production code
                worldserver.doTick();
                // this.methodProfiler.c("lights"); // CraftBukkit - not in production code

                while (true) {
                    if (!worldserver.updateLights()) {
                        // this.methodProfiler.b(); // CraftBukkit - not in production code
                        if (!worldserver.players.isEmpty()) {
                            worldserver.tickEntities();
                        }

                        // this.methodProfiler.a("tracker"); // CraftBukkit - not in production code
                        worldserver.getTracker().updatePlayers();
                        // this.methodProfiler.b(); // CraftBukkit - not in production code
                        // this.methodProfiler.b(); // CraftBukkit - not in production code
                        break;
                    }
                }
            // } // CraftBukkit

            // this.k[i][this.ticks % 100] = System.nanoTime() - j; // CraftBukkit
        }

        // this.methodProfiler.c("connection"); // CraftBukkit - not in production code
        this.ac().b();
        // this.methodProfiler.c("players"); // CraftBukkit - not in production code
        this.t.tick();
        // this.methodProfiler.c("tickables"); // CraftBukkit - not in production code
        Iterator iterator = this.p.iterator();

        while (iterator.hasNext()) {
            IUpdatePlayerListBox iupdateplayerlistbox = (IUpdatePlayerListBox) iterator.next();

            iupdateplayerlistbox.a();
        }

        // this.methodProfiler.b(); // CraftBukkit - not in production code
    }

    public boolean getAllowNether() {
        return true;
    }

    public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
        this.p.add(iupdateplayerlistbox);
    }

    public static void main(final OptionSet options) { // CraftBukkit - replaces main(String[] astring)
        StatisticList.a();

        try {
            /* CraftBukkit start - replace everything
            boolean flag = false;
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
                dedicatedserver.m((String) options.valueOf("world"));
            }

            /*
            if (s != null) {
                dedicatedserver.l(s);
            }

            if (s2 != null) {
                dedicatedserver.m(s2);
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
                dedicatedserver.aj();
            }
            */

            dedicatedserver.primaryThread.start();
            // Runtime.getRuntime().addShutdownHook(new ThreadShutdown(dedicatedserver));
            // CraftBukkit end
        } catch (Exception exception) {
            log.log(Level.SEVERE, "Failed to start the minecraft server", exception);
        }
    }

    public void s() {
        // (new ThreadServerApplication(this, "Server thread")).start(); // CraftBukkit - prevent abuse
    }

    public File f(String s) {
        return new File(this.n(), s);
    }

    public void info(String s) {
        log.info(s);
    }

    public void warning(String s) {
        log.warning(s);
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

    public String t() {
        return this.serverIp;
    }

    public int u() {
        return this.s;
    }

    public String v() {
        return this.motd;
    }

    public String getVersion() {
        return "1.3.1";
    }

    public int x() {
        return this.t.getPlayerCount();
    }

    public int y() {
        return this.t.getMaxPlayers();
    }

    public String[] getPlayers() {
        return this.t.d();
    }

    public String getPlugins() {
        // CraftBukkit start - whole method
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

    public String i(String s) {
        RemoteControlCommandListener.instance.b();
        // CraftBukkit start
        RemoteServerCommandEvent event = new RemoteServerCommandEvent(this.remoteConsole, s);
        this.server.getPluginManager().callEvent(event);
        ServerCommand servercommand = new ServerCommand(event.getCommand(), RemoteControlCommandListener.instance);
        // this.q.a(RemoteControlCommandListener.instance, s);
        this.server.dispatchServerCommand(this.remoteConsole, servercommand); // CraftBukkit
        // CraftBukkit end
        return RemoteControlCommandListener.instance.c();
    }

    public boolean isDebugging() {
        return this.getPropertyManager().getBoolean("debug", false); // CraftBukkit - don't hardcode
    }

    public void j(String s) {
        log.log(Level.SEVERE, s);
    }

    public void k(String s) {
        if (this.isDebugging()) {
            log.log(Level.INFO, s);
        }
    }

    public String getServerModName() {
        return "craftbukkit"; // CraftBukkit - cb > vanilla!
    }

    public CrashReport b(CrashReport crashreport) {
        crashreport.a("Is Modded", (Callable) (new CrashReportModded(this)));
        crashreport.a("Profiler Position", (Callable) (new CrashReportProfilerPosition(this)));
        if (this.t != null) {
            crashreport.a("Player Count", (Callable) (new CrashReportPlayerCount(this)));
        }

        // CraftBukkit start
        if (this.worlds != null) {
            for (int j = 0; j < this.worlds.size(); ++j) {
                WorldServer worldserver = this.worlds.get(j);
                // CraftBukkit end

                if (worldserver != null) {
                    worldserver.a(crashreport);
                }
            }
        }

        return crashreport;
    }

    public List a(ICommandListener icommandlistener, String s) {
        ArrayList arraylist = new ArrayList();

        if (s.startsWith("/")) {
            s = s.substring(1);
            boolean flag = !s.contains(" ");
            List list = this.q.b(icommandlistener, s);

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
            String[] astring1 = this.t.d();
            int i = astring1.length;

            for (int j = 0; j < i; ++j) {
                String s3 = astring1[j];

                if (CommandAbstract.a(s2, s3)) {
                    arraylist.add(s3);
                }
            }

            return arraylist;
        }
    }

    public static MinecraftServer getServer() {
        return l;
    }

    public String getName() {
        return "Server";
    }

    public void sendMessage(String s) {
        log.info(StripColor.a(s));
    }

    public boolean b(String s) {
        return true;
    }

    public String a(String s, Object... aobject) {
        return LocaleLanguage.a().a(s, aobject);
    }

    public ICommandHandler getCommandHandler() {
        return this.q;
    }

    public KeyPair E() {
        return this.I;
    }

    public int F() {
        return this.s;
    }

    public void setPort(int i) {
        this.s = i;
    }

    public String G() {
        return this.J;
    }

    public void l(String s) {
        this.J = s;
    }

    public boolean H() {
        return this.J != null;
    }

    public String I() {
        return this.K;
    }

    public void m(String s) {
        this.K = s;
    }

    public void a(KeyPair keypair) {
        this.I = keypair;
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
                } else if (this.H()) {
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

    public boolean L() {
        return this.demoMode;
    }

    public void b(boolean flag) {
        this.demoMode = flag;
    }

    public void c(boolean flag) {
        this.N = flag;
    }

    public Convertable getConvertable() {
        return this.convertable;
    }

    public void O() {
        this.O = true;
        this.getConvertable().d();

        // CraftBukkit start - This needs review, what does it do? (it's new)
        for (int i = 0; i < this.worlds.size(); ++i) {
            WorldServer worldserver = this.worlds.get(i);
            // CraftBukkit end

            if (worldserver != null) {
                worldserver.saveLevel();
            }
        }

        this.getConvertable().e(this.worlds.get(0).getDataManager().g());
        this.safeShutdown();
    }

    public String getTexturePack() {
        return this.P;
    }

    public void setTexturePack(String s) {
        this.P = s;
    }

    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", Boolean.valueOf(false));
        mojangstatisticsgenerator.a("whitelist_count", Integer.valueOf(0));
        mojangstatisticsgenerator.a("players_current", Integer.valueOf(this.x()));
        mojangstatisticsgenerator.a("players_max", Integer.valueOf(this.y()));
        mojangstatisticsgenerator.a("players_seen", Integer.valueOf(this.t.getSeenPlayers().length));
        mojangstatisticsgenerator.a("uses_auth", Boolean.valueOf(this.onlineMode));
        mojangstatisticsgenerator.a("gui_state", this.ae() ? "enabled" : "disabled");
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
                mojangstatisticsgenerator.a("world[" + i + "][chunks_loaded]", Integer.valueOf(worldserver.F().getLoadedChunks()));
                ++i;
            // } // CraftBukkit
        }

        mojangstatisticsgenerator.a("worlds", Integer.valueOf(i));
    }

    public void b(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("singleplayer", Boolean.valueOf(this.H()));
        mojangstatisticsgenerator.a("server_brand", this.getServerModName());
        mojangstatisticsgenerator.a("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        mojangstatisticsgenerator.a("dedicated", Boolean.valueOf(this.S()));
    }

    public boolean getSnooperEnabled() {
        return true;
    }

    public int R() {
        return 16;
    }

    public abstract boolean S();

    public boolean getOnlineMode() {
        return this.onlineMode;
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

    public ServerConfigurationManagerAbstract getServerConfigurationManager() {
        return this.t;
    }

    public void a(ServerConfigurationManagerAbstract serverconfigurationmanagerabstract) {
        this.t = serverconfigurationmanagerabstract;
    }

    public void a(EnumGamemode enumgamemode) {
        // CraftBukkit start
        for (int i = 0; i < this.worlds.size(); ++i) {
            getServer().worlds.get(i).getWorldData().setGameType(enumgamemode);
            // CraftBukkit end
        }
    }

    public abstract ServerConnection ac();

    public boolean ae() {
        return false;
    }

    public abstract String a(EnumGamemode enumgamemode, boolean flag);

    public int af() {
        return this.ticks;
    }

    public void ag() {
        this.T = true;
    }

    public static ServerConfigurationManagerAbstract a(MinecraftServer minecraftserver) {
        return minecraftserver.t;
    }
}
