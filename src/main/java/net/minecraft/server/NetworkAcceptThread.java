package net.minecraft.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

class NetworkAcceptThread extends Thread {

    final MinecraftServer a;

    final NetworkListenThread b;

    NetworkAcceptThread(NetworkListenThread networklistenthread, String s, MinecraftServer minecraftserver) {
        super(s);
        this.b = networklistenthread;
        this.a = minecraftserver;
    }

    public void run() {
        while (this.b.b) {
            try {
                Socket socket = NetworkListenThread.a(this.b).accept();

                if (socket != null) {
                    synchronized (NetworkListenThread.b(this.b)) {
                        InetAddress inetaddress = socket.getInetAddress();

                        if (NetworkListenThread.b(this.b).containsKey(inetaddress) && System.currentTimeMillis() - ((Long) NetworkListenThread.b(this.b).get(inetaddress)).longValue() < 6000L) { // CraftBukkit
                            NetworkListenThread.b(this.b).put(inetaddress, Long.valueOf(System.currentTimeMillis()));
                            socket.close();
                            continue;
                        }

                        NetworkListenThread.b(this.b).put(inetaddress, Long.valueOf(System.currentTimeMillis()));
                    }

                    NetLoginHandler netloginhandler = new NetLoginHandler(this.a, socket, "Connection #" + NetworkListenThread.c(this.b));

                    NetworkListenThread.a(this.b, netloginhandler);
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }
}
