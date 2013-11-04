package net.minecraft.server;

import java.math.BigInteger;

import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.exceptions.AuthenticationUnavailableException;

// CraftBukkit start
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
// CraftBukkit end

class ThreadPlayerLookupUUID extends Thread {

    final LoginListener a;

    ThreadPlayerLookupUUID(LoginListener loginlistener, String s) {
        super(s);
        this.a = loginlistener;
    }

    public void run() {
        try {
            String s = (new BigInteger(MinecraftEncryption.a(LoginListener.a(this.a), LoginListener.b(this.a).I().getPublic(), LoginListener.c(this.a)))).toString(16);

            LoginListener.a(this.a, LoginListener.b(this.a).as().hasJoinedServer(new GameProfile((String) null, LoginListener.d(this.a).getName()), s));
            if (LoginListener.d(this.a) != null) {
                // CraftBukkit start
                if (!this.a.networkManager.d()) {
                    return;
                }

                String playerName = LoginListener.d(this.a).getName();
                java.net.InetAddress address = ((java.net.InetSocketAddress) a.networkManager.getSocketAddress()).getAddress();
                final org.bukkit.craftbukkit.CraftServer server = LoginListener.b(this.a).server;

                AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address);
                server.getPluginManager().callEvent(asyncEvent);

                if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0) {
                    final PlayerPreLoginEvent event = new PlayerPreLoginEvent(playerName, address);
                    if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                        event.disallow(asyncEvent.getResult(), asyncEvent.getKickMessage());
                    }
                    Waitable<PlayerPreLoginEvent.Result> waitable = new Waitable<PlayerPreLoginEvent.Result>() {
                        @Override
                        protected PlayerPreLoginEvent.Result evaluate() {
                            server.getPluginManager().callEvent(event);
                            return event.getResult();
                        }};

                    LoginListener.b(this.a).processQueue.add(waitable);
                    if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED) {
                        this.a.disconnect(event.getKickMessage());
                        return;
                    }
                } else {
                    if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                        this.a.disconnect(asyncEvent.getKickMessage());
                        return;
                    }
                }
                // CraftBukkit end

                LoginListener.e().info("UUID of player " + LoginListener.d(this.a).getName() + " is " + LoginListener.d(this.a).getId());
                LoginListener.a(this.a, EnumProtocolState.READY_TO_ACCEPT);
            } else {
                this.a.disconnect("Failed to verify username!");
                LoginListener.e().error("Username \'" + LoginListener.d(this.a).getName() + "\' tried to join with an invalid session");
            }
        } catch (AuthenticationUnavailableException authenticationunavailableexception) {
            this.a.disconnect("Authentication servers are down. Please try again later, sorry!");
            LoginListener.e().error("Couldn\'t verify username because servers are unavailable");
            // CraftBukkit start
        } catch (Exception exception) {
            this.a.disconnect("Failed to verify username!");
            LoginListener.b(this.a).server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + LoginListener.d(this.a).getName(), exception);
            // CraftBukkit end
        }
    }
}
