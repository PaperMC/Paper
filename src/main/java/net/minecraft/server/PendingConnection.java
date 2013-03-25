package net.minecraft.server;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

public class PendingConnection extends Connection {

    private static Random random = new Random();
    private byte[] d;
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    public boolean b = false;
    private int f = 0;
    private String g = null;
    private volatile boolean h = false;
    private String loginKey = Long.toString(random.nextLong(), 16); // CraftBukkit - Security fix
    private boolean j = false;
    private SecretKey k = null;
    public String hostname = ""; // CraftBukkit - add field

    public PendingConnection(MinecraftServer minecraftserver, Socket socket, String s) throws java.io.IOException { // CraftBukkit - throws IOException
        this.server = minecraftserver;
        this.networkManager = new NetworkManager(minecraftserver.getLogger(), socket, s, this, minecraftserver.F().getPrivate());
        this.networkManager.e = 0;
    }

    // CraftBukkit start
    public Socket getSocket() {
        return this.networkManager.getSocket();
    }
    // CraftBukkit end

    public void c() {
        if (this.h) {
            this.d();
        }

        if (this.f++ == 600) {
            this.disconnect("Took too long to log in");
        } else {
            this.networkManager.b();
        }
    }

    public void disconnect(String s) {
        try {
            this.server.getLogger().info("Disconnecting " + this.getName() + ": " + s);
            this.networkManager.queue(new Packet255KickDisconnect(s));
            this.networkManager.d();
            this.b = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(Packet2Handshake packet2handshake) {
        // CraftBukkit start
        this.hostname = packet2handshake.c == null ? "" : packet2handshake.c + ':' + packet2handshake.d;
        // CraftBukkit end
        this.g = packet2handshake.f();
        if (!this.g.equals(StripColor.a(this.g))) {
            this.disconnect("Invalid username!");
        } else {
            PublicKey publickey = this.server.F().getPublic();

            if (packet2handshake.d() != 60) {
                if (packet2handshake.d() > 60) {
                    this.disconnect("Outdated server!");
                } else {
                    this.disconnect("Outdated client!");
                }
            } else {
                this.loginKey = this.server.getOnlineMode() ? Long.toString(random.nextLong(), 16) : "-";
                this.d = new byte[4];
                random.nextBytes(this.d);
                this.networkManager.queue(new Packet253KeyRequest(this.loginKey, publickey, this.d));
            }
        }
    }

    public void a(Packet252KeyResponse packet252keyresponse) {
        PrivateKey privatekey = this.server.F().getPrivate();

        this.k = packet252keyresponse.a(privatekey);
        if (!Arrays.equals(this.d, packet252keyresponse.b(privatekey))) {
            this.disconnect("Invalid client reply");
        }

        this.networkManager.queue(new Packet252KeyResponse());
    }

    public void a(Packet205ClientCommand packet205clientcommand) {
        if (packet205clientcommand.a == 0) {
            if (this.server.getOnlineMode()) {
                if (this.j) {
                    this.disconnect("Duplicate login");
                    return;
                }
                this.j = true;
                (new ThreadLoginVerifier(this, server.server)).start(); // CraftBukkit - add CraftServer
            } else {
                this.h = true;
            }
        }
    }

    public void a(Packet1Login packet1login) {}

    public void d() {
        // CraftBukkit start
        EntityPlayer s = this.server.getPlayerList().attemptLogin(this, this.g, this.hostname);

        if (s == null) {
            return;
            // CraftBukkit end
        } else {
            EntityPlayer entityplayer = this.server.getPlayerList().processLogin(s); // CraftBukkit - this.h -> s

            if (entityplayer != null) {
                this.server.getPlayerList().a((INetworkManager) this.networkManager, entityplayer);
            }
        }

        this.b = true;
    }

    public void a(String s, Object[] aobject) {
        this.server.getLogger().info(this.getName() + " lost connection");
        this.b = true;
    }

    public void a(Packet254GetInfo packet254getinfo) {
        if (this.networkManager.getSocket() == null) return; // CraftBukkit - fix NPE when a client queries a server that is unable to handle it.
        try {
            PlayerList playerlist = this.server.getPlayerList();
            String s = null;
            // CraftBukkit
            org.bukkit.event.server.ServerListPingEvent pingEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callServerListPingEvent(this.server.server, getSocket().getInetAddress(), this.server.getMotd(), playerlist.getPlayerCount(), playerlist.getMaxPlayers());

            if (packet254getinfo.a == 1) {
                // CraftBukkit start - Fix decompile issues, don't create a list from an array
                Object[] list = new Object[] { 1, 60, this.server.getVersion(), pingEvent.getMotd(), playerlist.getPlayerCount(), pingEvent.getMaxPlayers() };

                for (Object object : list) {
                    if (s == null) {
                        s = "\u00A7";
                    } else {
                        s = s + "\0";
                    }

                    s += org.apache.commons.lang.StringUtils.replace(object.toString(), "\0", "");
                }
                // CraftBukkit end
            } else {
                // CraftBukkit
                s = pingEvent.getMotd() + "\u00A7" + playerlist.getPlayerCount() + "\u00A7" + pingEvent.getMaxPlayers();
            }

            InetAddress inetaddress = null;

            if (this.networkManager.getSocket() != null) {
                inetaddress = this.networkManager.getSocket().getInetAddress();
            }

            this.networkManager.queue(new Packet255KickDisconnect(s));
            this.networkManager.d();
            if (inetaddress != null && this.server.ae() instanceof DedicatedServerConnection) {
                ((DedicatedServerConnection) this.server.ae()).a(inetaddress);
            }

            this.b = true;
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

    public boolean a() {
        return true;
    }

    static String a(PendingConnection pendingconnection) {
        return pendingconnection.loginKey;
    }

    static MinecraftServer b(PendingConnection pendingconnection) {
        return pendingconnection.server;
    }

    static SecretKey c(PendingConnection pendingconnection) {
        return pendingconnection.k;
    }

    static String d(PendingConnection pendingconnection) {
        return pendingconnection.g;
    }

    static boolean a(PendingConnection pendingconnection, boolean flag) {
        return pendingconnection.h = flag;
    }
}
