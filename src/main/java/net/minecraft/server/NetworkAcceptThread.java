package net.minecraft.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

class NetworkAcceptThread extends Thread {

    final MinecraftServer a;

    final NetworkListenThread listenThread;

    long connectionThrottle; // CraftBukkit

    NetworkAcceptThread(NetworkListenThread networklistenthread, String s, MinecraftServer minecraftserver) {
        super(s);
        this.listenThread = networklistenthread;
        this.a = minecraftserver;
    }

    public void run() {
        while (this.listenThread.b) {
            try {
                Socket socket = NetworkListenThread.a(this.listenThread).accept();

                if (socket != null) {
                    synchronized (NetworkListenThread.getRecentConnectionAttempts(this.listenThread)) {
                        InetAddress inetaddress = socket.getInetAddress();
                        connectionThrottle = this.a.server.getConnectionThrottle(); // CraftBukkit

                        // CraftBukkit
                        if (NetworkListenThread.getRecentConnectionAttempts(this.listenThread).containsKey(inetaddress) && System.currentTimeMillis() - ((Long) NetworkListenThread.getRecentConnectionAttempts(this.listenThread).get(inetaddress)).longValue() < connectionThrottle) {
                            NetworkListenThread.getRecentConnectionAttempts(this.listenThread).put(inetaddress, Long.valueOf(System.currentTimeMillis()));
                            socket.close();
                            continue;
                        }

                        NetworkListenThread.getRecentConnectionAttempts(this.listenThread).put(inetaddress, Long.valueOf(System.currentTimeMillis()));
                    }

                    NetLoginHandler netloginhandler = new NetLoginHandler(this.a, socket, "Connection #" + NetworkListenThread.c(this.listenThread));

                    NetworkListenThread.a(this.listenThread, netloginhandler);
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }
}
