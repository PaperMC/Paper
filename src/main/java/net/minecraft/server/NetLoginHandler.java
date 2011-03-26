package net.minecraft.server;

import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

public class NetLoginHandler extends NetHandler {

    public static Logger a = Logger.getLogger("Minecraft");
    private static Random d = new Random();
    public NetworkManager b;
    public boolean c = false;
    private MinecraftServer e;
    private int f = 0;
    private String g = null;
    private Packet1Login h = null;
    private String i = "";

    public NetLoginHandler(MinecraftServer minecraftserver, Socket socket, String s) {
        this.e = minecraftserver;
        this.b = new NetworkManager(socket, s, this);
        this.b.d = 0;
    }

    public void a() {
        if (this.h != null) {
            this.b(this.h);
            this.h = null;
        }

        if (this.f++ == 600) {
            this.a("Took too long to log in");
        } else {
            this.b.a();
        }
    }

    public void a(String s) {
        try {
            a.info("Disconnecting " + this.b() + ": " + s);
            this.b.a((Packet) (new Packet255KickDisconnect(s)));
            this.b.c();
            this.c = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(Packet2Handshake packet2handshake) {
        if (this.e.l) {
            this.i = Long.toHexString(d.nextLong());
            this.b.a((Packet) (new Packet2Handshake(this.i)));
        } else {
            this.b.a((Packet) (new Packet2Handshake("-")));
        }
    }

    public void a(Packet1Login packet1login) {
        this.g = packet1login.b;
        if (packet1login.a != 9) {
            if (packet1login.a > 9) {
                this.a("Outdated server!");
            } else {
                this.a("Outdated client!");
            }
        } else {
            if (!this.e.l) {
                this.b(packet1login);
            } else {
                (new ThreadLoginVerifier(this, packet1login)).start();
            }
        }
    }

    public void b(Packet1Login packet1login) {
        EntityPlayer entityplayer = this.e.f.a(this, packet1login.b, packet1login.c);

        if (entityplayer != null) {
            a.info(this.b() + " logged in with entity id " + entityplayer.id);
            NetServerHandler netserverhandler = new NetServerHandler(this.e, this.b, entityplayer);

            // CraftBukkit start
            ChunkCoordinates chunkcoordinates = entityplayer.world.l();
            netserverhandler.b((Packet) (new Packet1Login("", "", entityplayer.id, entityplayer.world.j(), (byte) entityplayer.world.m.g)));
            netserverhandler.b((Packet) (new Packet6SpawnPosition(chunkcoordinates.a, chunkcoordinates.b, chunkcoordinates.c)));
            // this.e.f.a((Packet) (new Packet3Chat("\u00A7e" + entityplayer.name + " joined the game.")));  // CraftBukkit - message moved to join event
            this.e.f.a(entityplayer);
            netserverhandler.a(entityplayer.locX, entityplayer.locY, entityplayer.locZ, entityplayer.yaw, entityplayer.pitch);
            this.e.c.a(netserverhandler);
            netserverhandler.b((Packet) (new Packet4UpdateTime(entityplayer.world.k())));
            // CraftBukkit end

            entityplayer.l();
        }

        this.c = true;
    }

    public void a(String s, Object[] aobject) {
        a.info(this.b() + " lost connection");
        this.c = true;
    }

    public void a(Packet packet) {
        this.a("Protocol error");
    }

    public String b() {
        return this.g != null ? this.g + " [" + this.b.b().toString() + "]" : this.b.b().toString();
    }

    static String a(NetLoginHandler netloginhandler) {
        return netloginhandler.i;
    }

    static Packet1Login a(NetLoginHandler netloginhandler, Packet1Login packet1login) {
        return netloginhandler.h = packet1login;
    }
}
