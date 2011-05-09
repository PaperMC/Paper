package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

// CraftBukkit start
import java.io.PrintStream;
import java.net.UnknownHostException;
import jline.ConsoleReader;
import joptsimple.OptionSet;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.LoggerOutputStream;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.util.ServerShutdownThread;
import org.bukkit.event.world.WorldSaveEvent;
// CraftBukkit

public class MinecraftServer implements Runnable, ICommandListener {

    public static Logger log = Logger.getLogger("Minecraft");
    public static HashMap b = new HashMap();
    public NetworkListenThread networkListenThread;
    public PropertyManager propertyManager;
    // public WorldServer worldServer; // CraftBukkit - removed
    public ServerConfigurationManager serverConfigurationManager;
    public ConsoleCommandHandler consoleCommandHandler; // CraftBukkit - made public
    private boolean isRunning = true;
    public boolean isStopped = false;
    int ticks = 0;
    public String i;
    public int j;
    private List r = new ArrayList();
    private List s = Collections.synchronizedList(new ArrayList());
    public EntityTracker tracker;
    public boolean onlineMode;
    public boolean spawnAnimals;
    public boolean pvpMode;
    public boolean o;

    // CraftBukkit start
    public int spawnProtection;
    public List<WorldServer> worlds = new ArrayList<WorldServer>();
    public CraftServer server;
    public OptionSet options;
    public ColouredConsoleSender console;
    public ConsoleReader reader;
    public static int currentTick;
    // Craftbukkit end

