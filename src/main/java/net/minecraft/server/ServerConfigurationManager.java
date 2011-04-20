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
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
// CraftBukkit end

public class ServerConfigurationManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public List players = new ArrayList();
    public MinecraftServer server; // CraftBukkit - private->public
    // public PlayerManager d; // CraftBukkit - removed!
    public int maxPlayers; // CraftBukkit - private->public
    private Set banByName = new HashSet();
    private Set banByIP = new HashSet();
    private Set h = new HashSet();
    private Set i = new HashSet();
    private File j;
    private File k;
    private File l;
    private File m;
    public PlayerFileData playerFileData; // CraftBukkit private->public
    private boolean o;

    // CraftBukkit start
    private CraftServer cserver;

    public ServerConfigurationManager(MinecraftServer minecraftserver) {
        minecraftserver.server = new CraftServer(minecraftserver, this);
        minecraftserver.console = new ColouredConsoleSender(minecraftserver.server);
        cserver = minecraftserver.server;
        // CraftBukkit end

        this.server = minecraftserver;
        this.j = minecraftserver.a("banned-players.txt");
        this.k = minecraftserver.a("banned-ips.txt");
        this.l = minecraftserver.a("ops.txt");
        this.m = minecraftserver.a("white-list.txt");
        // this.d = new PlayerManager(minecraftserver); // CraftBukkit - removed!
        this.maxPlayers = minecraftserver.propertyManager.getInt("max-players", 20);
        this.o = minecraftserver.propertyManager.getBoolean("white-list", false);
        this.g();
        this.i();
        this.k();
        this.m();
        this.h();
        this.j();
        this.l();
        this.n();
    }

    public void setPlayerFileData(WorldServer worldserver) {
        if (this.playerFileData != null) return; // CraftBukkit
        this.playerFileData = worldserver.o().d();
    }

    public int a() {
        return 144; // CraftBukkit - magic number from PlayerManager.b() (??)
    }

    public void a(EntityPlayer entityplayer) {
        this.players.add(entityplayer);
        this.playerFileData.b(entityplayer);
        // CraftBukkit start
        ((WorldServer) entityplayer.world).chunkProviderServer.getChunkAt((int) entityplayer.locX >> 4, (int) entityplayer.locZ >> 4);

        while (entityplayer.world.getEntities(entityplayer, entityplayer.boundingBox).size() != 0) {
            entityplayer.setPosition(entityplayer.locX, entityplayer.locY + 1.0D, entityplayer.locZ);
        }

        entityplayer.world.addEntity(entityplayer);

        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.name + " joined the game.");
        cserver.getPluginManager().callEvent(playerJoinEvent);

        String joinMessage = playerJoinEvent.getJoinMessage();

        if (joinMessage != null) {
            this.server.serverConfigurationManager.sendAll(new Packet3Chat(joinMessage));
        }

        ((WorldServer) entityplayer.world).manager.addPlayer(entityplayer);
        // CraftBukkit end
    }

    public void b(EntityPlayer entityplayer) {
        ((WorldServer) entityplayer.world).manager.movePlayer(entityplayer); // CraftBukkit
    }

    public String disconnect(EntityPlayer entityplayer) { // CraftBukkit - changed return type
        // CraftBukkit start
        // Quitting must be before we do final save of data, in case plugins need to modify it
        ((WorldServer) entityplayer.world).manager.removePlayer(entityplayer);
        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.name + " left the game.");
        cserver.getPluginManager().callEvent(playerQuitEvent);
        // CraftBukkit end

        this.playerFileData.a(entityplayer);
        entityplayer.world.kill(entityplayer); // CraftBukkit
        this.players.remove(entityplayer);

        return playerQuitEvent.getQuitMessage(); // CraftBukkit
    }

    public EntityPlayer a(NetLoginHandler netloginhandler, String s, String s1) {
        // CraftBukkit start - note: this entire method needs to be changed
        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome. Also change any reference to this.e.c to entity.world
        EntityPlayer entity = new EntityPlayer(this.server, this.server.worlds.get(0), s, new ItemInWorldManager(this.server.worlds.get(0)));
        Player player = (entity == null) ? null : (Player) entity.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(player);

        String s2 = netloginhandler.networkManager.getSocketAddress().toString();

        s2 = s2.substring(s2.indexOf("/") + 1);
        s2 = s2.substring(0, s2.indexOf(":"));

        if (this.banByName.contains(s.trim().toLowerCase())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned from this server!");
        } else if (!this.isWhitelisted(s)) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You are not white-listed on this server!");
        } else if (this.banByIP.contains(s2)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Your IP address is banned from this server!");
        } else if (this.players.size() >= this.maxPlayers) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "The server is full!");
        } else {
            event.disallow(PlayerLoginEvent.Result.ALLOWED, s2);
        }

        cserver.getPluginManager().callEvent(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            netloginhandler.disconnect(event.getKickMessage());
            return null;
        }

        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(i);

            if (entityplayer.name.equalsIgnoreCase(s)) {
                entityplayer.netServerHandler.disconnect("You logged in from another location");
            }
        }

        return new EntityPlayer(this.server, entity.world, s, new ItemInWorldManager(entity.world));
        // CraftBukkit end
    }

    public EntityPlayer d(EntityPlayer entityplayer) {
        // CraftBukkit start - every reference to this.minecraftServer.worldServer should be entityplayer.world
        this.server.tracker.trackPlayer(entityplayer);
        this.server.tracker.untrackEntity(entityplayer);
        ((WorldServer) entityplayer.world).manager.removePlayer(entityplayer);
        this.players.remove(entityplayer);
        entityplayer.world.removeEntity(entityplayer);
        ChunkCoordinates chunkcoordinates = entityplayer.H();
        EntityPlayer entityplayer1 = new EntityPlayer(this.server, entityplayer.world, entityplayer.name, new ItemInWorldManager(entityplayer.world));

        entityplayer1.id = entityplayer.id;
        entityplayer1.netServerHandler = entityplayer.netServerHandler;
        entityplayer1.displayName = entityplayer.displayName; // CraftBukkit
        entityplayer1.compassTarget = entityplayer.compassTarget; // CraftBukkit
        entityplayer1.fauxSleeping = entityplayer.fauxSleeping; // CraftBukkit

        if (chunkcoordinates != null) {
            ChunkCoordinates chunkcoordinates1 = EntityHuman.getBed(entityplayer.world, chunkcoordinates);

            if (chunkcoordinates1 != null) {
                entityplayer1.setPositionRotation((double) ((float) chunkcoordinates1.x + 0.5F), (double) ((float) chunkcoordinates1.y + 0.1F), (double) ((float) chunkcoordinates1.z + 0.5F), 0.0F, 0.0F);
                entityplayer1.a(chunkcoordinates);
            } else {
                entityplayer1.netServerHandler.sendPacket(new Packet70Bed(0));
            }
        }

        ((WorldServer) entityplayer.world).chunkProviderServer.getChunkAt((int) entityplayer1.locX >> 4, (int) entityplayer1.locZ >> 4);

        while (entityplayer.world.getEntities(entityplayer1, entityplayer1.boundingBox).size() != 0) {
            entityplayer1.setPosition(entityplayer1.locX, entityplayer1.locY + 1.0D, entityplayer1.locZ);
        }

        // CraftBukkit start
        Player respawnPlayer = cserver.getPlayer(entityplayer);
        Location respawnLocation = new Location(respawnPlayer.getWorld(), entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);

        PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, respawnLocation );
        cserver.getPluginManager().callEvent(respawnEvent);

        entityplayer1.world = ((CraftWorld) respawnEvent.getRespawnLocation().getWorld()).getHandle();
        entityplayer1.locX = respawnEvent.getRespawnLocation().getX();
        entityplayer1.locY = respawnEvent.getRespawnLocation().getY();
        entityplayer1.locZ = respawnEvent.getRespawnLocation().getZ();
        entityplayer1.yaw = respawnEvent.getRespawnLocation().getYaw();
        entityplayer1.pitch = respawnEvent.getRespawnLocation().getPitch();
        entityplayer1.itemInWorldManager = new ItemInWorldManager(((CraftWorld) respawnEvent.getRespawnLocation().getWorld()).getHandle());
        entityplayer1.itemInWorldManager.player = entityplayer1;
        ((WorldServer) entityplayer1.world).chunkProviderServer.getChunkAt((int) entityplayer1.locX >> 4, (int) entityplayer1.locZ >> 4);
        // CraftBukkit end

        entityplayer1.netServerHandler.sendPacket(new Packet9Respawn());
        entityplayer1.netServerHandler.a(entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);
        // CraftBukkit start
        ((WorldServer) entityplayer1.world).manager.addPlayer(entityplayer1);
        entityplayer.world.addEntity(entityplayer1);
        // CraftBukkit end
        this.players.add(entityplayer1);
        entityplayer1.syncInventory();
        entityplayer1.t();
        return entityplayer1;
    }

    public void b() {
        // CraftBukkit start
        for (WorldServer world: this.server.worlds) {
            world.manager.flush();
        }
        // CraftBukkit end
    }

    // CraftBukkit start - changed signature
    public void flagDirty(int i, int j, int k, WorldServer world) {
        world.manager.flagDirty(i, j, k);
    }
    // CraftBukkit end

    public void sendAll(Packet packet) {
        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(i);

            entityplayer.netServerHandler.sendPacket(packet);
        }
    }

    public String c() {
        String s = "";

        for (int i = 0; i < this.players.size(); ++i) {
            if (i > 0) {
                s = s + ", ";
            }

            s = s + ((EntityPlayer) this.players.get(i)).name;
        }

        return s;
    }

    public void a(String s) {
        this.banByName.add(s.toLowerCase());
        this.h();
    }

    public void b(String s) {
        this.banByName.remove(s.toLowerCase());
        this.h();
    }

    private void g() {
        try {
            this.banByName.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.j));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.banByName.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ban list: " + exception);
        }
    }

    private void h() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.j, false));
            Iterator iterator = this.banByName.iterator();

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
        this.banByIP.add(s.toLowerCase());
        this.j();
    }

    public void d(String s) {
        this.banByIP.remove(s.toLowerCase());
        this.j();
    }

    private void i() {
        try {
            this.banByIP.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.k));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.banByIP.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void j() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.k, false));
            Iterator iterator = this.banByIP.iterator();

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

    public boolean isWhitelisted(String s) {
        s = s.trim().toLowerCase();
        return !this.o || this.h.contains(s) || this.i.contains(s);
    }

    public boolean isOp(String s) {
        return this.h.contains(s.trim().toLowerCase());
    }

    public EntityPlayer i(String s) {
        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(i);

            if (entityplayer.name.equalsIgnoreCase(s)) {
                return entityplayer;
            }
        }

        return null;
    }

    public void a(String s, String s1) {
        EntityPlayer entityplayer = this.i(s);

        if (entityplayer != null) {
            entityplayer.netServerHandler.sendPacket(new Packet3Chat(s1));
        }
    }

    public void a(double d0, double d1, double d2, double d3, Packet packet) {
        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(i);
            double d4 = d0 - entityplayer.locX;
            double d5 = d1 - entityplayer.locY;
            double d6 = d2 - entityplayer.locZ;

            if (d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3) {
                entityplayer.netServerHandler.sendPacket(packet);
            }
        }
    }

    public void j(String s) {
        Packet3Chat packet3chat = new Packet3Chat(s);

        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(i);

            if (this.isOp(entityplayer.name)) {
                entityplayer.netServerHandler.sendPacket(packet3chat);
            }
        }
    }

    public boolean a(String s, Packet packet) {
        EntityPlayer entityplayer = this.i(s);

        if (entityplayer != null) {
            entityplayer.netServerHandler.sendPacket(packet);
            return true;
        } else {
            return false;
        }
    }

    public void savePlayers() {
        for (int i = 0; i < this.players.size(); ++i) {
            this.playerFileData.a((EntityHuman) this.players.get(i));
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
