package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.craftbukkit.CraftServer;

public class MinecraftServer
        implements fn, Runnable {

    public static Logger a = Logger.getLogger("Minecraft");
    public static HashMap<String, Integer> b = new HashMap<String, Integer>();
    public eh c;
    public dt d;
    public fm e;
    public hl f;
    private boolean o = true;
    public boolean g = false;
    int h = 0;
    public String i;
    public int j;
    private List p = new ArrayList();
    private List q = Collections.synchronizedList(new ArrayList());
    public hp k;
    public boolean l;
    public boolean m;
    public boolean n;
    public CraftServer server; // CraftBukkit

    public MinecraftServer() {
        new cn(this);
    }

    private boolean d() {
        cl localcl = new cl(this);

        localcl.setDaemon(true);
        localcl.start();

        hb.a();
        a.info("Starting minecraft server version Beta 1.1");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            a.warning("**** NOT ENOUGH RAM!");
            a.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        a.info("Loading properties");
        this.d = new dt(new File("server.properties"));
        String str1 = this.d.a("server-ip", "");

        this.l = this.d.a("online-mode", true);
        this.m = this.d.a("spawn-animals", true);
        this.n = this.d.a("pvp", true);

        InetAddress localInetAddress = null;
        if (str1.length() > 0) {
            try {
                localInetAddress = InetAddress.getByName(str1);
            } catch (UnknownHostException ex) {
                Logger.getLogger(MinecraftServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int i1 = this.d.a("server-port", 25565);

        a.info("Starting Minecraft server on " + (str1.length() == 0 ? "*" : str1) + ":" + i1);
        try {
            this.c = new eh(this, localInetAddress, i1);
        } catch (Throwable localIOException) {
            a.warning("**** FAILED TO BIND TO PORT!");
            a.log(Level.WARNING, "The exception was: " + localIOException.toString());
            a.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.l) {
            a.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            a.warning("The server will make no attempt to authenticate usernames. Beware.");
            a.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            a.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
        }

        this.f = new hl(this);
        this.k = new hp(this);

        server = new CraftServer(this, "1.1"); // CraftBukkit

        String str2 = this.d.a("level-name", "world");
        a.info("Preparing level \"" + str2 + "\"");
        c(str2);
        a.info("Done! For help, type \"help\" or \"?\"");

        return true;
    }

    private void c(String paramString) {
        a.info("Preparing start region");
        this.e = new fm(this, new File("."), paramString, this.d.a("hellworld", false) ? -1 : 0);
        this.e.a(new fj(this));
        this.e.k = (this.d.a("spawn-monsters", true) ? 1 : 0);
        this.f.a(this.e);
        int i1 = 10;
        for (int i2 = -i1; i2 <= i1; i2++) {
            a("Preparing spawn area", (i2 + i1) * 100 / (i1 + i1 + 1));
            for (int i3 = -i1; i3 <= i1; i3++) {
                if (!this.o) {
                    return;
                }
                this.e.A.d((this.e.m >> 4) + i2, (this.e.o >> 4) + i3);
            }
        }
        e();
    }

    private void a(String paramString, int paramInt) {
        this.i = paramString;
        this.j = paramInt;
        System.out.println(paramString + ": " + paramInt + "%");
    }

    private void e() {
        this.i = null;
        this.j = 0;
    }

    private void f() {
        a.info("Saving chunks");
        this.e.a(true, null);
    }

    private void g() {
        a.info("Stopping server");
        if (this.f != null) {
            this.f.d();
        }
        if (this.e != null) {
            f();
        }
    }

    public void a() {
        this.o = false;
    }

    public void run() {
        try {
            if (d()) {
                long l1 = System.currentTimeMillis();
                long l2 = 0L;
                while (this.o) {
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
                while (this.o) {
                    b();
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException localInterruptedException1) {
                        localInterruptedException1.printStackTrace();
                    }
                }
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            a.log(Level.SEVERE, "Unexpected exception", localException);
            while (this.o) {
                b();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException localInterruptedException2) {
                    localInterruptedException2.printStackTrace();
                }
            }
        } finally {
            g();
            this.g = true;
            System.exit(0);
        }
    }

    private void h() {
        ArrayList localArrayList = new ArrayList();
        for (String str : b.keySet()) {
            int i2 = ((Integer) b.get(str)).intValue();
            if (i2 > 0) {
                b.put(str, Integer.valueOf(i2 - 1));
            } else {
                localArrayList.add(str);
            }
        }
        for (int i1 = 0; i1 < localArrayList.size(); i1++) {
            b.remove(localArrayList.get(i1));
        }

        el.a();
        bn.a();
        this.h += 1;

        if (this.h % 20 == 0) {
            this.f.a(new hc(this.e.e));
        }

        this.e.f();
        while (this.e.d());
        this.e.c();
        this.c.a();
        this.f.b();
        this.k.a();

        for (int i1 = 0; i1 < this.p.size(); i1++) {
            ((fc) this.p.get(i1)).a();
        }
        try {
            b();
        } catch (Exception localException) {
            a.log(Level.WARNING, "Unexpected exception while parsing console command", localException);
        }
    }

    public void a(String paramString, fn paramfn) {
        this.q.add(new at(paramString, paramfn));
    }

    public void b() {
        while (this.q.size() > 0) {
            at localat = (at) this.q.remove(0);
            String str1 = localat.a;
            fn localfn = localat.b;
            String str2 = localfn.c();
            if ((str1.toLowerCase().startsWith("help")) || (str1.toLowerCase().startsWith("?"))) {
                localfn.b("To run the server without a gui, start it like this:");
                localfn.b("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
                localfn.b("Console commands:");
                localfn.b("   help  or  ?               shows this message");
                localfn.b("   kick <player>             removes a player from the server");
                localfn.b("   ban <player>              bans a player from the server");
                localfn.b("   pardon <player>           pardons a banned player so that they can connect again");
                localfn.b("   ban-ip <ip>               bans an IP address from the server");
                localfn.b("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
                localfn.b("   op <player>               turns a player into an op");
                localfn.b("   deop <player>             removes op status from a player");
                localfn.b("   tp <player1> <player2>    moves one player to the same location as another player");
                localfn.b("   give <player> <id> [num]  gives a player a resource");
                localfn.b("   tell <player> <message>   sends a private message to a player");
                localfn.b("   stop                      gracefully stops the server");
                localfn.b("   save-all                  forces a server-wide level save");
                localfn.b("   save-off                  disables terrain saving (useful for backup scripts)");
                localfn.b("   save-on                   re-enables terrain saving");
                localfn.b("   list                      lists all currently connected players");
                localfn.b("   say <message>             broadcasts a message to all players");
            } else if (str1.toLowerCase().startsWith("list")) {
                localfn.b("Connected players: " + this.f.c());
            } else if (str1.toLowerCase().startsWith("stop")) {
                a(str2, "Stopping the server..");
                this.o = false;
            } else if (str1.toLowerCase().startsWith("save-all")) {
                a(str2, "Forcing save..");
                this.e.a(true, null);
                a(str2, "Save complete.");
            } else if (str1.toLowerCase().startsWith("save-off")) {
                a(str2, "Disabling level saving..");
                this.e.C = true;
            } else if (str1.toLowerCase().startsWith("save-on")) {
                a(str2, "Enabling level saving..");
                this.e.C = false;
            } else {
                Object localObject1;
                if (str1.toLowerCase().startsWith("op ")) {
                    localObject1 = str1.substring(str1.indexOf(" ")).trim();
                    this.f.e((String) localObject1);
                    a(str2, "Opping " + (String) localObject1);
                    this.f.a((String) localObject1, "§eYou are now op!");
                } else if (str1.toLowerCase().startsWith("deop ")) {
                    localObject1 = str1.substring(str1.indexOf(" ")).trim();
                    this.f.f((String) localObject1);
                    this.f.a((String) localObject1, "§eYou are no longer op!");
                    a(str2, "De-opping " + (String) localObject1);
                } else if (str1.toLowerCase().startsWith("ban-ip ")) {
                    localObject1 = str1.substring(str1.indexOf(" ")).trim();
                    this.f.c((String) localObject1);
                    a(str2, "Banning ip " + (String) localObject1);
                } else if (str1.toLowerCase().startsWith("pardon-ip ")) {
                    localObject1 = str1.substring(str1.indexOf(" ")).trim();
                    this.f.d((String) localObject1);
                    a(str2, "Pardoning ip " + (String) localObject1);
                } else {
                    Object localObject2;
                    if (str1.toLowerCase().startsWith("ban ")) {
                        localObject1 = str1.substring(str1.indexOf(" ")).trim();
                        this.f.a((String) localObject1);
                        a(str2, "Banning " + (String) localObject1);

                        localObject2 = this.f.h((String) localObject1);

                        if (localObject2 != null) {
                            ((fi) localObject2).a.a("Banned by admin");
                        }

                    } else if (str1.toLowerCase().startsWith("pardon ")) {
                        localObject1 = str1.substring(str1.indexOf(" ")).trim();
                        this.f.b((String) localObject1);
                        a(str2, "Pardoning " + (String) localObject1);
                    } else if (str1.toLowerCase().startsWith("kick ")) {
                        localObject1 = str1.substring(str1.indexOf(" ")).trim();
                        localObject2 = null;
                        for (int i1 = 0; i1 < this.f.b.size(); i1++) {
                            fi localfi2 = (fi) this.f.b.get(i1);
                            if (localfi2.aw.equalsIgnoreCase((String) localObject1)) {
                                localObject2 = localfi2;
                            }
                        }

                        if (localObject2 != null) {
                            ((fi) localObject2).a.a("Kicked by admin");
                            a(str2, "Kicking " + ((fi) localObject2).aw);
                        } else {
                            localfn.b("Can't find user " + (String) localObject1 + ". No kick.");
                        }
                    } else {
                        fi localfi1;
                        if (str1.toLowerCase().startsWith("tp ")) {
                            String[] split = str1.split(" ");
                            if (split.length == 3) {
                                localObject2 = this.f.h(split[1]);
                                localfi1 = this.f.h(split[2]);

                                if (localObject2 == null) {
                                    localfn.b("Can't find user " + split[1] + ". No tp.");
                                } else if (localfi1 == null) {
                                    localfn.b("Can't find user " + split[2] + ". No tp.");
                                } else {
                                    ((fi) localObject2).a.a(localfi1.p, localfi1.q, localfi1.r, localfi1.v, localfi1.w);
                                    a(str2, "Teleporting " + split[1] + " to " + split[2] + ".");
                                }
                            } else {
                                localfn.b("Syntax error, please provice a source and a target.");
                            }
                        } else if (str1.toLowerCase().startsWith("give ")) {
                            String[] split = str1.split(" ");
                            if ((split.length != 3) && (split.length != 4)) {
                                return;
                            }

                            localObject2 = split[1];
                            localfi1 = this.f.h((String) localObject2);

                            if (localfi1 != null) {
                                try {
                                    int i2 = Integer.parseInt(split[2]);
                                    if (gm.c[i2] != null) {
                                        a(str2, "Giving " + localfi1.aw + " some " + i2);
                                        int i3 = 1;
                                        if (split.length > 3) {
                                            i3 = b(split[3], 1);
                                        }
                                        if (i3 < 1) {
                                            i3 = 1;
                                        }
                                        if (i3 > 64) {
                                            i3 = 64;
                                        }
                                        localfi1.b(new il(i2, i3));
                                    } else {
                                        localfn.b("There's no item with id " + i2);
                                    }
                                } catch (NumberFormatException localNumberFormatException) {
                                    localfn.b("There's no item with id " + split[2]);
                                }
                            } else {
                                localfn.b("Can't find user " + (String) localObject2);
                            }
                        } else if (str1.toLowerCase().startsWith("say ")) {
                            str1 = str1.substring(str1.indexOf(" ")).trim();
                            a.info("[" + str2 + "] " + str1);
                            this.f.a(new br("§d[Server] " + str1));
                        } else if (str1.toLowerCase().startsWith("tell ")) {
                            String[] split = str1.split(" ");
                            if (split.length >= 3) {
                                str1 = str1.substring(str1.indexOf(" ")).trim();
                                str1 = str1.substring(str1.indexOf(" ")).trim();

                                a.info("[" + str2 + "->" + split[1] + "] " + str1);
                                str1 = "§7" + str2 + " whispers " + str1;
                                a.info(str1);
                                if (!this.f.a(split[1], new br(str1))) {
                                    localfn.b("There's no player by that name online.");
                                }
                            }
                        } else {
                            a.info("Unknown console command. Type \"help\" for help.");
                        }
                    }
                }
            }
        }
    }

    private void a(String paramString1, String paramString2) {
        String str = paramString1 + ": " + paramString2;
        this.f.i("§7(" + str + ")");
        a.info(str);
    }

    private int b(String paramString, int paramInt) {
        try {
            return Integer.parseInt(paramString);
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramInt;
    }

    public void a(fc paramfc) {
        this.p.add(paramfc);
    }

    public static void main(String[] paramArrayOfString) {
        try {
            MinecraftServer localMinecraftServer = new MinecraftServer();

            if ((!GraphicsEnvironment.isHeadless()) && ((paramArrayOfString.length <= 0) || (!paramArrayOfString[0].equals("nogui")))) {
                hg.a(localMinecraftServer);
            }

            new cj("Server thread", localMinecraftServer).start();
        } catch (Exception localException) {
            a.log(Level.SEVERE, "Failed to start the minecraft server", localException);
        }
    }

    public File a(String paramString) {
        return new File(paramString);
    }

    public void b(String paramString) {
        a.info(paramString);
    }

    public String c() {
        return "CONSOLE";
    }
}
