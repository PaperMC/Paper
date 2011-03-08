package net.minecraft.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

class ThreadLoginVerifier extends Thread {

    final Packet1Login a;

    final NetLoginHandler b;

    ThreadLoginVerifier(NetLoginHandler netloginhandler, Packet1Login packet1login) {
        this.b = netloginhandler;
        this.a = packet1login;
    }

    public void run() {
        try {
            String s = NetLoginHandler.a(this.b);
            // Craftbukkit
            URL url = new URL("http://www.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(this.a.b, "UTF-8") + "&serverId=" + URLEncoder.encode(s, "UTF-8"));
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s1 = bufferedreader.readLine();

            bufferedreader.close();
            if (s1.equals("YES")) {
                NetLoginHandler.a(this.b, this.a);
            } else {
                this.b.a("Failed to verify username!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
