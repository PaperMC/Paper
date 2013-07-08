package net.minecraft.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DedicatedServerConnectionThread extends Thread {

    private final List a = Collections.synchronizedList(new ArrayList());
    private final HashMap b = new HashMap();
    private int c;
    private final ServerSocket d;
    private ServerConnection e;
    private final InetAddress f;
    private final int g;

    long connectionThrottle; // CraftBukkit

    public DedicatedServerConnectionThread(ServerConnection serverconnection, InetAddress inetaddress, int i) throws IOException { // CraftBukkit - added throws
        super("Listen thread");
        this.e = serverconnection;
        this.g = i;
        this.d = new ServerSocket(i, 0, inetaddress);
        this.f = inetaddress == null ? this.d.getInetAddress() : inetaddress;
        this.d.setPerformancePreferences(0, 2, 1);
    }

    public void a() {
        List list = this.a;

        synchronized (this.a) {
            for (int i = 0; i < this.a.size(); ++i) {
                PendingConnection pendingconnection = (PendingConnection) this.a.get(i);

                try {
                    pendingconnection.d();
                } catch (Exception exception) {
                    pendingconnection.disconnect("Internal server error");
                    this.e.d().getLogger().warning("Failed to handle packet for " + pendingconnection.getName() + ": " + exception, (Throwable) exception);
                }

                if (pendingconnection.b) {
                    this.a.remove(i--);
                }

                pendingconnection.networkManager.a();
            }
        }
    }

    public void run() {
        while (this.e.a) {
            try {
                Socket socket = this.d.accept();

                // CraftBukkit start - Connection throttle
                InetAddress address = socket.getInetAddress();
                long currentTime = System.currentTimeMillis();

                if (((MinecraftServer) this.e.d()).server == null) {
                    socket.close();
                    continue;
                }

                connectionThrottle = ((MinecraftServer) this.e.d()).server.getConnectionThrottle();

                synchronized (this.b) {
                    if (this.b.containsKey(address) && !"127.0.0.1".equals(address.getHostAddress()) && currentTime - ((Long) this.b.get(address)).longValue() < connectionThrottle) {
                        this.b.put(address, Long.valueOf(currentTime));
                        socket.close();
                        continue;
                    }

                    this.b.put(address, Long.valueOf(currentTime));
                }
                // CraftBukkit end

                PendingConnection pendingconnection = new PendingConnection(this.e.d(), socket, "Connection #" + this.c++);

                this.a(pendingconnection);
            } catch (IOException ioexception) {
                this.e.d().getLogger().warning("DSCT: " + ioexception.getMessage()); // CraftBukkit
            }
        }

        this.e.d().getLogger().info("Closing listening thread");
    }

    private void a(PendingConnection pendingconnection) {
        if (pendingconnection == null) {
            throw new IllegalArgumentException("Got null pendingconnection!");
        } else {
            List list = this.a;

            synchronized (this.a) {
                this.a.add(pendingconnection);
            }
        }
    }

    public void a(InetAddress inetaddress) {
        if (inetaddress != null) {
            HashMap hashmap = this.b;

            synchronized (this.b) {
                this.b.remove(inetaddress);
            }
        }
    }

    public void b() {
        try {
            this.d.close();
        } catch (Throwable throwable) {
            ;
        }
    }
}
