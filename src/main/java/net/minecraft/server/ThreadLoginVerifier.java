package net.minecraft.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.player.PlayerPreLoginEvent;

class ThreadLoginVerifier extends Thread {

    final Packet1Login a;

    final NetLoginHandler b;
    
    // CraftBukkit start
    CraftServer server;

    ThreadLoginVerifier(NetLoginHandler netloginhandler, Packet1Login packet1login, CraftServer server) {
        this.server = server;
        // CraftBukkit end
        this.b = netloginhandler;
        this.a = packet1login;
    }

    public void run() {
        try {
            String s = NetLoginHandler.a(this.b);
            URL url = new URL("http://www.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(this.a.b, "UTF-8") + "&serverId=" + URLEncoder.encode(s, "UTF-8"));
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s1 = bufferedreader.readLine();

            bufferedreader.close();
            if (s1.equals("YES")) {
                PlayerPreLoginEvent event = new PlayerPreLoginEvent(this.a.b, b.getSocket().getInetAddress());
                server.getPluginManager().callEvent(event);
                
                if (event.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                    this.b.a(event.getKickMessage());
                    return;
                }
                
                NetLoginHandler.a(this.b, this.a);
            } else {
                this.b.a("Failed to verify username!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
