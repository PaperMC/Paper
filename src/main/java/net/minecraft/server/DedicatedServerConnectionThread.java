package net.minecraft.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DedicatedServerConnectionThread extends Thread {

    private static Logger a = Logger.getLogger("Minecraft");
    private final List b = Collections.synchronizedList(new ArrayList());
    private final HashMap c = new HashMap();
    private int d = 0;
    private final ServerSocket e;
    private ServerConnection f;
    private final InetAddress g;
    private final int h;

    long connectionThrottle; // CraftBukkit

    public DedicatedServerConnectionThread(ServerConnection serverconnection, InetAddress inetaddress, int i) throws IOException { // CraftBukkit - added throws
        super("Listen thread");
        this.f = serverconnection;
        this.g = inetaddress;
        this.h = i;
        this.e = new ServerSocket(i, 0, inetaddress);
        this.e.setPerformancePreferences(0, 2, 1);
    }

    public void a() {
        List list = this.b;

        synchronized (this.b) {
            for (int i = 0; i < this.b.size(); ++i) {
                NetLoginHandler netloginhandler = (NetLoginHandler) this.b.get(i);

                try {
                    netloginhandler.c();
                } catch (Exception exception) {
                    netloginhandler.disconnect("Internal server error");
                    a.log(Level.WARNING, "Failed to handle packet: " + exception, exception);
                }

                if (netloginhandler.c) {
                    this.b.remove(i--);
                }

                netloginhandler.networkManager.a();
            }
        }
    }

    public void run() {
        while (this.f.b) {
            try {
                Socket socket = this.e.accept();
                InetAddress inetaddress = socket.getInetAddress();
                long i = System.currentTimeMillis();
                HashMap hashmap = this.c;

                // CraftBukkit start
                if (((MinecraftServer) this.f.d()).server == null) {
                    socket.close();
                    continue;
                }

                connectionThrottle = ((MinecraftServer) this.f.d()).server.getConnectionThrottle();
                // CraftBukkit end

                synchronized (this.c) {
                    if (this.c.containsKey(inetaddress) && !b(inetaddress) && i - ((Long) this.c.get(inetaddress)).longValue() < connectionThrottle) {
                        this.c.put(inetaddress, Long.valueOf(i));
                        socket.close();
                        continue;
                    }

                    this.c.put(inetaddress, Long.valueOf(i));
                }

                NetLoginHandler netloginhandler = new NetLoginHandler(this.f.d(), socket, "Connection #" + this.d++);

                this.a(netloginhandler);
            } catch (IOException ioexception) {
                a.warning("DSCT: " + ioexception.getMessage()); // CraftBukkit
            }
        }

        System.out.println("Closing listening thread");
    }

    private void a(NetLoginHandler netloginhandler) {
        if (netloginhandler == null) {
            throw new IllegalArgumentException("Got null pendingconnection!");
        } else {
            List list = this.b;

            synchronized (this.b) {
                this.b.add(netloginhandler);
            }
        }
    }

    private static boolean b(InetAddress inetaddress) {
        return "127.0.0.1".equals(inetaddress.getHostAddress());
    }

    public void a(InetAddress inetaddress) {
        if (inetaddress != null) {
            HashMap hashmap = this.c;

            synchronized (this.c) {
                this.c.remove(inetaddress);
            }
        }
    }

    public void b() {
        try {
            this.e.close();
        } catch (Throwable throwable) {
            ;
        }
    }
}