    public MinecraftServer(OptionSet options) { // CraftBukkit - adds argument OptionSet
        new ThreadSleepForever(this);

         // CraftBukkit start
        this.options = options;
        try {
            this.reader = new ConsoleReader();
        } catch (IOException ex) {
            Logger.getLogger(MinecraftServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runtime.getRuntime().addShutdownHook(new ServerShutdownThread(this));
        // CraftBukkit end
    }

    private boolean init() throws UnknownHostException { // CraftBukkit - added throws UnknownHostException
        this.consoleCommandHandler = new ConsoleCommandHandler(this);
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this);

        threadcommandreader.setDaemon(true);
        threadcommandreader.start();
        ConsoleLogManager.init(this); // Craftbukkit

        // CraftBukkit start
        System.setOut(new PrintStream(new LoggerOutputStream(log, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(log, Level.SEVERE), true));
        // CraftBukkit end

        log.info("Starting minecraft server version Beta 1.5_02");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            log.warning("**** NOT ENOUGH RAM!");
            log.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        log.info("Loading properties");
        this.propertyManager = new PropertyManager(options); // CraftBukkit - CLI argument support
        String s = this.propertyManager.getString("server-ip", "");

        this.onlineMode = this.propertyManager.getBoolean("online-mode", true);
        this.spawnAnimals = this.propertyManager.getBoolean("spawn-animals", true);
        this.pvpMode = this.propertyManager.getBoolean("pvp", true);
        this.o = this.propertyManager.getBoolean("allow-flight", false);
        this.spawnProtection = this.propertyManager.getInt("spawn-protection", 16); // CraftBukkit Configurable spawn protection start
        InetAddress inetaddress = null;

        if (s.length() > 0) {
            inetaddress = InetAddress.getByName(s);
        }

        int i = this.propertyManager.getInt("server-port", 25565);

        log.info("Starting Minecraft server on " + (s.length() == 0 ? "*" : s) + ":" + i);

        try {
            this.networkListenThread = new NetworkListenThread(this, inetaddress, i);
        } catch (Throwable ioexception) { // CraftBukkit - IOException -> Throwable
            log.warning("**** FAILED TO BIND TO PORT!");
            log.log(Level.WARNING, "The exception was: " + ioexception.toString());
            log.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.onlineMode) {
            log.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            log.warning("The server will make no attempt to authenticate usernames. Beware.");
            log.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            log.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
        }

        this.serverConfigurationManager = new ServerConfigurationManager(this);
        this.tracker = new EntityTracker(this);
        long j = System.nanoTime();
        String s1 = this.propertyManager.getString("level-name", "world");
        String s2 = this.propertyManager.getString("level-seed", "");
        long k = (new Random()).nextLong();

        if (s2.length() > 0) {
            try {
                k = Long.parseLong(s2);
            } catch (NumberFormatException numberformatexception) {
                k = (long) s2.hashCode();
            }
        }

        log.info("Preparing level \"" + s1 + "\"");
        this.a(new WorldLoaderServer(new File(".")), s1, k);

        // CraftBukkit start
        long elapsed = System.nanoTime() - j;
        String time = String.format( "%.3fs", elapsed / 10000000000.0D );
        log.info("Done (" + time + ")! For help, type \"help\" or \"?\"");
        // CraftBukkit end

        return true;
    }

    private void a(Convertable convertable, String s, long i) {
        if (convertable.isConvertable(s)) {
            log.info("Converting map!");
            convertable.convert(s, new ConvertProgressUpdater(this));
        }

        log.info("Preparing start region");

        // CraftBukkit start
        WorldServer world = new WorldServer(this, new ServerNBTManager(new File("."), s, true), s, this.propertyManager.getBoolean("hellworld", false) ? -1 : 0, i);
        world.addIWorldAccess((IWorldAccess)new WorldManager(this, world));
        world.spawnMonsters = this.propertyManager.getBoolean("spawn-monsters", true) ? 1 : 0;
        world.setSpawnFlags(this.propertyManager.getBoolean("spawn-monsters", true), this.spawnAnimals);
        this.serverConfigurationManager.setPlayerFileData(world);
        worlds.add(world);
        // CraftBukkit end

        short short1 = 196;
        long j = System.currentTimeMillis();
        ChunkCoordinates chunkcoordinates = world.getSpawn(); // CraftBukkit

        for (int k = -short1; k <= short1 && this.isRunning; k += 16) {
            for (int l = -short1; l <= short1 && this.isRunning; l += 16) {
                long i1 = System.currentTimeMillis();

                if (i1 < j) {
                    j = i1;
                }

                if (i1 > j + 1000L) {
                    int j1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                    int k1 = (k + short1) * (short1 * 2 + 1) + l + 1;

                    this.a("Preparing spawn area", k1 * 100 / j1);
                    j = i1;
                }

                // CraftBukkit start
                for (WorldServer worldserver: worlds) {
                    world.chunkProviderServer.getChunkAt(chunkcoordinates.x + k >> 4, chunkcoordinates.z + l >> 4);

                    while (world.doLighting() && this.isRunning) {
                        ;
                    }
                }
                // CraftBukkit end
            }
        }

        this.e();
    }

    private void a(String s, int i) {
        this.i = s;
        this.j = i;
        log.info(s + ": " + i + "%");
    }

    private void e() {
        this.i = null;
        this.j = 0;

        server.loadPlugins(); // CraftBukkit
    }

    void saveChunks() { // CraftBukkit - private -> default
        log.info("Saving chunks");

        // CraftBukkit start
        for (WorldServer world: worlds) {
            world.save(true, (IProgressUpdate) null);
            world.saveLevel();

            WorldSaveEvent event = new WorldSaveEvent( world.getWorld() );
            server.getPluginManager().callEvent( event );
        }

        this.serverConfigurationManager.savePlayers(); // CraftBukkit - player data should be saved whenever a save happens.
        // CraftBukkit end
    }

    public void stop() { // Craftbukkit: private -> public
        log.info("Stopping server");
        // CraftBukkit start
        if (server != null) {
            server.disablePlugins();
        }
        // CraftBukkit end

        if (this.serverConfigurationManager != null) {
            this.serverConfigurationManager.savePlayers();
        }

        if (this.worlds.size() > 0) { // CraftBukkit
            this.saveChunks();
        }
    }

    public void a() {
        this.isRunning = false;
    }

    public void run() {
        try {
            if (this.init()) {
                long i = System.currentTimeMillis();

                for (long j = 0L; this.isRunning; Thread.sleep(1L)) {
                    long k = System.currentTimeMillis();
                    long l = k - i;

                    if (l > 2000L) {
                        log.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        l = 2000L;
                    }

                    if (l < 0L) {
                        log.warning("Time ran backwards! Did the system time change?");
                        l = 0L;
                    }

                    j += l;
                    i = k;
                    // CraftBukkit - TODO - Replace with loop?
                    if (this.worlds.size() > 0 && this.worlds.get(0).everyoneDeeplySleeping()) {
                        this.h();
                        j = 0L;
                    } else {
                        while (j > 50L) {
                            MinecraftServer.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
                            j -= 50L;
                            this.h();
                        }
                    }
                }
            } else {
                while (this.isRunning) {
                    this.b();

                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException interruptedexception) {
                        interruptedexception.printStackTrace();
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.log(Level.SEVERE, "Unexpected exception", throwable);

            while (this.isRunning) {
                this.b();

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException interruptedexception1) {
                    interruptedexception1.printStackTrace();
                }
            }
        } finally {
            try {
                this.stop();
                this.isStopped = true;
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    }

    private void h() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = b.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            int i = ((Integer) b.get(s)).intValue();

            if (i > 0) {
                b.put(s, Integer.valueOf(i - 1));
            } else {
                arraylist.add(s);
            }
        }

        int j;

        for (j = 0; j < arraylist.size(); ++j) {
            b.remove(arraylist.get(j));
        }

        AxisAlignedBB.a();
        Vec3D.a();
        ++this.ticks;

        // CraftBukkit start
        if (this.ticks % 20 == 0) {
            for (int i = 0; i < this.serverConfigurationManager.players.size(); ++i) {
                EntityPlayer entityplayer = (EntityPlayer) this.serverConfigurationManager.players.get(i);
                entityplayer.netServerHandler.sendPacket(new Packet4UpdateTime(entityplayer.world.getTime()));
            }
        }

        ((CraftScheduler) server.getScheduler()).mainThreadHeartbeat(this.ticks);

        for (WorldServer world: worlds) {
            world.doTick();

            while (world.doLighting()) {
                ;
            }

            world.cleanUp();
        }
        // CraftBukkit end
        this.networkListenThread.a();
        this.serverConfigurationManager.b();
        this.tracker.a();

        for (j = 0; j < this.r.size(); ++j) {
            ((IUpdatePlayerListBox) this.r.get(j)).a();
        }

        try {
            this.b();
        } catch (Exception exception) {
            log.log(Level.WARNING, "Unexpected exception while parsing console command", exception);
        }
    }

    public void issueCommand(String s, ICommandListener icommandlistener) {
        this.s.add(new ServerCommand(s, icommandlistener));
    }

    public void b() {
        while (this.s.size() > 0) {
            ServerCommand servercommand = (ServerCommand) this.s.remove(0);

            // this.consoleCommandHandler.handle(servercommand); // CraftBukkit - Removed its now called in server.displatchCommand
            server.dispatchCommand(console, servercommand); // CraftBukkit
        }
    }

    public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
        this.r.add(iupdateplayerlistbox);
    }

    public static void main(final OptionSet options) { // CraftBukkit - replaces main(String args[])
        StatisticList.a();

        try {
            MinecraftServer minecraftserver = new MinecraftServer(options);

            // CraftBukkit - remove gui

            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        } catch (Exception exception) {
            log.log(Level.SEVERE, "Failed to start the minecraft server", exception);
        }
    }

    public File a(String s) {
        return new File(s);
    }

    public void sendMessage(String s) {
        log.info(s);
    }

    public void c(String s) {
        log.warning(s);
    }

    public String getName() {
        return "CONSOLE";
    }

    public static boolean isRunning(MinecraftServer minecraftserver) {
        return minecraftserver.isRunning;
    }
}
