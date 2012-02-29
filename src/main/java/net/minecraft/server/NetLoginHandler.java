package net.minecraft.server;

import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

// CraftBukkit start
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.server.ServerListPingEvent;
// CraftBukkit end

public class NetLoginHandler extends NetHandler {

    public static Logger logger = Logger.getLogger("Minecraft");
    private static Random random = new Random();
    public NetworkManager networkManager;
    public boolean c = false;
    private MinecraftServer server;
    private int f = 0;
    private String g = null;
    private Packet1Login h = null;
    private String loginKey = Long.toString(random.nextLong(), 16); // CraftBukkit - Security fix

    public NetLoginHandler(MinecraftServer minecraftserver, Socket socket, String s) {
        this.server = minecraftserver;
        this.networkManager = new NetworkManager(socket, s, this);
        this.networkManager.f = 0;
    }

    // CraftBukkit start
    public Socket getSocket() {
        return this.networkManager.socket;
    }
    // CraftBukkit end

    public void a() {
        if (this.h != null) {
            this.b(this.h);
            this.h = null;
        }

        if (this.f++ == 600) {
            this.disconnect("Took too long to log in");
        } else {
            this.networkManager.b();
        }
    }

    public void disconnect(String s) {
        try {
            logger.info("Disconnecting " + this.getName() + ": " + s);
            this.networkManager.queue(new Packet255KickDisconnect(s));
            this.networkManager.d();
            this.c = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(Packet2Handshake packet2handshake) {
        if (this.server.onlineMode) {
            this.loginKey = Long.toString(random.nextLong(), 16);
            this.networkManager.queue(new Packet2Handshake(this.loginKey));
        } else {
            this.networkManager.queue(new Packet2Handshake("-"));
        }
    }

    public void a(Packet1Login packet1login) {
        this.g = packet1login.name;
        if (packet1login.a != 23) {
            if (packet1login.a > 23) {
                this.disconnect("Outdated server!");
            } else {
                this.disconnect("Outdated client!");
            }
        } else {
            if (!this.server.onlineMode) {
                // CraftBukkit start - disallow colour in names
                if (!packet1login.name.equals(ChatColor.stripColor(packet1login.name))) {
                    this.disconnect("Colourful names are not permitted!");
                    return;
                }
                // CraftBukkit end
                this.b(packet1login);
            } else {
                (new ThreadLoginVerifier(this, packet1login, this.server.server)).start(); // CraftBukkit
            }
        }
    }

    public void b(Packet1Login packet1login) {
        EntityPlayer entityplayer = this.server.serverConfigurationManager.attemptLogin(this, packet1login.name);

        if (entityplayer != null) {
            this.server.serverConfigurationManager.b(entityplayer);
            // entityplayer.a((World) this.server.a(entityplayer.dimension)); // CraftBukkit - set by Entity
            entityplayer.itemInWorldManager.a((WorldServer) entityplayer.world);
            // CraftBukkit - add world and location to 'logged in' message.
            logger.info(this.getName() + " logged in with entity id " + entityplayer.id + " at ([" + entityplayer.world.worldData.name + "] " + entityplayer.locX + ", " + entityplayer.locY + ", " + entityplayer.locZ + ")");
            WorldServer worldserver = (WorldServer) entityplayer.world; // CraftBukkit
            ChunkCoordinates chunkcoordinates = worldserver.getSpawn();

            entityplayer.itemInWorldManager.b(worldserver.getWorldData().getGameType());
            NetServerHandler netserverhandler = new NetServerHandler(this.server, this.networkManager, entityplayer);

            // CraftBukkit start -- Don't send a higher than 60 MaxPlayer size, otherwise the PlayerInfo window won't render correctly.
            int maxPlayers = this.server.serverConfigurationManager.getMaxPlayers();
            if (maxPlayers > 60) {
                maxPlayers = 60;
            }
            netserverhandler.sendPacket(new Packet1Login("", entityplayer.id, worldserver.getSeed(), worldserver.getWorldData().getType(), entityplayer.itemInWorldManager.getGameMode(), (byte) worldserver.worldProvider.dimension, (byte) worldserver.difficulty, (byte) worldserver.height, (byte) maxPlayers));
            entityplayer.getBukkitEntity().sendSupportedChannels();
            // CraftBukkit end

            netserverhandler.sendPacket(new Packet6SpawnPosition(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z));
            this.server.serverConfigurationManager.a(entityplayer, worldserver);
            // this.server.serverConfigurationManager.sendAll(new Packet3Chat("\u00A7e" + entityplayer.name + " joined the game.")); // CraftBukkit - message moved to join event
            this.server.serverConfigurationManager.c(entityplayer);
            netserverhandler.a(entityplayer.locX, entityplayer.locY, entityplayer.locZ, entityplayer.yaw, entityplayer.pitch);
            this.server.networkListenThread.a(netserverhandler);
            netserverhandler.sendPacket(new Packet4UpdateTime(entityplayer.getPlayerTime())); // CraftBukkit - add support for player specific time
            Iterator iterator = entityplayer.getEffects().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                netserverhandler.sendPacket(new Packet41MobEffect(entityplayer.id, mobeffect));
            }

            entityplayer.syncInventory();
        }

        this.c = true;
    }

    public void a(String s, Object[] aobject) {
        logger.info(this.getName() + " lost connection");
        this.c = true;
    }

    public void a(Packet254GetInfo packet254getinfo) {
        if (this.networkManager.getSocket() == null) return; // CraftBukkit - fix NPE when a client queries a server that is unable to handle it.
        try {
            // CraftBukkit start
            ServerListPingEvent pingEvent = CraftEventFactory.callServerListPingEvent(this.server.server, getSocket().getInetAddress(), this.server.motd, this.server.serverConfigurationManager.getPlayerCount(), this.server.serverConfigurationManager.getMaxPlayers());
            String s = pingEvent.getMotd() + "\u00A7" + this.server.serverConfigurationManager.getPlayerCount() + "\u00A7" + pingEvent.getMaxPlayers();
            // CraftBukkit end

            this.networkManager.queue(new Packet255KickDisconnect(s));
            this.networkManager.d();
            this.server.networkListenThread.a(this.networkManager.getSocket());
            this.c = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void onUnhandledPacket(Packet packet) {
        this.disconnect("Protocol error");
    }

    public String getName() {
        return this.g != null ? this.g + " [" + this.networkManager.getSocketAddress().toString() + "]" : this.networkManager.getSocketAddress().toString();
    }

    public boolean c() {
        return true;
    }

    static String a(NetLoginHandler netloginhandler) {
        return netloginhandler.loginKey;
    }

    static Packet1Login a(NetLoginHandler netloginhandler, Packet1Login packet1login) {
        return netloginhandler.h = packet1login;
    }
}
