package net.minecraft.server;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
// CraftBukkit end
import io.netty.buffer.Unpooled; // Paper

public class LoginListener implements PacketLoginInListener {

    private static final AtomicInteger b = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random random = new Random();
    private final byte[] e = new byte[4];
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private LoginListener.EnumProtocolState g; public final LoginListener.EnumProtocolState getLoginState() { return this.g; }; // Paper - OBFHELPER
    private int h;
    private GameProfile i; private void setGameProfile(final GameProfile profile) { this.i = profile; } public GameProfile getGameProfile() { return this.i; } // Paper - OBFHELPER
    private final String j;
    private SecretKey loginKey;
    private EntityPlayer l;
    public String hostname = ""; // CraftBukkit - add field
    private int velocityLoginMessageId = -1; // Paper - Velocity support

    public LoginListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.g = LoginListener.EnumProtocolState.HELLO;
        this.j = "";
        this.server = minecraftserver;
        this.networkManager = networkmanager;
        LoginListener.random.nextBytes(this.e);
    }

    public void tick() {
        // Paper start - Do not allow logins while the server is shutting down
        if (!MinecraftServer.getServer().isRunning()) {
            this.disconnect(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(org.spigotmc.SpigotConfig.restartMessage)[0]);
            return;
        }
        // Paper end
        if (this.g == LoginListener.EnumProtocolState.READY_TO_ACCEPT) {
            // Paper start - prevent logins to be processed even though disconnect was called
            if (networkManager.isConnected()) {
                this.c();
            }
            // Paper end
        } else if (this.g == LoginListener.EnumProtocolState.DELAY_ACCEPT) {
            EntityPlayer entityplayer = this.server.getPlayerList().getActivePlayer(this.i.getId()); // Paper

            if (entityplayer == null) {
                this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
                this.server.getPlayerList().a(this.networkManager, this.l);
                this.l = null;
            }
        }

        if (this.h++ == 600) {
            this.disconnect(new ChatMessage("multiplayer.disconnect.slow_login"));
        }

    }

    // CraftBukkit start
    @Deprecated
    public void disconnect(String s) {
        try {
            IChatBaseComponent ichatbasecomponent = new ChatComponentText(s);
            LoginListener.LOGGER.info("Disconnecting {}: {}", this.d(), s);
            this.networkManager.sendPacket(new PacketLoginOutDisconnect(ichatbasecomponent));
            this.networkManager.close(ichatbasecomponent);
        } catch (Exception exception) {
            LoginListener.LOGGER.error("Error whilst disconnecting player", exception);
        }
    }
    // CraftBukkit end

    @Override
    public NetworkManager a() {
        return this.networkManager;
    }

    public void disconnect(IChatBaseComponent ichatbasecomponent) {
        try {
            LoginListener.LOGGER.info("Disconnecting {}: {}", this.d(), ichatbasecomponent.getString());
            this.networkManager.sendPacket(new PacketLoginOutDisconnect(ichatbasecomponent));
            this.networkManager.close(ichatbasecomponent);
        } catch (Exception exception) {
            LoginListener.LOGGER.error("Error whilst disconnecting player", exception);
        }

    }

    // Paper start - Cache authenticator threads
    private static final AtomicInteger threadId = new AtomicInteger(0);
    private static final java.util.concurrent.ExecutorService authenticatorPool = java.util.concurrent.Executors.newCachedThreadPool(
            r -> new Thread(r, "User Authenticator #" + threadId.incrementAndGet())
    );
    // Paper end
    // Spigot start
    public void initUUID()
    {
        UUID uuid;
        if ( networkManager.spoofedUUID != null )
        {
            uuid = networkManager.spoofedUUID;
        } else
        {
            uuid = EntityHuman.getOfflineUUID( this.i.getName() );
        }

        this.i = new GameProfile( uuid, this.i.getName() );

        if (networkManager.spoofedProfile != null)
        {
            for ( com.mojang.authlib.properties.Property property : networkManager.spoofedProfile )
            {
                this.i.getProperties().put( property.getName(), property );
            }
        }
    }
    // Spigot end

    public void c() {
        // Spigot start - Moved to initUUID
        /*
        if (!this.i.isComplete()) {
            this.i = this.a(this.i);
        }
        */
        // Spigot end

        // CraftBukkit start - fire PlayerLoginEvent
        EntityPlayer s = this.server.getPlayerList().attemptLogin(this, this.i, hostname);

        if (s == null) {
            // this.disconnect(ichatbasecomponent);
            // CraftBukkit end
        } else {
            this.g = LoginListener.EnumProtocolState.ACCEPTED;
            if (this.server.aw() >= 0 && !this.networkManager.isLocal()) {
                this.networkManager.sendPacket(new PacketLoginOutSetCompression(this.server.aw()), (channelfuture) -> {
                    this.networkManager.setCompressionLevel(this.server.aw());
                });
            }

            this.networkManager.sendPacket(new PacketLoginOutSuccess(this.i));
            EntityPlayer entityplayer = this.server.getPlayerList().getActivePlayer(this.i.getId()); // Paper

            if (entityplayer != null) {
                this.g = LoginListener.EnumProtocolState.DELAY_ACCEPT;
                this.l = this.server.getPlayerList().processLogin(this.i, s); // CraftBukkit - add player reference
            } else {
                this.server.getPlayerList().a(this.networkManager, this.server.getPlayerList().processLogin(this.i, s)); // CraftBukkit - add player reference
            }
        }

    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent) {
        LoginListener.LOGGER.info("{} lost connection: {}", this.d(), ichatbasecomponent.getString());
    }

    public String d() {
        return this.i != null ? this.i + " (" + this.networkManager.getSocketAddress() + ")" : String.valueOf(this.networkManager.getSocketAddress());
    }

    @Override
    public void a(PacketLoginInStart packetlogininstart) {
        Validate.validState(this.g == LoginListener.EnumProtocolState.HELLO, "Unexpected hello packet", new Object[0]);
        this.i = packetlogininstart.b();
        if (this.server.getOnlineMode() && !this.networkManager.isLocal()) {
            this.g = LoginListener.EnumProtocolState.KEY;
            this.networkManager.sendPacket(new PacketLoginOutEncryptionBegin("", this.server.getKeyPair().getPublic(), this.e));
        } else {
            // Paper start - Velocity support
            if (com.destroystokyo.paper.PaperConfig.velocitySupport) {
                this.velocityLoginMessageId = java.util.concurrent.ThreadLocalRandom.current().nextInt();
                PacketLoginOutCustomPayload packet = new PacketLoginOutCustomPayload(this.velocityLoginMessageId, com.destroystokyo.paper.proxy.VelocityProxy.PLAYER_INFO_CHANNEL, new PacketDataSerializer(Unpooled.EMPTY_BUFFER));
                this.networkManager.sendPacket(packet);
                return;
            }
            // Paper end
            // Spigot start
            // Paper start - Cache authenticator threads
            authenticatorPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        initUUID();
                        new LoginHandler().fireEvents();
                    } catch (Exception ex) {
                        disconnect("Failed to verify username!");
                        server.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + i.getName(), ex);
                    }
                }
            });
            // Paper end
            // Spigot end
        }

    }

    @Override
    public void a(PacketLoginInEncryptionBegin packetlogininencryptionbegin) {
        Validate.validState(this.g == LoginListener.EnumProtocolState.KEY, "Unexpected key packet", new Object[0]);
        PrivateKey privatekey = this.server.getKeyPair().getPrivate();

        if (!Arrays.equals(this.e, packetlogininencryptionbegin.b(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        } else {
            this.loginKey = packetlogininencryptionbegin.a(privatekey);
            this.g = LoginListener.EnumProtocolState.AUTHENTICATING;
            this.networkManager.a(this.loginKey);
            // Paper start - Cache authenticator threads
            authenticatorPool.execute(new Runnable() {
                public void run() {
                    GameProfile gameprofile = LoginListener.this.i;

                    try {
                        String s = (new BigInteger(MinecraftEncryption.a("", LoginListener.this.server.getKeyPair().getPublic(), LoginListener.this.loginKey))).toString(16);

                        LoginListener.this.i = LoginListener.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile((UUID) null, gameprofile.getName()), s, this.a());
                        if (LoginListener.this.i != null) {
                            // CraftBukkit start - fire PlayerPreLoginEvent
                            if (!networkManager.isConnected()) {
                                return;
                            }

                            new LoginHandler().fireEvents();
                        } else if (LoginListener.this.server.isEmbeddedServer()) {
                            LoginListener.LOGGER.warn("Failed to verify username but will let them in anyway!");
                            LoginListener.this.i = LoginListener.this.a(gameprofile);
                            LoginListener.this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
                        } else {
                            LoginListener.this.disconnect(new ChatMessage("multiplayer.disconnect.unverified_username"));
                            LoginListener.LOGGER.error("Username '{}' tried to join with an invalid session", gameprofile.getName());
                        }
                    } catch (AuthenticationUnavailableException authenticationunavailableexception) {
                        if (LoginListener.this.server.isEmbeddedServer()) {
                            LoginListener.LOGGER.warn("Authentication servers are down but will let them in anyway!");
                            LoginListener.this.i = LoginListener.this.a(gameprofile);
                            LoginListener.this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
                        } else {
                            // Paper start
                            if (com.destroystokyo.paper.PaperConfig.authenticationServersDownKickMessage != null) {
                                LoginListener.this.disconnect(new ChatComponentText(com.destroystokyo.paper.PaperConfig.authenticationServersDownKickMessage));
                            } else // Paper end
                            LoginListener.this.disconnect(new ChatMessage("multiplayer.disconnect.authservers_down"));
                            LoginListener.LOGGER.error("Couldn't verify username because servers are unavailable");
                        }
                        // CraftBukkit start - catch all exceptions
                    } catch (Exception exception) {
                        disconnect("Failed to verify username!");
                        server.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + gameprofile.getName(), exception);
                        // CraftBukkit end
                    }

                }

                @Nullable
                private InetAddress a() {
                    SocketAddress socketaddress = LoginListener.this.networkManager.getSocketAddress();

                    return LoginListener.this.server.V() && socketaddress instanceof InetSocketAddress ? ((InetSocketAddress) socketaddress).getAddress() : null;
                }
            });
            // Paper end
        }
    }

    // Spigot start
    public class LoginHandler {

        public void fireEvents() throws Exception {
                            // Paper start - Velocity support
                            if (LoginListener.this.velocityLoginMessageId == -1 && com.destroystokyo.paper.PaperConfig.velocitySupport) {
                                disconnect("This server requires you to connect with Velocity.");
                                return;
                            }
                            // Paper end
                            String playerName = i.getName();
                            java.net.InetAddress address = ((java.net.InetSocketAddress) networkManager.getSocketAddress()).getAddress();
                            java.util.UUID uniqueId = i.getId();
                            final org.bukkit.craftbukkit.CraftServer server = LoginListener.this.server.server;

                            // Paper start
                            PlayerProfile profile = CraftPlayerProfile.asBukkitMirror(getGameProfile());
                            AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address, uniqueId, profile);
                            server.getPluginManager().callEvent(asyncEvent);
                            profile = asyncEvent.getPlayerProfile();
                            profile.complete(true);
                            setGameProfile(CraftPlayerProfile.asAuthlib(profile));
                            playerName = i.getName();
                            uniqueId = i.getId();
                            // Paper end

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

                                LoginListener.this.server.processQueue.add(waitable);
                                if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED) {
                                    disconnect(event.getKickMessage());
                                    return;
                                }
                            } else {
                                if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                                    disconnect(asyncEvent.getKickMessage());
                                    return;
                                }
                            }
                            // CraftBukkit end
                            LoginListener.LOGGER.info("UUID of player {} is {}", LoginListener.this.i.getName(), LoginListener.this.i.getId());
                            LoginListener.this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
                }
        }
    // Spigot end

    public void a(PacketLoginInCustomPayload packetloginincustompayload) {
        // Paper start - Velocity support
        if (com.destroystokyo.paper.PaperConfig.velocitySupport && packetloginincustompayload.getId() == this.velocityLoginMessageId) {
            PacketDataSerializer buf = packetloginincustompayload.getBuf();
            if (buf == null) {
                this.disconnect("This server requires you to connect with Velocity.");
                return;
            }

            if (!com.destroystokyo.paper.proxy.VelocityProxy.checkIntegrity(buf)) {
                this.disconnect("Unable to verify player details");
                return;
            }

            this.networkManager.socketAddress = new java.net.InetSocketAddress(com.destroystokyo.paper.proxy.VelocityProxy.readAddress(buf), ((java.net.InetSocketAddress) this.networkManager.getSocketAddress()).getPort());

            this.setGameProfile(com.destroystokyo.paper.proxy.VelocityProxy.createProfile(buf));

            // Proceed with login
            authenticatorPool.execute(() -> {
                try {
                    new LoginHandler().fireEvents();
                } catch (Exception ex) {
                    disconnect("Failed to verify username!");
                    server.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + i.getName(), ex);
                }
            });
            return;
        }
        // Paper end
        this.disconnect(new ChatMessage("multiplayer.disconnect.unexpected_query_response"));
    }

    protected GameProfile a(GameProfile gameprofile) {
        UUID uuid = EntityHuman.getOfflineUUID(gameprofile.getName());

        return new GameProfile(uuid, gameprofile.getName());
    }

    static enum EnumProtocolState {

        HELLO, KEY, AUTHENTICATING, NEGOTIATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED;

        private EnumProtocolState() {}
    }
}
