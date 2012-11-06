package net.minecraft.server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;

// CraftBukkit start
import java.io.PrintStream;

import org.bukkit.craftbukkit.LoggerOutputStream;
import org.bukkit.event.server.ServerCommandEvent;
// CraftBukkit end

public class DedicatedServer extends MinecraftServer implements IMinecraftServer {

    private final List l = Collections.synchronizedList(new ArrayList());
    private RemoteStatusListener m;
    private RemoteControlListener n;
    public PropertyManager propertyManager; // CraftBukkit - private -> public
    private boolean generateStructures;
    private EnumGamemode q;
    private ServerConnection r;
    private boolean s = false;

    // CraftBukkit start - Signature changed
    public DedicatedServer(joptsimple.OptionSet options) {
        super(options);
        // CraftBukkit end
        new ThreadSleepForever(this);
    }

    protected boolean init() throws java.net.UnknownHostException { // CraftBukkit - throws UnknownHostException
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this);

        threadcommandreader.setDaemon(true);
        threadcommandreader.start();
        ConsoleLogManager.init(this); // CraftBukkit

        // CraftBukkit start
        System.setOut(new PrintStream(new LoggerOutputStream(log, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(log, Level.SEVERE), true));
        // CraftBukkit end

        log.info("Starting minecraft server version 1.4.4");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            log.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        log.info("Loading properties");
        this.propertyManager = new PropertyManager(this.options); // CraftBukkit - CLI argument support
        if (this.I()) {
            this.d("127.0.0.1");
        } else {
            this.setOnlineMode(this.propertyManager.getBoolean("online-mode", true));
            this.d(this.propertyManager.getString("server-ip", ""));
        }

        this.setSpawnAnimals(this.propertyManager.getBoolean("spawn-animals", true));
        this.setSpawnNPCs(this.propertyManager.getBoolean("spawn-npcs", true));
        this.setPvP(this.propertyManager.getBoolean("pvp", true));
        this.setAllowFlight(this.propertyManager.getBoolean("allow-flight", false));
        this.setTexturePack(this.propertyManager.getString("texture-pack", ""));
        this.setMotd(this.propertyManager.getString("motd", "A Minecraft Server"));
        if (this.propertyManager.getInt("difficulty", 1) < 0) {
            this.propertyManager.a("difficulty", Integer.valueOf(0));
        } else if (this.propertyManager.getInt("difficulty", 1) > 3) {
            this.propertyManager.a("difficulty", Integer.valueOf(3));
        }

        this.generateStructures = this.propertyManager.getBoolean("generate-structures", true);
        int i = this.propertyManager.getInt("gamemode", EnumGamemode.SURVIVAL.a());

        this.q = WorldSettings.a(i);
        log.info("Default game type: " + this.q);
        InetAddress inetaddress = null;

        if (this.getServerIp().length() > 0) {
            inetaddress = InetAddress.getByName(this.getServerIp());
        }

        if (this.G() < 0) {
            this.setPort(this.propertyManager.getInt("server-port", 25565));
        }

        log.info("Generating keypair");
        this.a(MinecraftEncryption.b());
        log.info("Starting Minecraft server on " + (this.getServerIp().length() == 0 ? "*" : this.getServerIp()) + ":" + this.G());

        try {
            this.r = new DedicatedServerConnection(this, inetaddress, this.G());
        } catch (Throwable ioexception) { // CraftBukkit - IOException -> Throwable
            log.warning("**** FAILED TO BIND TO PORT!");
            log.log(Level.WARNING, "The exception was: " + ioexception.toString());
            log.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.getOnlineMode()) {
            log.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            log.warning("The server will make no attempt to authenticate usernames. Beware.");
            log.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            log.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        this.a((ServerConfigurationManagerAbstract) (new ServerConfigurationManager(this)));
        this.convertable = new WorldLoaderServer(server.getWorldContainer()); // CraftBukkit - moved from MinecraftServer constructor
        long j = System.nanoTime();

        if (this.J() == null) {
            this.l(this.propertyManager.getString("level-name", "world"));
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

        this.d(this.propertyManager.getInt("max-build-height", 256));
        this.d((this.getMaxBuildHeight() + 8) / 16 * 16);
        this.d(MathHelper.a(this.getMaxBuildHeight(), 64, 256));
        this.propertyManager.a("max-build-height", Integer.valueOf(this.getMaxBuildHeight()));
        log.info("Preparing level \"" + this.J() + "\"");
        this.a(this.J(), this.J(), k, worldtype, s2);
        long i1 = System.nanoTime() - j;
        String s3 = String.format("%.3fs", new Object[] { Double.valueOf((double) i1 / 1.0E9D)});

        log.info("Done (" + s3 + ")! For help, type \"help\" or \"?\"");
        if (this.propertyManager.getBoolean("enable-query", false)) {
            log.info("Starting GS4 status listener");
            this.m = new RemoteStatusListener(this);
            this.m.a();
        }

        if (this.propertyManager.getBoolean("enable-rcon", false)) {
            log.info("Starting remote control listener");
            this.n = new RemoteControlListener(this);
            this.n.a();
            this.remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(); // CraftBukkit
        }

        // CraftBukkit start
        if (this.server.getBukkitSpawnRadius() > -1) {
            log.info("'settings.spawn-radius' in bukkit.yml has been moved to 'spawn-protection' in server.properties. I will move your config for you.");
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
        return this.q;
    }

    public int getDifficulty() {
        return Math.max(0, Math.min(3, this.propertyManager.getInt("difficulty", 1))); // CraftBukkit - clamp values
    }

    public boolean isHardcore() {
        return this.propertyManager.getBoolean("hardcore", false);
    }

    protected void a(CrashReport crashreport) {
        while (this.isRunning()) {
            this.al();

            try {
                Thread.sleep(10L);
            } catch (InterruptedException interruptedexception) {
                interruptedexception.printStackTrace();
            }
        }
    }

    public CrashReport b(CrashReport crashreport) {
        crashreport = super.b(crashreport);
        crashreport.g().a("Is Modded", (Callable) (new CrashReportModded(this)));
        crashreport.g().a("Type", (Callable) (new CrashReportType(this)));
        return crashreport;
    }

    protected void p() {
        System.exit(0);
    }

    public void r() {
        super.r();
        this.al();
    }

    public boolean getAllowNether() {
        return this.propertyManager.getBoolean("allow-nether", true);
    }

    public boolean getSpawnMonsters() {
        return this.propertyManager.getBoolean("spawn-monsters", true);
    }

    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", Boolean.valueOf(this.am().getHasWhitelist()));
        mojangstatisticsgenerator.a("whitelist_count", Integer.valueOf(this.am().getWhitelisted().size()));
        super.a(mojangstatisticsgenerator);
    }

    public boolean getSnooperEnabled() {
        return this.propertyManager.getBoolean("snooper-enabled", true);
    }

    public void issueCommand(String s, ICommandListener icommandlistener) {
        this.l.add(new ServerCommand(s, icommandlistener));
    }

    public void al() {
        while (!this.l.isEmpty()) {
            ServerCommand servercommand = (ServerCommand) this.l.remove(0);

            // CraftBukkit start - ServerCommand for preprocessing
            ServerCommandEvent event = new ServerCommandEvent(this.console, servercommand.command);
            this.server.getPluginManager().callEvent(event);
            servercommand = new ServerCommand(event.getCommand(), servercommand.source);

            // this.getCommandHandler().a(servercommand.source, servercommand.command); // Called in dispatchServerCommand
            this.server.dispatchServerCommand(this.console, servercommand);
            // CraftBukkit end
        }
    }

    public boolean T() {
        return true;
    }

    public ServerConfigurationManager am() {
        return (ServerConfigurationManager) super.getServerConfigurationManager();
    }

    public ServerConnection ae() {
        return this.r;
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

    public String b_() {
        File file1 = this.propertyManager.c();

        return file1 != null ? file1.getAbsolutePath() : "No settings file";
    }

    public void an() {
        ServerGUI.a(this);
        this.s = true;
    }

    public boolean ag() {
        return this.s;
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

    public ServerConfigurationManagerAbstract getServerConfigurationManager() {
        return this.am();
    }
}
