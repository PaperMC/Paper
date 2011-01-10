package net.minecraft.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

class NetworkAcceptThread extends Thread {

    final MinecraftServer a; /* synthetic field */

    final NetworkListenThread b; /* synthetic field */


    NetworkAcceptThread(NetworkListenThread networklistenthread, String s, MinecraftServer minecraftserver) {
        b = networklistenthread;
        a = minecraftserver;
        // super(s);
    }

    @Override
    public void run() {
        HashMap<InetAddress, Long> clients = new HashMap<InetAddress, Long>();
        do {
            if (!b.b) {
                break;
            }
            try {
                java.net.Socket socket = NetworkListenThread.a(b).accept();
                if (socket != null) {
                    InetAddress addr = socket.getInetAddress();
                    if (clients.containsKey(addr)) {
                        if (System.currentTimeMillis() - clients.get(addr) < 5000) {
                            clients.put(addr, System.currentTimeMillis());
                            socket.close();
                            continue;
                        }
                    }
                    clients.put(addr, System.currentTimeMillis());
                    NetLoginHandler netloginhandler = new NetLoginHandler(a, socket, (new StringBuilder()).append("Connection #").append(NetworkListenThread.b(b)).toString());
                    NetworkListenThread.a(b, netloginhandler);
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        } while (true);
    }
}
