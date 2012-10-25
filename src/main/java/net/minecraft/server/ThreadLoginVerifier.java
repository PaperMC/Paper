package net.minecraft.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
// CraftBukkit end

class ThreadLoginVerifier extends Thread {

    final NetLoginHandler netLoginHandler;

    // CraftBukkit start
    CraftServer server;

    ThreadLoginVerifier(NetLoginHandler netloginhandler, CraftServer server) {
        this.server = server;
        // CraftBukkit end
        this.netLoginHandler = netloginhandler;
    }

    public void run() {
        try {
            String s = (new BigInteger(MinecraftEncryption.a(NetLoginHandler.a(this.netLoginHandler), NetLoginHandler.b(this.netLoginHandler).F().getPublic(), NetLoginHandler.c(this.netLoginHandler)))).toString(16);
            URL url = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(NetLoginHandler.d(this.netLoginHandler), "UTF-8") + "&serverId=" + URLEncoder.encode(s, "UTF-8"));
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s1 = bufferedreader.readLine();

            bufferedreader.close();
            if (!"YES".equals(s1)) {
                this.netLoginHandler.disconnect("Failed to verify username!");
                return;
            }

            // CraftBukkit start
            if (this.netLoginHandler.getSocket() == null) {
                return;
            }

            AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(NetLoginHandler.d(this.netLoginHandler), this.netLoginHandler.getSocket().getInetAddress());
            this.server.getPluginManager().callEvent(asyncEvent);

            if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0) {
                final PlayerPreLoginEvent event = new PlayerPreLoginEvent(NetLoginHandler.d(this.netLoginHandler), this.netLoginHandler.getSocket().getInetAddress());
                if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                    event.disallow(asyncEvent.getResult(), asyncEvent.getKickMessage());
                }
                Waitable<PlayerPreLoginEvent.Result> waitable = new Waitable<PlayerPreLoginEvent.Result>() {
                    @Override
                    protected PlayerPreLoginEvent.Result evaluate() {
                        ThreadLoginVerifier.this.server.getPluginManager().callEvent(event);
                        return event.getResult();
                    }};

                NetLoginHandler.b(this.netLoginHandler).processQueue.add(waitable);
                if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED) {
                    this.netLoginHandler.disconnect(event.getKickMessage());
                    return;
                }
            } else {
                if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                    this.netLoginHandler.disconnect(asyncEvent.getKickMessage());
                    return;
                }
            }
            // CraftBukkit end

            NetLoginHandler.a(this.netLoginHandler, true);
            // CraftBukkit start
        } catch (java.io.IOException exception) {
            this.netLoginHandler.disconnect("Failed to verify username, session authentication server unavailable!");
        } catch (Exception exception) {
            this.netLoginHandler.disconnect("Failed to verify username!");
            server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + NetLoginHandler.d(this.netLoginHandler), exception);
            // CraftBukkit end
        }
    }
}
