package net.minecraft.server;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerLoginEvent;
// CraftBukkit end

public class ServerConfigurationManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public List b;
    private MinecraftServer c;
    private PlayerManager d;
    private int e;
    private Set f;
    private Set g;
    private Set h;
    private File i;
    private File j;
    private File k;
    private PlayerNBTManager l;

    private CraftServer server; // CraftBukkit

    public ServerConfigurationManager(MinecraftServer minecraftserver) {
        // CraftBukkit 2 lines!
        minecraftserver.server = new CraftServer(minecraftserver, this);
        server = minecraftserver.server;

        b = ((List) (new ArrayList()));
        f = ((Set) (new HashSet()));
        g = ((Set) (new HashSet()));
        h = ((Set) (new HashSet()));
        c = minecraftserver;
        i = minecraftserver.a("banned-players.txt");
        j = minecraftserver.a("banned-ips.txt");
        k = minecraftserver.a("ops.txt");
        d = new PlayerManager(minecraftserver);
        e = minecraftserver.d.a("max-players", 20);
        e();
        g();
        i();
        f();
        h();
        j();
    }

    public void a(WorldServer worldserver) {
        l = new PlayerNBTManager(new File(worldserver.t, "players"));
    }

    public int a() {
        return d.b();
    }

    public void a(EntityPlayerMP entityplayermp) {
        b.add(((entityplayermp)));
        l.b(entityplayermp);
        c.e.A.d((int) entityplayermp.p >> 4, (int) entityplayermp.r >> 4);
        for (; c.e.a(((Entity) (entityplayermp)), entityplayermp.z).size() != 0; entityplayermp.a(entityplayermp.p, entityplayermp.q + 1.0D, entityplayermp.r)) {
            ;
        }
        c.e.a(((Entity) (entityplayermp)));
        d.a(entityplayermp);

        // CraftBukkit
        server.getPluginManager().callEvent(new PlayerEvent(PlayerEvent.Type.PLAYER_JOIN, server.getPlayer(entityplayermp)));
    }

    public void b(EntityPlayerMP entityplayermp) {
        d.c(entityplayermp);
    }

    public void c(EntityPlayerMP entityplayermp) {
        l.a(entityplayermp);
        c.e.d(((Entity) (entityplayermp)));
        b.remove(((entityplayermp)));
        d.b(entityplayermp);

        // CraftBukkit
        server.getPluginManager().callEvent(new PlayerEvent(PlayerEvent.Type.PLAYER_QUIT, server.getPlayer(entityplayermp)));
    }

    public EntityPlayerMP a(NetLoginHandler netloginhandler, String s, String s1) {
        // CraftBukkit start - note: this entire method needs to be changed
        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome.
        EntityPlayerMP entity = new EntityPlayerMP(c, ((World) (c.e)), s, new ItemInWorldManager(((World) (c.e))));
        PlayerLoginEvent event = new PlayerLoginEvent(Type.PLAYER_LOGIN, new CraftPlayer(server, entity));
        // CraftBukkit end

        String s2 = ((netloginhandler.b.b())).toString();

        s2 = s2.substring(s2.indexOf("/") + 1);
        s2 = s2.substring(0, s2.indexOf(":"));

        // CraftBukkit start
        if (f.contains(s.trim().toLowerCase())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned from this server!");
        } else if (g.contains(s2)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Your IP address is banned from this server!");
        } else if (b.size() >= e) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "The server is full!");
        }

        server.getPluginManager().callEvent(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            netloginhandler.a(event.getKickMessage());
            return null;
        }
        // CraftBukkit end
        for (int i1 = 0; i1 < b.size(); i1++) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) b.get(i1);

            if (entityplayermp.aw.equalsIgnoreCase(s)) {
                entityplayermp.a.a("You logged in from another location");
            }
        }

        // CraftBukkit
        return entity;
    }

    public EntityPlayerMP d(EntityPlayerMP entityplayermp) {
        c.k.a(entityplayermp);
        c.k.b(((Entity) (entityplayermp)));
        d.b(entityplayermp);
        b.remove(((entityplayermp)));
        c.e.e(((Entity) (entityplayermp)));
        EntityPlayerMP entityplayermp1 = new EntityPlayerMP(c, ((World) (c.e)), entityplayermp.aw, new ItemInWorldManager(((World) (c.e))));

        entityplayermp1.g = entityplayermp.g;
        entityplayermp1.a = entityplayermp.a;
        c.e.A.d((int) entityplayermp1.p >> 4, (int) entityplayermp1.r >> 4);
        for (; c.e.a(((Entity) (entityplayermp1)), entityplayermp1.z).size() != 0; entityplayermp1.a(entityplayermp1.p, entityplayermp1.q + 1.0D, entityplayermp1.r)) {
            ;
        }
        entityplayermp1.a.b(((Packet) (new Packet9())));
        entityplayermp1.a.a(entityplayermp1.p, entityplayermp1.q, entityplayermp1.r, entityplayermp1.v, entityplayermp1.w);
        d.a(entityplayermp1);
        c.e.a(((Entity) (entityplayermp1)));
        b.add(((entityplayermp1)));
        entityplayermp1.k();
        return entityplayermp1;
    }

    public void b() {
        d.a();
    }

    public void a(int i1, int j1, int k1) {
        d.a(i1, j1, k1);
    }

    public void a(Packet packet) {
        for (int i1 = 0; i1 < b.size(); i1++) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) b.get(i1);

            entityplayermp.a.b(packet);
        }
    }

    public String c() {
        String s = "";

        for (int i1 = 0; i1 < b.size(); i1++) {
            if (i1 > 0) {
                s = (new StringBuilder()).append(s).append(", ").toString();
            }
            s = (new StringBuilder()).append(s).append(((EntityPlayerMP) b.get(i1)).aw).toString();
        }

        return s;
    }

    public void a(String s) {
        f.add(((s.toLowerCase())));
        f();
    }

    public void b(String s) {
        f.remove(((s.toLowerCase())));
        f();
    }

    private void e() {
        try {
            f.clear();
            BufferedReader bufferedreader = new BufferedReader(((java.io.Reader) (new FileReader(i))));

            for (String s = ""; (s = bufferedreader.readLine()) != null;) {
                f.add(((s.trim().toLowerCase())));
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning((new StringBuilder()).append("Failed to load ban list: ").append(((exception))).toString());
        }
    }

    private void f() {
        try {
            PrintWriter printwriter = new PrintWriter(((java.io.Writer) (new FileWriter(i, false))));
            String s;

            for (Iterator iterator = f.iterator(); iterator.hasNext(); printwriter.println(s)) {
                s = (String) iterator.next();
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning((new StringBuilder()).append("Failed to save ban list: ").append(((exception))).toString());
        }
    }

    public void c(String s) {
        g.add(((s.toLowerCase())));
        h();
    }

    public void d(String s) {
        g.remove(((s.toLowerCase())));
        h();
    }

    private void g() {
        try {
            g.clear();
            BufferedReader bufferedreader = new BufferedReader(((java.io.Reader) (new FileReader(j))));

            for (String s = ""; (s = bufferedreader.readLine()) != null;) {
                g.add(((s.trim().toLowerCase())));
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning((new StringBuilder()).append("Failed to load ip ban list: ").append(((exception))).toString());
        }
    }

    private void h() {
        try {
            PrintWriter printwriter = new PrintWriter(((java.io.Writer) (new FileWriter(j, false))));
            String s;

            for (Iterator iterator = g.iterator(); iterator.hasNext(); printwriter.println(s)) {
                s = (String) iterator.next();
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning((new StringBuilder()).append("Failed to save ip ban list: ").append(((exception))).toString());
        }
    }

    public void e(String s) {
        h.add(((s.toLowerCase())));
        j();
    }

    public void f(String s) {
        h.remove(((s.toLowerCase())));
        j();
    }

    private void i() {
        try {
            h.clear();
            BufferedReader bufferedreader = new BufferedReader(((java.io.Reader) (new FileReader(k))));

            for (String s = ""; (s = bufferedreader.readLine()) != null;) {
                h.add(((s.trim().toLowerCase())));
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning((new StringBuilder()).append("Failed to load ip ban list: ").append(((exception))).toString());
        }
    }

    private void j() {
        try {
            PrintWriter printwriter = new PrintWriter(((java.io.Writer) (new FileWriter(k, false))));
            String s;

            for (Iterator iterator = h.iterator(); iterator.hasNext(); printwriter.println(s)) {
                s = (String) iterator.next();
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning((new StringBuilder()).append("Failed to save ip ban list: ").append(((exception))).toString());
        }
    }

    public boolean g(String s) {
        return h.contains(((s.trim().toLowerCase())));
    }

    public EntityPlayerMP h(String s) {
        for (int i1 = 0; i1 < b.size(); i1++) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) b.get(i1);

            if (entityplayermp.aw.equalsIgnoreCase(s)) {
                return entityplayermp;
            }
        }

        return null;
    }

    public void a(String s, String s1) {
        EntityPlayerMP entityplayermp = h(s);

        if (entityplayermp != null) {
            entityplayermp.a.b(((Packet) (new Packet3Chat(s1))));
        }
    }

    public void a(double d1, double d2, double d3, double d4, Packet packet) {
        for (int i1 = 0; i1 < b.size(); i1++) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) b.get(i1);
            double d5 = d1 - entityplayermp.p;
            double d6 = d2 - entityplayermp.q;
            double d7 = d3 - entityplayermp.r;

            if (d5 * d5 + d6 * d6 + d7 * d7 < d4 * d4) {
                entityplayermp.a.b(packet);
            }
        }
    }

    public void i(String s) {
        Packet3Chat packet3chat = new Packet3Chat(s);

        for (int i1 = 0; i1 < b.size(); i1++) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) b.get(i1);

            if (g(entityplayermp.aw)) {
                entityplayermp.a.b(((Packet) (packet3chat)));
            }
        }
    }

    public boolean a(String s, Packet packet) {
        EntityPlayerMP entityplayermp = h(s);

        if (entityplayermp != null) {
            entityplayermp.a.b(packet);
            return true;
        } else {
            return false;
        }
    }

    public void d() {
        for (int i1 = 0; i1 < b.size(); i1++) {
            l.a((EntityPlayerMP) b.get(i1));
        }
    }

    public void a(int i1, int j1, int k1, TileEntity tileentity) {}
}
