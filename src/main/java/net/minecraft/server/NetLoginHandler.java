package net.minecraft.server;

import java.net.Socket;
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
    private String i = "";

    public NetLoginHandler(MinecraftServer minecraftserver, Socket socket, String s) {
        this.server = minecraftserver;
        this.networkManager = new NetworkManager(socket, s, this);
        this.networkManager.d = 0;
    }

    // CraftBukkit start
    public Socket getSocket() {
        return networkManager.socket;
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
            this.networkManager.a();
        }
    }

    public void disconnect(String s) {
        try {
            a.info("Disconnecting " + this.b() + ": " + s);
            this.networkManager.a((Packet) (new Packet255KickDisconnect(s)));
            this.networkManager.c();
            this.c = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(Packet2Handshake packet2handshake) {
        if (this.server.onlineMode) {
            this.i = Long.toHexString(d.nextLong());
            this.networkManager.a((Packet) (new Packet2Handshake(this.i)));
        } else {
            this.networkManager.a((Packet) (new Packet2Handshake("-")));
        }
    }

    public void a(Packet1Login packet1login) {
        this.g = packet1login.name;
        if (packet1login.a != 10) {
            if (packet1login.a > 10) {
                this.disconnect("Outdated server!");
            } else {
                this.disconnect("Outdated client!");
            }
        } else {
            if (!this.server.onlineMode) {
                this.b(packet1login);
            } else {
                (new ThreadLoginVerifier(this, packet1login, this.server.server)).start();
            }
        }
    }

    public void b(Packet1Login packet1login) {
        EntityPlayer entityplayer = this.server.serverConfigurationManager.a(this, packet1login.name, packet1login.c);

        if (entityplayer != null) {
            a.info(this.b() + " logged in with entity id " + entityplayer.id);
            ChunkCoordinates chunkcoordinates = entityplayer.world.getSpawn(); // CraftBukkit
            NetServerHandler netserverhandler = new NetServerHandler(this.server, this.networkManager, entityplayer);

            netserverhandler.sendPacket(new Packet1Login("", "", entityplayer.id, entityplayer.world.getSeed(), (byte) entityplayer.world.worldProvider.dimension)); // CraftBukkit
            netserverhandler.sendPacket(new Packet6SpawnPosition(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z));
            // this.server.serverConfigurationManager.sendAll(new Packet3Chat("\u00A7e" + entityplayer.name + " joined the game."));  // CraftBukkit - message moved to join event
            this.server.serverConfigurationManager.a(entityplayer);
            netserverhandler.a(entityplayer.locX, entityplayer.locY, entityplayer.locZ, entityplayer.yaw, entityplayer.pitch);
            this.server.networkListenThread.a(netserverhandler);
            netserverhandler.sendPacket(new Packet4UpdateTime(entityplayer.world.getTime())); // CraftBukkit
            entityplayer.syncInventory();
        }

        this.c = true;
    }

    public void a(String s, Object[] aobject) {
        a.info(this.b() + " lost connection");
        this.c = true;
    }

    public void a(Packet packet) {
        this.disconnect("Protocol error");
    }

    public String b() {
        return this.g != null ? this.g + " [" + this.networkManager.getSocketAddress().toString() + "]" : this.networkManager.getSocketAddress().toString();
    }

    static String a(NetLoginHandler netloginhandler) {
        return netloginhandler.i;
    }

    static Packet1Login a(NetLoginHandler netloginhandler, Packet1Login packet1login) {
        return netloginhandler.h = packet1login;
    }
}
