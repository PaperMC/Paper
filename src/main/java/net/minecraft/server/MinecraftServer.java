package net.minecraft.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

// CraftBukkit start
import java.net.UnknownHostException;
import joptsimple.OptionSet;
import org.bukkit.craftbukkit.CraftServer;
// CraftBukkit end

public class MinecraftServer implements ICommandListener, Runnable {

    public static Logger a = Logger.getLogger("Minecraft");
    public static HashMap b = new HashMap();
    public NetworkListenThread c;
    public PropertyManager d;
    public WorldServer e;
    public ServerConfigurationManager f;
    private boolean o;
    public boolean g;
    int h;
    public String i;
    public int j;
    private List p;
    private List q;
    public EntityTracker k;
    public boolean l;
    public boolean m;
    public boolean n;

    public CraftServer server; // CraftBukkit
    public OptionSet options; // CraftBukkit

    // CraftBukkit: Added arg "OptionSet options"
    public MinecraftServer(final OptionSet options) {
        o = true;
        g = false;
        h = 0;
        p = ((List) (new ArrayList()));
        q = Collections.synchronizedList(((List) (new ArrayList())));
        new ThreadSleepForever(this);

        this.options = options; // CraftBukkit
    }

    // CraftBukkit: added throws UnknownHostException
    private boolean d() throws UnknownHostException {
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this);

