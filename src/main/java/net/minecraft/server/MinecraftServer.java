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
import java.util.logging.Level;
import java.util.logging.Logger;

// CraftBukkit start
import java.io.PrintStream;
import java.net.UnknownHostException;
import jline.ConsoleReader;
import joptsimple.OptionSet;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.LoggerOutputStream;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldEvent;
// CraftBukkit end

public class MinecraftServer implements Runnable, ICommandListener {

    public static Logger a = Logger.getLogger("Minecraft");
    public static HashMap b = new HashMap();
    public NetworkListenThread c;
    public PropertyManager d;
    // public WorldServer e; // CraftBukkit - removed
    public ServerConfigurationManager f;
    private ConsoleCommandHandler o;
    private boolean p = true;
    public boolean g = false;
    int h = 0;
    public String i;
    public int j;
    private List q = new ArrayList();
    private List r = Collections.synchronizedList(new ArrayList());
    public EntityTracker k;
    public boolean l;
    public boolean m;
    public boolean n;

    // CraftBukkit start
    public int spawnProtection;
    public List<WorldServer> worlds = new ArrayList<WorldServer>();
    public CraftServer server;
    public OptionSet options;
    public ColouredConsoleSender console;
    public ConsoleReader reader;
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
        // CraftBukkit end
    }

    private boolean d() throws UnknownHostException { // CraftBukkit - added throws UnknownHostException
        this.o = new ConsoleCommandHandler(this);
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this);

        threadcommandreader.setDaemon(true);
        threadcommandreader.start();
        ConsoleLogManager.a(reader); // Craftbukkit

        // CraftBukkit start
        System.setOut(new PrintStream(new LoggerOutputStream(a, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(a, Level.SEVERE), true));
        // CraftBukkit end

        a.info("Starting minecraft server version Beta 1.3");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            a.warning("**** NOT ENOUGH RAM!");
            a.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        a.info("Loading properties");
        this.d = new PropertyManager(options); // CraftBukkit
        String s = this.d.a("server-ip", "");

        this.l = this.d.a("online-mode", true);
        this.m = this.d.a("spawn-animals", true);
        this.n = this.d.a("pvp", true);
        this.spawnProtection = this.d.a("spawn-protection", 16); // CraftBukkit Configurable spawn protection start
        InetAddress inetaddress = null;

        if (s.length() > 0) {
            inetaddress = InetAddress.getByName(s);
        }

        int i = this.d.a("server-port", 25565);

        a.info("Starting Minecraft server on " + (s.length() == 0 ? "*" : s) + ":" + i);

        try {
            this.c = new NetworkListenThread(this, inetaddress, i);
        } catch (Throwable ioexception) { // CraftBukkit - IOException -> Throwable
            a.warning("**** FAILED TO BIND TO PORT!");
            a.log(Level.WARNING, "The exception was: " + ioexception.toString());
            a.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.l) {
            a.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            a.warning("The server will make no attempt to authenticate usernames. Beware.");
            a.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            a.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
        }

        this.f = new ServerConfigurationManager(this);
        this.k = new EntityTracker(this);
        long j = System.nanoTime();
        String s1 = this.d.a("level-name", "world");

        a.info("Preparing level \"" + s1 + "\"");
        this.a((Convertable) (new WorldLoaderServer(new File("."))), s1);

        // CraftBukkit start
        long elapsed = System.nanoTime() - j;
        String time = String.format( "%.3fs", elapsed / 10000000000.0D );
        a.info("Done (" + time + ")! For help, type \"help\" or \"?\"");
        // CraftBukkit end

        return true;
    }

    private void a(Convertable convertable, String s) {
        if (convertable.a(s)) {
            a.info("Converting map!");
            convertable.a(s, new ConvertProgressUpdater(this));
        }

        a.info("Preparing start region");

        // CraftBukkit start
        WorldServer world = new WorldServer(this, new ServerNBTManager(new File("."), s, true), s, this.d.a("hellworld", false) ? -1 : 0);
        world.a(new WorldManager(this, world));
        world.j = this.d.a("spawn-monsters", true) ? 1 : 0;
        world.a(this.d.a("spawn-monsters", true), this.m);
        this.f.a(world);
        worlds.add(world);
        // CraftBukkit end

        short short1 = 196;
        long i = System.currentTimeMillis();
        ChunkCoordinates chunkcoordinates = worlds.get(0).l(); // CraftBukkit

        for (int j = -short1; j <= short1 && this.p; j += 16) {
            for (int k = -short1; k <= short1 && this.p; k += 16) {
                long l = System.currentTimeMillis();

                if (l < i) {
                    i = l;
                }

                if (l > i + 1000L) {
                    int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                    int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                    this.a("Preparing spawn area", j1 * 100 / i1);
                    i = l;
                }

                // CraftBukkit start
                for (WorldServer worldserver: worlds) {
                    world.u.d(chunkcoordinates.a + j >> 4, chunkcoordinates.c + k >> 4);

                    while (world.e() && this.p) {
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
        a.info(s + ": " + i + "%");
    }

    private void e() {
        this.i = null;
        this.j = 0;

        server.loadPlugins(); // CraftBukkit
    }

    void f() { //CraftBukkit - private -> default
        a.info("Saving chunks");

        // CraftBukkit start
        for (WorldServer world: worlds) {
            world.a(true, (IProgressUpdate) null);
            world.r();

            WorldEvent event = new WorldEvent( Event.Type.WORLD_SAVED, world.getWorld() );
            server.getPluginManager().callEvent( event );
        }

        this.f.d(); // CraftBukkit - player data should be saved whenever a save happens.
        // CraftBukkit end
    }

    private void g() {
        a.info("Stopping server");
        // CraftBukkit start
        if (server != null) {
            server.disablePlugins();
        }
        // CraftBukkit end

        if (this.f != null) {
            this.f.d();
        }

        if (this.worlds.size() > 0) { // CraftBukkit
            this.f();
        }
    }

    public void a() {
        this.p = false;
    }

    public void run() {
        try {
            if (this.d()) {
                long i = System.currentTimeMillis();

                for (long j = 0L; this.p; Thread.sleep(1L)) {
                    long k = System.currentTimeMillis();
                    long l = k - i;

                    if (l > 2000L) {
                        a.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        l = 2000L;
                    }

                    if (l < 0L) {
                        a.warning("Time ran backwards! Did the system time change?");
                        l = 0L;
                    }

                    j += l;
                    i = k;
                    // CraftBukkit - TODO - Replace with loop?
                    if (this.worlds.size() > 0 && this.worlds.get(0).q()) {
                        this.h();
                        j = 0L;
                    } else {
                        while (j > 50L) {
                            j -= 50L;
                            this.h();
                        }
                    }
                }
            } else {
                while (this.p) {
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
            a.log(Level.SEVERE, "Unexpected exception", throwable);

            while (this.p) {
                this.b();

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException interruptedexception1) {
                    interruptedexception1.printStackTrace();
                }
            }
        } finally {
            try {
                this.g();
                this.g = true;
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
        ++this.h;

        // CraftBukkit start
        if (this.h % 20 == 0) {
            for (int i = 0; i < this.f.b.size(); ++i) {
                EntityPlayer entityplayer = (EntityPlayer) this.f.b.get(i);
                entityplayer.a.b((Packet) (new Packet4UpdateTime(entityplayer.world.k())));
            }
        }

        ((CraftScheduler) server.getScheduler()).mainThreadHeartbeat(this.h);

        for (WorldServer world: worlds) {
            world.g();

            while (world.e()) {
                ;
            }

            world.d();
        }
        // CraftBukkit end
        this.c.a();
        this.f.b();
        this.k.a();

        for (j = 0; j < this.q.size(); ++j) {
            ((IUpdatePlayerListBox) this.q.get(j)).a();
        }

        try {
            this.b();
        } catch (Exception exception) {
            a.log(Level.WARNING, "Unexpected exception while parsing console command", exception);
        }
    }

    public void a(String s, ICommandListener icommandlistener) {
        this.r.add(new ServerCommand(s, icommandlistener));
    }

    public void b() {
        while (this.r.size() > 0) {
            ServerCommand servercommand = (ServerCommand) this.r.remove(0);

            // CraftBukkit start
            if (server.dispatchCommand(console, servercommand.a)) {
                continue;
            }
            // CraftBukkit end

            this.o.a(servercommand);
        }
    }

    public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
        this.q.add(iupdateplayerlistbox);
    }

    public static void main(final OptionSet options) { // CraftBukkit - replaces main(String args[])
        try {
            MinecraftServer minecraftserver = new MinecraftServer(options);

            // CraftBukkit - remove gui

            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        } catch (Exception exception) {
            a.log(Level.SEVERE, "Failed to start the minecraft server", exception);
        }
    }

    public File a(String s) {
        return new File(s);
    }

    public void b(String s) {
        a.info(s);
    }

    public String c() {
        return "CONSOLE";
    }

    public static boolean a(MinecraftServer minecraftserver) {
        return minecraftserver.p;
    }
}
