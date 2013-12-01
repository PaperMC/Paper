package net.minecraft.server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.io.PrintStream;
import org.apache.logging.log4j.Level;

import org.bukkit.craftbukkit.LoggerOutputStream;
import org.bukkit.event.server.ServerCommandEvent;
// CraftBukkit end

public class DedicatedServer extends MinecraftServer implements IMinecraftServer {

    private static final Logger h = LogManager.getLogger();
    private final List i = Collections.synchronizedList(new ArrayList());
    private RemoteStatusListener j;
    private RemoteControlListener k;
    public PropertyManager propertyManager; // CraftBukkit - private -> public
    private boolean generateStructures;
    private EnumGamemode n;
    private boolean o;

    // CraftBukkit start - Signature changed
    public DedicatedServer(joptsimple.OptionSet options) {
        super(options, Proxy.NO_PROXY);
        // super(file1, Proxy.NO_PROXY);
        // CraftBukkit end
        new ThreadSleepForever(this, "Server Infinisleeper");
    }

    protected boolean init() throws java.net.UnknownHostException { // CraftBukkit - throws UnknownHostException
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this, "Server console handler");

        threadcommandreader.setDaemon(true);
        threadcommandreader.start();

        // CraftBukkit start - TODO: handle command-line logging arguments
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        global.setUseParentHandlers(false);
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }
        global.addHandler(new org.bukkit.craftbukkit.util.ForwardLogHandler());

        final org.apache.logging.log4j.core.Logger logger = ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger());
        for (org.apache.logging.log4j.core.Appender appender : logger.getAppenders().values()) {
            if (appender instanceof org.apache.logging.log4j.core.appender.ConsoleAppender) {
                logger.removeAppender(appender);
            }
        }

        new Thread(new org.bukkit.craftbukkit.util.TerminalConsoleWriterThread(System.out, this.reader)).start();

        System.setOut(new PrintStream(new LoggerOutputStream(logger, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(logger, Level.WARN), true));
        // CraftBukkit end

        h.info("Starting minecraft server version 1.7.2");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            h.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        h.info("Loading properties");
        this.propertyManager = new PropertyManager(this.options); // CraftBukkit - CLI argument support
        if (this.L()) {
            this.c("127.0.0.1");
        } else {
            this.setOnlineMode(this.propertyManager.getBoolean("online-mode", true));
            this.c(this.propertyManager.getString("server-ip", ""));
        }

        this.setSpawnAnimals(this.propertyManager.getBoolean("spawn-animals", true));
        this.setSpawnNPCs(this.propertyManager.getBoolean("spawn-npcs", true));
        this.setPvP(this.propertyManager.getBoolean("pvp", true));
        this.setAllowFlight(this.propertyManager.getBoolean("allow-flight", false));
        this.setTexturePack(this.propertyManager.getString("resource-pack", ""));
        this.setMotd(this.propertyManager.getString("motd", "A Minecraft Server"));
        this.setForceGamemode(this.propertyManager.getBoolean("force-gamemode", false));
        this.d(this.propertyManager.getInt("player-idle-timeout", 0));
        if (this.propertyManager.getInt("difficulty", 1) < 0) {
            this.propertyManager.a("difficulty", Integer.valueOf(0));
        } else if (this.propertyManager.getInt("difficulty", 1) > 3) {
            this.propertyManager.a("difficulty", Integer.valueOf(3));
        }

        this.generateStructures = this.propertyManager.getBoolean("generate-structures", true);
        int i = this.propertyManager.getInt("gamemode", EnumGamemode.SURVIVAL.a());

        this.n = WorldSettings.a(i);
        h.info("Default game type: " + this.n);
        InetAddress inetaddress = null;

        if (this.getServerIp().length() > 0) {
            inetaddress = InetAddress.getByName(this.getServerIp());
        }

        if (this.J() < 0) {
            this.setPort(this.propertyManager.getInt("server-port", 25565));
        }

        h.info("Generating keypair");
        this.a(MinecraftEncryption.b());
        h.info("Starting Minecraft server on " + (this.getServerIp().length() == 0 ? "*" : this.getServerIp()) + ":" + this.J());

        try {
            this.ag().a(inetaddress, this.J());
        } catch (Throwable ioexception) { // CraftBukkit - IOException -> Throwable
            h.warn("**** FAILED TO BIND TO PORT!");
            h.warn("The exception was: {}", new Object[] { ioexception.toString()});
            h.warn("Perhaps a server is already running on that port?");
            return false;
        }

        this.a((PlayerList) (new DedicatedPlayerList(this))); // CraftBukkit

        if (!this.getOnlineMode()) {
            h.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            h.warn("The server will make no attempt to authenticate usernames. Beware.");
            h.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            h.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        // this.a((PlayerList) (new DedicatedPlayerList(this))); // CraftBukkit - moved up
        this.convertable = new WorldLoaderServer(server.getWorldContainer()); // CraftBukkit - moved from MinecraftServer constructor
        long j = System.nanoTime();

        if (this.M() == null) {
            this.k(this.propertyManager.getString("level-name", "world"));
        }

        String s = this.propertyManager.getString("level-seed", "");
        String s1 = this.propertyManager.getString("level-type", "DEFAULT");
        String s2 = this.propertyManager.getString("generator-settings", "");
        long k = (new Random()).nextLong();

        if (s.length() > 0) {
            try {
                long l = Long.parseLong(s);

                if (l != 0L) {
                    k = l;
                }
            } catch (NumberFormatException numberformatexception) {
                k = (long) s.hashCode();
            }
        }

        WorldType worldtype = WorldType.getType(s1);

        if (worldtype == null) {
            worldtype = WorldType.NORMAL;
        }

        this.ar();
        this.getEnableCommandBlock();
        this.l();
        this.getSnooperEnabled();
        this.c(this.propertyManager.getInt("max-build-height", 256));
        this.c((this.getMaxBuildHeight() + 8) / 16 * 16);
        this.c(MathHelper.a(this.getMaxBuildHeight(), 64, 256));
        this.propertyManager.a("max-build-height", Integer.valueOf(this.getMaxBuildHeight()));
        h.info("Preparing level \"" + this.M() + "\"");
        this.a(this.M(), this.M(), k, worldtype, s2);
        long i1 = System.nanoTime() - j;
        String s3 = String.format("%.3fs", new Object[] { Double.valueOf((double) i1 / 1.0E9D)});

        h.info("Done (" + s3 + ")! For help, type \"help\" or \"?\"");
        if (this.propertyManager.getBoolean("enable-query", false)) {
            h.info("Starting GS4 status listener");
            this.j = new RemoteStatusListener(this);
            this.j.a();
        }

        if (this.propertyManager.getBoolean("enable-rcon", false)) {
            h.info("Starting remote control listener");
            this.k = new RemoteControlListener(this);
            this.k.a();
            this.remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(); // CraftBukkit
        }

        // CraftBukkit start
        if (this.server.getBukkitSpawnRadius() > -1) {
            h.info("'settings.spawn-radius' in bukkit.yml has been moved to 'spawn-protection' in server.properties. I will move your config for you.");
            this.propertyManager.properties.remove("spawn-protection");
            this.propertyManager.getInt("spawn-protection", this.server.getBukkitSpawnRadius());
            this.server.removeBukkitSpawnRadius();
            this.propertyManager.savePropertiesFile();
        }

        return true;
    }

    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }
    // CraftBukkit end

    public boolean getGenerateStructures() {
        return this.generateStructures;
    }

    public EnumGamemode getGamemode() {
        return this.n;
    }

    public EnumDifficulty getDifficulty() {
        return EnumDifficulty.a(this.propertyManager.getInt("difficulty", 1));
    }

    public boolean isHardcore() {
        return this.propertyManager.getBoolean("hardcore", false);
    }

    protected void a(CrashReport crashreport) {
        while (this.isRunning()) {
            this.aw();

            try {
                Thread.sleep(10L);
            } catch (InterruptedException interruptedexception) {
                ;
            }
        }
    }

    public CrashReport b(CrashReport crashreport) {
        crashreport = super.b(crashreport);
        crashreport.g().a("Is Modded", (Callable) (new CrashReportModded(this)));
        crashreport.g().a("Type", (Callable) (new CrashReportType(this)));
        return crashreport;
    }

    protected void s() {
        System.exit(0);
    }

    public void u() { // CraftBukkit - protected -> public
        super.u();
        this.aw();
    }

    public boolean getAllowNether() {
        return this.propertyManager.getBoolean("allow-nether", true);
    }

    public boolean getSpawnMonsters() {
        return this.propertyManager.getBoolean("spawn-monsters", true);
    }

    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", Boolean.valueOf(this.ax().getHasWhitelist()));
        mojangstatisticsgenerator.a("whitelist_count", Integer.valueOf(this.ax().getWhitelisted().size()));
        super.a(mojangstatisticsgenerator);
    }

    public boolean getSnooperEnabled() {
        return this.propertyManager.getBoolean("snooper-enabled", true);
    }

    public void issueCommand(String s, ICommandListener icommandlistener) {
        this.i.add(new ServerCommand(s, icommandlistener));
    }

    public void aw() {
        while (!this.i.isEmpty()) {
            ServerCommand servercommand = (ServerCommand) this.i.remove(0);

            // CraftBukkit start - ServerCommand for preprocessing
            ServerCommandEvent event = new ServerCommandEvent(this.console, servercommand.command);
            this.server.getPluginManager().callEvent(event);
            servercommand = new ServerCommand(event.getCommand(), servercommand.source);

            // this.getCommandHandler().a(servercommand.source, servercommand.command); // Called in dispatchServerCommand
            this.server.dispatchServerCommand(this.console, servercommand);
            // CraftBukkit end
        }
    }

    public boolean V() {
        return true;
    }

    public DedicatedPlayerList ax() {
        return (DedicatedPlayerList) super.getPlayerList();
    }

    public int a(String s, int i) {
        return this.propertyManager.getInt(s, i);
    }

    public String a(String s, String s1) {
        return this.propertyManager.getString(s, s1);
    }

    public boolean a(String s, boolean flag) {
        return this.propertyManager.getBoolean(s, flag);
    }

    public void a(String s, Object object) {
        this.propertyManager.a(s, object);
    }

    public void a() {
        this.propertyManager.savePropertiesFile();
    }

    public String b() {
        File file1 = this.propertyManager.c();

        return file1 != null ? file1.getAbsolutePath() : "No settings file";
    }

    public void ay() {
        ServerGUI.a(this);
        this.o = true;
    }

    public boolean ai() {
        return this.o;
    }

    public String a(EnumGamemode enumgamemode, boolean flag) {
        return "";
    }

    public boolean getEnableCommandBlock() {
        return this.propertyManager.getBoolean("enable-command-block", false);
    }

    public int getSpawnProtection() {
        return this.propertyManager.getInt("spawn-protection", super.getSpawnProtection());
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.worldProvider.dimension != 0) {
            return false;
        } else if (this.ax().getOPs().isEmpty()) {
            return false;
        } else if (this.ax().isOp(entityhuman.getName())) {
            return false;
        } else if (this.getSpawnProtection() <= 0) {
            return false;
        } else {
            ChunkCoordinates chunkcoordinates = world.getSpawn();
            int l = MathHelper.a(i - chunkcoordinates.x);
            int i1 = MathHelper.a(k - chunkcoordinates.z);
            int j1 = Math.max(l, i1);

            return j1 <= this.getSpawnProtection();
        }
    }

    public int l() {
        return this.propertyManager.getInt("op-permission-level", 4);
    }

    public void d(int i) {
        super.d(i);
        this.propertyManager.a("player-idle-timeout", Integer.valueOf(i));
        this.a();
    }

    public boolean ar() {
        return this.propertyManager.getBoolean("announce-player-achievements", true);
    }

    public PlayerList getPlayerList() {
        return this.ax();
    }

    static Logger az() {
        return h;
    }
}