        ((Thread) (threadcommandreader)).setDaemon(true);
        ((Thread) (threadcommandreader)).start();
        ConsoleLogManager.a();
        a.info("Starting minecraft server version Beta 1.2_01");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            a.warning("**** NOT ENOUGH RAM!");
            a.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }
        a.info("Loading properties");
        d = new PropertyManager(options);
        String s = d.a("server-ip", "");

        l = d.a("online-mode", true);
        m = d.a("spawn-animals", true);
        n = d.a("pvp", true);
        InetAddress inetaddress = null;

        if (s.length() > 0) {
            inetaddress = InetAddress.getByName(s);
        }
        int i1 = d.a("server-port", 25565);

        a.info((new StringBuilder()).append("Starting Minecraft server on ").append(s.length() != 0 ? s : "*").append(":").append(i1).toString());
        try {
            c = new NetworkListenThread(this, inetaddress, i1);
            // CraftBukkit: Be more generic; IOException -> Throwable
        } catch (Throwable ioexception) {
            a.warning("**** FAILED TO BIND TO PORT!");
            a.log(Level.WARNING, (new StringBuilder()).append("The exception was: ").append(ioexception.toString()).toString());
            a.warning("Perhaps a server is already running on that port?");
            return false;
        }
        if (!l) {
            a.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            a.warning("The server will make no attempt to authenticate usernames. Beware.");
            a.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            a.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
        }
        f = new ServerConfigurationManager(this);
        k = new EntityTracker(this);
        String s1 = d.a("level-name", "world");

        a.info((new StringBuilder()).append("Preparing level \"").append(s1).append("\"").toString());
        c(s1);
        a.info("Done! For help, type \"help\" or \"?\"");
        return true;
    }

    private void c(String s) {
        a.info("Preparing start region");
        e = new WorldServer(this, new File("."), s, d.a("hellworld", false) ? -1 : 0);
        e.a(((IWorldAccess) (new WorldManager(this))));
        e.k = d.a("spawn-monsters", true) ? 1 : 0;
        e.a(d.a("spawn-monsters", true), m);
        f.a(e);
        char c1 = '\304';
        long l1 = System.currentTimeMillis();

        for (int i1 = -c1; i1 <= c1 && o; i1 += 16) {
            for (int j1 = -c1; j1 <= c1 && o; j1 += 16) {
                long l2 = System.currentTimeMillis();

                if (l2 < l1) {
                    l1 = l2;
                }
                if (l2 > l1 + 1000L) {
                    int k1 = (c1 * 2 + 1) * (c1 * 2 + 1);
                    int i2 = (i1 + c1) * (c1 * 2 + 1) + (j1 + 1);

                    a("Preparing spawn area", (i2 * 100) / k1);
                    l1 = l2;
                }
                e.A.d(e.m + i1 >> 4, e.o + j1 >> 4);
                while (e.d() && o) {
                    ;
                }
            }
        }

        e();
    }

    private void a(String s, int i1) {
        i = s;
        j = i1;
        System.out.println((new StringBuilder()).append(s).append(": ").append(i1).append("%").toString());
    }

    private void e() {
        i = null;
        j = 0;

        server.loadPlugins(); // CraftBukkit
    }

    private void f() {
        a.info("Saving chunks");
        e.a(true, ((IProgressUpdate) (null)));
    }

    private void g() {
        a.info("Stopping server");
        if(server != null) {
            server.disablePlugins();
        }

        if (f != null) {
            f.d();
        }
        if (e != null) {
            f();
        }
    }

    public void a() {
        o = false;
    }

    public void run() {
        try {
            if (d()) {
                long l1 = System.currentTimeMillis();
                long l2 = 0L;

                while (o) {
                    long l3 = System.currentTimeMillis();
                    long l4 = l3 - l1;

                    if (l4 > 2000L) {
                        a.warning("Can't keep up! Did the system time change, or is the server overloaded?");
                        l4 = 2000L;
                    }
                    if (l4 < 0L) {
                        a.warning("Time ran backwards! Did the system time change?");
                        l4 = 0L;
                    }
                    l2 += l4;
                    l1 = l3;
                    while (l2 > 50L) {
                        l2 -= 50L;
                        h();
                    }
                    Thread.sleep(1L);
                }
            } else {
                while (o) {
                    b();
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException interruptedexception) {
                        interruptedexception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            a.log(Level.SEVERE, "Unexpected exception", ((Throwable) (exception)));
            while (o) {
                b();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException interruptedexception1) {
                    interruptedexception1.printStackTrace();
                }
            }
        } finally {
            try {
                g();
                g = true;
            } finally {
                System.exit(0);
            }
        }
    }

    private void h() {
        ArrayList arraylist = new ArrayList();

        for (Iterator iterator = b.keySet().iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            int k1 = ((Integer) b.get(((s)))).intValue();

            if (k1 > 0) {
                b.put(((s)), ((Integer.valueOf(k1 - 1))));
            } else {
                ((List) (arraylist)).add(((s)));
            }
        }

        for (int i1 = 0; i1 < ((List) (arraylist)).size(); i1++) {
            b.remove(((List) (arraylist)).get(i1));
        }

        AxisAlignedBB.a();
        Vec3D.a();
        h++;
        if (h % 20 == 0) {
            f.a(((Packet) (new Packet4UpdateTime(e.e))));
        }
        e.f();
        while (e.d()) {
            ;
        }
        e.c();
        c.a();
        f.b();
        k.a();
        for (int j1 = 0; j1 < p.size(); j1++) {
            ((IUpdatePlayerListBox) p.get(j1)).a();
        }

        try {
            b();
        } catch (Exception exception) {
            a.log(Level.WARNING, "Unexpected exception while parsing console command", ((Throwable) (exception)));
        }
    }

    public void a(String s, ICommandListener icommandlistener) {
        q.add(((new ServerCommand(s, icommandlistener))));
    }

    public void b() {
        do {
            if (q.size() <= 0) {
                break;
            }
            ServerCommand servercommand = (ServerCommand) q.remove(0);
            String s = servercommand.a;
            ICommandListener icommandlistener = servercommand.b;
            String s1 = icommandlistener.c();

            if (s.toLowerCase().startsWith("#help") || s.toLowerCase().startsWith("#?")) {
                icommandlistener.b("To run the server without a gui, start it like this:");
                icommandlistener.b("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
                icommandlistener.b("Console commands:");
                icommandlistener.b("   help  or  ?               shows this message");
                icommandlistener.b("   kick <player>             removes a player from the server");
                icommandlistener.b("   ban <player>              bans a player from the server");
                icommandlistener.b("   pardon <player>           pardons a banned player so that they can connect again");
                icommandlistener.b("   ban-ip <ip>               bans an IP address from the server");
                icommandlistener.b("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
                icommandlistener.b("   op <player>               turns a player into an op");
                icommandlistener.b("   deop <player>             removes op status from a player");
                icommandlistener.b("   tp <player1> <player2>    moves one player to the same location as another player");
                icommandlistener.b("   give <player> <id> [num]  gives a player a resource");
                icommandlistener.b("   tell <player> <message>   sends a private message to a player");
                icommandlistener.b("   stop                      gracefully stops the server");
                icommandlistener.b("   save-all                  forces a server-wide level save");
                icommandlistener.b("   save-off                  disables terrain saving (useful for backup scripts)");
                icommandlistener.b("   save-on                   re-enables terrain saving");
                icommandlistener.b("   list                      lists all currently connected players");
                icommandlistener.b("   say <message>             broadcasts a message to all players");
            } else if (s.toLowerCase().startsWith("#list")) {
                icommandlistener.b((new StringBuilder()).append("Connected players: ").append(f.c()).toString());
            } else if (s.toLowerCase().startsWith("#stop")) {
                a(s1, "Stopping the server..");
                o = false;
            } else if (s.toLowerCase().startsWith("#save-all")) {
                a(s1, "Forcing save..");
                e.a(true, ((IProgressUpdate) (null)));
                a(s1, "Save complete.");
            } else if (s.toLowerCase().startsWith("#save-off")) {
                a(s1, "Disabling level saving..");
                e.C = true;
            } else if (s.toLowerCase().startsWith("#save-on")) {
                a(s1, "Enabling level saving..");
                e.C = false;
            } else if (s.toLowerCase().startsWith("#op ")) {
                String s2 = s.substring(s.indexOf(" ")).trim();

                f.e(s2);
                a(s1, (new StringBuilder()).append("Opping ").append(s2).toString());
                f.a(s2, "\247eYou are now op!");
            } else if (s.toLowerCase().startsWith("#deop ")) {
                String s3 = s.substring(s.indexOf(" ")).trim();

                f.f(s3);
                f.a(s3, "\247eYou are no longer op!");
                a(s1, (new StringBuilder()).append("De-opping ").append(s3).toString());
            } else if (s.toLowerCase().startsWith("#ban-ip ")) {
                String s4 = s.substring(s.indexOf(" ")).trim();

                f.c(s4);
                a(s1, (new StringBuilder()).append("Banning ip ").append(s4).toString());
            } else if (s.toLowerCase().startsWith("#pardon-ip ")) {
                String s5 = s.substring(s.indexOf(" ")).trim();

                f.d(s5);
                a(s1, (new StringBuilder()).append("Pardoning ip ").append(s5).toString());
            } else if (s.toLowerCase().startsWith("#ban ")) {
                String s6 = s.substring(s.indexOf(" ")).trim();

                f.a(s6);
                a(s1, (new StringBuilder()).append("Banning ").append(s6).toString());
                EntityPlayerMP entityplayermp = f.h(s6);

                if (entityplayermp != null) {
                    entityplayermp.a.a("Banned by admin");
                }
            } else if (s.toLowerCase().startsWith("#pardon ")) {
                String s7 = s.substring(s.indexOf(" ")).trim();

                f.b(s7);
                a(s1, (new StringBuilder()).append("Pardoning ").append(s7).toString());
            } else if (s.toLowerCase().startsWith("#kick ")) {
                String s8 = s.substring(s.indexOf(" ")).trim();
                EntityPlayerMP entityplayermp1 = null;

                for (int i1 = 0; i1 < f.b.size(); i1++) {
                    EntityPlayerMP entityplayermp5 = (EntityPlayerMP) f.b.get(i1);

                    if (entityplayermp5.aw.equalsIgnoreCase(s8)) {
                        entityplayermp1 = entityplayermp5;
                    }
                }

                if (entityplayermp1 != null) {
                    entityplayermp1.a.a("Kicked by admin");
                    a(s1, (new StringBuilder()).append("Kicking ").append(entityplayermp1.aw).toString());
                } else {
                    icommandlistener.b((new StringBuilder()).append("Can't find user ").append(s8).append(". No kick.").toString());
                }
            } else if (s.toLowerCase().startsWith("#tp ")) {
                String as[] = s.split(" ");

                if (as.length == 3) {
                    EntityPlayerMP entityplayermp2 = f.h(as[1]);
                    EntityPlayerMP entityplayermp3 = f.h(as[2]);

                    if (entityplayermp2 == null) {
                        icommandlistener.b((new StringBuilder()).append("Can't find user ").append(as[1]).append(". No tp.").toString());
                    } else if (entityplayermp3 == null) {
                        icommandlistener.b((new StringBuilder()).append("Can't find user ").append(as[2]).append(". No tp.").toString());
                    } else {
                        entityplayermp2.a.a(entityplayermp3.p, entityplayermp3.q, entityplayermp3.r, entityplayermp3.v, entityplayermp3.w);
                        a(s1, (new StringBuilder()).append("Teleporting ").append(as[1]).append(" to ").append(as[2]).append(".").toString());
                    }
                } else {
                    icommandlistener.b("Syntax error, please provice a source and a target.");
                }
            } else if (s.toLowerCase().startsWith("#give ")) {
                String as1[] = s.split(" ");

                if (as1.length != 3 && as1.length != 4) {
                    return;
                }
                String s9 = as1[1];
                EntityPlayerMP entityplayermp4 = f.h(s9);

                if (entityplayermp4 != null) {
                    try {
                        int j1 = Integer.parseInt(as1[2]);

                        if (Item.c[j1] != null) {
                            a(s1, (new StringBuilder()).append("Giving ").append(entityplayermp4.aw).append(" some ").append(j1).toString());
                            int k1 = 1;

                            if (as1.length > 3) {
                                k1 = b(as1[3], 1);
                            }
                            if (k1 < 1) {
                                k1 = 1;
                            }
                            if (k1 > 64) {
                                k1 = 64;
                            }
                            entityplayermp4.b(new ItemStack(j1, k1, 0));
                        } else {
                            icommandlistener.b((new StringBuilder()).append("There's no item with id ").append(j1).toString());
                        }
                    } catch (NumberFormatException numberformatexception) {
                        icommandlistener.b((new StringBuilder()).append("There's no item with id ").append(as1[2]).toString());
                    }
                } else {
                    icommandlistener.b((new StringBuilder()).append("Can't find user ").append(s9).toString());
                }
            } else if (s.toLowerCase().startsWith("#say ")) {
                s = s.substring(s.indexOf(" ")).trim();
                a.info((new StringBuilder()).append("[").append(s1).append("] ").append(s).toString());
                f.a(((Packet) (new Packet3Chat((new StringBuilder()).append("\247d[Server] ").append(s).toString()))));
            } else if (s.toLowerCase().startsWith("#tell ")) {
                String as2[] = s.split(" ");

                if (as2.length >= 3) {
                    s = s.substring(s.indexOf(" ")).trim();
                    s = s.substring(s.indexOf(" ")).trim();
                    a.info((new StringBuilder()).append("[").append(s1).append("->").append(as2[1]).append("] ").append(s).toString());
                    s = (new StringBuilder()).append("\2477").append(s1).append(" whispers ").append(s).toString();
                    a.info(s);
                    if (!f.a(as2[1], ((Packet) (new Packet3Chat(s))))) {
                        icommandlistener.b("There's no player by that name online.");
                    }
                }
            } else {
                a.info("Unknown console command. Type \"help\" for help.");
            }
        } while (true);
    }

    private void a(String s, String s1) {
        String s2 = (new StringBuilder()).append(s).append(": ").append(s1).toString();

        f.i((new StringBuilder()).append("\2477(").append(s2).append(")").toString());
        a.info(s2);
    }

    private int b(String s, int i1) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException numberformatexception) {
            return i1;
        }
    }

    public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
        p.add(((iupdateplayerlistbox)));
    }

    // Craftbukkit start - replaces main(String args[])
    public static void main(final OptionSet options) {
        try {
            // CraftBukkit - remove gui
            MinecraftServer minecraftserver = new MinecraftServer(options);

            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        } catch (Exception exception) {
            a.log(Level.SEVERE, "Failed to start the minecraft server", ((Throwable) (exception)));
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
        return minecraftserver.o;
    }
}
