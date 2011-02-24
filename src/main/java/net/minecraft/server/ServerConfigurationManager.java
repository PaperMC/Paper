package net.minecraft.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
// CraftBukkit end

public class ServerConfigurationManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public List b = new ArrayList();
    public MinecraftServer c; // CraftBukkit - private->public
    // public PlayerManager d; // CraftBukkit - removed!
    public int e; // CraftBukkit - private->public
    private Set f = new HashSet();
    private Set g = new HashSet();
    private Set h = new HashSet();
    private Set i = new HashSet();
    private File j;
    private File k;
    private File l;
    private File m;
    private PlayerFileData n;
    private boolean o;

    // CraftBukkit start
    private CraftServer server;

    public ServerConfigurationManager(MinecraftServer minecraftserver) {
        minecraftserver.server = new CraftServer(minecraftserver, this);
        minecraftserver.console = new ConsoleCommandSender(minecraftserver.server);
        server = minecraftserver.server;
        // CraftBukkit end

        this.c = minecraftserver;
        this.j = minecraftserver.a("banned-players.txt");
        this.k = minecraftserver.a("banned-ips.txt");
        this.l = minecraftserver.a("ops.txt");
        this.m = minecraftserver.a("white-list.txt");
        // this.d = new PlayerManager(minecraftserver); // CraftBukkit - removed!
        this.e = minecraftserver.d.a("max-players", 20);
        this.o = minecraftserver.d.a("white-list", false);
        this.g();
        this.i();
        this.k();
        this.m();
        this.h();
        this.j();
        this.l();
        this.n();
    }

    public void a(WorldServer worldserver) {
        // CraftBukkit start
        if (this.n == null) {
            this.n = worldserver.m().d();
        }
        // CraftBukkit end
    }

    public int a() {
        return 144; // CraftBukkit - magic number from PlayerManager.b() (??)
    }

    public void a(EntityPlayer entityplayer) {
        this.b.add(entityplayer);
        this.n.b(entityplayer);
        // CraftBukkit start
        ((WorldServer) entityplayer.world).u.d((int) entityplayer.locX >> 4, (int) entityplayer.locZ >> 4);

        while (entityplayer.world.a(entityplayer, entityplayer.boundingBox).size() != 0) {
            entityplayer.a(entityplayer.locX, entityplayer.locY + 1.0D, entityplayer.locZ);
        }

        entityplayer.world.a(entityplayer);

        server.getPluginManager().callEvent(new PlayerEvent(PlayerEvent.Type.PLAYER_JOIN, server.getPlayer(entityplayer)));

        ((WorldServer) entityplayer.world).manager.a(entityplayer);
        // CraftBukkit end
    }

    public void b(EntityPlayer entityplayer) {
        ((WorldServer) entityplayer.world).manager.c(entityplayer); // CraftBukkit
    }

    public void c(EntityPlayer entityplayer) {
        this.n.a(entityplayer);
        entityplayer.world.d(entityplayer); // CraftBukkit
        this.b.remove(entityplayer);

        // CraftBukkit start
        ((WorldServer) entityplayer.world).manager.b(entityplayer);
        server.getPluginManager().callEvent(new PlayerEvent(PlayerEvent.Type.PLAYER_QUIT, server.getPlayer(entityplayer))); // CraftBukkit
        // CraftBukkit end
    }

    public EntityPlayer a(NetLoginHandler netloginhandler, String s, String s1) {
        // CraftBukkit start - note: this entire method needs to be changed
        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome. Also change any reference to this.e.c to entity.world
        EntityPlayer entity = new EntityPlayer(c, c.worlds.get(0), s, new ItemInWorldManager(c.worlds.get(0)));
        Player player = (entity == null) ? null : (Player) entity.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(Type.PLAYER_LOGIN, player);

        String s2 = netloginhandler.b.b().toString();

        s2 = s2.substring(s2.indexOf("/") + 1);
        s2 = s2.substring(0, s2.indexOf(":"));

        if (this.f.contains(s.trim().toLowerCase())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned from this server!");
        } else if (!this.g(s)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are not white-listed on this server!");
        } else if (this.g.contains(s2)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Your IP address is banned from this server!");
        } else if (this.b.size() >= this.e) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "The server is full!");
        }

        server.getPluginManager().callEvent(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            netloginhandler.a(event.getKickMessage());
            return null;
        }

        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (entityplayer.name.equalsIgnoreCase(s)) {
                entityplayer.a.a("You logged in from another location");
            }
        }

        return new EntityPlayer(this.c, entity.world, s, new ItemInWorldManager(entity.world));
        // CraftBukkit end
    }

    public EntityPlayer d(EntityPlayer entityplayer) {
        // CraftBukkit start - every reference to this.c.e should be entityplayer.world
        this.c.k.a(entityplayer);
        this.c.k.b(entityplayer);
        ((WorldServer) entityplayer.world).manager.b(entityplayer);
        this.b.remove(entityplayer);
        entityplayer.world.e(entityplayer);
        EntityPlayer entityplayer1 = new EntityPlayer(this.c, entityplayer.world, entityplayer.name, new ItemInWorldManager(entityplayer.world));

        entityplayer1.id = entityplayer.id;
        entityplayer1.a = entityplayer.a;
        ((WorldServer) entityplayer.world).u.d((int) entityplayer1.locX >> 4, (int) entityplayer1.locZ >> 4);

        while (entityplayer.world.a(entityplayer1, entityplayer1.boundingBox).size() != 0) {
            entityplayer1.a(entityplayer1.locX, entityplayer1.locY + 1.0D, entityplayer1.locZ);
        }

        // CraftBukkit start
        Player respawnPlayer = server.getPlayer(entityplayer);
        Location respawnLocation = new Location(respawnPlayer.getWorld(), entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);

        PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(Event.Type.PLAYER_RESPAWN, respawnPlayer, respawnLocation );
        server.getPluginManager().callEvent(respawnEvent);

        entityplayer1.world = ((CraftWorld) respawnEvent.getRespawnLocation().getWorld()).getHandle();
        entityplayer1.locX = respawnEvent.getRespawnLocation().getX();
        entityplayer1.locY = respawnEvent.getRespawnLocation().getY();
        entityplayer1.locZ = respawnEvent.getRespawnLocation().getZ();
        entityplayer1.yaw = respawnEvent.getRespawnLocation().getYaw();
        entityplayer1.pitch = respawnEvent.getRespawnLocation().getPitch();
        entityplayer1.c = new ItemInWorldManager(((CraftWorld) respawnEvent.getRespawnLocation().getWorld()).getHandle());
        entityplayer1.c.a = entityplayer1;
        ((WorldServer) entityplayer1.world).u.d((int) entityplayer1.locX >> 4, (int) entityplayer1.locZ >> 4);
        // CraftBukkit end

        entityplayer1.a.b((Packet) (new Packet9Respawn()));
        entityplayer1.a.a(entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);
        // CraftBukkit start
        ((WorldServer) entityplayer1.world).manager.a(entityplayer1);
        entityplayer.world.a(entityplayer1);
        // CraftBukkit end
        this.b.add(entityplayer1);
        entityplayer1.l();
        entityplayer1.s();
        return entityplayer1;
    }

    public void b() {
        // CraftBukkit start
        for (WorldServer world: c.worlds) {
            world.manager.a();
        }
        // CraftBukkit end
    }

    // CraftBukkit start - changed signature
    public void a(int i, int j, int k, WorldServer world) {
        world.manager.a(i, j, k);
    }
    // CraftBukkit end

    public void a(Packet packet) {
        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            entityplayer.a.b(packet);
        }
    }

    public String c() {
        String s = "";

        for (int i = 0; i < this.b.size(); ++i) {
            if (i > 0) {
                s = s + ", ";
            }

            s = s + ((EntityPlayer) this.b.get(i)).name;
        }

        return s;
    }

    public void a(String s) {
        this.f.add(s.toLowerCase());
        this.h();
    }

    public void b(String s) {
        this.f.remove(s.toLowerCase());
        this.h();
    }

    private void g() {
        try {
            this.f.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.j));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.f.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ban list: " + exception);
        }
    }

    private void h() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.j, false));
            Iterator iterator = this.f.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save ban list: " + exception);
        }
    }

    public void c(String s) {
        this.g.add(s.toLowerCase());
        this.j();
    }

    public void d(String s) {
        this.g.remove(s.toLowerCase());
        this.j();
    }

    private void i() {
        try {
            this.g.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.k));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.g.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void j() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.k, false));
            Iterator iterator = this.g.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save ip ban list: " + exception);
        }
    }

    public void e(String s) {
        this.h.add(s.toLowerCase());
        this.l();
    }

    public void f(String s) {
        this.h.remove(s.toLowerCase());
        this.l();
    }

    private void k() {
        try {
            this.h.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.l));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.h.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ops: " + exception);  // CraftBukkit corrected text
        }
    }

    private void l() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.l, false));
            Iterator iterator = this.h.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save ops: " + exception); // CraftBukkit corrected text
        }
    }

    private void m() {
        try {
            this.i.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.m));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.i.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load white-list: " + exception);
        }
    }

    private void n() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.m, false));
            Iterator iterator = this.i.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save white-list: " + exception);
        }
    }

    public boolean g(String s) {
        s = s.trim().toLowerCase();
        return !this.o || this.h.contains(s) || this.i.contains(s);
    }

    public boolean h(String s) {
        return this.h.contains(s.trim().toLowerCase());
    }

    public EntityPlayer i(String s) {
        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (entityplayer.name.equalsIgnoreCase(s)) {
                return entityplayer;
            }
        }

        return null;
    }

    public void a(String s, String s1) {
        EntityPlayer entityplayer = this.i(s);

        if (entityplayer != null) {
            entityplayer.a.b((Packet) (new Packet3Chat(s1)));
        }
    }

    public void a(double d0, double d1, double d2, double d3, Packet packet) {
        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);
            double d4 = d0 - entityplayer.locX;
            double d5 = d1 - entityplayer.locY;
            double d6 = d2 - entityplayer.locZ;

            if (d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3) {
                entityplayer.a.b(packet);
            }
        }
    }

    public void j(String s) {
        Packet3Chat packet3chat = new Packet3Chat(s);

        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (this.h(entityplayer.name)) {
                entityplayer.a.b((Packet) packet3chat);
            }
        }
    }

    public boolean a(String s, Packet packet) {
        EntityPlayer entityplayer = this.i(s);

        if (entityplayer != null) {
            entityplayer.a.b(packet);
            return true;
        } else {
            return false;
        }
    }

    public void d() {
        for (int i = 0; i < this.b.size(); ++i) {
            this.n.a((EntityHuman) this.b.get(i));
        }
    }

    public void a(int i, int j, int k, TileEntity tileentity) {}

    public void k(String s) {
        this.i.add(s);
        this.n();
    }

    public void l(String s) {
        this.i.remove(s);
        this.n();
    }

    public Set e() {
        return this.i;
    }

    public void f() {
        this.m();
    }
}
