package net.minecraft.server;

import java.math.BigInteger;
import java.util.UUID;

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
        GameProfile gameprofile = LoginListener.a(this.a);

        try {
            String s = (new BigInteger(MinecraftEncryption.a(LoginListener.b(this.a), LoginListener.c(this.a).K().getPublic(), LoginListener.d(this.a)))).toString(16);

            LoginListener.a(this.a, LoginListener.c(this.a).av().hasJoinedServer(new GameProfile((UUID) null, gameprofile.getName()), s));
            if (LoginListener.a(this.a) != null) {
                // CraftBukkit start - fire PlayerPreLoginEvent
                if (!this.a.networkManager.isConnected()) {
                    return;
                }

                String playerName = LoginListener.a(this.a).getName();
                java.net.InetAddress address = ((java.net.InetSocketAddress) a.networkManager.getSocketAddress()).getAddress();
                java.util.UUID uniqueId = LoginListener.a(this.a).getId();
                final org.bukkit.craftbukkit.CraftServer server = LoginListener.c(this.a).server;

                AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address, uniqueId);
                server.getPluginManager().callEvent(asyncEvent);

                if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0) {
                    final PlayerPreLoginEvent event = new PlayerPreLoginEvent(playerName, address, uniqueId);
                    if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                        event.disallow(asyncEvent.getResult(), asyncEvent.getKickMessage());
                    }
                    Waitable<PlayerPreLoginEvent.Result> waitable = new Waitable<PlayerPreLoginEvent.Result>() {
                        @Override
                        protected PlayerPreLoginEvent.Result evaluate() {
                            server.getPluginManager().callEvent(event);
                            return event.getResult();
                        }};

                    LoginListener.c(this.a).processQueue.add(waitable);
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

                LoginListener.e().info("UUID of player " + LoginListener.a(this.a).getName() + " is " + LoginListener.a(this.a).getId());
                LoginListener.a(this.a, EnumProtocolState.READY_TO_ACCEPT);
            } else if (LoginListener.c(this.a).N()) {
                LoginListener.e().warn("Failed to verify username but will let them in anyway!");
                LoginListener.a(this.a, this.a.a(gameprofile));
                LoginListener.a(this.a, EnumProtocolState.READY_TO_ACCEPT);
            } else {
                this.a.disconnect("Failed to verify username!");
                LoginListener.e().error("Username \'" + LoginListener.a(this.a).getName() + "\' tried to join with an invalid session");
            }
        } catch (AuthenticationUnavailableException authenticationunavailableexception) {
            if (LoginListener.c(this.a).N()) {
                LoginListener.e().warn("Authentication servers are down but will let them in anyway!");
                LoginListener.a(this.a, this.a.a(gameprofile));
                LoginListener.a(this.a, EnumProtocolState.READY_TO_ACCEPT);
            } else {
                this.a.disconnect("Authentication servers are down. Please try again later, sorry!");
                LoginListener.e().error("Couldn\'t verify username because servers are unavailable");
            }
            // CraftBukkit start - catch all exceptions
        } catch (Exception exception) {
            this.a.disconnect("Failed to verify username!");
            LoginListener.c(this.a).server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + LoginListener.a(this.a).getName(), exception);
            // CraftBukkit end
        }
    }
}
