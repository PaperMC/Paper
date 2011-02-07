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
import org.bukkit.craftbukkit.CraftServer;
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
    public MinecraftServer c; // Craftbukkit - public
    // public PlayerManager d; // Craftbukkit - removed!
    private int e;
    private Set f = new HashSet();
    private Set g = new HashSet();
    private Set h = new HashSet();
    private File i;
    private File j;
    private File k;
    private PlayerNBTManager l;

    // CraftBukkit start
    private CraftServer server;

    public int getMaxPlayers() {
        return this.e;
    }

    public ServerConfigurationManager(MinecraftServer minecraftserver) {
        minecraftserver.server = new CraftServer(minecraftserver, this);
        server = minecraftserver.server;
        // CraftBukkit end

        this.c = minecraftserver;
        this.i = minecraftserver.a("banned-players.txt");
        this.j = minecraftserver.a("banned-ips.txt");
        this.k = minecraftserver.a("ops.txt");
        // this.d = new PlayerManager(minecraftserver); // Craftbukkit - removed!
        this.e = minecraftserver.d.a("max-players", 20);
        this.e();
        this.g();
        this.i();
        this.f();
        this.h();
        this.j();
    }

    public void a(WorldServer worldserver) {
        // Craftbukkit start
        if (this.l == null) {
            this.l = new PlayerNBTManager(new File(worldserver.t, "players"));
        }
        // Craftbukkit end
    }

    public int a() {
        return 144; // Craftbukkit - magic number from PlayerManager.b() (??)
    }

    public void a(EntityPlayer entityplayer) {
        this.b.add(entityplayer);
        this.l.b(entityplayer);

        // Craftbukkit start
        ((WorldServer)entityplayer.world).A.d((int) entityplayer.locX >> 4, (int) entityplayer.locZ >> 4);

        while (entityplayer.world.a(entityplayer, entityplayer.boundingBox).size() != 0) {
            entityplayer.a(entityplayer.locX, entityplayer.locY + 1.0D, entityplayer.locZ);
        }

        entityplayer.world.a(entityplayer);

        server.getPluginManager().callEvent(new PlayerEvent(PlayerEvent.Type.PLAYER_JOIN, server.getPlayer(entityplayer)));

        ((WorldServer)entityplayer.world).manager.a(entityplayer);
        // Craftbukkit end
    }

    public void b(EntityPlayer entityplayer) {
        ((WorldServer)entityplayer.world).manager.c(entityplayer); // Craftbukkit
    }

    public void c(EntityPlayer entityplayer) {
        this.l.a(entityplayer);
        entityplayer.world.d(entityplayer); // Craftbukkit
        this.b.remove(entityplayer);

        // CraftBukkit start
        ((WorldServer)entityplayer.world).manager.b(entityplayer);
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
        // Craftbukkit start - every reference to this.c.e should be entityplayer.world
        this.c.k.a(entityplayer);
        this.c.k.b(entityplayer);
        ((WorldServer)entityplayer.world).manager.b(entityplayer);
        this.b.remove(entityplayer);
        entityplayer.world.e(entityplayer);
        EntityPlayer entityplayer1 = new EntityPlayer(this.c, entityplayer.world, entityplayer.name, new ItemInWorldManager(entityplayer.world));

        entityplayer1.id = entityplayer.id;
        entityplayer1.a = entityplayer.a;
        ((WorldServer)entityplayer.world).A.d((int) entityplayer1.locX >> 4, (int) entityplayer1.locZ >> 4);

        while (entityplayer.world.a(entityplayer1, entityplayer1.boundingBox).size() != 0) {
            entityplayer1.a(entityplayer1.locX, entityplayer1.locY + 1.0D, entityplayer1.locZ);
        }

        // CraftBukkit start
        Player respawnPlayer = server.getPlayer(entityplayer);
        Location respawnLocation = new Location(respawnPlayer.getWorld(), entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);
        PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(Event.Type.PLAYER_RESPAWN, respawnPlayer, respawnLocation );
        server.getPluginManager().callEvent(respawnEvent);
        entityplayer1.locX = respawnEvent.getRespawnLocation().getX();
        entityplayer1.locY = respawnEvent.getRespawnLocation().getY();
        entityplayer1.locZ = respawnEvent.getRespawnLocation().getZ();
        entityplayer1.yaw = respawnEvent.getRespawnLocation().getYaw();
        entityplayer1.pitch = respawnEvent.getRespawnLocation().getPitch();
        // CraftBukkit end

        entityplayer1.a.b((Packet) (new Packet9Respawn()));
        entityplayer1.a.a(entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);
        ((WorldServer)entityplayer1.world).manager.a(entityplayer1);
        entityplayer.world.a(entityplayer1);
        this.b.add(entityplayer1);
        entityplayer1.l();
        return entityplayer1;
        // Craftbukkit end
    }

    public void b() {
        // Craftbukkit start
        for (WorldServer world : c.worlds) {
            world.manager.a();
        }
        // Craftbukkit end
    }

    // Craftbukkit - changed signature
    public void a(int i, int j, int k, WorldServer world) {
        world.manager.a(i, j, k, world); // Craftbukkit
    }

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
        this.f();
    }

    public void b(String s) {
        this.f.remove(s.toLowerCase());
        this.f();
    }

    private void e() {
        try {
            this.f.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.i));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.f.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ban list: " + exception);
        }
    }

    private void f() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.i, false));
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
        this.h();
    }

    public void d(String s) {
        this.g.remove(s.toLowerCase());
        this.h();
    }

    private void g() {
        try {
            this.g.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.j));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.g.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void h() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.j, false));
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
        this.j();
    }

    public void f(String s) {
        this.h.remove(s.toLowerCase());
        this.j();
    }

    private void i() {
        try {
            this.h.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.k));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.h.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void j() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.k, false));
            Iterator iterator = this.h.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save ip ban list: " + exception);
        }
    }

    public boolean g(String s) {
        return this.h.contains(s.trim().toLowerCase());
    }

    public EntityPlayer h(String s) {
        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (entityplayer.name.equalsIgnoreCase(s)) {
                return entityplayer;
            }
        }

        return null;
    }

    public void a(String s, String s1) {
        EntityPlayer entityplayer = this.h(s);

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

    public void i(String s) {
        Packet3Chat packet3chat = new Packet3Chat(s);

        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (this.g(entityplayer.name)) {
                entityplayer.a.b((Packet) packet3chat);
            }
        }
    }

    public boolean a(String s, Packet packet) {
        EntityPlayer entityplayer = this.h(s);

        if (entityplayer != null) {
            entityplayer.a.b(packet);
            return true;
        } else {
            return false;
        }
    }

    public void d() {
        for (int i = 0; i < this.b.size(); ++i) {
            this.l.a((EntityPlayer) this.b.get(i));
        }
    }

    public void a(int i, int j, int k, TileEntity tileentity) {}
}
