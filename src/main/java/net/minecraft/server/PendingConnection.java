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

    private byte[] d;
    public static Logger logger = Logger.getLogger("Minecraft");
    private static Random random = new Random();
    public NetworkManager networkManager;
    public boolean c = false;
    private MinecraftServer server;
    private int g = 0;
    private String h = null;
    private volatile boolean i = false;
    private String loginKey = Long.toString(random.nextLong(), 16); // CraftBukkit - Security fix
    private boolean k = false;
    private SecretKey l = null;
    public String hostname = ""; // CraftBukkit - add field

    public PendingConnection(MinecraftServer minecraftserver, Socket socket, String s) throws java.io.IOException { // CraftBukkit - throws IOException
        this.server = minecraftserver;
        this.networkManager = new NetworkManager(socket, s, this, minecraftserver.F().getPrivate());
        this.networkManager.e = 0;
    }

    // CraftBukkit start
    public Socket getSocket() {
        return this.networkManager.getSocket();
    }
    // CraftBukkit end

    public void c() {
        if (this.i) {
            this.d();
        }

        if (this.g++ == 600) {
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
        // CraftBukkit start
        this.hostname = packet2handshake.c == null ? "" : packet2handshake.c + ':' + packet2handshake.d;
        // CraftBukkit end
        this.h = packet2handshake.f();
        if (!this.h.equals(StripColor.a(this.h))) {
            this.disconnect("Invalid username!");
        } else {
            PublicKey publickey = this.server.F().getPublic();

            if (packet2handshake.d() != 51) {
                if (packet2handshake.d() > 51) {
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

        this.l = packet252keyresponse.a(privatekey);
        if (!Arrays.equals(this.d, packet252keyresponse.b(privatekey))) {
            this.disconnect("Invalid client reply");
        }

        this.networkManager.queue(new Packet252KeyResponse());
    }

    public void a(Packet205ClientCommand packet205clientcommand) {
        if (packet205clientcommand.a == 0) {
            if (this.server.getOnlineMode()) {
                if (this.k) {
                    this.disconnect("Duplicate login");
                    return;
                }
                this.k = true;
                (new ThreadLoginVerifier(this, server.server)).start(); // CraftBukkit - add CraftServer
            } else {
                this.i = true;
            }
        }
    }

    public void a(Packet1Login packet1login) {}

    public void d() {
        // CraftBukkit start
        EntityPlayer s = this.server.getPlayerList().attemptLogin(this, this.h, this.hostname);

        if (s == null) {
            return;
            // CraftBukkit end
        } else {
            EntityPlayer entityplayer = this.server.getPlayerList().processLogin(s); // CraftBukkit - this.h -> s

            if (entityplayer != null) {
                this.server.getPlayerList().a((INetworkManager) this.networkManager, entityplayer);
            }
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
            PlayerList playerlist = this.server.getPlayerList();
            String s = null;
            // CraftBukkit
            org.bukkit.event.server.ServerListPingEvent pingEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callServerListPingEvent(this.server.server, getSocket().getInetAddress(), this.server.getMotd(), playerlist.getPlayerCount(), playerlist.getMaxPlayers());

            if (packet254getinfo.a == 1) {
                // CraftBukkit start - fix decompile issues, don't create a list from an array
                Object[] list = new Object[] { 1, 51, this.server.getVersion(), pingEvent.getMotd(), playerlist.getPlayerCount(), pingEvent.getMaxPlayers() };

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

            this.c = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void onUnhandledPacket(Packet packet) {
        this.disconnect("Protocol error");
    }

    public String getName() {
        return this.h != null ? this.h + " [" + this.networkManager.getSocketAddress().toString() + "]" : this.networkManager.getSocketAddress().toString();
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
        return pendingconnection.l;
    }

    static String d(PendingConnection pendingconnection) {
        return pendingconnection.h;
    }

    static boolean a(PendingConnection pendingconnection, boolean flag) {
        return pendingconnection.i = flag;
    }
}
