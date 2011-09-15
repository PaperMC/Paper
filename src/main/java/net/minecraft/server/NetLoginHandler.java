package net.minecraft.server;

import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

public class NetLoginHandler extends NetHandler {

    public static Logger a = Logger.getLogger("Minecraft");
    private static Random d = new Random();
    public NetworkManager networkManager;
    public boolean c = false;
    private MinecraftServer server;
    private int f = 0;
    private String g = null;
    private Packet1Login h = null;
    private String i = Long.toHexString(d.nextLong());

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
            a.info("Disconnecting " + this.b() + ": " + s);
            this.networkManager.queue(new Packet255KickDisconnect(s));
            this.networkManager.d();
            this.c = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(Packet2Handshake packet2handshake) {
        if (this.server.onlineMode) {
            // this.i = Long.toHexString(d.nextLong()); // CraftBukkit
            this.networkManager.queue(new Packet2Handshake(this.i));
        } else {
            this.networkManager.queue(new Packet2Handshake("-"));
        }
    }

    public void a(Packet1Login packet1login) {
        this.g = packet1login.name;
        if (packet1login.a != 17) {
            if (packet1login.a > 17) {
                this.disconnect("Outdated server!");
            } else {
                this.disconnect("Outdated client!");
            }
        } else {
            if (!this.server.onlineMode) {
                this.b(packet1login);
            } else {
                (new ThreadLoginVerifier(this, packet1login, this.server.server)).start(); // CraftBukkit
            }
        }
    }

    public void b(Packet1Login packet1login) {
        EntityPlayer entityplayer = this.server.serverConfigurationManager.a(this, packet1login.name);

        if (entityplayer != null) {
            this.server.serverConfigurationManager.b(entityplayer);
            // entityplayer.a((World) this.server.a(entityplayer.dimension)); // CraftBukkit - set by Entity
            // CraftBukkit - add world and location to 'logged in' message.
            entityplayer.itemInWorldManager.a((WorldServer) entityplayer.world);
            a.info(this.b() + " logged in with entity id " + entityplayer.id + " at ([" + entityplayer.world.worldData.name + "] " + entityplayer.locX + ", " + entityplayer.locY + ", " + entityplayer.locZ + ")");
            WorldServer worldserver = (WorldServer) entityplayer.world; // CraftBukkit
            ChunkCoordinates chunkcoordinates = worldserver.getSpawn();

            entityplayer.itemInWorldManager.b(worldserver.p().n());
            NetServerHandler netserverhandler = new NetServerHandler(this.server, this.networkManager, entityplayer);

            int i = entityplayer.id;
            long j = worldserver.getSeed();
            int k = entityplayer.itemInWorldManager.a();
            byte b0 = (byte) worldserver.worldProvider.dimension;
            byte b1 = (byte) worldserver.spawnMonsters;

            worldserver.getClass();
            Packet1Login packet1login1 = new Packet1Login("", i, j, k, b0, b1, (byte) -128, (byte) this.server.serverConfigurationManager.h());

            netserverhandler.sendPacket(packet1login1);
            netserverhandler.sendPacket(new Packet6SpawnPosition(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z));
            this.server.serverConfigurationManager.a(entityplayer, worldserver);
            // this.server.serverConfigurationManager.sendAll(new Packet3Chat("\u00A7e" + entityplayer.name + " joined the game."));  // CraftBukkit - message moved to join event
            this.server.serverConfigurationManager.c(entityplayer);
            netserverhandler.a(entityplayer.locX, entityplayer.locY, entityplayer.locZ, entityplayer.yaw, entityplayer.pitch);
            this.server.networkListenThread.a(netserverhandler);
            netserverhandler.sendPacket(new Packet4UpdateTime(entityplayer.getPlayerTime())); // CraftBukkit - add support for player specific time
            Iterator iterator = entityplayer.ak().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                netserverhandler.sendPacket(new Packet41MobEffect(entityplayer.id, mobeffect));
            }

            entityplayer.syncInventory();
        }

        this.c = true;
    }

    public void a(String s, Object[] aobject) {
        a.info(this.b() + " lost connection");
        this.c = true;
    }

    public void a(Packet254GetInfo packet254getinfo) {
        try {
            String s = this.server.p + "\u00A7" + this.server.serverConfigurationManager.g() + "\u00A7" + this.server.serverConfigurationManager.h();

            this.networkManager.queue(new Packet255KickDisconnect(s));
            this.networkManager.d();
            this.server.networkListenThread.a(this.networkManager.f());
            this.c = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(Packet packet) {
        this.disconnect("Protocol error");
    }

    public String b() {
        return this.g != null ? this.g + " [" + this.networkManager.getSocketAddress().toString() + "]" : this.networkManager.getSocketAddress().toString();
    }

    public boolean c() {
        return true;
    }

    static String a(NetLoginHandler netloginhandler) {
        return netloginhandler.i;
    }

    static Packet1Login a(NetLoginHandler netloginhandler, Packet1Login packet1login) {
        return netloginhandler.h = packet1login;
    }
}
