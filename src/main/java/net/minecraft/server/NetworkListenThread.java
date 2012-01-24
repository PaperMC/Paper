package net.minecraft.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException; // CraftBukkit

public class NetworkListenThread {

    public static Logger a = Logger.getLogger("Minecraft");
    private ServerSocket d;
    private Thread e;
    public volatile boolean b = false;
    private int f = 0;
    private ArrayList g = new ArrayList();
    private ArrayList h = new ArrayList();
    public MinecraftServer c;
    private HashMap i = new HashMap();

    public NetworkListenThread(MinecraftServer minecraftserver, InetAddress inetaddress, int i) throws IOException { // CraftBukkit
        this.c = minecraftserver;
        this.d = new ServerSocket(i, 0, inetaddress);
        this.d.setPerformancePreferences(0, 2, 1);
        this.b = true;
        this.e = new NetworkAcceptThread(this, "Listen thread", minecraftserver);
        this.e.start();
    }

    public void a(Socket socket) {
        InetAddress inetaddress = socket.getInetAddress();
        HashMap hashmap = this.i;

        synchronized (this.i) {
            this.i.remove(inetaddress);
        }
    }

    public void a(NetServerHandler netserverhandler) {
        this.h.add(netserverhandler);
    }

    private void a(NetLoginHandler netloginhandler) {
        if (netloginhandler == null) {
            throw new IllegalArgumentException("Got null pendingconnection!");
        } else {
            this.g.add(netloginhandler);
        }
    }

    public void a() {
        int i;

        for (i = 0; i < this.g.size(); ++i) {
            NetLoginHandler netloginhandler = (NetLoginHandler) this.g.get(i);

            try {
                netloginhandler.a();
            } catch (Exception exception) {
                netloginhandler.disconnect("Internal server error");
                a.log(Level.WARNING, "Failed to handle packet: " + exception, exception);
            }

            if (netloginhandler.c) {
                this.g.remove(i--);
            }

            netloginhandler.networkManager.a();
        }

        for (i = 0; i < this.h.size(); ++i) {
            NetServerHandler netserverhandler = (NetServerHandler) this.h.get(i);

            try {
                netserverhandler.a();
            } catch (Exception exception1) {
                a.log(Level.WARNING, "Failed to handle packet: " + exception1, exception1);
                netserverhandler.disconnect("Internal server error");
            }

            if (netserverhandler.disconnected) {
                this.h.remove(i--);
            }

            netserverhandler.networkManager.a();
        }
    }

    static ServerSocket a(NetworkListenThread networklistenthread) {
        return networklistenthread.d;
    }

    static HashMap b(NetworkListenThread networklistenthread) {
        return networklistenthread.i;
    }

    static int c(NetworkListenThread networklistenthread) {
        return networklistenthread.f++;
    }

    static void a(NetworkListenThread networklistenthread, NetLoginHandler netloginhandler) {
        networklistenthread.a(netloginhandler);
    }
}
